package jp.ac.nitech.itolab.mwitter.util;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * Jackson 用ファクトリー
 * Created by masayuki on 12/02/2016.
 */
public class JacksonFactory {

    public static ObjectMapper create() {
        return new ObjectMapper();
    }

}
