package com.zzp.showdoc;

import java.util.List;

/**
 * @author zhangzhaopeng
 * @date 2020/11/5 5:48 下午
 */
public class TableInfo {

    private String tableName = "Unknown";

    private List<ParamInfo> paramInfos;

    public TableInfo() {
    }

    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public List<ParamInfo> getParamInfos() {
        return paramInfos;
    }

    public void setParamInfos(List<ParamInfo> paramInfos) {
        this.paramInfos = paramInfos;
    }
}
