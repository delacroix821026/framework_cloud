package ${package}.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ${daoPath}.${domainName}Dao;
import ${package}.${domainName}Service;

@Service("${smallDomainName}Service")
public class ${domainName}ServiceImpl implements ${domainName}Service {

    @Autowired
    private ${domainName}Dao ${smallDomainName}Dao;

}
