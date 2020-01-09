package generator.plugins;

import java.util.List;

import com.itfsw.mybatis.generator.plugins.SelectiveEnhancedPlugin;
import org.mybatis.generator.api.IntrospectedTable;
import org.mybatis.generator.api.PluginAdapter;
import org.mybatis.generator.api.dom.java.FullyQualifiedJavaType;
import org.mybatis.generator.api.dom.xml.Attribute;
import org.mybatis.generator.api.dom.xml.XmlElement;

/**
 * MySQL 返回主键属性插件<br>
 * 在insert节点中增加useGeneratedKeys和keyProperty属性，增加记录后可以直接返回生成的自增长主键
 *
 * @author wwy
 * @version 1.0.0.0
 */
public final class MySQLGeneratorPrimaryKeyPlugin extends PluginAdapter {//SelectiveEnhancedPlugin {

    @Override
    public boolean sqlMapInsertElementGenerated(XmlElement element, IntrospectedTable introspectedTable) {
        if (introspectedTable.getPrimaryKeyColumns().size() == 1) {
            FullyQualifiedJavaType fullyQualifiedJavaType = introspectedTable.getPrimaryKeyColumns().get(0)
                    .getFullyQualifiedJavaType();
            String primaryKeyClassTypeName = fullyQualifiedJavaType.getFullyQualifiedName();
            if ("java.lang.Integer".equals(primaryKeyClassTypeName) || "java.lang.Long".equals(primaryKeyClassTypeName)) {
                element.addAttribute(new Attribute("useGeneratedKeys", "true"));
                element.addAttribute(new Attribute("keyProperty",
                        introspectedTable.getPrimaryKeyColumns().get(0).getJavaProperty()));
            }
        } else {
            System.out.println("主键不止一个，不生成useGeneratedKeys和keyProperty属性!");
        }
        return super.sqlMapInsertElementGenerated(element, introspectedTable);
    }

    @Override
    public boolean sqlMapInsertSelectiveElementGenerated(XmlElement element, IntrospectedTable introspectedTable) {
        boolean bool = super.sqlMapInsertSelectiveElementGenerated(element, introspectedTable);
        if (introspectedTable.getPrimaryKeyColumns().size() == 1) {
            FullyQualifiedJavaType fullyQualifiedJavaType = introspectedTable.getPrimaryKeyColumns().get(0)
                    .getFullyQualifiedJavaType();
            String primaryKeyClassTypeName = fullyQualifiedJavaType.getFullyQualifiedName();
            if ("java.lang.Integer".equals(primaryKeyClassTypeName) || "java.lang.Long".equals(primaryKeyClassTypeName)) {
                element.addAttribute(new Attribute("useGeneratedKeys", "true"));
                element.addAttribute(new Attribute("keyProperty",
                        introspectedTable.getPrimaryKeyColumns().get(0).getJavaProperty()));
            }
        } else {
            System.out.println("主键不止一个，不生成useGeneratedKeys和keyProperty属性!");
        }
        return bool;
    }

    @Override
    public boolean validate(List<String> warnings) {
        return true;
    }
}