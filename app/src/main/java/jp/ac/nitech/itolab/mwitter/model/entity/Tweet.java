package jp.ac.nitech.itolab.mwitter.model.entity;

import android.database.Cursor;
import android.provider.BaseColumns;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;
import com.activeandroid.query.Select;

import java.text.ParseException;
import java.util.Date;
import java.util.HashMap;

import jp.ac.nitech.itolab.mwitter.util.DateUtils;

/**
 * ツイートのエンティティ
 * Created by masayuki on 12/02/2016.
 */
@Table(name = "tweets", id = BaseColumns._ID)
public class Tweet extends Model {
    @Column(name = "tid", unique = true, notNull = true, index = true, onUniqueConflict = Column.ConflictAction.REPLACE)
    public Long tid;
    @Column(name = "parent_tid")
    public Long parentTid;
    @Column(name = "user")
    public User user;
    @Column(name = "content")
    public String content;
    @Column(name = "created_at")
    public Date createdAt;
    @Column(name = "updated_at")
    public Date updatedAt;


    public static final String ORDER_BY_CREATION =
            "created_at DESC";


    /**
     * ユーザ情報を保持する{@link HashMap}
     * ユーザ情報を永続化する場合は別途モデルを作って格納してもいいが
     * ユーザ数が膨れ上がった際に同期のタイミング、範囲が決めづらい為
     * 今回は JSON 文字列そのままを保存し、{@link Tweet}毎に別のデータを持つ
     */
    public static class User extends HashMap<String, Object> {

        public Long getUid() {
            return (Long) get("uid");
        }
        void setUid(Long uid) {
            put("uid", uid);
        }

        public String getFirstName() {
            return (String) get("first_name");
        }

        public String getLastName() {
            return (String) get("last_name");
        }

        public String getDispName() {
            return (String) get("disp_name");
        }

        public Date getCreatedAt() {
            try {
                return DateUtils.parseISO8601((String) get("created_at"));
            } catch (ParseException e) {
                return null;
            }
        }

        public Date getUpdatedAt() {
            try {
                return DateUtils.parseISO8601((String) get("updated_at"));
            } catch (ParseException e) {
                return null;
            }
        }
    }


    /**
     * {@link jp.ac.nitech.itolab.mwitter.io.request.Tweet} に変換する
     * @return
     */
    public jp.ac.nitech.itolab.mwitter.io.request.Tweet toRequest() {
        jp.ac.nitech.itolab.mwitter.io.request.Tweet tweet = new jp.ac.nitech.itolab.mwitter.io.request.Tweet();
        tweet.tid = this.tid;
        tweet.uid = this.user.getUid();
        tweet.parentTid = this.parentTid;
        tweet.content = this.content;
        tweet.createdAt = new Date(this.createdAt.getTime());
        tweet.updatedAt = new Date(this.updatedAt.getTime());
        return tweet;
    }

    /**
     * {@link jp.ac.nitech.itolab.mwitter.io.response.Tweet} から変換する
     * @param response
     * @return
     */
    public static Tweet fromResponse(jp.ac.nitech.itolab.mwitter.io.response.Tweet response) {
        Tweet tweet = new Tweet();
        tweet.tid = response.tid;

        tweet.user = new Tweet.User();
        tweet.user.put("uid", response.user.uid);
        tweet.user.put("first_name", response.user.firstName);
        tweet.user.put("last_name", response.user.lastName);
        tweet.user.put("disp_name", response.user.dispName);
        tweet.user.put("created_at", response.user.createdAt);
        tweet.user.put("updated_at", response.user.updatedAt);

        tweet.parentTid = response.parentTid;
        tweet.content = response.content;
        tweet.createdAt = new Date(response.createdAt.getTime());
        tweet.updatedAt = new Date(response.updatedAt.getTime());
        return tweet;
    }

    /**
     * {@link Cursor} から変換する
     * @param cursor
     * @return
     */
    public static Tweet fromCursor(Cursor cursor) {
        Tweet tweet = new Tweet();
        tweet.loadFromCursor(cursor);
        return tweet;
    }

    /**
     * id でレコードを検索する
     * @param id
     * @return
     */
    public static Tweet findById(Long id) {
        return new Select()
                .from(Tweet.class)
                .where(BaseColumns._ID+"=?", id)
                .executeSingle();
    }

    /**
     * tid でレコードを検索する
     * @param tid
     * @return
     */
    public static Tweet findByTid(Long tid) {
        return new Select()
                .from(Tweet.class)
                .where("tid=?", tid)
                .executeSingle();
    }

    private static Tweet create(Long parentTid, String content) {
        Date now = new Date();

        Tweet tweet = new Tweet();
        tweet.tid = null;

        tweet.user = new Tweet.User();
        tweet.user.setUid(1L); // FIXME: 14/02/2016 UIDを正しく設定する

        tweet.parentTid = parentTid;
        tweet.content = content;
        tweet.createdAt = now;
        tweet.updatedAt = now;

        return tweet;
    }

    /**
     * 新規 {@link Tweet} を作る
     * @param content
     * @return
     */
    public static Tweet createNew(String content) {
        return create(null, content);
    }

    /**
     * 返信 {@link Tweet} を作る
     * @param replyTo
     * @param content
     * @return
     */
    public static Tweet createReply(Tweet replyTo, String content) {
        return create(replyTo.tid, content);
    }
}
