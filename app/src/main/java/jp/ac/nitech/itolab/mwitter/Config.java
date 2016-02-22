package jp.ac.nitech.itolab.mwitter;

import android.net.Uri;

import java.util.Locale;

/**
 * 静的設定ファイル
 * Created by masayuki on 12/02/2016.
 */
public interface Config {

    /**
     * アプリに関する設定
     */
    interface App {

        /**
         * DB の設定
         */
        interface DB {
            String NAME = "mwitter.db";
            int VERSION = 1;
        }

        /**
         * {@link android.content.SharedPreferences} の設定
         */
        interface Preferences {
            String NAME = "mwitter.prefs";
        }

        /**
         * 日時の設定
         */
        interface DateTime {
            Locale LOCALE = Locale.JAPAN;
        }

        /**
         * モデル {@link jp.ac.nitech.itolab.mwitter.model.entity.Tweet} 周りの設定
         */
        interface Tweet {
            long LIMIT = 0;
        }
    }

    /**
     * API の設定
     */
    interface Api {
        interface URL {
            String BASE = "http://133.68.108.19/~masayuki/mwitter/api/";
            String LOGIN = BASE+"welcome/login";
            String TIMELINE = BASE+"tweet/timeline";
            String TWEET = BASE + "tweet/tweet";
            String ME = BASE + "user/me";
        }

        interface Charset {
            String UTF8 = "UTF-8";
            String DEFAULT = UTF8;
        }
    }

}
