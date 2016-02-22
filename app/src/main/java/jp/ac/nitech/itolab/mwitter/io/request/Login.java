package jp.ac.nitech.itolab.mwitter.io.request;

import com.fasterxml.jackson.annotation.JsonProperty;

import jp.ac.nitech.itolab.mwitter.io.Entity;

/**
 * API用ログインのエンティティ
 * Created by masayuki on 20/02/2016.
 */
public class Login extends Entity {
    @JsonProperty("username")
    public String username;

    @JsonProperty("password")
    public String password;
}
