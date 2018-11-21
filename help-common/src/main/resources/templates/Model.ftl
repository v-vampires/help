package ${packageName}.model;
/**
* 描述：${tableMetaInfo.tableName}模型
* @author ${author}
*/
public class ${tableMetaInfo.tableName}{

<#if tableMetaInfo.columns?exists>
    <#list tableMetaInfo.columns as column>
    /**
    *${column!}
    */
    private String ${column};
    </#list>
</#if>

<#if tableMetaInfo.columns?exists>
    <#list tableMetaInfo.columns as column>
    public String get${column?cap_first}() {
        return this.${column};
    }
    public void set${column?cap_first}(String ${column}) {
        this.${column} = ${column};
    }
    </#list>
</#if>

}
