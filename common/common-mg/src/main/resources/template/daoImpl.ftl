package ${package}.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import ${mapperPackage};
import ${genericMapperPackage};
import ${genericServiceImplPackage};
import ${baseModelPackage}.${domainName};
import ${baseModelPackage}.${domainName}Example;
import ${package}.${domainName}Dao;

@Repository("${smallDomainName}Dao")
public class ${domainName}DaoImpl extends GenericServiceImpl<${domainName}, ${domainName}Example, ${primaryKeyType}> implements ${domainName}Dao {

	@Autowired
	private ${domainName}Mapper ${smallDomainName}Mapper;

	@Override
    protected GenericMapper<${domainName}, ${domainName}Example, ${primaryKeyType}> getGenericMapper() {
        return ${smallDomainName}Mapper;
	}
}
