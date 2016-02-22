package jp.ac.nitech.itolab.mwitter.io.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import java.util.Date;

import jp.ac.nitech.itolab.mwitter.io.Entity;
import jp.ac.nitech.itolab.mwitter.io.adapter.DateAdapter;

/**
 * Created by masayuki on 20/02/2016.
 */
public class Login extends Entity {
    @JsonProperty("result")
    public boolean result;

    @JsonProperty("auth_token")
    public String authToken;

    @JsonProperty("auth_token_expire_at")
    @JsonDeserialize(using = DateAdapter.Deserializer.class)
    public Date authTokenExpireAt;
}
