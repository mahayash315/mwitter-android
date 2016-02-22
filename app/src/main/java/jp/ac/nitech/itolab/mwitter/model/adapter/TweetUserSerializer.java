package jp.ac.nitech.itolab.mwitter.model.adapter;

import com.activeandroid.serializer.TypeSerializer;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import jp.ac.nitech.itolab.mwitter.model.entity.Tweet;

/**
 * {@link Map} をJSON文字列としてSQLiteに保存する為の {@link TypeSerializer}
 * Created by masayuki on 12/02/2016.
 */
public class TweetUserSerializer extends TypeSerializer {
    @Override
    public Class<?> getDeserializedType() {
        return Tweet.User.class;
    }

    @Override
    public Class<?> getSerializedType() {
        return String.class;
    }

    @Override
    public Object serialize(Object data) {
        if (data == null) {
            return null;
        }
        return new JSONObject((Map) data).toString();
    }

    @Override
    public Object deserialize(Object data) {
        if (data == null) {
            return null;
        }
        try {
            JSONObject json = new JSONObject((String) data);
            Iterator<String> keys = json.keys();
            Map<String, Object> params = new Tweet.User();
            while (keys.hasNext()) {
                String key = keys.next();
                Object val = json.get(key);
                params.put(key, val);
            }

            return params;
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return null;
    }
}
