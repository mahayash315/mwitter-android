package jp.ac.nitech.itolab.mwitter.service;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.os.ResultReceiver;

import com.activeandroid.ActiveAndroid;

import java.io.IOException;
import java.util.Date;
import java.util.List;

import jp.ac.nitech.itolab.mwitter.Config;
import jp.ac.nitech.itolab.mwitter.io.TweetHandler;
import jp.ac.nitech.itolab.mwitter.model.entity.Tweet;
import jp.ac.nitech.itolab.mwitter.util.PreferencesUtils;

import static jp.ac.nitech.itolab.mwitter.util.LogUtils.LOGD;
import static jp.ac.nitech.itolab.mwitter.util.LogUtils.LOGE;
import static jp.ac.nitech.itolab.mwitter.util.LogUtils.makeLogTag;

/**
 * {@link TweetService} 用のヘルパー
 * Created by masayuki on 12/02/2016.
 */
public class TweetHelper {
    private static final String TAG = makeLogTag(TweetHelper.class);

    private final Context mContext;

    TweetHelper(Context context) {
        mContext = context;
    }

    public void updateTimeline(ResultReceiver receiver) {
        long limit = Config.App.Tweet.LIMIT;
        Date referenceTime = PreferencesUtils.Tweet.getReferenceTime(mContext, null);

        LOGD(TAG, "Begin updating timeline");
        TweetHandler handler = new TweetHandler(mContext);
        try {
            // 現在時刻取得
            Date newReferenceTime = new Date();

            // タイムライン取得
            List<jp.ac.nitech.itolab.mwitter.io.response.Tweet> timeline =
                    handler.getTimeline(limit, 0, referenceTime);// FIXME: 13/02/2016 limitとoffsetを正しく実

            // モデルへ変換して保存
            ActiveAndroid.beginTransaction();
            try {
                for (jp.ac.nitech.itolab.mwitter.io.response.Tweet t : timeline) {
                    Tweet tweet = Tweet.fromResponse(t);
                    tweet.save();
                }
                ActiveAndroid.setTransactionSuccessful();
            } finally {
                ActiveAndroid.endTransaction();
            }

            // ReferenceTime 更新
            PreferencesUtils.Tweet.setReferenceTime(mContext, newReferenceTime);

            LOGD(TAG, "Successfully updated timeline, adding "+timeline.size()+" tweets.");
            receiver.send(TweetService.STATE_OK, new Bundle()); // FIXME: 13/02/2016 implement properly
        } catch (IOException e) {
            LOGE(TAG, "Error while updating timeline", e);
            receiver.send(TweetService.STATE_NG, new Bundle()); // FIXME: 13/02/2016 implement properly
        }
        LOGD(TAG, "End updating timeline");
    }

}
