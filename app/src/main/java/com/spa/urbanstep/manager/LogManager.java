package com.spa.urbanstep.manager;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.util.Log;

import com.spa.urbanstep.App;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * Manager to write to the log.
 *
 * @author alexander.ivanov
 */
public class LogManager {

    private static final boolean log;
    private static final boolean debugable;
    private static Method _getApplicationInfo;

    static {
        initCompatibility();
        debugable = (getApplicationInfo(App.Companion.applicationContext()).flags & ApplicationInfo.FLAG_DEBUGGABLE) != 0;
        log = debugable;
    }

    private static void initCompatibility() {
        try {
            _getApplicationInfo = Context.class.getMethod("getApplicationInfo");
        } catch (NoSuchMethodException nsme) {
        }
    }

    public static ApplicationInfo getApplicationInfo(Context context) {
        ApplicationInfo applicationInfo;
        if (_getApplicationInfo != null) {
            try {
                applicationInfo = (ApplicationInfo) _getApplicationInfo
                        .invoke(context);
            } catch (InvocationTargetException e) {
                Throwable cause = e.getCause();
                if (cause instanceof RuntimeException) {
                    throw (RuntimeException) cause;
                } else if (cause instanceof Error) {
                    throw (Error) cause;
                } else {
                    throw new RuntimeException(e);
                }
            } catch (IllegalAccessException ie) {
                throw new RuntimeException(ie);
            }
        } else {
            try {
                applicationInfo = context.getPackageManager()
                        .getApplicationInfo(context.getPackageName(), 0);
            } catch (NameNotFoundException e) {
                Log.e("LogManager",
                        "I can`t find my package in the system. Debug will be disabled.");
                applicationInfo = new ApplicationInfo();
                applicationInfo.flags = 0;
            }
        }
        return applicationInfo;
    }

    private final static LogManager instance;

    static {
        instance = new LogManager(App.Companion.applicationContext());
       // DayZero.getInstance().addManager(instance);
    }

    public static LogManager getInstance() {
        return instance;
    }

    private LogManager(Context application) {
    }


    static public int dString(String tag, String msg) {
        if (log)
            return Log.d(tag, msg);
        else
            return 0;
    }

    static public int eString(String tag, String msg) {
        if (log)
            return Log.e(tag, msg);
        else
            return 0;
    }

    static public int iString(String tag, String msg) {
        if (log)
            return Log.i(tag, msg);
        else
            return 0;
    }

    static public int wString(String tag, String msg) {
        if (log)
            return Log.w(tag, msg);
        else
            return 0;
    }

    static public int vString(String tag, String msg) {
        if (log)
            return Log.v(tag, msg);
        else
            return 0;
    }

    static public int d(Object obj, String msg) {
        return dString(obj.toString(), msg);
    }

    static public int e(Object obj, String msg) {
        return eString(obj.toString(), msg);
    }

    static public int i(Object obj, String msg) {
        return iString(obj.toString(), msg);
    }

    static public int w(Object obj, String msg) {
        return wString(obj.toString(), msg);
    }

    static public int v(Object obj, String msg) {
        return vString(obj.toString(), msg);
    }

    /**
     * Print stack trace if log is enabled.
     *
     * @param obj
     * @param exception
     */
    public static void exception(Object obj, Exception exception) {
        if (!log)
            return;
        forceException(obj, exception);
    }

    /**
     * Print stack trace even if log is disabled.
     *
     * @param obj
     * @param exception
     */
    public static void forceException(Object obj, Exception exception) {
        System.err.println(obj.toString());
        System.err.println(getStackTrace(exception));
    }

    /**
     * @param exception
     * @return stack trace.
     */
    private static String getStackTrace(Exception exception) {
        final StringWriter result = new StringWriter();
        final PrintWriter printWriter = new PrintWriter(result);
        exception.printStackTrace(printWriter);
        return result.toString();
    }

    public static boolean isDebugable() {
        return debugable;
    }

}
