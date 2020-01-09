/*
 * Copyright (C), 2013-2014, 汽车街股份有限公司
 * FileName: ${domainName}Controller.java
 * Author:   ${classAuthor}
 * Date:     ${currDate}
 * Description: //${packageName}业务逻辑类
 * History: //修改记录
 * <author>      <time>      <version>    <desc>
 * 修改人姓名                           修改时间                        版本号                                描述
 */
package com.autostreets.web.controller.${moduleName};

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import com.alibaba.fastjson.JSON;
import com.autostreets.core.Pagination;
import com.autostreets.util.UUIDUtil;
import com.autostreets.model<#if (moduleName)??>.${moduleName}</#if>.${domainName};
import com.autostreets.service<#if (moduleName)??>.${moduleName}</#if>.${domainName}Service;

/**
 * ${packageName}业务逻辑类<br> 
 *
 * @author ${classAuthor}
 * @since [产品/模块版本] （可选）
 */
@Controller
@RequestMapping(value = "/${smallDomainName}")
public class ${domainName}Controller extends BaseController {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(UserController.class);
	@Autowired
	private ${domainName}Service ${smallDomainName}Service;
	
	/**
	 * ${packageName}分页查询<br> 
	 * @author ${classAuthor}
	 * @since [产品/模块版本] （可选）
	 */
	@RequestMapping("/index")
	public String list(Pagination pagination, ${domainName} ${smallDomainName}, Model model) {
        pagination = ${smallDomainName}Service.selectSplit${domainName}(pagination, ${domainName}); // 分页查询
		return "<#if (moduleName)??>/${moduleName}/</#if>" + "${domainName}/index";
	}

	/**
	 * ${packageName}新增<br> 
	 * @author ${classAuthor}
	 * @since [产品/模块版本] （可选）
	 */
	@RequestMapping(value = "/add")
	public String add(${domainName} ${smallDomainName}) {
		return "<#if (moduleName)??>/${moduleName}/</#if>" + "${domainName}/add";
	}

	/**
	 * ${packageName}新增保存<br> 
	 * @author ${classAuthor}
	 * @since [产品/模块版本] （可选）
	 */
	@RequestMapping(value = "/save")
	@ResponseBody
	public String save(${domainName} ${smallDomainName}, Model model) {
		try {
            <#if dataType == 'varchar'>${smallDomainName}.setSid(UUIDUtil.getUUID());</#if>
            ${smallDomainName}.setCreatedTime(new Date());
            ${smallDomainName}Service.insert(${domainName});
            return true;
        } catch (Exception e) {
            LOGGER.error("保存${packageName}失败：" + JSON.toJSONString(${smallDomainName}));
            e.printStackTrace();
            return false;
        }
	}
	
	/**
	 * ${packageName}编辑<br> 
	 * @author ${classAuthor}
	 * @since [产品/模块版本] （可选）
	 */
	@RequestMapping(value = "/edit")
	public String edit(${domainName} ${smallDomainName}) {
		return "<#if (moduleName)??>/${moduleName}/</#if>" + "${domainName}/add";
	}

	/**
	 * ${packageName}编辑保存<br> 
	 * @author ${classAuthor}
	 * @since [产品/模块版本] （可选）
	 */
	@RequestMapping(value = "/update")
	@ResponseBody
	public String update(${domainName} ${smallDomainName}, Model model) {
		try {
            ${smallDomainName}.setModifyTime(new Date());
            ${smallDomainName}Service.updateByPrimaryKey(${smallDomainName});
            return true;
        } catch (Exception e) {
            LOGGER.error("更新${packageName}失败：" + JSON.toJSONString(${smallDomainName}));
            e.printStackTrace();
            return false;
        }
	}
	
	/**
	 * ${packageName}删除<br> 
	 * @author ${classAuthor}
	 * @since [产品/模块版本] （可选）
	 */
	@RequestMapping(value = "delete")
	public String delete(String sid) {
		try {
            ${smallDomainName}Service.deleteByPrimaryKey(sid);
            return true;
        } catch (Exception e) {
            LOGGER.error("删除${packageName}失败" + JSON.toJSONString(sid));
            e.printStackTrace();
            return false;
        }
	}
	
	/**
	 * ${packageName}查看详情<br> 
	 * @author ${classAuthor}
	 * @since [产品/模块版本] （可选）
	 */
	@RequestMapping(value = "/show")
	public String show(${domainName} ${smallDomainName}, , String sid) {
		${domainName} ${smallDomainName} = ${smallDomainName}Service.selectByPrimaryKey(sid);
		return "<#if (moduleName)??>/${moduleName}/</#if>" + "${domainName}/show";
	}
}
