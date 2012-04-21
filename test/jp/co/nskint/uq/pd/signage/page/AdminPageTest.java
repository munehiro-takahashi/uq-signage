package jp.co.nskint.uq.pd.signage.page;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

import jp.co.nskint.uq.pd.signage.controller.AppUrls;
import jp.co.nskint.uq.pd.signage.model.Administrator;
import jp.co.nskint.uq.pd.signage.model.Editor;
import jp.co.nskint.uq.pd.signage.model.Manager;
import jp.co.nskint.uq.pd.signage.model.User;
import jp.co.nskint.uq.pd.signage.service.UserService;

import org.junit.Test;
import org.slim3.datastore.Datastore;

/**
 * AdminPageのテスト。
 *
 * @author NAGASAWA takahiro <tnagasaw@nskint.co.jp>
 *
 */
public class AdminPageTest extends PageTestBase {

    public AdminPageTest() {
        super(AppUrls.class, AdminPage.class);
    }

    static final String UID = "testuid0";
    static final String NAME = "テストユーザ";
    static final String MAIL = "testmail@nskint.co.jp";

    /**
     * <ul>
     * <li>administrator未登録の場合
     * <ul>
     * <li>administratorユーザを登録しようとすること</li>
     * <li>パスワードを登録しようとすること</li>
     * <li>Navigationオブジェクトが返されること</li>
     * <li>遷移先Pathは"/index.jsp"であること</li>
     * <li>forwardであること。</li>
     * </ul>
     * </li>
     * <li>administrator登録済の場合
     * <ul>
     * <li>ユーザ登録、パスワード登録共に行われないこと</li>
     * <li>Navigationオブジェクトが返されること</li>
     * <li>遷移先Pathは"/index.jsp"であること</li>
     * <li>forwardであること。</li>
     * </ul>
     * </li>
     * </ul>
     *
     * @see jp.co.nskint.uq.pd.signage.page.AdminPage#create()
     */
    @Test
    public void create() throws Exception {
        final String UID = "administrator";

        // administrator未登録の場合
        tester.start("/admin/create");
        assertThat(tester.getPage(), is(instanceOf(AdminPage.class)));
        assertThat(tester.getActionMethodName(), is("create"));
        assertThat(tester.isRedirect(), is(false));
        assertThat(tester.getDestinationPath(), is("/index.jsp"));

        Administrator admin =
            Datastore.get(Administrator.class, UserService.createKey(UID));

        assertThat(admin.getUid().getName(), is(UID));
        assertThat(admin.getPassword(), is("password"));
        assertThat(admin.getMail().getEmail(), is("tngasaw@nskint.co,jp"));
        assertThat(admin.getName(), is("管理者"));

        admin.setPassword("newpassword");

        Datastore.put(admin);

        // administrator登録済の場合
        tester.start("/admin/create");
        assertThat(tester.getPage(), is(instanceOf(AdminPage.class)));
        assertThat(tester.getActionMethodName(), is("create"));
        assertThat(tester.isRedirect(), is(false));
        assertThat(tester.getDestinationPath(), is("/index.jsp"));

        admin =
            Datastore.get(Administrator.class, UserService.createKey(UID));

        assertThat(admin.getUid().getName(), is(UID));
        assertThat(admin.getPassword(), is("newpassword"));
        assertThat(admin.getMail().getEmail(), is("tngasaw@nskint.co,jp"));
        assertThat(admin.getName(), is("管理者"));
    }

    /**
     * ログインユーザが管理者の場合。
     * <ul>
     * <li>Navigationオブジェクトが返されること</li>
     * <li>遷移先Pathは"/user/regist.jsp"であること</li>
     * <li>forwardであること</li>
     * <li>ページタイトルが設定されること</li>
     * </ul>
     *
     * @see jp.co.nskint.uq.pd.signage.page.AdminPage#regist()
     */
    @Test
    public void regist() throws Exception {

        // ログインユーザが管理者の場合。
        tester.request.getSession(true).setAttribute(
            AdminPage.SESS_KEY_LOGIN_USER,
            new Administrator());
        tester.start("/admin/regist");
        assertThat(tester.getPage(), is(instanceOf(AdminPage.class)));
        assertThat(tester.getActionMethodName(), is("regist"));
        assertThat(tester.isRedirect(), is(false));
        assertThat(tester.getDestinationPath(), is("/user/regist.jsp"));

        assertThat(tester.getErrors().isEmpty(), is(true));
    }

    /**
     * ログインユーザが管理者でない場合。
     * <ul>
     * <li>Navigationオブジェクトが返されること</li>
     * <li>遷移先Pathは"/error.jsp"であること</li>
     * <li>forwardであること</li>
     * <li>エラーメッセージが登録されていること</li>
     * </ul>
     *
     * @see jp.co.nskint.uq.pd.signage.page.AdminPage#regist()
     */
    @Test
    public void regist_error() throws Exception {

        // ログインユーザが管理者でない場合。
        tester.request.getSession(true).setAttribute(
            AdminPage.SESS_KEY_LOGIN_USER,
            new Manager());
        tester.start("/admin/regist");
        assertThat(tester.getPage(), is(instanceOf(AdminPage.class)));
        assertThat(tester.getActionMethodName(), is("regist"));
        assertThat(tester.isRedirect(), is(false));
        assertThat(tester.getDestinationPath(), is("/error.jsp"));

        assertThat(tester.getErrors().isEmpty(), is(false));
        assertThat(
            tester.getErrors().get("page"),
            is(getMessage("error.authority")));

        tester.request.getSession(true).setAttribute(
            AdminPage.SESS_KEY_LOGIN_USER,
            new Editor());
        tester.start("/admin/regist");
        assertThat(tester.getPage(), is(instanceOf(AdminPage.class)));
        assertThat(tester.getActionMethodName(), is("regist"));
        assertThat(tester.isRedirect(), is(false));
        assertThat(tester.getDestinationPath(), is("/error.jsp"));

        assertThat(tester.getErrors().isEmpty(), is(false));
        assertThat(
            tester.getErrors().get("page"),
            is(getMessage("error.authority")));

        tester.request.getSession(true).setAttribute(
            AdminPage.SESS_KEY_LOGIN_USER,
            new User());
        tester.start("/admin/regist");
        assertThat(tester.getPage(), is(instanceOf(AdminPage.class)));
        assertThat(tester.getActionMethodName(), is("regist"));
        assertThat(tester.isRedirect(), is(false));
        assertThat(tester.getDestinationPath(), is("/error.jsp"));

        assertThat(tester.getErrors().isEmpty(), is(false));
        assertThat(
            tester.getErrors().get("page"),
            is(getMessage("error.authority")));
    }

    /**
     * <ul>
     * <li>ログインユーザが管理者の場合
     * <li>入力項目にエラーが無い場合
     * <li>uidが未登録の場合
     * <li>メールが送信できた場合
     * <li>
     * <ul>
     * <li>Navigationオブジェクトが返されること</li>
     * <li>遷移先Pathは"/user/"であること</li>
     * <li>redirectであること</li>
     * </ul>
     * </li>
     * </ul>
     *
     * @see jp.co.nskint.uq.pd.signage.page.AdminPage#insert(java.lang.String,
     *      java.lang.String, java.lang.String)
     */
    @Test
    public void insert() throws Exception {

        // ログインユーザが管理者の場合
        tester.request.getSession(true).setAttribute(
            AdminPage.SESS_KEY_LOGIN_USER,
            new Administrator());

        tester.param("uid", UID);
        tester.param("name", NAME);
        tester.param("mail", MAIL);

        tester.start("/admin/insert");
        assertThat(tester.getPage(), is(instanceOf(AdminPage.class)));
        assertThat(tester.getActionMethodName(), is("insert"));
        assertThat(tester.isRedirect(), is(true));
        assertThat(tester.getDestinationPath(), is("/user/"));

        assertThat(tester.getErrors().isEmpty(), is(true));

        // UID長さ上限
        tester.param("uid", UID + UID);
        tester.param("name", NAME);
        tester.param("mail", MAIL);

        tester.start("/admin/insert");
        assertThat(tester.getPage(), is(instanceOf(AdminPage.class)));
        assertThat(tester.getActionMethodName(), is("insert"));
        assertThat(tester.isRedirect(), is(true));
        assertThat(tester.getDestinationPath(), is("/user/"));

        assertThat(tester.getErrors().isEmpty(), is(true));
    }

    /**
     * <ul>
     * <li>ログインユーザが管理者以外の場合
     * <ul>
     * <li>Navigationオブジェクトが返されること</li>
     * <li>遷移先Pathは"/error.jsp"であること</li>
     * <li>forwardであること</li>
     * <li>エラーメッセージが登録されていること</li>
     * </ul>
     * </li>
     *
     * @see jp.co.nskint.uq.pd.signage.page.AdminPage#insert(java.lang.String,
     *      java.lang.String, java.lang.String)
     */
    @Test
    public void insert_error_LoginUserIsNotAdministrator() throws Exception {

        // ログインユーザが管理者以外の場合
        tester.request.getSession(true).setAttribute(
            AdminPage.SESS_KEY_LOGIN_USER,
            new Manager());

        tester.param("uid", UID);
        tester.param("name", NAME);
        tester.param("mail", MAIL);

        tester.start("/admin/insert");
        assertThat(tester.getPage(), is(instanceOf(AdminPage.class)));
        assertThat(tester.getActionMethodName(), is("insert"));
        assertThat(tester.isRedirect(), is(false));
        assertThat(tester.getDestinationPath(), is("/error.jsp"));

        assertThat(tester.getErrors().isEmpty(), is(false));

        assertThat(
            tester.getErrors().get("page"),
            is(getMessage("error.authority")));

    }

    /**
     * UIDが登録済みの場合。
     * <ul>
     * <li>遷移先Pathは"/user/regist.jsp"であること</li>
     * <li>forwardであること</li>
     * <li>エラーメッセージの出力があること</li>
     * </ul>
     *
     * @see jp.co.nskint.uq.pd.signage.page.AdminPage#insert(java.lang.String,
     *      java.lang.String, java.lang.String)
     */
    @Test
    public void insert_error_registedUID() throws Exception {

        // ログインユーザが管理者の場合
        tester.request.getSession(true).setAttribute(
            AdminPage.SESS_KEY_LOGIN_USER,
            new Administrator());

        Administrator admin = new Administrator();
        admin.setUid(UserService.createKey(UID));
        admin.setName(NAME);
        Datastore.put(admin);

        tester.param("uid", UID);
        tester.param("name", NAME);
        tester.param("mail", MAIL);

        tester.start("/admin/insert");
        assertThat(tester.getPage(), is(instanceOf(AdminPage.class)));
        assertThat(tester.getActionMethodName(), is("insert"));
        assertThat(tester.isRedirect(), is(false));
        assertThat(tester.getDestinationPath(), is("/user/regist.jsp"));

        assertThat(tester.getErrors().isEmpty(), is(false));

        assertThat(
            tester.getErrors().get("uid"),
            is(getMessage("error.id", getMessage("label.uid"), UID)));
    }

    /**
     * UIDが入力エラーの場合。
     * <ul>
     * <li>遷移先Pathは"/user/regist.jsp"であること</li>
     * <li>forwardであること</li>
     * <li>エラーメッセージの出力があること</li>
     * </ul>
     *
     * @see jp.co.nskint.uq.pd.signage.page.AdminPage#insert(java.lang.String,
     *      java.lang.String, java.lang.String)
     */
    @Test
    public void insert_error_input_UID() throws Exception {

        tester.request.getSession(true).setAttribute(
            AdminPage.SESS_KEY_LOGIN_USER,
            new Administrator());

        // UID未入力
        tester.param("uid", "");
        tester.param("name", NAME);
        tester.param("mail", MAIL);

        tester.start("/admin/insert");
        assertThat(tester.getPage(), is(instanceOf(AdminPage.class)));
        assertThat(tester.getActionMethodName(), is("insert"));
        assertThat(tester.isRedirect(), is(false));
        assertThat(tester.getDestinationPath(), is("/user/regist.jsp"));

        assertThat(tester.getErrors().isEmpty(), is(false));

        assertThat(
            tester.getErrors().get("uid"),
            is(getMessage("validator.required", getMessage("label.uid"))));

        // UIDが短すぎる
        tester.param("uid", "0123456");
        tester.param("name", NAME);
        tester.param("mail", MAIL);

        tester.start("/admin/insert");
        assertThat(tester.getPage(), is(instanceOf(AdminPage.class)));
        assertThat(tester.getActionMethodName(), is("insert"));
        assertThat(tester.isRedirect(), is(false));
        assertThat(tester.getDestinationPath(), is("/user/regist.jsp"));

        assertThat(tester.getErrors().isEmpty(), is(false));

        assertThat(
            tester.getErrors().get("uid"),
            is(getMessage(
                "validator.minlength",
                getMessage("label.uid"),
                AdminPage.VALID_MIN_UID)));

        // UIDが長すぎる
        tester.param("uid", "0123456789abcdefg");
        tester.param("name", NAME);
        tester.param("mail", MAIL);

        tester.start("/admin/insert");
        assertThat(tester.getPage(), is(instanceOf(AdminPage.class)));
        assertThat(tester.getActionMethodName(), is("insert"));
        assertThat(tester.isRedirect(), is(false));
        assertThat(tester.getDestinationPath(), is("/user/regist.jsp"));

        assertThat(tester.getErrors().isEmpty(), is(false));

        assertThat(
            tester.getErrors().get("uid"),
            is(getMessage(
                "validator.maxlength",
                getMessage("label.uid"),
                AdminPage.VALID_MAX_UID)));
    }

    /**
     * 氏名が入力エラーの場合。
     * <ul>
     * <li>遷移先Pathは"/user/regist.jsp"であること</li>
     * <li>forwardであること</li>
     * <li>エラーメッセージの出力があること</li>
     * </ul>
     *
     * @see jp.co.nskint.uq.pd.signage.page.AdminPage#insert(java.lang.String,
     *      java.lang.String, java.lang.String)
     */
    @Test
    public void insert_error_input_name() throws Exception {

        tester.request.getSession(true).setAttribute(
            AdminPage.SESS_KEY_LOGIN_USER,
            new Administrator());

        // UID未入力
        tester.param("uid", UID);
        tester.param("name", "");
        tester.param("mail", MAIL);

        tester.start("/admin/insert");
        assertThat(tester.getPage(), is(instanceOf(AdminPage.class)));
        assertThat(tester.getActionMethodName(), is("insert"));
        assertThat(tester.isRedirect(), is(false));
        assertThat(tester.getDestinationPath(), is("/user/regist.jsp"));

        assertThat(tester.getErrors().isEmpty(), is(false));

        assertThat(
            tester.getErrors().get("name"),
            is(getMessage("validator.required", getMessage("label.name"))));

    }

    /**
     * メールアドレスが入力エラーの場合。
     * <ul>
     * <li>遷移先Pathは"/user/regist.jsp"であること</li>
     * <li>forwardであること</li>
     * <li>エラーメッセージの出力があること</li>
     * </ul>
     *
     * @see jp.co.nskint.uq.pd.signage.page.AdminPage#insert(java.lang.String,
     *      java.lang.String, java.lang.String)
     */
    @Test
    public void insert_error_input_mail() throws Exception {

        tester.request.getSession(true).setAttribute(
            AdminPage.SESS_KEY_LOGIN_USER,
            new Administrator());

        // メールアドレス未入力
        tester.param("uid", UID);
        tester.param("name", NAME);
        tester.param("mail", "");

        tester.start("/admin/insert");
        assertThat(tester.getPage(), is(instanceOf(AdminPage.class)));
        assertThat(tester.getActionMethodName(), is("insert"));
        assertThat(tester.isRedirect(), is(false));
        assertThat(tester.getDestinationPath(), is("/user/regist.jsp"));

        assertThat(tester.getErrors().isEmpty(), is(false));

        assertThat(
            tester.getErrors().get("mail"),
            is(getMessage("validator.required", getMessage("label.mail"))));

        // 不正なメールアドレス
        tester.param("uid", UID);
        tester.param("name", NAME);
        tester.param("mail", "mail.example.com");

        tester.start("/admin/insert");
        assertThat(tester.getPage(), is(instanceOf(AdminPage.class)));
        assertThat(tester.getActionMethodName(), is("insert"));
        assertThat(tester.isRedirect(), is(false));
        assertThat(tester.getDestinationPath(), is("/user/regist.jsp"));

        assertThat(tester.getErrors().isEmpty(), is(false));

        assertThat(
            tester.getErrors().get("mail"),
            is(getMessage("validator.regexp", getMessage("label.mail"))));

    }

    /**
     * ログインユーザが管理者の場合。
     * <ul>
     * <li>Navigationオブジェクトが返されること</li>
     * <li>遷移先Pathは"/user/edit.jsp"であること</li>
     * <li>forwardであること</li>
     * <li>ページタイトルが設定されること</li>
     * </ul>
     *
     * @see jp.co.nskint.uq.pd.signage.page.AdminPage#edit(java.lang.String)
     */
    @Test
    public void edit() throws Exception {

        // ログインユーザが管理者の場合。
        tester.request.getSession(true).setAttribute(
            AdminPage.SESS_KEY_LOGIN_USER,
            new Administrator());

        Administrator admin = new Administrator();
        admin.setUid(UserService.createKey(UID));
        admin.setName(NAME);
        Datastore.put(admin);

        tester.start("/admin/" + UID + "/edit");
        assertThat(tester.getPage(), is(instanceOf(AdminPage.class)));
        assertThat(tester.getActionMethodName(), is("edit"));
        assertThat(tester.isRedirect(), is(false));
        assertThat(tester.getDestinationPath(), is("/user/edit.jsp"));

        assertThat(tester.getErrors().isEmpty(), is(true));
    }

    /**
     * ログインユーザが管理者でない場合。
     * <ul>
     * <li>Navigationオブジェクトが返されること</li>
     * <li>遷移先Pathは"/error.jsp"であること</li>
     * <li>forwardであること</li>
     * <li>エラーメッセージが登録されていること</li>
     * </ul>
     *
     * @see jp.co.nskint.uq.pd.signage.page.AdminPage#edit(java.lang.String)
     */
    @Test
    public void edit_error() throws Exception {

        Administrator admin = new Administrator();
        admin.setUid(UserService.createKey(UID));
        admin.setName(NAME);
        Datastore.put(admin);

        // ログインユーザが管理者でない場合。
        tester.request.getSession(true).setAttribute(
            AdminPage.SESS_KEY_LOGIN_USER,
            new Manager());
        tester.start("/admin/" + UID + "/edit");
        assertThat(tester.getPage(), is(instanceOf(AdminPage.class)));
        assertThat(tester.getActionMethodName(), is("edit"));
        assertThat(tester.isRedirect(), is(false));
        assertThat(tester.getDestinationPath(), is("/error.jsp"));

        assertThat(tester.getErrors().isEmpty(), is(false));
        assertThat(
            tester.getErrors().get("page"),
            is(getMessage("error.authority")));

        tester.request.getSession(true).setAttribute(
            AdminPage.SESS_KEY_LOGIN_USER,
            new Editor());
        tester.start("/admin/" + UID + "/edit");
        assertThat(tester.getPage(), is(instanceOf(AdminPage.class)));
        assertThat(tester.getActionMethodName(), is("edit"));
        assertThat(tester.isRedirect(), is(false));
        assertThat(tester.getDestinationPath(), is("/error.jsp"));

        assertThat(tester.getErrors().isEmpty(), is(false));
        assertThat(
            tester.getErrors().get("page"),
            is(getMessage("error.authority")));

        tester.request.getSession(true).setAttribute(
            AdminPage.SESS_KEY_LOGIN_USER,
            new User());
        tester.start("/admin/" + UID + "/edit");
        assertThat(tester.getPage(), is(instanceOf(AdminPage.class)));
        assertThat(tester.getActionMethodName(), is("edit"));
        assertThat(tester.isRedirect(), is(false));
        assertThat(tester.getDestinationPath(), is("/error.jsp"));

        assertThat(tester.getErrors().isEmpty(), is(false));
        assertThat(
            tester.getErrors().get("page"),
            is(getMessage("error.authority")));
    }

    /**
     * 対象ユーザが未登録の場合。
     * <ul>
     * <li>Navigationオブジェクトが返されること</li>
     * <li>遷移先Pathは"/error.jsp"であること</li>
     * <li>forwardであること</li>
     * <li>エラーメッセージが登録されていること</li>
     * </ul>
     *
     * @see jp.co.nskint.uq.pd.signage.page.AdminPage#edit(java.lang.String)
     */
    @Test
    public void edit_error_NotFoundUser() throws Exception {

        // ログインユーザが管理者の場合。
        tester.request.getSession(true).setAttribute(
            AdminPage.SESS_KEY_LOGIN_USER,
            new Administrator());

        tester.start("/admin/" + UID + "/edit");
        assertThat(tester.getPage(), is(instanceOf(AdminPage.class)));
        assertThat(tester.getActionMethodName(), is("edit"));
        assertThat(tester.isRedirect(), is(false));
        assertThat(tester.getDestinationPath(), is("/error.jsp"));

        assertThat(tester.getErrors().isEmpty(), is(false));
        assertThat(
            tester.getErrors().get("page"),
            is(getMessage("error.invalid_user")));
    }

    /**
     * <ul>
     * <li>ログインユーザが管理者の場合
     * <li>入力項目にエラーが無い場合
     * <li>uidが未登録の場合
     * <li>メールが送信できた場合
     * <li>
     * <ul>
     * <li>Navigationオブジェクトが返されること</li>
     * <li>遷移先Pathは"/user/"であること</li>
     * <li>redirectであること</li>
     * </ul>
     * </li>
     * </ul>
     *
     * @see jp.co.nskint.uq.pd.signage.page.AdminPage#update(java.lang.String,
     *      java.lang.String, java.lang.String)
     */
    @Test
    public void update() throws Exception {

        // ログインユーザが管理者の場合
        tester.request.getSession(true).setAttribute(
            AdminPage.SESS_KEY_LOGIN_USER,
            new Administrator());

        tester.param("name", NAME);
        tester.param("mail", MAIL);

        Administrator admin = new Administrator();
        admin.setUid(UserService.createKey(UID));
        admin.setName(NAME);
        Datastore.put(admin);

        tester.start("/admin/" + UID + "/update");
        assertThat(tester.getPage(), is(instanceOf(AdminPage.class)));
        assertThat(tester.getActionMethodName(), is("update"));
        assertThat(tester.isRedirect(), is(true));
        assertThat(tester.getDestinationPath(), is("/user/"));

        assertThat(tester.getErrors().isEmpty(), is(true));

    }

    /**
     * 対象ユーザが未登録の場合。
     * <ul>
     * <li>Navigationオブジェクトが返されること</li>
     * <li>遷移先Pathは"/error.jsp"であること</li>
     * <li>forwardであること</li>
     * <li>エラーメッセージが登録されていること</li>
     * </ul>
     *
     * @see jp.co.nskint.uq.pd.signage.page.AdminPage#edit(java.lang.String)
     */
    @Test
    public void update_error_NotFoundUser() throws Exception {

        // ログインユーザが管理者の場合。
        tester.request.getSession(true).setAttribute(
            AdminPage.SESS_KEY_LOGIN_USER,
            new Administrator());

        tester.start("/admin/" + UID + "/update");
        assertThat(tester.getPage(), is(instanceOf(AdminPage.class)));
        assertThat(tester.getActionMethodName(), is("update"));
        assertThat(tester.isRedirect(), is(false));
        assertThat(tester.getDestinationPath(), is("/error.jsp"));

        assertThat(tester.getErrors().isEmpty(), is(false));
        assertThat(
            tester.getErrors().get("page"),
            is(getMessage("error.invalid_user")));
    }

    /**
     * ログインユーザが管理者以外の場合
     * <ul>
     * <li>Navigationオブジェクトが返されること</li>
     * <li>遷移先Pathは"/error.jsp"であること</li>
     * <li>forwardであること</li>
     * <li>エラーメッセージが登録されていること</li>
     * </ul>
     *
     * @see jp.co.nskint.uq.pd.signage.page.AdminPage#update(java.lang.String,
     *      java.lang.String, java.lang.String)
     */
    @Test
    public void update_error_LoginUserIsNotAdministrator() throws Exception {

        // ログインユーザが管理者以外の場合
        tester.request.getSession(true).setAttribute(
            AdminPage.SESS_KEY_LOGIN_USER,
            new Manager());

        tester.param("name", NAME);
        tester.param("mail", MAIL);

        Administrator admin = new Administrator();
        admin.setUid(UserService.createKey(UID));
        admin.setName(NAME);
        Datastore.put(admin);

        tester.start("/admin/" + UID + "/update");
        assertThat(tester.getPage(), is(instanceOf(AdminPage.class)));
        assertThat(tester.getActionMethodName(), is("update"));
        assertThat(tester.isRedirect(), is(false));
        assertThat(tester.getDestinationPath(), is("/error.jsp"));

        assertThat(tester.getErrors().isEmpty(), is(false));

        assertThat(
            tester.getErrors().get("page"),
            is(getMessage("error.authority")));

    }

    /**
     * ログインユーザが管理者以外の場合
     * <ul>
     * <li>Navigationオブジェクトが返されること</li>
     * <li>遷移先Pathは"/error.jsp"であること</li>
     * <li>forwardであること</li>
     * <li>エラーメッセージが登録されていること</li>
     * </ul>
     *
     * @see jp.co.nskint.uq.pd.signage.page.AdminPage#update(java.lang.String,
     *      java.lang.String, java.lang.String)
     */
    @Test
    public void update_error_TargetUserIsNotAdministrator() throws Exception {

        // ログインユーザが管理者以外の場合
        tester.request.getSession(true).setAttribute(
            AdminPage.SESS_KEY_LOGIN_USER,
            new Manager());

        tester.param("name", NAME);
        tester.param("mail", MAIL);

        Administrator admin = new Administrator();
        admin.setUid(UserService.createKey(UID));
        admin.setName(NAME);
        Datastore.put(admin);

        tester.start("/admin/" + UID + "/update");
        assertThat(tester.getPage(), is(instanceOf(AdminPage.class)));
        assertThat(tester.getActionMethodName(), is("update"));
        assertThat(tester.isRedirect(), is(false));
        assertThat(tester.getDestinationPath(), is("/error.jsp"));

        assertThat(tester.getErrors().isEmpty(), is(false));

        assertThat(
            tester.getErrors().get("page"),
            is(getMessage("error.authority")));

    }

    /**
     * 氏名が入力エラーの場合。
     * <ul>
     * <li>遷移先Pathは"/user/edit.jsp"であること</li>
     * <li>forwardであること</li>
     * <li>エラーメッセージの出力があること</li>
     * </ul>
     *
     * @see jp.co.nskint.uq.pd.signage.page.AdminPage#update(java.lang.String,
     *      java.lang.String, java.lang.String)
     */
    @Test
    public void update_error_input_name() throws Exception {

        tester.request.getSession(true).setAttribute(
            AdminPage.SESS_KEY_LOGIN_USER,
            new Administrator());

        // 氏名未入力
        tester.param("name", "");
        tester.param("mail", MAIL);

        Administrator admin = new Administrator();
        admin.setUid(UserService.createKey(UID));
        admin.setName(NAME);
        Datastore.put(admin);

        tester.start("/admin/" + UID + "/update");
        assertThat(tester.getPage(), is(instanceOf(AdminPage.class)));
        assertThat(tester.getActionMethodName(), is("update"));
        assertThat(tester.isRedirect(), is(false));
        assertThat(tester.getDestinationPath(), is("/user/edit.jsp"));

        assertThat(tester.getErrors().isEmpty(), is(false));

        assertThat(
            tester.getErrors().get("name"),
            is(getMessage("validator.required", getMessage("label.name"))));

    }

    /**
     * メールアドレスが入力エラーの場合。
     * <ul>
     * <li>遷移先Pathは"/user/edit.jsp"であること</li>
     * <li>forwardであること</li>
     * <li>エラーメッセージの出力があること</li>
     * </ul>
     *
     * @see jp.co.nskint.uq.pd.signage.page.AdminPage#update(java.lang.String,
     *      java.lang.String, java.lang.String)
     */
    @Test
    public void update_error_input_mail() throws Exception {

        tester.request.getSession(true).setAttribute(
            AdminPage.SESS_KEY_LOGIN_USER,
            new Administrator());

        // メールアドレス未入力
        tester.param("name", NAME);
        tester.param("mail", "");

        Administrator admin = new Administrator();
        admin.setUid(UserService.createKey(UID));
        admin.setName(NAME);
        Datastore.put(admin);

        tester.start("/admin/" + UID + "/update");
        assertThat(tester.getPage(), is(instanceOf(AdminPage.class)));
        assertThat(tester.getActionMethodName(), is("update"));
        assertThat(tester.isRedirect(), is(false));
        assertThat(tester.getDestinationPath(), is("/user/edit.jsp"));

        assertThat(tester.getErrors().isEmpty(), is(false));

        assertThat(
            tester.getErrors().get("mail"),
            is(getMessage("validator.required", getMessage("label.mail"))));

        // 不正なメールアドレス
        tester.param("name", NAME);
        tester.param("mail", "mail.example.com");

        tester.start("/admin/" + UID + "/update");
        assertThat(tester.getPage(), is(instanceOf(AdminPage.class)));
        assertThat(tester.getActionMethodName(), is("update"));
        assertThat(tester.isRedirect(), is(false));
        assertThat(tester.getDestinationPath(), is("/user/edit.jsp"));

        assertThat(tester.getErrors().isEmpty(), is(false));

        assertThat(
            tester.getErrors().get("mail"),
            is(getMessage("validator.regexp", getMessage("label.mail"))));

    }
}
