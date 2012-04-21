package jp.co.nskint.uq.pd.signage.service;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;
import jp.co.nskint.uq.pd.signage.model.Editor;
import jp.co.nskint.uq.pd.signage.model.Manager;
import jp.co.nskint.uq.pd.signage.model.User;

import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.slim3.datastore.Datastore;
import org.slim3.tester.AppEngineTestCase;

import com.google.appengine.api.datastore.Transaction;

/**
 * EditorServiceのテスト。
 * @author NAGASAWA takahiro <tnagasaw@nskint.co.jp>
 *
 */
public class EditorServiceTest extends AppEngineTestCase {

    private static final String UID = "testeditor";
    private static final String MUID = "testmanager";
    private EditorService service = new EditorService();

    private Manager manager = null;

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
     * テストケース実行前に行われる処理。
     *
     * <ul>
     * <li>テスト用のマネージャを登録する。</li>
     * </ul>
     */
    @Before
    public void before() {

        manager = (Manager)new ManagerService().put(MUID, "TEST Manager", "testmanager@mail.com", "052-551-1861", "450-0002", "架空の住所");
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
        User user = service.put(uid, name, mail, manager);
        assertThat(user, is(notNullValue()));
        assertThat(user, instanceOf(Editor.class));
        assertThat(user.getName(), is(name));
        assertThat(user.getMail().getEmail(), is(mail));

        assertNotNull(user.getInitialKey());
        assertThat(user.getInitialKey(), not(""));

        Editor stored = Datastore.get(Editor.class, user.getUid());
        assertThat(stored, instanceOf(Editor.class));
        assertThat(stored.getName(), is(name));
        assertThat(stored.getMail().getEmail(), is(mail));
        assertThat(stored.getRegisteredDate(), is(stored.getUpdatedDate()));

        // パスワードを保存する
        service.savePassword(uid, "password");

        final String name2 = "Test User2";
        final String mail2 = "testuser2@mail.com";
        user = service.put(uid, name2, mail2, manager);
        assertThat(user, is(notNullValue()));
        assertThat(user, instanceOf(Editor.class));
        assertThat(user.getName(), is(name2));
        assertThat(user.getMail().getEmail(), is(mail2));

        assertThat(user.getInitialKey(), is(""));

        stored = Datastore.get(Editor.class, user.getUid());
        assertThat(stored, instanceOf(Editor.class));
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
        User user = service.put(tx, uid, name, mail, manager);
        assertThat(user, is(notNullValue()));
        assertThat(user, instanceOf(Editor.class));
        assertThat(user.getName(), is(name));
        assertThat(user.getMail().getEmail(), is(mail));

        assertNotNull(user.getInitialKey());
        assertThat(user.getInitialKey(), not(""));

        Editor stored = Datastore.getOrNull(Editor.class, user.getUid());
        assertThat(stored, is(nullValue()));

        tx.commit();
        stored = Datastore.getOrNull(Editor.class, user.getUid());
        assertThat(stored, instanceOf(Editor.class));
        assertThat(stored.getName(), is(name));
        assertThat(stored.getMail().getEmail(), is(mail));
        assertThat(stored.getRegisteredDate(), is(stored.getUpdatedDate()));

    }

    /**
     * テストケースの実行後に呼ばれる処理。
     *
     * <ul>
     * <li>登録したユーザを削除する。</li>
     * <li>テスト用のマネージャを削除する。</li>
     * </ul>
     */
    @After
    public void after() {
        Datastore.delete(UserService.createKey(UID));
        Datastore.delete(manager.getUid());
    }
}
