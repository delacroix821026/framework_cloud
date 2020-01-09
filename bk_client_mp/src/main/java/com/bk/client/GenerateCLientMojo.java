package com.fw.oauth.client;

import com.fw.oauth.client.exception.ClientNotExistException;
import com.fw.oauth.client.exception.JarNotFoundException;
import com.fw.oauth.client.utils.JarLoaderUtil;
import com.fw.oauth.client.utils.PackageUtil;
import com.fw.oauth.client.utils.ZipUtils;
import freemarker.cache.ClassTemplateLoader;
import freemarker.ext.beans.BeansWrapper;
import freemarker.template.Configuration;
import freemarker.template.DefaultObjectWrapper;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import freemarker.template.TemplateHashModel;
import freemarker.template.TemplateModelException;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.apache.maven.project.MavenProject;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Mojo(name = "generate", threadSafe = true)
public class GenerateCLientMojo extends AbstractMojo {
    @Parameter(defaultValue = "${project}", required = true, readonly = true)
    private MavenProject project;

    @Parameter(defaultValue = "Controller", readonly = true)
    private String controllerProfix;

    private MavenProject topProject;

    private String clientPath;

    public void execute() throws MojoExecutionException, MojoFailureException {
        getLog().info("Generate client java file!");
        init();
        processClass();

        getLog().debug("project.getCompileSourceRoots().isEmpty: " + project.getCompileSourceRoots().isEmpty());
        if (!project.getCompileSourceRoots().isEmpty()) {
            getLog().debug("project.getCompileSourceRoots().size: : " + project.getCompileSourceRoots().size());
            for (int count = 0; count < project.getCompileSourceRoots().size(); count++) {
                getLog().debug("project.getCompileSourceRoots().get(" + count + ").path: " + project.getCompileSourceRoots().get(count));
                project.getCompileSourceRoots().get(count);
            }
        }
        getLog().info("BaseDir: " + project.getBasedir());

        //generate();
    }

    private void init() throws ClientNotExistException, JarNotFoundException {
        getTopProject();
        getClientPath();
        loadServerControllerJar();
    }

    private void getTopProject() {
        topProject = project.getParent().getParent();
        getLog().debug("topProject name is: " + topProject.getName());
    }

    private void getClientPath() throws ClientNotExistException {
        String clientProjectName = null;
        for (String modulesName : topProject.getModules()) {
            if (modulesName.indexOf("_client") > 0) {
                getLog().debug("clientName: " + modulesName);
                clientProjectName = modulesName;
                break;
            }
        }

        if (clientProjectName == null) {
            throw new ClientNotExistException("No project without the name of client here!");
        }

        clientPath = topProject.getBasedir().getPath() + "/" + clientProjectName + "/";
        getLog().debug("clientProject pom file path is : " + clientPath);

    }

    private void loadServerControllerJar() throws JarNotFoundException {
        String jarPath = project.getBuild().getDirectory() + "/" + project.getBuild().getFinalName();// + ".jar"
        getLog().debug("Server output directory is : " + jarPath);
        File file = new File(jarPath + ".jar.original");
        if (!file.exists())
            throw new JarNotFoundException("Server controller jar not found!");
        /*File unzipFile = new File(jarPath);
        if(!unzipFile.exists())
            unzipFile.mkdirs();*/

        ZipUtils.unZip(jarPath + ".jar", jarPath);
        getLog().info("Unzip jar success!");

        //JarLoaderUtil.loadJarFile(new File(jarPath));
        JarLoaderUtil.loadJarPath(jarPath);
        JarLoaderUtil.loadJarFile(new File(jarPath + ".jar.original"));
        getLog().info("load server jar success!");

    }

    private void loadClientControllerJar() throws JarNotFoundException {
        String jarPath = clientPath + "controller/target/" + project.getBuild().getFinalName().replace("-server", "-client");
        getLog().debug("Client output directory is : " + jarPath);
        File file = new File(jarPath + ".jar.original");
        if (file.exists()) {
            ZipUtils.unZip(jarPath + ".jar", jarPath);
            getLog().info("Unzip jar success!");

            //JarLoaderUtil.loadJarFile(new File(jarPath));
            JarLoaderUtil.loadJarPath(jarPath);
            JarLoaderUtil.loadJarFile(new File(jarPath + ".jar.original"));
            getLog().info("load client jar success!");
        }

    }

    private void processClass() {
        String jarPath = project.getBuild().getDirectory() + "/" + project.getBuild().getFinalName();
        List<String> classList = PackageUtil.getClassNameByJar(jarPath + ".jar.original");
        for (String classFullNmae : classList) {
            getLog().debug("process class named:" + classFullNmae);
            try {
                Class source = JarLoaderUtil.system.loadClass(classFullNmae);
                Annotation annotation = source.getAnnotation(JarLoaderUtil.system.loadClass("org.springframework.web.bind.annotation.RestController"));
                if (annotation != null) {
                    getLog().debug(classFullNmae + "is a " + annotation);
                    String targetControllerClassName = classFullNmae.replaceAll("\\." + topProject.getName() + "\\.", ".client." + topProject.getName() + ".");

                    boolean targetExists = false;
                    try {
                        Class targetClazz = JarLoaderUtil.system.loadClass(targetControllerClassName);
                    } catch (ClassNotFoundException e) {
                        generateClentSrc(source);
                    }
                }
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
    }

    private void generateClentSrc(Class source) {
        String jarPath = clientPath + "controller/target/" + project.getBuild().getFinalName().replace("-server", "-client");
        String packageName = source.getPackage().getName().replaceAll("\\." + topProject.getName() + "\\.", ".client." + topProject.getName() + ".");
        getLog().debug("Client output directory is : " + jarPath);
        String srcPath = clientPath + "controller/src/main/java/" + packageName
                .replaceAll("\\.", "/") + "/";
        getLog().debug("Client src directory is : " + srcPath);
        File file = new File(srcPath);
        if (!file.exists())
            file.mkdirs();

        //src存在异常

        ClassModel model = new ClassModel();
        model.setPackageName(packageName);
        model.setServiceName(source.getSimpleName().replaceAll(controllerProfix, "Service"));
        model.setServiceVariable((new StringBuilder()).append(Character.toLowerCase(model.getServiceName().charAt(0))).append(model.getServiceName().substring(1)).toString());
        model.getImportList().add(source.getName().replaceAll(controllerProfix.toLowerCase(), "service")
                .replaceAll("\\." + topProject.getName() + "\\.", ".client." + topProject.getName() + ".")
                .replaceAll(controllerProfix, "Service"));
        model.setClassName(source.getSimpleName());


        try {
            Annotation annotation = source.getAnnotation(JarLoaderUtil.system.loadClass("org.springframework.web.bind.annotation.RequestMapping"));
            model.setRequestMapping(annotation);
        } catch (ClassNotFoundException e) {
        }

        model.setMethods(source.getDeclaredMethods());

        for (Method method : model.getMethods()) {
            for (java.lang.reflect.Parameter parameter : method.getParameters()) {
                if (parameter.getType().getPackage() != null && !"java.lang".equals(parameter.getType().getPackage().getName()))
                    model.getImportList().add(parameter.getType().getName());
                //parameter.getAnnotations()[0].annotationType().getField()[0].getName();
            }
        }

        Map<String, Object> context = new HashMap<String, Object>();
        context.put("model", model);


        generatorFile("client-controler.ftl", context, srcPath + source.getSimpleName() + ".java");
    }

    private void generatorFile(String templateName, Map<String, Object> context, String filePath) {
        getLog().debug("generatorFile path is : " + filePath);
        Configuration configuration = new Configuration();
        configuration.setObjectWrapper(new DefaultObjectWrapper());
        configuration.setTemplateLoader(new ClassTemplateLoader(this.getClass(), "/"));

        BeansWrapper wrapper = BeansWrapper.getDefaultInstance();
        TemplateHashModel staticModels = wrapper.getStaticModels();
        TemplateHashModel proxyStatics = null;
        try {
            proxyStatics = (TemplateHashModel) staticModels.get("com.fw.oauth.client.utils.ReflectAnnotation");
            context.put("ReflectAnnotation", proxyStatics);
        } catch (TemplateModelException e) {
            e.printStackTrace();
        }


        OutputStreamWriter writer = null;
        try {
            Template template = configuration.getTemplate(templateName);
            File file = new File(project.getBuild().getDirectory());
            if (!file.exists()) {
                file.mkdir();
            }
            writer = new FileWriter(new File(filePath));

            template.process(context, writer);
            getLog().debug("generatorFile success!");
        } catch (IOException e) {
            e.printStackTrace();
        } catch (TemplateException e) {
            e.printStackTrace();
        } finally {
            if (writer != null)
                try {
                    writer.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
        }
    }

}
