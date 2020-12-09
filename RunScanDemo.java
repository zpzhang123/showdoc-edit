package com.zzp.showdoc;


import java.util.Scanner;

/**
 * @author zhangzhaopeng
 * @date 2020/11/5 7:12 下午
 */
public class RunScanDemo {

    private static Scanner scanner = new Scanner(System.in);
    private static AnalyzeUtil analyzeUtil = new AnalyzeUtil();
    private static ExUtil exUtil = new ExUtil();

    private static final String stopCommon = "stop"; // 结束命令，任何时候可输入
    private static final String resetCommon = "reset";// 重置命令，任何时候可输入
    private static final String okCommon = "ok"; // 确认命令，结束对象输入，执行解析操作

    /**
     * 测试用例： ParamInfo, 使用了常见的注释方式
     * @param args
     */
    public static void main(String[] args) {
        String titleType = "";

        while (true) {
            boolean reset = false;
            System.out.println("\n================================");
            while (true) {
                System.out.println("请选择标题类型：\t 1.入参 2.出参");
                String line = scanner.nextLine();
                if ("1".equals(line.trim())) {
                    titleType = "request";
                    break;
                } else if ("2".equals(line)) {
                    titleType = "response";
                    break;
                } else if (stopCommon.equalsIgnoreCase(line)) {
                    return;
                } else if (resetCommon.equalsIgnoreCase(line)) {
                    reset = true;
                    break;
                }
            }
            if (reset) {
                break;
            }
            System.out.println("请输入对象属性：\n");
            StringBuilder clazz = new StringBuilder();
            boolean ok = false;
            while (true) {
                String pl = scanner.nextLine();
                if (stopCommon.equalsIgnoreCase(pl)) {
                    return;
                } else if (okCommon.equalsIgnoreCase(pl)) {
                    ok = true;
                    break;
                } else if (resetCommon.equalsIgnoreCase(pl)) {
                    break;
                }
                clazz.append(pl + "\n");
            }
            if (ok) {
                String msg = exUtil.getEx(titleType, analyzeUtil.run(clazz.toString()));
                System.out.println(msg);
            }
        }

    }

}
