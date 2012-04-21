/**
 *
 */
package jp.co.nskint.uq.pd.signage.model;

import java.io.Serializable;
import java.util.Date;


import org.slim3.datastore.Attribute;
import org.slim3.datastore.CreationDate;
import org.slim3.datastore.Model;
import org.slim3.datastore.ModificationDate;

import com.google.appengine.api.datastore.Email;
import com.google.appengine.api.datastore.Key;

/**
 * ユーザモデル
 * @author NAGASAWA Takahiro<tnagasaw@nskint.co.jp>
 */
@Model(schemaVersion = 1)
public class User implements Serializable {

    private static final long serialVersionUID = 1L;

    /** ユーザ種別(デフォルト) */
    public static final String TYPE_USER = "user";

    /** ユーザ種別(編集者) */
    public static final String TYPE_EDITOR = "editor";

    /** ユーザ種別(代表者) */
    public static final String TYPE_MANAGER = "manager";

    /** ユーザ種別(管理者) */
    public static final String TYPE_ADMINISTRATOR = "admin";

    /** ユーザID */
    @Attribute(primaryKey = true)
    private Key uid;

    /** パスワード */
    @Attribute(cipher=true)
    private String password;

    /** ユーザ名 */
    private String name;

    /** 初期登録キー */
    private String initialKey;

    /** 登録日 */
    @Attribute(listener = CreationDate.class)
    private Date registeredDate;
    /** 更新日 */
    @Attribute(listener = ModificationDate.class)
    private Date updatedDate;

    /** 連絡先 メールアドレス */
    private Email mail;

    /**
     * ユーザIDを取得します。
     * @return ユーザID
     */
    public Key getUid() {
        return uid;
    }

    /**
     * ユーザIDを設定します。
     * @param uid ユーザID
     */
    public void setUid(Key uid) {
        this.uid = uid;
    }

    /**
     * パスワードを取得します。
     * @return パスワード
     */
    public String getPassword() {
        return password;
    }

    /**
     * パスワードを設定します。
     * @param password パスワード
     */
    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * ユーザ名を取得します。
     * @return ユーザ名
     */
    public String getName() {
        return name;
    }

    /**
     * ユーザ名を設定します。
     * @param name ユーザ名
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * 初期登録キーを取得します。
     * @return 初期登録キー
     */
    public String getInitialKey() {
        return initialKey;
    }

    /**
     * 初期登録キーを設定します。
     * @param initialKey 初期登録キー
     */
    public void setInitialKey(String initialKey) {
        this.initialKey = initialKey;
    }

    /**
     * 登録日を取得します。
     * @return 登録日
     */
    public Date getRegisteredDate() {
        return registeredDate;
    }

    /**
     * 登録日を設定します。
     * @param registeredDate 登録日
     */
    public void setRegisteredDate(Date registeredDate) {
        this.registeredDate = registeredDate;
    }

    /**
     * 更新日を取得します。
     * @return 更新日
     */
    public Date getUpdatedDate() {
        return updatedDate;
    }

    /**
     * 更新日を設定します。
     * @param updatedDate 更新日
     */
    public void setUpdatedDate(Date updatedDate) {
        this.updatedDate = updatedDate;
    }

    /**
     * 連絡先 メールアドレスを取得します。
     * @return 連絡先 メールアドレス
     */
    public Email getMail() {
        return mail;
    }

    /**
     * 連絡先 メールアドレスを設定します。
     * @param mail 連絡先 メールアドレス
     */
    public void setMail(Email mail) {
        this.mail = mail;
    }

    /**
     * ユーザ種別を取得する
     * @return ユーザ種別
     */
    public String getType() {
        return TYPE_USER;
    }

    /**
     *
     * @see java.lang.Object#equals(java.lang.Object)
     */
    @Override
    public boolean equals(Object obj) {
        boolean result = false;

        if (obj instanceof User) {
            User user = (User)obj;
            if (user.getUid().equals(getUid())) {
                result = true;
            }
        }

        return result;
    }


}
