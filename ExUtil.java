package com.zzp.showdoc;


import java.util.HashMap;

/**
 * 属性生成表格样式
 * @author zhangzhaopeng
 * @date 2020/11/5 6:42 下午
 */
public class ExUtil {

    private static HashMap<String, String> cnNameMap = new HashMap<>();
    private static HashMap<String, String[]> titleMap = new HashMap<>();
    private static final String table_name_format = "##### %s :"; // 数据表名称
    static {
        // 注册标题（列）名称，key（英文） 在代码中使用，value（中文名称）在生成的表中使用，中文名称可改
        cnNameMap.put("cnName", "名称");
        cnNameMap.put("type", "类型");
        cnNameMap.put("must", "必选");
        cnNameMap.put("def", "默认");
        cnNameMap.put("note", "注释");
        // 1.title排版与命名 request 是入参格式，response是出参格式
        // 2.标题按照在数组中的位置排序
        //   如 ["cnName", "type", "note"],在生成的表中将展示为 "名称 类型 注释"
        // 3.展示数量及顺序可调整，但仅限于上述5个标题
        titleMap.put("request", new String[]{"cnName", "type", "must", "note"});
        titleMap.put("response", new String[]{"cnName", "type", "note"});
    }


    /**
     * 根据解析后的class信息生成 show-doc 数据表
     * @param titleType 选择格式，"request" or "response"
     * @param info 解析后的类或属性信息
     * @return
     */
    public String getEx(String titleType, TableInfo info){
        String[] titleArr = titleMap.get(titleType);
        StringBuilder builder = new StringBuilder();
        builder.append(String.format(table_name_format, info.getTableName()) + "\n\n");
        StringBuilder br = new StringBuilder();
        for (String title : titleArr){
            builder.append("|").append(cnNameMap.get(title)).append(" ");
            br.append("|: ----");
        }
        builder.append("|\n").append(br).append("|\n");
        for (ParamInfo paramInfo : info.getParamInfos()){
            for (int i = 0; i < titleArr.length; i ++){
                if ("cnName".equalsIgnoreCase(titleArr[i])){
                    builder.append("|").append(paramInfo.getName()).append(" ");
                }if ("type".equalsIgnoreCase(titleArr[i])){
                    builder.append("|").append(paramInfo.getType()).append(" ");
                }else if ("must".equalsIgnoreCase(titleArr[i])){
                    builder.append("|").append(paramInfo.getMust() ? "是" : "否").append(" ");
                }else if ("def".equalsIgnoreCase(titleArr[i])){
                    builder.append("|").append(paramInfo.getDef()).append(" ");
                }else if ("note".equalsIgnoreCase(titleArr[i])){
                    builder.append("|").append(paramInfo.getNote()).append(" ");
                }
            }
            builder.append("|\n");
        }
        return builder.toString();
    }








}
