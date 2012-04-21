package jp.co.nskint.uq.pd.signage.service;



import org.slim3.datastore.Datastore;

import com.google.appengine.api.datastore.Transaction;

import jp.co.nskint.uq.pd.signage.model.Administrator;
import jp.co.nskint.uq.pd.signage.model.User;

/**
 * 管理者情報 サービス
 * @author NAGASAWA takahiro <tnagasaw@nskint.co.jp>
 */
public class AdminService extends UserService {

    /**
     * @return
     */
    @Override
    protected User createModel() {
        return new Administrator();
    }

    /**
     * 管理者情報の保存
     * @param uid ユーザID
     * @param name 氏名
     * @param mail メールアドレス
     * @return 保存したユーザ情報
     */
    public User put(String uid, String name, String mail) {
        User result = null;
        Transaction tx = Datastore.beginTransaction();
        result = put(tx, uid, name, mail);

        tx.commit();

        return result;
    }

    /**
     * 管理者情報の保存
     * @param tx トランザクション
     * @param uid ユーザID
     * @param name 氏名
     * @param mail メールアドレス
     * @return 保存したユーザ情報
     */
    public User put(Transaction tx, String uid, String name, String mail) {
        User result = createOrSetUser(tx, uid, name, mail);

        Datastore.put(tx, result);

        return result;
    }

}
