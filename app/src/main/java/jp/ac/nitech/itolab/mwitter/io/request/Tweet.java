package jp.ac.nitech.itolab.mwitter.io.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

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

    @JsonProperty("uid")
    public Long uid;

    @JsonProperty("parent_tid")
    public Long parentTid;

    @JsonProperty("content")
    public String content;

    @JsonProperty("created_at")
    @JsonSerialize(using = DateAdapter.Serializer.class)
    public Date createdAt;

    @JsonProperty("updated_at")
    @JsonSerialize(using = DateAdapter.Serializer.class)
    public Date updatedAt;
}
