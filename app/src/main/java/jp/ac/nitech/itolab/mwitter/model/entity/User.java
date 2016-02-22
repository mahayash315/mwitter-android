package jp.ac.nitech.itolab.mwitter.model.entity;

import android.content.Context;
import android.provider.BaseColumns;
import android.text.TextUtils;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;
import com.activeandroid.query.Select;

import java.io.IOException;
import java.util.Arrays;
import java.util.Date;

import jp.ac.nitech.itolab.mwitter.io.UserHandler;
import jp.ac.nitech.itolab.mwitter.util.PreferencesUtils;

/**
 * ユーザのモデル
 * Created by masayuki on 15/02/2016.
 */
@Table(name = "users", id = BaseColumns._ID)
public class User extends Model {
    @Column(name = "uid", unique = true, notNull = true, index = true, onUniqueConflict = Column.ConflictAction.REPLACE)
    public Long uid;
    @Column(name = "first_name")
    public String firstName;
    @Column(name = "last_name")
    public String lastName;
    @Column(name = "disp_name")
    public String dispName;
    @Column(name = "created_at")
    public Date createdAt;
    @Column(name = "updated_at")
    public Date updatedAt;


    /**
     * {@link jp.ac.nitech.itolab.mwitter.io.response.User} から変換する
     * @param response
     * @return
     */
    public static User fromResponse(jp.ac.nitech.itolab.mwitter.io.response.User response) {
        User user = new User();
        user.uid = response.uid;
        user.firstName = response.firstName;
        user.lastName = response.lastName;
        user.dispName = response.dispName;
        user.createdAt = new Date(response.createdAt.getTime());
        user.updatedAt = new Date(response.updatedAt.getTime());
        return user;
    }

    /**
     * 自分の {@link User} を返す
     * @param context
     * @return
     */
    public static User getMe(Context context) {
        long myUid = PreferencesUtils.User.getMyUid(context, -1L);
        if (myUid < 0) {
            return null;
        }
        return new Select().from(User.class).where("uid=?", myUid).executeSingle();
    }

    /**`
     * 自分の {@link User} をサーバから取得して更新する
     * @param context
     * @return
     * @throws IOException
     */
    public static User updateMe(Context context) throws IOException {
        User me = fromResponse(new UserHandler(context).getMe());
        if (me == null) {
            return null;
        }
        PreferencesUtils.User.setMyUid(context, me.uid);
        me.save();
        return me;
    }


    /**
     * firstName + lastName を返す
     * @return
     */
    public String getName() {
        return TextUtils.join(" ", new String[]{firstName, lastName});
    }

}
