package jp.co.nskint.uq.pd.signage.service;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;
import jp.co.nskint.uq.pd.signage.model.Manager;
import jp.co.nskint.uq.pd.signage.model.User;

import org.junit.After;
import org.junit.BeforeClass;
import org.junit.Test;
import org.slim3.datastore.Datastore;
import org.slim3.tester.AppEngineTestCase;

import com.google.appengine.api.datastore.Transaction;

/**
 * ManagerServiceのテスト。
 * @author NAGASAWA takahiro <tnagasaw@nskint.co.jp>
 *
 */
public class ManagerServiceTest extends AppEngineTestCase {

    private ManagerService service = new ManagerService();

    private static final String UID = "testmanager";

    /**
     * テストクラス実行前に行われる処理。
     *
     * <ul>
     * <li>パスワードを暗号しているので、ユーザ系のモデルを触るときはキー設定を行う。</li>
     * </ul>
     */
    @BeforeClass
    public static void beforeClass() {
        Datastore.setGlobalCipherKey("testcipherkey123");

    }

    /**
     * インスタンスを作れるかテスト
     * @throws Exception
     */
    @Test
    public void test() throws Exception {
        assertThat(service, is(notNullValue()));
    }

    /**
     * 保存機能のテスト
     * <ul>
     * <li>指定した値が戻り値のオブジェクトに設定されていること</li>
     * <li>初回保存時は初期化キーが設定されていること</li>
     * <li>DBから抽出し、保存した通りの値が取得できること</li>
     * <li>更新時も指定した値が戻り値のオブジェクトに設定されていること</li>
     * <li>更新時は初期化キーが設定されないこと</li>
     * <li>更新時も保存した通りの値が取得できること</li>
     * </ul>
     */
    @Test
    public void put() throws Exception {
        final String uid = UID;
        final String name = "Test User";
        final String mail = "testuser@mail.com";
        final String phone = "052-551-1861";
        final String zipcode = "450-0002";
        final String address = "架空の住所";
        User user = service.put(uid, name, mail, phone, zipcode, address);
        assertThat(user, is(notNullValue()));
        assertThat(user, instanceOf(Manager.class));
        Manager manager = (Manager)user;
        assertThat(manager.getName(), is(name));
        assertThat(manager.getMail().getEmail(), is(mail));
        assertThat(manager.getPhone().getNumber(), is(phone));
        assertThat(manager.getZipcode(), is(zipcode));
        assertThat(manager.getAddress().getAddress(), is(address));

        assertNotNull(user.getInitialKey());
        assertThat(user.getInitialKey(), not(""));

        Manager stored = Datastore.get(Manager.class, user.getUid());
        assertThat(stored, instanceOf(Manager.class));
        manager = (Manager)stored;
        assertThat(manager.getName(), is(name));
        assertThat(manager.getMail().getEmail(), is(mail));
        assertThat(manager.getPhone().getNumber(), is(phone));
        assertThat(manager.getZipcode(), is(zipcode));
        assertThat(manager.getAddress().getAddress(), is(address));
        assertThat(stored.getRegisteredDate(), is(stored.getUpdatedDate()));

        // パスワードを保存する
        service.savePassword(uid, "password");

        final String name2 = "Test User2";
        final String mail2 = "testuser2@mail.com";
        final String phone2 = "052-551-1862";
        final String zipcode2 = "450-0003";
        final String address2 = "架空の住所2";
        user = service.put(uid, name2, mail2, phone2, zipcode2, address2);
        assertThat(user, is(notNullValue()));
        assertThat(user, instanceOf(Manager.class));
        assertThat(user.getName(), is(name2));
        assertThat(user.getMail().getEmail(), is(mail2));

        assertThat(user.getInitialKey(), is(""));

        stored = Datastore.get(Manager.class, user.getUid());
        assertThat(stored, instanceOf(Manager.class));
        manager = (Manager)stored;
        assertThat(manager.getName(), is(name2));
        assertThat(manager.getMail().getEmail(), is(mail2));
        assertThat(manager.getPhone().getNumber(), is(phone2));
        assertThat(manager.getZipcode(), is(zipcode2));
        assertThat(manager.getAddress().getAddress(), is(address2));
        assertTrue(stored.getRegisteredDate().before(stored.getUpdatedDate()));
    }

    /**
     * 保存機能のテスト(トランザクション有)
     * <ul>
     * <li>指定した値が戻り値のオブジェクトに設定されていること</li>
     * <li>初回保存時は初期化キーが設定されていること</li>
     * <li>コミット前はDBから抽出できないこと</li>
     * <li>コミット後はDBから抽出し、保存した通りの値が取得できること</li>
     * </ul>
     */
    @Test
    public void put2() throws Exception {
        final String uid = UID;
        final String name = "Test User";
        final String mail = "testuser@mail.com";
        final String phone = "052-551-1861";
        final String zipcode = "450-0002";
        final String address = "架空の住所";
        Transaction tx = Datastore.beginTransaction();
        User user = service.put(tx, uid, name, mail, phone, zipcode, address);
        assertThat(user, is(notNullValue()));
        assertThat(user, instanceOf(Manager.class));
        Manager manager = (Manager)user;
        assertThat(manager.getName(), is(name));
        assertThat(manager.getMail().getEmail(), is(mail));
        assertThat(manager.getPhone().getNumber(), is(phone));
        assertThat(manager.getZipcode(), is(zipcode));
        assertThat(manager.getAddress().getAddress(), is(address));

        assertNotNull(user.getInitialKey());
        assertThat(user.getInitialKey(), not(""));

        Manager stored = Datastore.getOrNull(Manager.class, user.getUid());
        assertThat(stored, is(nullValue()));

        tx.commit();
        stored = Datastore.getOrNull(Manager.class, user.getUid());
        assertThat(stored, instanceOf(Manager.class));
        manager = (Manager)stored;
        assertThat(manager.getName(), is(name));
        assertThat(manager.getMail().getEmail(), is(mail));
        assertThat(manager.getPhone().getNumber(), is(phone));
        assertThat(manager.getZipcode(), is(zipcode));
        assertThat(manager.getAddress().getAddress(), is(address));
        assertThat(stored.getRegisteredDate(), is(stored.getUpdatedDate()));

    }

    /**
     * テストケースの実行後に呼ばれる処理。
     *
     * 登録したユーザを削除する。
     */
    @After
    public void after() {
        Datastore.delete(UserService.createKey(UID));
    }
}
