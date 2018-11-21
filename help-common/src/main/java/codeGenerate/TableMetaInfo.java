package codeGenerate;

import java.util.List;

public class TableMetaInfo {
    private String tableName;
    private List<String> columns;

    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public List<String> getColumns() {
        return columns;
    }

    public void setColumns(List<String> columns) {
        this.columns = columns;
    }

    public TableMetaInfo(String tableName, List<String> columns) {
        this.tableName = tableName;
        this.columns = columns;
    }
}