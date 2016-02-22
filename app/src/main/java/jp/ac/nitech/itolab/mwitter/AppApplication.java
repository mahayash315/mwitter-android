package jp.ac.nitech.itolab.mwitter;

import android.app.Application;

import com.activeandroid.ActiveAndroid;
import com.activeandroid.Configuration;
import com.activeandroid.serializer.TypeSerializer;

import jp.ac.nitech.itolab.mwitter.model.adapter.TweetUserSerializer;

/**
 * Created by masayuki on 12/02/2016.
 */
public class AppApplication extends Application {

    /** ActiveAndroid に登録する {@link TypeSerializer} */
    private static Class<?>[] sTypeSerializers = new Class<?>[] {
            TweetUserSerializer.class
    };

    @Override
    public void onCreate() {
        super.onCreate();
        setupDatabase();
    }

    @Override
    public void onTerminate() {
        super.onTerminate();
        disposeDatabse();
    }

    /**
     * ORM 初期化
     */
    private void setupDatabase() {
        @SuppressWarnings("unchecked")
        Configuration dbConfiguration = new Configuration.Builder(this)
                .setDatabaseName(Config.App.DB.NAME)
                .setDatabaseVersion(Config.App.DB.VERSION)
                .addTypeSerializers((Class<? extends TypeSerializer>[]) sTypeSerializers)
                .create();
        ActiveAndroid.initialize(dbConfiguration);
    }

    /**
     * ORM 破棄
     */
    private void disposeDatabse() {
        ActiveAndroid.dispose();
    }

}
