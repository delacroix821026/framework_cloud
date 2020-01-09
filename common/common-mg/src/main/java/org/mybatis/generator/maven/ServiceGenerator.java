package org.mybatis.generator.maven;

import freemarker.template.Template;
import generator.constant.Constants;
import org.apache.maven.project.MavenProject;
import org.codehaus.plexus.util.StringUtils;
import org.mybatis.generator.api.IntrospectedTable;
import org.mybatis.generator.api.ProgressCallback;
import org.mybatis.generator.config.Configuration;
import org.mybatis.generator.config.Context;
import org.mybatis.generator.config.JavaModelGeneratorConfiguration;
import org.mybatis.generator.config.TableConfiguration;
import org.mybatis.generator.config.xml.ConfigurationParser;
import org.mybatis.generator.exception.XMLParserException;
import org.mybatis.generator.internal.NullProgressCallback;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.StringWriter;
import java.lang.reflect.Field;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ServiceGenerator {

    @SuppressWarnings("unchecked")
    public static void main(Configuration config, List<String> warnings, MavenProject project) {
        try {
            List<Context> contexts = config.getContexts();

            initXml(warnings, contexts);// 初始化xml，获取introspectedTables信息

            // 加载freemarker配置和模板    `
            freemarker.template.Configuration cfg = new freemarker.template.Configuration(
                    freemarker.template.Configuration.VERSION_2_3_22);
            cfg.setDefaultEncoding("UTF-8");
            cfg.setClassForTemplateLoading(ServiceGenerator.class, "/template/");
            //cfg.setDirectoryForTemplateLoading(new File("/Users/daijuancen/ec/mybatis-generator-tools/generator-parent/generator-mbg/src/main/resources/template/"));

            for (Context context : contexts) {
                List<TableConfiguration> configuration = context.getTableConfigurations();
                JavaModelGeneratorConfiguration javaModelGeneratorConfiguration = context
                        .getJavaModelGeneratorConfiguration();
                String modelTarget = javaModelGeneratorConfiguration.getTargetPackage();
                String modelPath = modelTarget.replace(".", "/");
                String servicePath = modelTarget.substring(0, modelTarget.lastIndexOf(".")) + ".service";
                String serviceImplPath = servicePath + ".impl";
                String daoPath = modelTarget.substring(0, modelTarget.lastIndexOf(".")) + ".dao";
                String daoImplPath = daoPath + ".impl";
                String mapperPath = context.getJavaClientGeneratorConfiguration().getTargetPackage();
                String domain = null;
                context.getTargetRuntime();
                Field filed = context.getClass().getDeclaredField("introspectedTables");
                filed.setAccessible(true);
                List<IntrospectedTable> introspectedTables = (List<IntrospectedTable>) filed.get(context);
                if (introspectedTables == null || introspectedTables.size() == 0) {
                    System.err.println("没有可生成的表!请检查数据库连接及生成表配置");
                    return;
                }
                for (TableConfiguration tableConfiguration : configuration) {
                    String targetProject = project.getBasedir() + javaModelGeneratorConfiguration.getTargetProject();

                    domain = tableConfiguration.getDomainObjectName();// 实体类名称
                    String domainPackage = tableConfiguration.getProperty("domainPackage");// xml中配置的domainPackage属性
                    String smallDomainName = StringUtils.lowercaseFirstLetter(domain);
                    String primaryKeyType = null;// 主键类型
                    for (IntrospectedTable introspectedTable : introspectedTables) {
                        String domainObject = introspectedTable.getFullyQualifiedTable().getDomainObjectName();
                        if (domainObject.equals(domain)) {
                            primaryKeyType = introspectedTable.getPrimaryKeyColumns().get(0).getFullyQualifiedJavaType()
                                    .getShortName();// 主键类型
                        }
                    }

                    String targetPath = targetProject + "/" + modelPath + "/" + domain + ".java";
                    File targetFile = new File(targetPath);
                    // 开始生成entity和example
                    Map<String, Object> entityMap = new HashMap<String, Object>();
                    entityMap.put("package", modelTarget);
                    entityMap.put("domainName", domain);
                    entityMap.put("baseDomain", modelTarget + "." + domainPackage + ".Base" + domain);
                    entityMap.put("paginationPackage", Constants.PAGINATION_PACKAGE);
                    generateEntityExample(cfg, entityMap,
                            targetProject + "/" + modelPath + "/" + domain + "Example.java");// 生成entityExample
                    if (!targetFile.exists()) {
                        generateEntity(cfg, entityMap, targetProject + "/" + modelPath + "/" + domain + ".java");// 生成entity
                    } else {
                        System.out.println(targetPath + "已经存在，跳过生成！");
                    }

                    targetProject = project.getBasedir() + context.getJavaClientGeneratorConfiguration().getTargetProject();
                    // 开始生成javaMapper
                    Map<String, Object> javaMapperMap = new HashMap<String, Object>();
                    javaMapperMap.put("package", mapperPath);
                    javaMapperMap.put("domainName", domain);
                    javaMapperMap.put("primaryKeyType", primaryKeyType);
                    javaMapperMap.put("baseModelPackage", modelTarget);
                    javaMapperMap.put("genericMapperPackage", Constants.GENERIC_MAPPER_PACKAGE);
                    generateJavaMap(cfg, javaMapperMap,
                            targetProject + "/" + mapperPath.replace(".", "/") + "/" + domain + "Mapper.java");// 生成entity

                    targetPath = targetProject + "/" + daoPath.replace(".", "/") + "/" + domain + "Dao.java";
                    targetFile = new File(targetPath);
                    if (!targetFile.exists()) {
                        // 开始生成dao,daoImpl
                        Map<String, Object> daoMap = new HashMap<String, Object>();
                        daoMap.put("domainName", domain);
                        daoMap.put("package", daoPath);
                        daoMap.put("primaryKeyType", primaryKeyType);
                        daoMap.put("baseModelPackage", modelTarget);
                        daoMap.put("genericServicePackage", Constants.GENERIC_SERVICE_PACKAGE);
                        daoMap.put("genericMapperPackage", Constants.GENERIC_MAPPER_PACKAGE);
                        daoMap.put("genericServiceImplPackage", Constants.GENERIC_SERVICEIMPL_PACKAGE);
                        daoMap.put("smallDomainName", smallDomainName);
                        daoMap.put("mapperPackage", mapperPath + "." + domain + "Mapper");
                        generateDao(cfg, daoMap,
                                targetProject + "/" + daoPath.replace(".", "/") + "/" + domain + "Dao.java");// 生成service
                        generateDaoImpl(cfg, daoMap, targetProject + "/" + daoImplPath.replace(".", "/") + "/"
                                + domain + "DaoImpl.java");// 生成daoImpl
                    } else {
                        System.out.println(targetPath + "已经存在，跳过生成！");
                    }

                    targetProject = project.getBasedir() + context.getJavaClientGeneratorConfiguration().getTargetProject().replaceAll("dao", "service");
                    targetPath = targetProject + "/" + servicePath.replace(".", "/") + "/" + domain + "Service.java";
                    targetFile = new File(targetPath);
                    if (!targetFile.exists()) {
                        // 开始生成service,serviceImpl
                        Map<String, Object> serviceMap = new HashMap<String, Object>();
                        serviceMap.put("domainName", domain);
                        serviceMap.put("package", servicePath);
                        serviceMap.put("daoPath", daoPath);
                        serviceMap.put("primaryKeyType", primaryKeyType);
                        serviceMap.put("baseModelPackage", modelTarget);
                        serviceMap.put("genericServicePackage", Constants.GENERIC_SERVICE_PACKAGE);
                        serviceMap.put("genericMapperPackage", Constants.GENERIC_MAPPER_PACKAGE);
                        serviceMap.put("genericServiceImplPackage", Constants.GENERIC_SERVICEIMPL_PACKAGE);
                        serviceMap.put("smallDomainName", smallDomainName);
                        serviceMap.put("mapperPackage", mapperPath + "." + domain + "Mapper");
                        generateService(cfg, serviceMap,
                                targetProject + "/" + servicePath.replace(".", "/") + "/" + domain + "Service.java");// 生成dao
                        generateServiceImpl(cfg, serviceMap, targetProject + "/" + serviceImplPath.replace(".", "/") + "/"
                                + domain + "ServiceImpl.java");// 生成daoImpl
                    } else {
                        System.out.println(targetPath + "已经存在，跳过生成！");
                    }
                }
            }

        } catch (IOException | NoSuchFieldException | SecurityException | IllegalArgumentException
                | IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    private static void initXml(List<String> warnings, List<Context> contexts) {
        ProgressCallback callback = new NullProgressCallback();
        int totalSteps = 0;
        for (Context context : contexts) {
            totalSteps += context.getIntrospectionSteps();
        }
        callback.introspectionStarted(totalSteps);
        for (Context context : contexts) {
            try {
                context.introspectTables(callback, warnings, null);
            } catch (SQLException | InterruptedException | SecurityException | IllegalArgumentException e) {
                e.printStackTrace();
            }
        }
    }

    public static void generate(String templateName, freemarker.template.Configuration cfg, Map<String, Object> map,
                                String filePath) {
        Template template;
        try {
            template = cfg.getTemplate(templateName);
            String content = renderTemplate(template, map);
            writeFile(new File(filePath), content, "UTF-8");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void generateEntity(freemarker.template.Configuration cfg, Map<String, Object> map, String filePath)
            throws IOException {
        generate("model.ftl", cfg, map, filePath);
    }

    public static void generateEntityExample(freemarker.template.Configuration cfg, Map<String, Object> map,
                                             String filePath) throws IOException {
        generate("modelExample.ftl", cfg, map, filePath);
    }

    public static void generateJavaMap(freemarker.template.Configuration cfg, Map<String, Object> map, String filePath)
            throws IOException {
        generate("mapper.ftl", cfg, map, filePath);
    }

    public static void generateDao(freemarker.template.Configuration cfg, Map<String, Object> map, String filePath)
            throws IOException {
        generate("dao.ftl", cfg, map, filePath);
    }

    public static void generateDaoImpl(freemarker.template.Configuration cfg, Map<String, Object> map,
                                       String filePath) throws IOException {
        generate("daoImpl.ftl", cfg, map, filePath);
    }

    public static void generateService(freemarker.template.Configuration cfg, Map<String, Object> map, String filePath)
            throws IOException {
        generate("service.ftl", cfg, map, filePath);
    }

    public static void generateServiceImpl(freemarker.template.Configuration cfg, Map<String, Object> map,
                                           String filePath) throws IOException {
        generate("serviceImpl.ftl", cfg, map, filePath);
    }

    private static String renderTemplate(Template template, Object model) {
        try {
            StringWriter result = new StringWriter();
            template.process(model, result);
            return result.toString();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    private static void writeFile(File file, String content, String fileEncoding) throws IOException {
        if (file.exists()) {
            file.delete();
        }
        int index = file.getPath().lastIndexOf(File.separator);
        File dir = new File(file.getPath().substring(0, index));
        dir.mkdirs();
        file.createNewFile();
        FileOutputStream fos = new FileOutputStream(file, false);
        OutputStreamWriter osw;
        if (fileEncoding == null) {
            osw = new OutputStreamWriter(fos);
        } else {
            osw = new OutputStreamWriter(fos, fileEncoding);
        }

        BufferedWriter bw = new BufferedWriter(osw);
        bw.write(content);
        bw.close();
    }
}
