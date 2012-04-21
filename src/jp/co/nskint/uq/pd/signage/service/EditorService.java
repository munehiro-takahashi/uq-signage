package jp.co.nskint.uq.pd.signage.service;

import org.slim3.datastore.Datastore;

import com.google.appengine.api.datastore.Transaction;

import jp.co.nskint.uq.pd.signage.model.Editor;
import jp.co.nskint.uq.pd.signage.model.Manager;
import jp.co.nskint.uq.pd.signage.model.User;

/**
 * 編集者情報 サービス
 * @author NAGASAWA takahiro <tnagasaw@nskint.co.jp>
 */
public class EditorService extends UserService {

    @Override
    protected User createModel() {
        return new Editor();
    }

    /**
     * 編集者情報の保存
     * @param uid ユーザID
     * @param name 氏名
     * @param mail メールアドレス
     * @param manager 代表者
     * @return 保存したユーザ情報
     */
    public Editor put(String uid, String name, String mail, Manager manager) {
        Transaction tx = Datastore.beginTransaction();
        Editor result = put(tx, uid, name, mail, manager);

        tx.commit();

        return result;
    }

    /**
     * 編集者情報の保存
     * @param tx トランザクション
     * @param uid ユーザID
     * @param name 氏名
     * @param mail メールアドレス
     * @param manager 代表者
     * @return 保存したユーザ情報
     */
    public Editor put(Transaction tx, String uid, String name, String mail, Manager manager) {
        Editor result = (Editor)createOrSetUser(tx, uid, name, mail);
        if (manager != null) {
            result.getManagerRef().setModel(manager);
        } else if (result.getRegisteredDate().equals(result.getUpdatedDate())) {
            throw new IllegalArgumentException("manager is required.");
        }

        Datastore.put(tx, result);

        return result;
    }
}
