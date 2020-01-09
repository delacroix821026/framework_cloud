package generator.plugins;

import java.util.Iterator;
import java.util.List;

import org.mybatis.generator.api.IntrospectedTable;
import org.mybatis.generator.api.PluginAdapter;
import org.mybatis.generator.api.dom.xml.Attribute;
import org.mybatis.generator.api.dom.xml.Document;
import org.mybatis.generator.api.dom.xml.Element;
import org.mybatis.generator.api.dom.xml.XmlElement;

public class BaseClassPlugin extends PluginAdapter {

    @Override
    public boolean validate(List<String> warnings) {
        return true;
    }

    private String mapperPackage = null;
    private String domainPackage = null;

    @Override
    public void initialized(IntrospectedTable introspectedTable) {
        mapperPackage = introspectedTable.getTableConfigurationProperty("mapperPackage");
        domainPackage = introspectedTable.getTableConfigurationProperty("domainPackage");
        String domain = introspectedTable.getTableConfiguration().getDomainObjectName();
        String mapperInterfacePackage = null;
        String myBatis3JavaMapperType = introspectedTable.getMyBatis3JavaMapperType();
        System.out.println("------------------------");
        System.out.println(myBatis3JavaMapperType);
        System.out.println("------------------------");
        if (mapperPackage != null) {
            String domainMapper = domain + "Mapper";
            mapperInterfacePackage = myBatis3JavaMapperType.replace(domainMapper, mapperPackage + "." + domainMapper);
            introspectedTable.setMyBatis3JavaMapperType(mapperInterfacePackage);
        }
        String oldExampleType = introspectedTable.getExampleType();
        introspectedTable.setExampleType(replaceOldType(domainPackage, oldExampleType));
        String oldBaseType = introspectedTable.getBaseRecordType();
        introspectedTable.setBaseRecordType(replaceOldType(domainPackage, oldBaseType));
    }

    private String replaceOldType(String shortNamePackage, String oldType) {
        if (shortNamePackage == null) {
            shortNamePackage = mapperPackage;
        }
        int index = oldType.lastIndexOf(".");
        if (index == -1) {
            throw new RuntimeException("replaceOldType add \'base\' failure ");
        }
        String newType = null;
        if (shortNamePackage != null && shortNamePackage.length() > 0) {
            newType = oldType.substring(0, index + 1) + shortNamePackage + "." + "Base"
                    + oldType.substring(index + 1, oldType.length());
        } else {
            newType = oldType.substring(0, index + 1) + "Base" + oldType.substring(index + 1, oldType.length());
        }
        return newType;
    }

    private String removeBaseStr(String type) {
        int index = type.lastIndexOf(".");
        if (index == -1) {
            throw new RuntimeException("replaceOldType remove \'base\' failure ");
        }
        if (domainPackage != null) {
            int domainPackageIndex = type.lastIndexOf(domainPackage + ".");// wwy.model.base.BaseUser
            return type.substring(0, domainPackageIndex) + type.substring(index + 5, type.length());
        } else {
            return type.substring(0, index + 1) + type.substring(index + 5, type.length());
        }
    }

    @Override
    public boolean sqlMapDocumentGenerated(Document document, IntrospectedTable introspectedTable) {
        XmlElement rootElement = document.getRootElement();
        List<Element> elements = rootElement.getElements();
        for (Element element : elements) {
            if (element instanceof XmlElement) {
                List<Attribute> attrList = ((XmlElement) element).getAttributes();
                Iterator<Attribute> it = attrList.iterator();
                while (it.hasNext()) {
                    Attribute attr = it.next();
                    String oldValue = attr.getValue();
                    if (oldValue.equals(introspectedTable.getBaseRecordType())
                            || oldValue.equals(introspectedTable.getExampleType())) {
                        String name = attr.getName();
                        it.remove();
                        attrList.add(new Attribute(name, removeBaseStr(oldValue)));
                        break;
                    }
                }

            }
        }
        return super.sqlMapDocumentGenerated(document, introspectedTable);
    }
}
