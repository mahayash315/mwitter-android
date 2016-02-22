package jp.ac.nitech.itolab.mwitter.util;

import android.text.TextUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import jp.ac.nitech.itolab.mwitter.Config;

/**
 * 日付を扱うユーティリティ
 * Created by masayuki on 12/02/2016.
 */
public class DateUtils extends android.text.format.DateUtils {

    private static final Locale LOCALE = Config.App.DateTime.LOCALE;
    private static final String[] ISO8601_PARCEABLE = new String[]{
            "yyyy-MM-dd'T'HH:mm:ss.SSSSSSZ",
            "yyyy-MM-dd'T'HH:mm:ss.SSSZ",
            "yyyy-MM-dd'T'HH:mm:ssZ",
    };
    private static final String ISO8601_FORMAT = "yyyy-MM-dd'T'HH:mm:ss.SSSSSSZ";

    /**
     * ISO8601 形式の文字列を {@link Date} に変換する
     * @param string ISO8601形式の文字列
     * @return {@link Date} インスタンス
     * @throws ParseException
     */
    public static Date parseISO8601(String string) throws ParseException {
        ParseException lastException = null;
        for (String parceale : ISO8601_PARCEABLE) {
            try {
                final SimpleDateFormat format = new SimpleDateFormat(parceale, LOCALE);
                return format.parse(string);
            } catch (ParseException e) {
                lastException = e;
            }
        }
        throw lastException;
    }

    /**
     * {@link Date} を ISO8601 形式の文字列に変換する
     * @param date {@link Date} のインスタンス
     * @return ISO8601形式の文字列
     */
    public static String formatISO8601(Date date) {
        final SimpleDateFormat format = new SimpleDateFormat(ISO8601_FORMAT, LOCALE);
        return format.format(date);
    }

}
