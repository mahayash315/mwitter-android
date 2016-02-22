package jp.ac.nitech.itolab.mwitter.io;

import android.content.Context;
import android.net.Uri;

import java.io.IOException;
import java.net.URI;
import java.net.URL;

import jp.ac.nitech.itolab.mwitter.Config;
import jp.ac.nitech.itolab.mwitter.io.response.Login;
import jp.ac.nitech.itolab.mwitter.io.response.User;

/**
 * {@link jp.ac.nitech.itolab.mwitter.io.response.User} を処理する {@link JSONHandler}
 * Created by masayuki on 15/02/2016.
 */
public class UserHandler extends JSONHandler {

    public UserHandler(Context context) {
        super(context);
    }

    public Login login(jp.ac.nitech.itolab.mwitter.io.request.Login request) throws IOException {
        Uri.Builder builder = Uri.parse(Config.Api.URL.LOGIN).buildUpon();
        URI uri = URI.create(builder.build().toString());
        URL url = uri.toURL();

        Login response = httpPost(url, request, Login.class);
        return response;
    }

    /**
     * 自分の {@link User} を取得する
     * @return
     * @throws IOException
     */
    public User getMe() throws IOException {
        Uri.Builder builder = Uri.parse(Config.Api.URL.ME).buildUpon();
        URI uri = URI.create(builder.build().toString());
        URL url = uri.toURL();

        User response = httpGet(url, User.class);
        return response;
    }


}
