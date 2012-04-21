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

import com.google.appengine.api.datastore.Email;

/**
 * FrontPageのテスト。
 *
 * Slim3はDI機能が無いため、リフレクションを用いてコンテナ回りのオブジェクトを、 ページオブジェクトに注入する必要がある。
 * そのため、PageTestBaseにリフレクションでオブジェクトを注入するためのメソッドを用意している。
 *
 * @author NAGASAWA takahiro <tnagasaw@nskint.co.jp>
 *
 */
public class FrontPageTest extends PageTestBase {

    public FrontPageTest() {
        super(AppUrls.class, FrontPage.class);

    }

    /**
     * <ul>
     * <li>Navigationオブジェクトが返されること</li>
     * <li>遷移先Pathは"/index.jsp"であること</li>
     * <li>forwardであること。</li>
     * </ul>
     */
    @Test
    public void index() throws Exception {

        this.tester.start("/");
        assertThat(tester.getPage(), is(instanceOf(FrontPage.class)));
        assertThat(tester.getActionMethodName(), is("index"));
        assertThat(tester.isRedirect(), is(false));
        assertThat(tester.getDestinationPath(), is("/index.jsp"));
        assertThat(tester.getErrors().isEmpty(), is(true));
    }

    /**
     * </li> <li>ログインユーザが不正なユーザである場合
     * <ul>
     * <li>Navigationオブジェクトが返されること</li>
     * <li>遷移先Pathは"/error.jsp"であること</li>
     * <li>forwardであること。</li>
     * </ul>
     * </li> <li>ログインユーザがいない場合
     * <ul>
     * <li>nullが返されること</li>
     * <li>遷移先Pathは"/error.jsp"であること</li>
     * <li>forwardであること。</li>
     * </ul>
     * </li> </ul>
     */
    @Test
    public void menu_error() throws Exception {

        // ログインユーザが不正なユーザである場合(UserオブジェクトそのままのユーザはDB二登録されない)
        tester.request.getSession(true).setAttribute(
            FrontPage.SESS_KEY_LOGIN_USER,
            new User());
        tester.start("/menu");
        assertThat(tester.getPage(), is(instanceOf(FrontPage.class)));
        assertThat(tester.getActionMethodName(), is("menu"));
        assertThat(tester.isRedirect(), is(false));
        assertThat(tester.getDestinationPath(), is("/error.jsp"));
        assertThat(tester.getErrors().isEmpty(), is(true));

        // ログインユーザがない場合
        tester.request.getSession(true).setAttribute(
            FrontPage.SESS_KEY_LOGIN_USER,
            null);
        tester.start("/menu");
        assertThat(tester.getPage(), is(instanceOf(FrontPage.class)));
        assertThat(tester.getActionMethodName(), is("menu"));
        assertThat(tester.isRedirect(), is(false));
        assertThat(tester.getDestinationPath(), is("/error.jsp"));
        assertThat(tester.getErrors().isEmpty(), is(true));
    }

    /**
     * <ul>
     * <li>ログインユーザが代表者である場合
     * <ul>
     * <li>Navigationオブジェクトが返されること</li>
     * <li>遷移先Pathは"/board/"であること</li>
     * <li>redirectであること。</li>
     * </ul>
     * </li>
     * <li>ログインユーザが編集者である場合
     * <ul>
     * <li>Navigationオブジェクトが返されること</li>
     * <li>遷移先Pathは"/board/"であること</li>
     * <li>redirectであること。</li>
     * </ul>
     * </li>
     * <li>ログインユーザが管理者である場合
     * <ul>
     * <li>Navigationオブジェクトが返されること</li>
     * <li>遷移先Pathは"/user/"であること</li>
     * <li>redirectであること。</li>
     * </ul>
     * </ul>
     */
    @Test
    public void menu() throws Exception {

        // ログインユーザが代表者である場合
        tester.request.getSession(true).setAttribute(
            FrontPage.SESS_KEY_LOGIN_USER,
            new Manager());
        tester.start("/menu");
        assertThat(tester.getPage(), is(instanceOf(FrontPage.class)));
        assertThat(tester.getActionMethodName(), is("menu"));
        assertThat(tester.isRedirect(), is(true));
        assertThat(tester.getDestinationPath(), is("/board/"));
        assertThat(tester.getErrors().isEmpty(), is(true));

        // ログインユーザが編集者である場合
        tester.request.getSession(true).setAttribute(
            FrontPage.SESS_KEY_LOGIN_USER,
            new Editor());
        tester.start("/menu");
        assertThat(tester.getPage(), is(instanceOf(FrontPage.class)));
        assertThat(tester.getActionMethodName(), is("menu"));
        assertThat(tester.isRedirect(), is(true));
        assertThat(tester.getDestinationPath(), is("/board/"));
        assertThat(tester.getErrors().isEmpty(), is(true));

        // ログインユーザが管理者である場合
        tester.request.getSession(true).setAttribute(
            FrontPage.SESS_KEY_LOGIN_USER,
            new Administrator());
        tester.start("/menu");
        assertThat(tester.getPage(), is(instanceOf(FrontPage.class)));
        assertThat(tester.getActionMethodName(), is("menu"));
        assertThat(tester.isRedirect(), is(true));
        assertThat(tester.getDestinationPath(), is("/user/"));
        assertThat(tester.getErrors().isEmpty(), is(true));

    }

    /**
     * <ul>
     * <li>Navigationオブジェクトが返されること</li>
     * <li>遷移先Pathは"/login.jsp"であること</li>
     * <li>forwardであること</li>
     * <li>requestのattributeに"url"と言うキーで、引数として渡した値がセットされること</li>
     * </ul>
     */
    @Test
    public void login() throws Exception {
        final String url = "testurl";
        tester.start("/login");
        assertThat(tester.getPage(), is(instanceOf(FrontPage.class)));
        assertThat(tester.getActionMethodName(), is("login"));
        assertThat(tester.isRedirect(), is(false));
        assertThat(tester.getDestinationPath(), is("/login.jsp"));
        assertThat(tester.getErrors().isEmpty(), is(true));

        tester.param("url", url);
        tester.start("/login");
        assertThat(tester.getPage(), is(instanceOf(FrontPage.class)));
        assertThat(tester.getActionMethodName(), is("login"));
        assertThat(tester.isRedirect(), is(false));
        assertThat(tester.getDestinationPath(), is("/login.jsp"));
        assertEquals(tester.requestScope("url"), url);
        assertThat(tester.getErrors().isEmpty(), is(true));
    }

    /**
     * 元URLを指定して、ログインに成功する場合
     * <ul>
     * <li>Navigationオブジェクトが返されること</li>
     * <li>遷移先Pathは引数で指定した値であること</li>
     * <li>forwardであること</li>
     * </ul>
     * </ul>
     */
    @Test
    public void doLogin() throws Exception {
        final String uid = "testuid0";
        final String password = "testpassword";
        final String url = "/testuri";

        Administrator user = new Administrator();
        user.setUid(UserService.createKey(uid));
        user.setName("Test User Name");
        user.setPassword(password);
        user.setMail(new Email("email@example.com"));
        Datastore.put(user);

        // 元URLを指定して、ログインに成功する場合
        tester.param("url", url);
        tester.param("uid", uid);
        tester.param("password", password);
        tester.start("/doLogin");
        assertThat(tester.getPage(), is(instanceOf(FrontPage.class)));
        assertThat(tester.getActionMethodName(), is("doLogin"));
        assertThat(tester.isRedirect(), is(false));
        assertThat(tester.getDestinationPath(), is(url));
        assertThat(tester.getErrors().isEmpty(), is(true));
    }

    /**
     * 元URLを指定せず(null)、ログインに成功する場合
     * <ul>
     * <li>Navigationオブジェクトが返されること</li>
     * <li>遷移先Pathは"/menu"であること</li>
     * <li>redirectであること</li>
     * </ul>
     */
    @Test
    public void doLogin2() throws Exception {
        final String uid = "testuid0";
        final String password = "testpassword";

        Administrator user = new Administrator();
        user.setUid(UserService.createKey(uid));
        user.setName("Test User Name");
        user.setPassword(password);
        user.setMail(new Email("email@example.com"));
        Datastore.put(user);

        // 元URLを指定せず(null)、ログインに成功する場合
        tester.param("uid", uid);
        tester.param("password", password);
        tester.start("/doLogin");
        assertThat(tester.getPage(), is(instanceOf(FrontPage.class)));
        assertThat(tester.getActionMethodName(), is("doLogin"));
        assertThat(tester.isRedirect(), is(true));
        assertThat(tester.getDestinationPath(), is("/menu"));
        assertThat(tester.getErrors().isEmpty(), is(true));
    }

    /**
     * 元URLを指定せず("")、ログインに成功する場合
     * <ul>
     * <li>Navigationオブジェクトが返されること</li>
     * <li>遷移先Pathは"/menu"であること</li>
     * <li>redirectであること</li>
     * </ul>
     */
    @Test
    public void doLogin3() throws Exception {
        final String uid = "testuid0";
        final String password = "testpassword";

        Administrator user = new Administrator();
        user.setUid(UserService.createKey(uid));
        user.setName("Test User Name");
        user.setPassword(password);
        user.setMail(new Email("email@example.com"));
        Datastore.put(user);

        // 元URLを指定せず("")、ログインに成功する場合
        tester.param("uid", uid);
        tester.param("password", password);
        tester.param("url", "");
        tester.start("/doLogin");
        assertThat(tester.getPage(), is(instanceOf(FrontPage.class)));
        assertThat(tester.getActionMethodName(), is("doLogin"));
        assertThat(tester.isRedirect(), is(true));
        assertThat(tester.getDestinationPath(), is("/menu"));
        assertThat(tester.getErrors().isEmpty(), is(true));
    }

    /**
     * 指定したユーザがいない場合
     * <ul>
     * <li>Navigationオブジェクトが返されること</li>
     * <li>遷移先Pathは"/login.jsp"であること</li>
     * <li>forwardであること</li>
     * <li>requestのattributeに"url"と言うキーで、引数として渡した値がセットされること</li>
     * </ul>
     */
    @Test
    public void doLogin_error1() throws Exception {
        final String uid = "testuid0";
        final String password = "testpassword";

        Administrator user = new Administrator();
        user.setUid(UserService.createKey(uid));
        user.setName("Test User Name");
        user.setPassword(password);
        user.setMail(new Email("email@example.com"));
        Datastore.put(user);

        // 指定したユーザがいない場合
        tester.param("uid", uid + "a");
        tester.param("passwoed", password);
        tester.start("/doLogin");
        assertThat(tester.getPage(), is(instanceOf(FrontPage.class)));
        assertThat(tester.getActionMethodName(), is("doLogin"));
        assertThat(tester.isRedirect(), is(false));
        assertThat(tester.getDestinationPath(), is("/login.jsp"));
        assertThat(tester.getErrors().isEmpty(), is(false));
        assertThat(
            tester.getErrors().get("uid"),
            is(getMessage("error.login")));
    }

    /**
     * パスワードが間違っている場合
     * <ul>
     * <li>Navigationオブジェクトが返されること</li>
     * <li>遷移先Pathは"/login.jsp"であること</li>
     * <li>forwardであること</li>
     * <li>requestのattributeに"url"と言うキーで、引数として渡した値がセットされること</li>
     * </ul>
     */
    @Test
    public void doLogin_error2() throws Exception {
        final String uid = "testuid0";
        final String password = "testpassword";

        Administrator user = new Administrator();
        user.setUid(UserService.createKey(uid));
        user.setName("Test User Name");
        user.setPassword(password);
        user.setMail(new Email("email@example.com"));
        Datastore.put(user);

        // パスワードが間違っている場合
        tester.param("uid", uid);
        tester.param("passwoed", password + "a");
        tester.start("/doLogin");
        assertThat(tester.getPage(), is(instanceOf(FrontPage.class)));
        assertThat(tester.getActionMethodName(), is("doLogin"));
        assertThat(tester.isRedirect(), is(false));
        assertThat(tester.getDestinationPath(), is("/login.jsp"));
        assertThat(tester.getErrors().isEmpty(), is(false));
        assertThat(
            tester.getErrors().get("uid"),
            is(getMessage("error.login")));
    }

}
