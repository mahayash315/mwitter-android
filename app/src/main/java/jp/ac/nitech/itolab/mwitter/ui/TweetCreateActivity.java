package jp.ac.nitech.itolab.mwitter.ui;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import jp.ac.nitech.itolab.mwitter.R;
import jp.ac.nitech.itolab.mwitter.model.entity.Tweet;

import static jp.ac.nitech.itolab.mwitter.util.LogUtils.LOGD;
import static jp.ac.nitech.itolab.mwitter.util.LogUtils.LOGE;
import static jp.ac.nitech.itolab.mwitter.util.LogUtils.makeLogTag;

public class TweetCreateActivity extends AppCompatActivity {
    private static final String TAG = makeLogTag(TweetCreateActivity.class);

    public static final String RESULT_KEY_CONTENT = "content";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tweet_create);

        final EditText etTweet = (EditText) findViewById(R.id.editText);
        final Button btnTweet = (Button) findViewById(R.id.button);

        btnTweet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {
                String content = etTweet.getText().toString();

                Intent data = new Intent();
                Bundle extras = new Bundle();
                extras.putString(RESULT_KEY_CONTENT, content);
                data.putExtras(extras);

                setResult(RESULT_OK, data);

                finish();
            }
        });
    }
}
