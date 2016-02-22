package jp.ac.nitech.itolab.mwitter.io;

import android.content.Context;

import com.fasterxml.jackson.core.JsonParseException;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.ProtocolException;
import java.net.URL;

import jp.ac.nitech.itolab.mwitter.Config;
import jp.ac.nitech.itolab.mwitter.util.JacksonFactory;
import jp.ac.nitech.itolab.mwitter.util.PreferencesUtils;

import static jp.ac.nitech.itolab.mwitter.util.LogUtils.LOGD;
import static jp.ac.nitech.itolab.mwitter.util.LogUtils.makeLogTag;

/**
 * APIとのやり取りを行うハンドラ
 * Created by masayuki on 12/02/2016.
 */
public class JSONHandler {
    private static final String TAG = makeLogTag(JSONHandler.class);

    protected final Context mContext;

    public JSONHandler(Context context) {
        mContext = context;
    }

    /**
     * GET メソッドでリクエストを送る
     * @param url URL
     * @return {@link HttpURLConnection#connect()} された後のインスタンス
     * @throws IOException
     */
    public HttpURLConnection httpGet(URL url) throws IOException {
        LOGD(TAG, "HttpGet: "+url);

        // 接続用HttpURLConnectionオブジェクト作成
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        // リクエストメソッドの設定
        conn.setRequestMethod("GET");
        // リクエストヘッダの設定
        conn.setRequestProperty("Authorization", "Bearer " + PreferencesUtils.User.getAuthToken(mContext, ""));
        // リダイレクトを自動で許可しない設定
        conn.setInstanceFollowRedirects(false);
        // URL接続からデータを読み取る場合はtrue
        conn.setDoInput(true);
        // URL接続にデータを書き込む場合はtrue
        conn.setDoOutput(false);
        // 接続
        conn.connect();

        return conn;
    }

    /**
     * POST メソッドでリクエストを送る
     * @param url URL
     * @param data 送信するデータ
     * @return {@link HttpURLConnection#connect()} された後のインスタンス
     * @throws IOException
     */
    public HttpURLConnection httpPost(URL url, byte[] data) throws IOException {
        LOGD(TAG, "HttpPost: "+url+"\nwith data: "+new String(data, Config.Api.Charset.DEFAULT));

        // 接続用HttpURLConnectionオブジェクト作成
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        // リクエストメソッドの設定
        conn.setRequestMethod("POST");
        // リクエストヘッダの設定
        conn.setRequestProperty("Authorization", "Bearer " + PreferencesUtils.User.getAuthToken(mContext, ""));
        // リダイレクトを自動で許可しない設定
        conn.setInstanceFollowRedirects(false);
        // URL接続からデータを読み取る場合はtrue
        conn.setDoInput(true);
        // URL接続にデータを書き込む場合はtrue
        conn.setDoOutput(true);
        // 接続
        conn.connect();

        // データ送信
        OutputStream out = null;
        try {
            out = conn.getOutputStream();
            out.write(data);
            out.flush();
        } finally {
            if (out != null) {
                out.close();
            }
        }

        return conn;
    }


    /**
     * GET メソッドでリクエストを送る
     * @param url URL
     * @param clazz JSON エンティティクラス
     * @param <T> JSON エンティティ
     * @return オブジェクト
     * @throws IOException
     */
    protected <T> T httpGet(URL url, Class<T> clazz) throws IOException {
        HttpURLConnection conn = httpGet(url);
        return deserialize(conn.getInputStream(), clazz);
    }

    /**
     * POST メソッドでリクエストを送る
     * @param url URL
     * @param data 送信するデータ
     * @param clazz JSON エンティティクラス
     * @param <T> JSON エンティティ
     * @return オブジェクト
     * @throws IOException
     */
    protected <T> T httpPost(URL url, byte[] data, Class<T> clazz) throws IOException {
        HttpURLConnection conn = httpPost(url, data);
        return deserialize(conn.getInputStream(), clazz);
    }

    /**
     * POST メソッドでリクエストを送る
     * @param url URL
     * @param entity 送信するデータ
     * @param clazz JSON エンティティクラス
     * @param <T> JSON エンティティ
     * @return オブジェクト
     * @throws IOException
     */
    protected <T> T httpPost(URL url, Entity entity, Class<T> clazz) throws IOException {
        byte[] data = entity.toJson().getBytes(Config.Api.Charset.DEFAULT);
        return httpPost(url, data, clazz);
    }

    /**
     * {@link InputStream} から読み込んだJSONから {@link T} を作る
     * @param in unmarshalする{@link InputStream}
     * @param clazz JSON のエンティティクラス
     * @param <T> JSONのエンティティ
     * @return オブジェクト
     * @throws IOException
     */
    protected <T> T deserialize(InputStream in, Class<T> clazz) throws IOException {
        try {
            return JacksonFactory.create().readValue(in, clazz);
        } finally {
            if (in != null) {
                in.close();
            }
        }
    }

}
