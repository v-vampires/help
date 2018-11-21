package codeGenerate;

import com.google.common.collect.Lists;
import freemarker.template.Template;

import java.io.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by fitz.li on 2017/12/19.
 */
public class CodeGenerateUtils {

    private final String diskPath="D://";
    private final String packageName = "codeGenerate";
    private final String AUTHOR = "CodeGenerator";

    public static void main(String[] args) throws Exception {
        CodeGenerateUtils codeGenerateUtils = new CodeGenerateUtils();
        codeGenerateUtils.generate();
    }

    public TableMetaInfo init(){
        String tableName = "City";
        List<String> columns = Lists.newArrayList("code","enName","zhName","belongCountry");
        return new TableMetaInfo(tableName, columns);
    }

    public void generate() throws Exception{
        TableMetaInfo tableMetaInfo = init();
        //生成Model文件
        generateModelFile(tableMetaInfo);
    }

    private void generateModelFile(TableMetaInfo tableMetaInfo) throws Exception {
        final String suffix = ".java";
        final String path = diskPath + tableMetaInfo.getTableName() + suffix;
        final String templateName = "Model.ftl";
        File mapperFile = new File(path);
        generateFileByTemplate(templateName,mapperFile,tableMetaInfo);
    }

    private void generateFileByTemplate(String templateName, File mapperFile, TableMetaInfo tableMetaInfo) throws Exception {
        Template template = FreeMarkerTemplateUtils.getTemplate(templateName);
        FileOutputStream fos = new FileOutputStream(mapperFile);
        Map<String,Object> dataMap = new HashMap<>();
        dataMap.put("tableMetaInfo",tableMetaInfo);
        dataMap.put("author",AUTHOR);
        dataMap.put("packageName",packageName);
        Writer out = new BufferedWriter(new OutputStreamWriter(System.out, "utf-8"),10240);
        template.process(dataMap,out);
    }


}
