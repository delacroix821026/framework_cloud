package com.fw.common.config;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportResource;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@Configuration
//@ImportResource(value = "classpath*:context-transaction.xml")
@EnableTransactionManagement
@MapperScan("com.**.mapper")
public class OrmConfigration {
}
