package com.zzp.showdoc;


import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 解析对象内容，获取表名称与属性
 *
 * @author zhangzhaopeng
 * @date 2020/11/5 4:57 下午
 */
public class AnalyzeUtil {

    private static final Pattern p_class = Pattern.compile("class\\s+\\w+((\\s*<[\\s\\S]*>(?=\\s*\\{*))|(?=\\s*\\{*))");
    private static final Pattern p_param_def = Pattern.compile("((\\w+\\s*<[\\s\\S]*>|\\w+))\\s+\\w+\\s*(=[\\s\\S]*(?=;)|(?=;))");
    private static final Pattern p_param_def_simple = Pattern.compile("\\w+\\s+\\w+\\s*(=[\\s\\S]*(?=;)|(?=;))");
    private static final Pattern p_param_def_group = Pattern.compile("\\w+\\s*<[\\s\\S]*>\\s+\\w+\\s*(=[\\s\\S]*(?=;)|(?=;))");
    private static final Pattern p_noteWhole = Pattern.compile("(?<=//)\\w*[\\s\\S]*");
    private static final Pattern p_noteStart = Pattern.compile("(?<=/\\*\\*)([\\s\\S]*(?=\\*/)|[\\s\\S]*)");
    private static final Pattern p_noteCenter = Pattern.compile("(?<=\\*)([\\s\\S]*(?=\\*/)|[\\s\\S]*)");
    private static final Pattern p_noteEnd = Pattern.compile("[\\s\\S]*(?=\\*/)");


    public static void main(String[] args) {
        String pa = "private Map<String, Integer> attackByDevice;";
        System.out.println("match = " + match(p_param_def, pa));
        System.out.println("match = " + match(p_param_def_simple, pa));
        System.out.println("match = " + match(p_param_def_group, pa));
    }


    /**
     * 解析对象
     * @param demo 对象内容
     */
    public TableInfo run(String demo){
        TableInfo tableInfo = new TableInfo();
        List<ParamInfo> paramInfoList = new ArrayList<>();
        StringBuilder lastNoteBuilder = new StringBuilder();
        boolean alreadyClass = false;

        String ret;
        String[] paramArray;
        for (String line : demo.split("\n")){
            ret = null;
            // 是否是class 行
            if (!alreadyClass && (ret = getTableName(line)) != null){
                alreadyClass = true;
                tableInfo.setTableName(ret.split("\\s+", 2)[1]);
                continue;
            }
            // 是否是长注释头部
            if ((ret = getNoteStart(line)) != null){
                lastNoteBuilder.append(ret.trim());
                continue;
            }
            // 是否是长注释尾部
            if ((ret = getNoteEnd(line)) != null){
                lastNoteBuilder.append(ret.trim());
                continue;
            }
            // 是否是长注释中间部分
            if ((ret = getNoteCenter(line)) != null){
                lastNoteBuilder.append(ret.trim());
                continue;
            }
            // 是否是参数
            if ((paramArray = getParamArray(line)) != null){
                // 是否被注释掉
                if (line.trim().startsWith("//")){
                    lastNoteBuilder.delete(0, lastNoteBuilder.length());
                    continue;
                }
                // 是否是一行注释
                if ((ret = getWholeNote(line)) != null){
                    lastNoteBuilder.append(ret.trim());
                }
                // 生成实例
                ParamInfo paramInfo = new ParamInfo(paramArray[1], paramArray[0], lastNoteBuilder.toString());
                // 添加默认值
                if (paramArray.length == 3){
                    paramInfo.setDef(paramArray[2]);
                }
                lastNoteBuilder.delete(0, lastNoteBuilder.length());
                paramInfoList.add(paramInfo);
                continue;
            }

            // 是否是一行注释
            if ((ret = getWholeNote(line)) != null){
                lastNoteBuilder.append(ret.trim());
                continue;
            }
        }

        tableInfo.setParamInfos(paramInfoList);
        return tableInfo;
    }

    private String[] getParamArray(String line){
        // 如果属性带有<>
        String ret;
        String[] sRet = null;
        if ((ret = match(p_param_def_group, line)) != null){
            if (ret.contains("=")){
                // 属性 参数 = 值 -> [属性 参数， 值]
                String[] s1 = ret.split("=", 2);
                // 属性 参数 -》 [属性，参数]
                String[] s2 = splitParamToDouble(s1[0]);
                sRet = new String[]{s2[0], s2[1], s1[1]};
            }else {
                sRet = splitParamToDouble(ret);
            }
        }else if ((ret = match(p_param_def_simple, line)) != null){
            String[] ss = ret.split("\\s+", 4);
            sRet = new String[]{ss[0], ss[1], ss.length == 4 ? ss[3] : ""};
        }
        return sRet;
    }


    private String getTableName(String line){
        return match(p_class, line);
    }

    private String getWholeNote(String line){
        return match(p_noteWhole, line);
    }
    private String getNoteStart(String line){
        return match(p_noteStart, line);
    }

    private String getNoteEnd(String line){
        return match(p_noteEnd, line);
    }

    private String getNoteCenter(String line){
        return match(p_noteCenter, line);
    }


    private static String match(Pattern pattern, String line){
        Matcher matcher = pattern.matcher(line);
        if (matcher.find()) {
            return matcher.group();
        }
        return null;
    }

    /**
     *  (属性 参数) 切割成数组【属性，参数】
     *  需要注意属性中带有空格的问题，比如 Map<String, String>
     * @param line
     * @return
     */
    private String[] splitParamToDouble(String line){
        String[] s1 = line.split("\\s+");
        StringBuilder typeBuilder = new StringBuilder();
        for (int i = 0; i < s1.length-1; i ++){
            typeBuilder.append(s1[i]).append(" ");
        }
        typeBuilder.delete(typeBuilder.length()-1, typeBuilder.length());
        return new String[]{typeBuilder.toString(), s1[s1.length-1]};
    }

}
