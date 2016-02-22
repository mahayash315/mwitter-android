package jp.ac.nitech.itolab.mwitter.io;

import android.content.Context;
import android.net.Uri;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import jp.ac.nitech.itolab.mwitter.Config;
import jp.ac.nitech.itolab.mwitter.io.response.Tweet;
import jp.ac.nitech.itolab.mwitter.util.DateUtils;
import jp.ac.nitech.itolab.mwitter.util.JacksonFactory;

/**
 * {@link jp.ac.nitech.itolab.mwitter.io.response.Tweet} を処理する {@link JSONHandler}
 * Created by masayuki on 12/02/2016.
 */
public class TweetHandler extends JSONHandler {

    public TweetHandler(Context context) {
        super(context);
    }

    /**
     * タイムラインを取得する
     * @param limit リミット
     * @param offset オフセット
     * @param referenceTime 最終取得時刻
     * @return ツイートのリスト
     * @throws IOException
     */
    public List<Tweet> getTimeline(long limit, long offset, Date referenceTime) throws IOException {
        Uri.Builder builder = Uri.parse(Config.Api.URL.TIMELINE).buildUpon();
        if (0 < limit) {
            builder.appendQueryParameter("limit", String.valueOf(limit));
        }
        if (0 < offset) {
            builder.appendQueryParameter("offset", String.valueOf(offset));
        }
        if (referenceTime != null) {
            builder.appendQueryParameter("reference_time", DateUtils.formatISO8601(referenceTime));
        }
        URI uri = URI.create(builder.build().toString());
        URL url = uri.toURL();

        Tweet[] response = httpGet(url, Tweet[].class);
        return (List<Tweet>) Arrays.asList(response);
    }

    /**
     * ツイートを投稿する
     * @param request ツイート
     * @return 投稿したツイート
     * @throws IOException
     */
    public Tweet postTweet(jp.ac.nitech.itolab.mwitter.io.request.Tweet request) throws IOException {
        Uri.Builder builder = Uri.parse(Config.Api.URL.TWEET).buildUpon();
        URI uri = URI.create(builder.build().toString());
        URL url = uri.toURL();

        Tweet response = httpPost(url, request, Tweet.class);
        return response;
    }

}
