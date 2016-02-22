package jp.ac.nitech.itolab.mwitter.io.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import java.util.Date;

import jp.ac.nitech.itolab.mwitter.io.Entity;
import jp.ac.nitech.itolab.mwitter.io.adapter.DateAdapter;

/**
 * API用ツイートのエンティティ
 * Created by masayuki on 12/02/2016.
 */
public class Tweet extends Entity {
    @JsonProperty("tid")
    public Long tid;

    @JsonProperty("parent_tid")
    public Long parentTid;

    @JsonProperty("user")
    public User user;

    @JsonProperty("content")
    public String content;

    @JsonProperty("created_at")
    @JsonDeserialize(using = DateAdapter.Deserializer.class)
    public Date createdAt;

    @JsonProperty("updated_at")
    @JsonDeserialize(using = DateAdapter.Deserializer.class)
    public Date updatedAt;

    /**
     * API用のユーザのエンティティ
     * Created by masayuki on 12/02/2016.
     */
    public static class User extends Entity {
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
}
