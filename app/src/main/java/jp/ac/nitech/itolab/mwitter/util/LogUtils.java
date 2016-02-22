package jp.ac.nitech.itolab.mwitter.util;

import android.util.Log;

/**
 * ログ周り用ユーリティティ
 * Created by masayuki on 13/02/2016.
 */
public class LogUtils {

    /**
     * ログ出力用タグを生成する
     * @param clazz
     * @return
     */
    public static String makeLogTag(Class<?> clazz) {
        return clazz.getSimpleName();
    }

    public static void LOGD(String tag, String msg) {
        Log.d(tag, msg);
    }
    public static void LOGD(String tag, String msg, Throwable tr) {
        Log.d(tag, msg, tr);
    }

    public static void LOGI(String tag, String msg) {
        Log.i(tag, msg);
    }
    public static void LOGI(String tag, String msg, Throwable tr) {
        Log.i(tag, msg, tr);
    }

    public static void LOGW(String tag, String msg) {
        Log.w(tag, msg);
    }
    public static void LOGW(String tag, String msg, Throwable tr) {
        Log.w(tag, msg, tr);
    }

    public static void LOGE(String tag, String msg) {
        Log.e(tag, msg);
    }
    public static void LOGE(String tag, String msg, Throwable tr) {
        Log.e(tag, msg, tr);
    }


}
