package jp.ac.nitech.itolab.mwitter.io.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import java.util.Date;

import jp.ac.nitech.itolab.mwitter.io.Entity;
import jp.ac.nitech.itolab.mwitter.io.adapter.DateAdapter;

/**
 * API ユーザ用のエンティティ
 * Created by masayuki on 15/02/2016.
 */
public class User extends Entity {
    @JsonProperty("uid")
    public Long uid;

    @JsonProperty("first_name")
    public String firstName;

    @JsonProperty("last_name")
    public String lastName;

    @JsonProperty("disp_name")
    public String dispName;

    @JsonProperty("created_at")
    @JsonDeserialize(using = DateAdapter.Deserializer.class)
    public Date createdAt;

    @JsonProperty("updated_at")
    @JsonDeserialize(using = DateAdapter.Deserializer.class)
    public Date updatedAt;

}
