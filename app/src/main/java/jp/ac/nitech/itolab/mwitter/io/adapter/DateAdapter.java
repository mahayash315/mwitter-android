package jp.ac.nitech.itolab.mwitter.io.adapter;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

import java.io.IOException;
import java.text.ParseException;
import java.util.Date;

import jp.ac.nitech.itolab.mwitter.util.DateUtils;

/**
 * Jackson 用のアダプタ
 * Created by masayuki on 14/02/2016.
 */
public class DateAdapter {

    public static class Serializer extends JsonSerializer<Date> {
        @Override
        public void serialize(Date value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
            gen.writeString(DateUtils.formatISO8601(value));
        }
    }

    public static class Deserializer extends JsonDeserializer<Date> {
        @Override
        public Date deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
            try {
                return DateUtils.parseISO8601(p.getText());
            } catch (ParseException e) {
                throw new IOException("Parse failed", e);
            }
        }
    }
}
