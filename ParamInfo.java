package com.zzp.showdoc;

/**
 * @author zhangzhaopeng
 * @date 2020/11/5 4:48 下午
 */
public class ParamInfo {


    private String name; // 参数名称

    /**
     * 类型
     */
    private String type;

    // 默认数据
    private String def;

    /** kkkkkkk */
    private Boolean must = true;

    /**
     注释 */
    private String note = "  没有 注释！";

    public ParamInfo(String name, String type, String note) {
        this.name = name;
        this.type = type;
        this.note = note;
        this.def = "";
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getDef() {
        return def;
    }

    public void setDef(String def) {
        this.def = def;
    }

    public Boolean getMust() {
        return must;
    }

    public void setMust(Boolean must) {
        this.must = must;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }
}
