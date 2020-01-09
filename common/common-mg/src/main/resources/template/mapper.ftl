package ${package};

import ${baseModelPackage}.${domainName};
import ${baseModelPackage}.${domainName}Example;
import ${genericMapperPackage};
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface ${domainName}Mapper extends GenericMapper<${domainName}, ${domainName}Example, ${primaryKeyType}> {

}
