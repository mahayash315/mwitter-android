package jp.ac.nitech.itolab.mwitter.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;

import java.text.ParseException;
import java.util.Date;

import jp.ac.nitech.itolab.mwitter.Config;

import static jp.ac.nitech.itolab.mwitter.util.LogUtils.LOGE;
import static jp.ac.nitech.itolab.mwitter.util.LogUtils.makeLogTag;

/**
 * {@link SharedPreferences} 周りのユーティリティ
 * Created by masayuki on 13/02/2016.
 */
public class PreferencesUtils {
    private static final String TAG = makeLogTag(PreferencesUtils.class);
    private static final String PREFERENCES_NAME = Config.App.Preferences.NAME;

    public static class Tweet {
        private static final String REFERENCE_TIME = "tweet.reference_time";

        public static Date getReferenceTime(Context context, Date defValue) {
            long time = getPreferences(context).getLong(REFERENCE_TIME, -1);
            if (time < 0) {
                return defValue;
            }
            return new Date(time);
        }
        public static void setReferenceTime(Context context, Date referenceTime) {
            getPreferences(context).edit().putLong(REFERENCE_TIME, referenceTime.getTime()).apply();
        }
    }

    public static class User {
        private static final String MY_UID = "user.my_uid";
        private static final String AUTH_USERNAME = "user.auth_username";
        private static final String AUTH_PASSWORD = "user.auth_password";
        private static final String AUTH_TOKEN = "user.auth_token";

        public static long getMyUid(Context context, long defValue) {
            return getPreferences(context).getLong(MY_UID, defValue);
        }
        public static void setMyUid(Context context, long myUid) {
            getPreferences(context).edit().putLong(MY_UID, myUid).apply();
        }

        public static String getAuthUsername(Context context, String defValue) {
            return getPreferences(context).getString(AUTH_USERNAME, defValue);
        }
        public static void setAuthUsername(Context context, String authUsername) {
            getPreferences(context).edit().putString(AUTH_USERNAME, authUsername).apply();
        }

        public static String getAuthPassword(Context context, String defValue) {
            return getPreferences(context).getString(AUTH_PASSWORD, defValue);
        }
        public static void setAuthPassword(Context context, String authPassword) {
            getPreferences(context).edit().putString(AUTH_PASSWORD, authPassword).apply();
        }

        public static String getAuthToken(Context context, String defValue) {
            return getPreferences(context).getString(AUTH_TOKEN, defValue);
        }
        public static void setAuthToken(Context context, String authToken) {
            getPreferences(context).edit().putString(AUTH_TOKEN, authToken).apply();
        }
    }


    private static SharedPreferences getPreferences(Context context) {
        return context.getSharedPreferences(PREFERENCES_NAME, Context.MODE_PRIVATE);
    }
}
