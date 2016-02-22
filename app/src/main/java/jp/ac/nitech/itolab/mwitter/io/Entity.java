package jp.ac.nitech.itolab.mwitter.io;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import jp.ac.nitech.itolab.mwitter.util.JacksonFactory;

/**
 * データ層の入出力用エンティティ
 * Created by masayuki on 12/02/2016.
 */
public class Entity {

    /**
     * JSON 文字列にシリアライズする
     * @return
     */
    public String toJson() throws IOException {
        return JacksonFactory.create().writeValueAsString(this);
    }

    /**
     * JSON 文字列からシリアライズする
     * @param json
     * @return
     */
    public static <T extends Entity> T fromJson(String json, Class<T> clazz) throws IOException {
        return JacksonFactory.create().readValue(json, clazz);
    }

}
