package fun.nemo.community.utils;

import android.util.Log;

import fun.nemo.community.BuildConfig;

import static fun.nemo.community.utils.MethodLineUtil.createLog;
import static fun.nemo.community.utils.MethodLineUtil.getMethodNames;


/**
 * @author liuyufei
 */
public class LogUtil {


    private LogUtil(){
    }

    public static boolean isDebuggable() {
        return BuildConfig.DEBUG;
    }


    public static void e(String message){
        if (!isDebuggable()) {
            return;
        }
        getMethodNames(new Throwable().getStackTrace());
        Log.e(MethodLineUtil.className, createLog(message));
    }

    public static void i(String message){
        if (!isDebuggable()) {
            return;
        }

        getMethodNames(new Throwable().getStackTrace());
        Log.i(MethodLineUtil.className, createLog(message));
    }

    public static void d(String message){
        if (!isDebuggable()) {
            return;
        }
        getMethodNames(new Throwable().getStackTrace());
        Log.d(MethodLineUtil.className, createLog(message));
    }

    public static void v(String message){
        if (!isDebuggable()) {
            return;
        }
        getMethodNames(new Throwable().getStackTrace());
        Log.v(MethodLineUtil.className, createLog(message));
    }

    public static void w(String message){
        if (!isDebuggable()) {
            return;
        }
        getMethodNames(new Throwable().getStackTrace());
        Log.w(MethodLineUtil.className, createLog(message));
    }

    public static void wtf(String message){
        if (!isDebuggable()) {
            return;
        }
        getMethodNames(new Throwable().getStackTrace());
        Log.wtf(MethodLineUtil.className, createLog(message));
    }
}
