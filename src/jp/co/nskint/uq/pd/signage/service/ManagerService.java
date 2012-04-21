package jp.co.nskint.uq.pd.signage.service;

import org.slim3.datastore.Datastore;

import com.google.appengine.api.datastore.PhoneNumber;
import com.google.appengine.api.datastore.PostalAddress;
import com.google.appengine.api.datastore.Transaction;

import jp.co.nskint.uq.pd.signage.model.Manager;
import jp.co.nskint.uq.pd.signage.model.User;

/**
 * 代表者情報 サービス
 * @author NAGASAWA takahiro <tnagasaw@nskint.co.jp>
 */
public class ManagerService extends UserService {

    @Override
    protected User createModel() {
        return new Manager();
    }

    /**
     * 代表者情報を保存
     * @param uid ユーザID
     * @param name 氏名
     * @param mail メールアドレス
     * @param phone 電話番号
     * @param zipcode 郵便番号
     * @param address 住所
     * @return 保存した管理者情報
     */
    public User put(String uid, String name, String mail,
            String phone, String zipcode, String address) {
        User result = null;
        Transaction tx = Datastore.beginTransaction();
        result = put(tx, uid, name, mail, phone, zipcode, address);

        tx.commit();

        return result;
    }

    /**
     * 代表者情報を保存
     * @param tx トランザクション
     * @param uid ユーザID
     * @param name 氏名
     * @param mail メールアドレス
     * @param phone 電話番号
     * @param zipcode 郵便番号
     * @param address 住所
     * @return 保存した管理者情報
     */
    public User put(Transaction tx, String uid, String name, String mail,
            String phone, String zipcode, String address) {

        Manager result = (Manager)createOrSetUser(tx, uid, name, mail);
        result.setPhone(new PhoneNumber(phone));
        result.setZipcode(zipcode);
        result.setAddress(new PostalAddress(address));

        Datastore.put(tx, result);

        return result;
    }

}
