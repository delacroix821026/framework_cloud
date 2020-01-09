package ${model.packageName};

<#list model.importList as importStr>
import ${importStr};
</#list>

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.web.bind.annotation.RequestBody;
<#if model.requestMapping??>
import org.springframework.web.bind.annotation.RequestMapping;
</#if>
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.utils.List;
import java.utils.Map;

/**
 * ${model.serviceName} client.
 */
@RestController
@RefreshScope
@Slf4j
<#if model.requestMapping??>
@RequestMapping(value = {<#list model.requestMapping.value() as vl><#if vl_index!=0> ,</#if>"${vl}"</#list>})

</#if>
public class ${model.className} {
    /**
     * {@link ${model.serviceName}}.
     */
    @Autowired
    private ${model.serviceName} ${model.serviceVariable};

<#if model.methods??>
    <#list model.methods as method>
        <#if method.annotations??>
            <#list method.annotations as annotation>
                <#if annotation.annotationType().simpleName == "RequestMapping">
    @${annotation.annotationType().simpleName}(value = {<#list annotation.value() as val><#if val_index!=0> ,</#if>"${val}"</#list>}<#if annotation.method()?? && annotation.method()?size gt 0>, method = {<#list annotation.method() as requestMethod><#if requestMethod_index!=0> ,</#if>RequestMethod.${requestMethod}</#list>}</#if>)
                </#if>
            </#list>
        </#if>
    public ${method.returnType.simpleName} ${method.name}(<#if method.parameters?? && method.parameters?size gt 0><#list method.parameters as parameter><#if parameter_index!=0> ,</#if><#list parameter.annotations as paramAnnotation>@${paramAnnotation.annotationType().simpleName}(
    <#if paramAnnotation.annotationType().getDeclaredMethods()?? && paramAnnotation.annotationType().getDeclaredMethods()?size gt 0>
    <#assign addPoint = false/>
    <#list paramAnnotation.annotationType().getDeclaredMethods() as method>
        <#assign result = ReflectAnnotation.getAnnotationValue(paramAnnotation, method.name)/>
        <#if result?is_boolean>
            <#if !addPoint><#assign addPoint = true/><#else> ,</#if>${method.name} = ${result?c}
        <#elseif result != "">
            <#if !addPoint><#assign addPoint = true/><#else> ,</#if>${method.name} = "${result}"
        </#if>
    </#list>
    </#if>
) </#list>${parameter.type.simpleName} ${parameter.name}</#list></#if>) {
        <#if method.returnType.simpleName != "void">return </#if>${model.serviceVariable}.${method.name}(<#if method.parameters?? && method.parameters?size gt 0><#list method.parameters as parameter><#if parameter_index!=0> ,</#if><#if parameter.annotations?? && parameter.annotations?size gt 0></#if>${parameter.name}</#list></#if>);
    }

    </#list>
</#if>
}