package jp.co.nskint.uq.pd.signage.service;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;
import jp.co.nskint.uq.pd.signage.model.Administrator;
import jp.co.nskint.uq.pd.signage.model.User;

import org.junit.After;
import org.junit.BeforeClass;
import org.junit.Test;
import org.slim3.datastore.Datastore;
import org.slim3.tester.AppEngineTestCase;

import com.google.appengine.api.datastore.Transaction;

/**
 * AdminServiceのテスト。
 * UserServiceのテストを含む。
 * @author NAGASAWA takahiro <tnagasaw@nskint.co.jp>
 *
 */
public class AdminServiceTest extends AppEngineTestCase {

    private static final String UID = "testuserid";
    private AdminService service = new AdminService();

    /**
     * テストクラス実行前に行われる処理。
     *
     * パスワードを暗号しているので、ユーザ系のモデルを触るときはキー設定を行う。
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
        User user = service.put(uid, name, mail);
        assertThat(user, is(notNullValue()));
        assertThat(user, instanceOf(Administrator.class));
        assertThat(user.getName(), is(name));
        assertThat(user.getMail().getEmail(), is(mail));

        assertNotNull(user.getInitialKey());
        assertThat(user.getInitialKey(), not(""));

        Administrator stored = Datastore.get(Administrator.class, user.getUid());
        assertThat(stored, instanceOf(Administrator.class));
        assertThat(stored.getName(), is(name));
        assertThat(stored.getMail().getEmail(), is(mail));
        assertThat(stored.getRegisteredDate(), is(stored.getUpdatedDate()));

        // パスワードを保存する
        service.savePassword(uid, "password");

        final String name2 = "Test User2";
        final String mail2 = "testuser2@mail.com";
        user = service.put(uid, name2, mail2);
        assertThat(user, is(notNullValue()));
        assertThat(user, instanceOf(Administrator.class));
        assertThat(user.getName(), is(name2));
        assertThat(user.getMail().getEmail(), is(mail2));

        assertThat(user.getInitialKey(), is(""));

        stored = Datastore.get(Administrator.class, user.getUid());
        assertThat(stored, instanceOf(Administrator.class));
        assertThat(stored.getName(), is(name2));
        assertThat(stored.getMail().getEmail(), is(mail2));
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
        Transaction tx = Datastore.beginTransaction();
        User user = service.put(tx, uid, name, mail);
        assertThat(user, is(notNullValue()));
        assertThat(user, instanceOf(Administrator.class));
        assertThat(user.getName(), is(name));
        assertThat(user.getMail().getEmail(), is(mail));

        assertNotNull(user.getInitialKey());
        assertThat(user.getInitialKey(), not(""));

        Administrator stored = Datastore.getOrNull(Administrator.class, user.getUid());
        assertThat(stored, is(nullValue()));

        tx.commit();
        stored = Datastore.getOrNull(Administrator.class, user.getUid());
        assertThat(stored, instanceOf(Administrator.class));
        assertThat(stored.getName(), is(name));
        assertThat(stored.getMail().getEmail(), is(mail));
        assertThat(stored.getRegisteredDate(), is(stored.getUpdatedDate()));

    }

    /**
     * UID重複チェックのテスト
     * (対UserService)
     * <ul>
     * <li>保存されていない状態でtrueを返すこと</li>
     * <li>保存されている状態でfalseを返すこと</li>
     * </ul>
     */
    @Test
    public void checkDuplicationUid() throws Exception {
        final String uid = UID;
        assertTrue(service.checkDuplicationUid(uid));

        final String name = "Test User";
        final String mail = "testuser@mail.com";
        service.put(uid, name, mail);

        assertFalse(service.checkDuplicationUid(uid));

    }
    /**
     * パスワード保存機能のテスト
     * (対UserService)
     * <ul>
     * <li>パスワードが保存されること</li>
     * <li>初期化キーがクリアされること</li>
     * </ul>
     */
    @Test
    public void savePassword() throws Exception {
        final String uid = UID;
        final String name = "Test User";
        final String mail = "testuser@mail.com";
        service.put(uid, name, mail);

        // パスワードを保存する
        final String password = "password";
        service.savePassword(uid, password);

        Administrator user = Datastore.get(Administrator.class, UserService.createKey(uid));
        assertThat(user.getPassword(), is(password));
        assertThat(user.getInitialKey(), is(""));
    }
    /**
     * 削除機能のテスト
     * (対UserService)
     * <ul>
     * <li>登録したユーザが削除処理によって削除されること。</li>
     * </ul>
     */
    @Test
    public void delete() throws Exception {
        final String uid = UID;
        final String name = "Test User";
        final String mail = "testuser@mail.com";
        User user = service.put(uid, name, mail);

        Administrator stored = Datastore.getOrNull(Administrator.class, user.getUid());
        assertThat(stored, is(notNullValue()));

        // 削除
        service.delete(UID);

        stored = Datastore.getOrNull(Administrator.class, user.getUid());
        assertThat(stored, is(nullValue()));

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
