/**
 *
 */
package jp.co.nskint.uq.pd.signage.page;

import java.util.logging.Logger;

import jp.co.nskint.uq.pd.signage.model.Administrator;
import jp.co.nskint.uq.pd.signage.model.Editor;
import jp.co.nskint.uq.pd.signage.model.Manager;
import jp.co.nskint.uq.pd.signage.model.User;
import scenic3.ScenicPage;

/**
 * 各ページ共通
 *
 * <ul>
 * <li>ログインユーザ管理
 * <li>ロールのチェック処理
 * <li>メール送信
 * </ul>
 *
 * @author NAGASAWA Takahiro<tnagasaw@nskint.co.jp>
 */
public abstract class BasePage extends ScenicPage {

    protected Logger logger = Logger.getLogger(this.getClass().getName());

    /** ログインユーザを示すセッションキー */
    protected static final String SESS_KEY_LOGIN_USER = "loginUser";
    /** 正しいメールアドレスかどうかをチェックするためのパターン文字列 */
    protected static final String VALID_PAT_MAILADDR =
        "[0-9a-zA-Z_.\\-]+@[0-9a-zA-Z_\\-]+(\\.[0-9a-zA-Z_\\-]+){1,}";
    /** ユーザIDの最小文字数 */
    protected static final int VALID_MIN_UID = 8;
    /** ユーザIDの最大文字数 */
    protected static final int VALID_MAX_UID = 16;

    /** パスワードの最小文字数 */
    protected static final int VALID_MIN_PASSWD = 8;
    /** パスワードの最大文字数 */
    protected static final int VALID_MAX_PASSWD = 256;

    /**
     * ログイン中かどうかチェックする。
     *
     * @return true:ログイン中 / false:未ログイン|セッションタイムアウト
     */
    protected boolean checkLogin() {
        User user = getLoginUser();
        return user != null;
    }

    /**
     * ログインユーザが管理者かどうかチェックする。
     *
     * @return true:管理者である / false:管理者ではない
     */
    protected boolean checkAdmin() {
        User user = getLoginUser();
        return user != null && user instanceof Administrator;
    }

    /**
     * ログインユーザが編集者かどうかチェックする。
     *
     * @return true:編集者である / false:編集者ではない
     */
    protected boolean checkEditor() {
        User user = getLoginUser();
        return user != null && user instanceof Editor;
    }

    /**
     * ログインユーザが代表者かどうかチェックする。
     *
     * @return true:代表者である / false:代表者ではない
     */
    protected boolean checkManager() {
        User user = getLoginUser();
        return user != null && user instanceof Manager;
    }

    /**
     * ユーザ情報をセッションに登録し、ログイン状態にする。
     *
     * @param user
     *            ログインユーザ情報
     */
    protected void setLoginUser(User user) {
        request.getSession().setAttribute(SESS_KEY_LOGIN_USER, user);
    }

    /**
     * ログイン中のユーザ情報を取得する。
     *
     * @return ユーザ情報
     */
    protected User getLoginUser() {
        return (User) request.getSession().getAttribute(SESS_KEY_LOGIN_USER);
    }

    protected void startSession() {
        request.getSession(true);
    }

    /**
     * @return
     */
    protected void putEnteringLog() {
        String methodName =
            Thread.currentThread().getStackTrace()[2].getMethodName();
        logger.entering(this.getClass().getName(), methodName);
    }

    /**
     * @return
     */
    protected void putDebugLog(String message) {
        String methodName =
            Thread.currentThread().getStackTrace()[2].getMethodName();
        logger.finest(this.getClass().getName() + ":" + methodName + ":" + message);
    }

    /**
     * @return
     */
    protected void putExitingLog() {
        String methodName =
            Thread.currentThread().getStackTrace()[2].getMethodName();
        logger.exiting(this.getClass().getName(), methodName);
    }

}
