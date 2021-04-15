package fun.nemo.community.utils;

/**
 * 获取方法名 和所在行
 *
 * @author liuyufei
 * @version MethodLineUtil.java, v 0.1 2019-08-24 17:51 liuyufei
 */
public class MethodLineUtil {
    public static volatile String className;
    public static volatile String methodName;
    public static volatile int lineNumber;

    public static String createLog(String log) {
        return "(" + className + ":" + lineNumber + ")" +
                log;
    }

    public static void getMethodNames(StackTraceElement[] sElements) {
        className = sElements[1].getFileName();
        methodName = sElements[1].getMethodName();
        lineNumber = sElements[1].getLineNumber();
    }

    public static String getCurMethodLog(String log) {
        getMethodNames(new Throwable().getStackTrace());
        return createLog(log);
    }
}
