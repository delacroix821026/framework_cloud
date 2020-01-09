package com.fw.k8s;

import freemarker.cache.ClassTemplateLoader;
import freemarker.template.Configuration;
import freemarker.template.DefaultObjectWrapper;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import org.apache.maven.model.Profile;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.apache.maven.plugins.annotations.ResolutionScope;
import org.apache.maven.project.MavenProject;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Mojo(name = "generate", defaultPhase = LifecyclePhase.COMPILE, aggregator = true,
        requiresDependencyResolution = ResolutionScope.COMPILE, threadSafe = true)
public class GenerateMojo extends AbstractMojo {
    @Parameter(defaultValue = "${project}", required = true, readonly = true)
    private MavenProject project;

    @Parameter(defaultValue = "project", required = false, readonly = true)
    private String templateName = "";

    @Parameter(readonly = true)
    private String logstashUrl = null;

    @Parameter(readonly = true)
    private String configLocation = null;

    @Parameter(readonly = true)
    private String configName = null;

    @Parameter(readonly = true)
    private String label = null;

    @Parameter(defaultValue = "true", readonly = true)
    private String needClient = null;

    @Parameter(readonly = true)
    private Map<String, String> env = new HashMap<String, String>();

    private String profile;

    public void execute() throws MojoExecutionException, MojoFailureException {
        getLog().info("Generate k8s config file!templateName:" + templateName);
        /*if (projects.size() > 1) {
            throw new ProjectSizeException();
        }*/

        profile = getActiveProfileStatement();
        getLog().info("Profile is " + profile);

        generate();
    }

    private Map convert() {
        Map<String, String> map = new HashMap<String, String>();
        for (Map.Entry<String, String> entry : env.entrySet()) {
            String entryValue = entry.getValue();
            if (entryValue == null || "".equals(entryValue))
                entryValue = entry.getKey();
            String value = convert(entryValue);
            if (value != null)
                map.put(entry.getKey(), convert(entryValue));
            else
                map.put(entry.getKey(), entryValue);

        }

        String schemaUsername = System.getenv("schemaUsername-" + profile);
        getLog().info("schemaUsername value:" + schemaUsername);
        if (schemaUsername != null)
            map.put("spring.datasource.schemaUsername", schemaUsername);

        String dataUsername = System.getenv("dataUsername-" + profile);
        getLog().info("dataUsername value:" + dataUsername);
        if (dataUsername != null)
            map.put("spring.datasource.dataUsername", dataUsername);

        String schemaPassword = System.getenv("schemaPassword-" + profile);
        //getLog().info("schemaPassword value:" + schemaPassword);
        if (dataUsername != null)
            map.put("spring.datasource.schemaPassword", schemaPassword);

        String dataPassword = System.getenv("dataPassword-" + profile);
        //getLog().info("dataPassword value:" + dataPassword);
        if (dataPassword != null)
            map.put("spring.datasource.dataPassword", dataPassword);

        String branchName = System.getenv("branchName");
        if(branchName != null && branchName.indexOf("/") > -1)
            branchName = branchName.substring(branchName.indexOf("/") + 1);
        map.put("spring.cloud.config.label", branchName != null ? branchName : "master");

        getLog().info("mapSize: " + map.size());
        return map;
    }

    private String convert(String key) {
        String result = null;
        for (Profile profile : project.getParent().getActiveProfiles()) {
            String value = profile.getProperties().getProperty(key);
            if (value != null) {
                result = value;
                break;
            }
        }

        for (Profile profile : project.getActiveProfiles()) {
            String value = profile.getProperties().getProperty(key);
            if (value != null) {
                result = value;
                break;
            }
        }
        return result;
    }

    private String getActiveProfileStatement() throws ProfileException {
        Map<String, List<String>> activeProfileIds = project.getInjectedProfileIds();

        if (activeProfileIds.isEmpty()) {
            throw new ProfileException("Profile must not be empty!");
        } else {
            for (Map.Entry<String, List<String>> entry : activeProfileIds.entrySet()) {
                if (entry.getKey().indexOf("fw") > 0)
                    for (String profileId : entry.getValue()) {
                        return profileId;
                    }
            }
        }
        throw new ProfileException("Profile must be one!");
    }

    private void generate() {
        Configuration configuration = new Configuration();
        configuration.setObjectWrapper(new DefaultObjectWrapper());
        configuration.setTemplateLoader(new ClassTemplateLoader(this.getClass(), "/"));

        OutputStreamWriter writer = null;
        try {
            Template template = configuration.getTemplate("k8s-" + templateName + ".ftl");
            String filePath = project.getBuild().getDirectory() + "/" + project.getName() + ".yaml";
            getLog().info(filePath);
            File file = new File(project.getBuild().getDirectory());
            if (!file.exists()) {
                file.mkdir();
            }
            writer = new FileWriter(new File(filePath));
            Map<String, Object> context = new HashMap<String, Object>();

            context.put("project", project);
            context.put("profile", profile);
            context.put("msgGroup", convert("msgGroup"));
            context.put("DEVLOPER_NAME", convert("DEVLOPER_NAME"));
            context.put("needClient", needClient);

            String branchName = System.getenv("branchName");
            if(branchName != null && branchName.indexOf("/") > -1)
                branchName = branchName.substring(branchName.indexOf("/") + 1);
            context.put("branchName", branchName != null ? branchName : "");

            /*String profileConvert = "test";
            if ("alpha".equals(profile) || "bugfix".equals(profile)) {
                profileConvert = "dev";
            }*/
            context.put("profileConvert", profile);

            context.put("env", convert());

            if (project.getVersion().indexOf("SNAPSHOT") > 0) {
                context.put("regsecret", "regsecret");
                context.put("nexusPort", "8083");
            } else {
                context.put("regsecret", "regsecret-release");
                context.put("nexusPort", "8082");
            }


            template.process(context, writer);
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
