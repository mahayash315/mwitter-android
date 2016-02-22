package jp.ac.nitech.itolab.mwitter.service;

import android.app.IntentService;
import android.content.Intent;
import android.os.Parcelable;
import android.support.v4.os.ResultReceiver;

import java.io.IOException;

import static jp.ac.nitech.itolab.mwitter.util.LogUtils.LOGD;
import static jp.ac.nitech.itolab.mwitter.util.LogUtils.LOGE;
import static jp.ac.nitech.itolab.mwitter.util.LogUtils.LOGI;
import static jp.ac.nitech.itolab.mwitter.util.LogUtils.makeLogTag;

/**
 * ツイート用 {@link android.app.Service}
 * Created by masayuki on 12/02/2016.
 */
public class TweetService extends IntentService {
    private static final String TAG = makeLogTag(TweetService.class);

    public static final String INTENT_KEY_RECEIVER = "receiver";

    // --- ACTIONS

    public static final String ACTION_UPDATE =
            "jp.ac.nitech.itolab.mwitter.action.UPDATE";

    // --- STATES

    public static final int STATE_OK = 200;
    public static final int STATE_NG = 400;

    public TweetService() {
        super(TAG);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        final String action = intent.getAction();
        ResultReceiver receiver = intent.getParcelableExtra(INTENT_KEY_RECEIVER);

        LOGD(TAG, "Received action: "+action);

        switch (action) {
            case ACTION_UPDATE:
                onPerformUpdate(receiver);
                break;
            default:
                LOGE(TAG, "Unknown action: "+action);
        }
    }

    /**
     * ツイート一覧を更新する
     */
    private void onPerformUpdate(ResultReceiver receiver) {
        // タイムラインを更新する
        new TweetHelper(this).updateTimeline(receiver);
    }
}
