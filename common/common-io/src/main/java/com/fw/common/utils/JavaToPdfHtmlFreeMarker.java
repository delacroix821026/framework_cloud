package com.fw.common.utils;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.tool.xml.XMLWorkerFontProvider;
import com.itextpdf.tool.xml.XMLWorkerHelper;

import freemarker.template.Configuration;
import freemarker.template.Template;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 生成PDF工具
 *
 * @author Liam
 * @since 2017年9月27日下午1:45:25
 */
public class JavaToPdfHtmlFreeMarker {

    private static final Logger LOGGER = LoggerFactory.getLogger(JavaToPdfHtmlFreeMarker.class);
    private static final String FONT = "";

    public static Template getTemplate(String template) {
        Configuration configuration = new Configuration(Configuration.VERSION_2_3_22);
        configuration.setDefaultEncoding("utf-8");
        try {
            configuration.setDirectoryForTemplateLoading(new File(System.getProperty("sys.root") + "WEB-INF/classes/META-INF/template"));
            return configuration.getTemplate(template + ".ftl");
        } catch (IOException e) {
            LOGGER.error(e.getMessage(), e);
            throw new RuntimeException(e);
        }
    }


    public static void main(String[] args) throws IOException, DocumentException {
        String DEST = "/app/file/HelloWorld_CN_HTML_FREEMARKER.pdf";
        String HTMLDEST = "/app/file/HelloWorld_CN_HTML_FREEMARKER.html";
        //  String HTML = "/META-INF/template/modules/app_overview.ftl";
        String HTML = "/META-INF/template/modules/app_zhixin_court.ftl";

        Map<String, Object> data = new HashMap<>();
        String content = JavaToPdfHtmlFreeMarker.freeMarkerRender(data, HTML);
        createHTML(content, HTMLDEST);
        createPdf(content, DEST);
    }

    /**
     * 生成html
     * date: 2017年9月27日 下午1:49:58
     *
     * @param content 内容
     * @param dest    目录
     * @throws IOException       异常
     * @throws DocumentException 异常
     * @author Liam
     * @since JDK 1.8
     */
    public static void createHTML(String content, String dest) throws IOException, DocumentException {
        FileOutputStream fos = new FileOutputStream(dest); // 把jsp输出的内容写到xxx.htm
        fos.write(content.getBytes());
        fos.close();
    }

    /**
     * 生成pdf
     * date: 2017年9月27日 下午1:50:27
     *
     * @param content 内容
     * @param dest    目录
     * @throws IOException       异常
     * @throws DocumentException 异常
     * @author Liam
     * @since JDK 1.8
     */
    public static void createPdf(String content, String dest) throws IOException, DocumentException {
        // step 1
        Document document = new Document();
        // step 2
        PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream(dest));
        // step 3
        document.open();
        // step 4
        XMLWorkerFontProvider fontImp = new XMLWorkerFontProvider(XMLWorkerFontProvider.DONTLOOKFORFONTS);
        fontImp.register(FONT);
        XMLWorkerHelper.getInstance().parseXHtml(writer, document,
                new ByteArrayInputStream(content.getBytes()), null, Charset.forName("UTF-8"), fontImp);
        // step 5
        document.close();
    }

    /**
     * freemarker渲染html
     *
     * @param data    Map
     * @param htmlTmp String
     * @return String
     */
    public static String freeMarkerRender(Map<String, Object> data, String htmlTmp) {
        try (Writer out = new StringWriter()) {
            // 获取模板,并设置编码方式
            Template template = getTemplate(htmlTmp);
            // 合并数据模型与模板
            template.process(data, out); //将合并后的数据和模板写入到流中，这里使用的字符流
            out.flush();
            return out.toString();
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
        }
        return null;
    }
}