package jp.co.nskint.uq.pd.signage.service;

import java.util.regex.Pattern;

import jp.co.nskint.uq.pd.signage.meta.UserMeta;
import jp.co.nskint.uq.pd.signage.model.User;

import org.apache.commons.lang3.RandomStringUtils;
import org.slim3.datastore.Datastore;

import com.google.appengine.api.datastore.Email;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.Transaction;

/**
 * ユーザ情報 サービス
 * @author NAGASAWA takahiro <tnagasaw@nskint.co.jp>
 */
public class UserService extends Service {
    /** アクションと被る可能性のある文字列はUIDに使えない */
    private static final String REGEXP_INVALID_UID = "(regist)|(insert)|(edit)|(update)|(create)";

    /**
     * @param tx
     * @param uid
     * @param name
     * @param mail
     * @return
     */
    protected User createOrSetUser(Transaction tx, String uid, String name,
            String mail) {
        User result = get(tx, uid);
        if (result == null) {
            result = createModel();
            Key key = UserService.createKey(uid);
            result.setUid(key);
            // 初期化キーを設定
            result.setInitialKey(RandomStringUtils.randomAlphanumeric(16));
        }
        result.setName(name);
        result.setMail(new Email(mail));
        return result;
    }

    /**
     * ユーザ情報の取得
     * @param uid ユーザID
     * @return ユーザ情報
     */
    public User get(String uid) {
        Transaction tx = Datastore.beginTransaction();
        User result = this.get(tx, uid);
        tx.commit();
        return result;
    }
    /**
     * ユーザ情報の取得
     * @param tx トランザクション
     * @param uid ユーザID
     * @return ユーザ情報
     */
    protected User get(Transaction tx, String uid) {
        User result;
        result = Datastore.getOrNull(tx, UserMeta.get(), UserService.createKey(uid));
        return result;
    }

    /**
     * モデルオブジェクト生成
     * @return オブジェクト
     */
    protected User createModel() {
        return null;
    }

    /**
     * パスワードの保存
     * @param uid ユーザID
     * @param password パスワード
     */
    public void savePassword(String uid, String password) {
        Transaction tx = Datastore.beginTransaction();
        User user = Datastore.getOrNull(tx, UserMeta.get(), UserService.createKey(uid));
        user.setInitialKey("");
        user.setPassword(password);
        Datastore.put(tx, user);
        tx.commit();
    }

    /**
     * ユーザIDが重複していないかチェックする。
     * @param uid ユーザID
     * @return true:重複していない / false:重複している
     */
    public boolean checkDuplicationUid(String uid) {
        if (Pattern.matches(REGEXP_INVALID_UID, uid)) {
            return false;
        }
        Key key = UserService.createKey(uid);
        User user = Datastore.getOrNull(UserMeta.get(), key);
        return user == null;
    }

    /**
     * ユーザ情報の削除
     * @param uid ユーザID
     */
    public void delete(String uid) {
        Datastore.delete(UserService.createKey(uid));
    }

    /**
     * キー生成
     * @param uid ユーザID
     * @return キー
     */
    public static Key createKey(String uid) {
        return Datastore.createKey(UserMeta.get(), uid);
    }
}
