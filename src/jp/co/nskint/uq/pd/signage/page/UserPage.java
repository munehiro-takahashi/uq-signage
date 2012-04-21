/**
 *
 */
package jp.co.nskint.uq.pd.signage.page;

import java.util.Iterator;
import java.util.List;

import jp.co.nskint.uq.pd.signage.meta.UserMeta;
import jp.co.nskint.uq.pd.signage.model.Administrator;
import jp.co.nskint.uq.pd.signage.model.Editor;
import jp.co.nskint.uq.pd.signage.model.Layout;
import jp.co.nskint.uq.pd.signage.model.Manager;
import jp.co.nskint.uq.pd.signage.model.TimeLine;
import jp.co.nskint.uq.pd.signage.model.User;
import jp.co.nskint.uq.pd.signage.service.UserService;
import jp.co.nskint.uq.pd.signage.validator.StringValidator;

import org.slim3.controller.Navigation;
import org.slim3.controller.validator.Validators;
import org.slim3.datastore.Datastore;
import org.slim3.util.ApplicationMessage;

import scenic3.annotation.ActionPath;
import scenic3.annotation.Default;
import scenic3.annotation.Page;
import scenic3.annotation.RequestParam;
import scenic3.annotation.Var;

import com.google.appengine.api.datastore.Key;

/**
 * ユーザ情報(共通) 操作系画面
 *
 * @author NAGASAWA takahiro <tnagasaw@nskint.co.jp>
 */
@Page("/user")
public class UserPage extends BaseUserPage<UserService> {

    private UserService service = new UserService();

    /*
     * (非 Javadoc)
     *
     * @see jp.co.nskint.uq.pd.signage.page.BaseUserPage#getService()
     */
    @Override
    public UserService getService() {
        return service;
    }

    /**
     * ユーザ情報の削除
     *
     * @param uid
     *            ユーザID
     */
    @ActionPath("{uid}/remove")
    public Navigation remove(@Var("uid") String uid) {
        putEnteringLog();
        Key key = UserService.createKey(uid);
        User user = Datastore.get(UserMeta.get(), key);
        if (checkAdmin()
            || checkManager()
            && user instanceof Editor
            && ((Editor) user)
                .getManagerRef()
                .getModel()
                .equals(getLoginUser())) {
            if (user instanceof Manager) {
                // 代表者の場合は、付随する情報も削除する。
                Manager man = (Manager) user;


                    // レイアウト情報を削除
                    List<Layout> layouts =
                        man.getLayoutListRef().getModelList();
                    for (Layout layout : layouts) {
                        Datastore.delete(layout.getId());
                    }

                    List<TimeLine> timelines =
                        man.getTimelineRef().getModelList();
                    for (TimeLine timeline : timelines) {
                        // タイムライン情報を削除
                        Datastore.delete(timeline.getId());
                    }

                // 編集者情報を削除
                List<Editor> editors = man.getEditorListRef().getModelList();
                for (Iterator<Editor> iterator = editors.iterator(); iterator
                    .hasNext();) {
                    Editor editor = iterator.next();
                    Datastore.delete(editor.getUid());
                }
            }
            // ユーザ情報を削除
            Datastore.delete(key);
            return redirect("/menu");
        }
        errors.put("page", ApplicationMessage.get("error.authority"));
        return forward("error.jsp");
    }

    /**
     * ユーザ情報の編集。 ユーザの種類によって、処理を振り分ける。
     *
     * @param uid
     *            ユーザID
     */
    @ActionPath("{uid}/edit")
    public Navigation edit(@Var("uid") String uid) {
        putEnteringLog();
        Key key = UserService.createKey(uid);
        User user = Datastore.get(UserMeta.get(), key);
        User loginUser = getLoginUser();
        if (user instanceof Administrator && checkAdmin()) {
            // 管理者のみ、管理者情報の編集
            return redirect("/admin/" + uid + "/edit");
        } else if (user instanceof Manager
            && (checkAdmin() || loginUser.equals(user))) {
            // 管理者か本人の場合のみ代表者情報の編集
            return redirect("/manager/" + uid + "/edit");
        } else if (user instanceof Editor
            && (checkAdmin() || loginUser.equals(user) || checkManager()
                && ((Editor) user).getManagerRef().getModel().equals(loginUser))) {
            // 管理者か、本人か、所属する代表者の場合のみ
            // 編集者情報の編集
            return redirect("/editor/" + uid + "/edit");
        }
        errors.put("page", ApplicationMessage.get("error.authority"));
        return redirect("/error.jsp");
    }

    /**
     * パスワードの初期化
     *
     * @param uid
     *            ユーザID
     */
    @ActionPath("{uid}/initPassword")
    public Navigation initPassword(@Var("uid") String uid) {
        putEnteringLog();
        if (checkAdmin()) {
            // 管理者のみ、パスワード初期化が可能
            Key key = UserService.createKey(uid);
            User user = Datastore.get(UserMeta.get(), key);
            // ユーザ登録メール送信
            user.setPassword("");
            if (!sendConfirmationMail(user)) {
                return forward("/error.jsp");
            }
            Datastore.put(user);
        }
        return redirect("/menu");
    }

    /**
     * パスワード更新画面表示
     *
     * @param uid
     *            ユーザID
     */
    @ActionPath("{uid}/password")
    public Navigation password(@Var("uid") String uid) {
        putEnteringLog();
        logger.finest("uid : " + uid);
        try {
            User user = service.get(uid);
            User loginUser = getLoginUser();
            logger.finest("loginUser uid : " + loginUser.getUid().getName());
            logger.finest("equal? : " + user.equals(loginUser));
            if (user != null && user.equals(loginUser)) {
                // 本人しか変更できない
                request.setAttribute("user", user);
                return forward("/user/password.jsp");
            } else {
                errors.put("page", ApplicationMessage.get("error.authority"));
                return forward("/error.jsp");
            }
        } finally {
            putExitingLog();
        }
    }

    /**
     * パスワード更新
     *
     * @param uid
     *            ユーザID
     * @param oldPassword
     *            旧パスワード
     * @param newPassword
     *            新パスワード
     * @param newPassword2
     *            新パスワード2
     */
    @ActionPath("{uid}/updatePassword")
    public Navigation updatePassword(@Var("uid") String uid,
            @RequestParam("oldPassword") String oldPassword,
            @RequestParam("newPassword") String newPassword,
            @RequestParam("newPassword2") String newPassword2) {
        putEnteringLog();
        try {
            logger.finest("uid : " + uid +"  password : " + oldPassword + " / " + newPassword + " / " + newPassword2);
            User user = service.get(uid);
            if (user != null && user.equals(getLoginUser())) {
                // 本人しか変更できない
                Validators v = new Validators(request);
                logger.finest("old password : " + user.getPassword());
                // パスワードが空なら古いパスワードのチェックはしない。
                if (null != oldPassword) {
                    v.add("oldPassword", new StringValidator(user.getPassword()));
                }
                v.add(
                    "newPassword",
                    v.required(),
                    v.minlength(VALID_MIN_PASSWD),
                    v.maxlength(VALID_MAX_PASSWD),
                    new StringValidator(oldPassword, true));
                v.add("newPassword2", v.required(), new StringValidator(
                    newPassword));

                if (v.validate()) {
                    // パスワード更新
                    service.savePassword(uid, newPassword);

                    // ログイン状態にする
                    setLoginUser(user);
                    logger.finest("User : " + uid);
                    logger.finest("Password : " + newPassword);
                    return redirect("/menu");
                } else {
                    logger.finest("User : " + uid);
                    logger.finest("invalid password.");
                    request.setAttribute("user", user);
                    return forward("/user/password.jsp");
                }
            } else {
                logger.fine("user " + uid + " is not registered.");
                errors.put("page", ApplicationMessage.get("error.authority"));
                
                return forward("/error.jsp");
            }
        } finally {
            putExitingLog();
        }
    }

    /**
     * ユーザ情報登録完了
     */
    @ActionPath("{uid}/initial/{key}")
    public Navigation complete(@Var("uid") String uid, @Var("key") String key) {
        putEnteringLog();
        try {
            User user = service.get(uid);
            this.logger.finest("initial " + key + " / " + user.getInitialKey());
            if (user != null && user.getInitialKey().equals(key)) {
                setLoginUser(user);
                return forward("/user/" + uid + "/password");
            } else {
                errors.put(
                    "page",
                    ApplicationMessage.get("error.invalid_initialkey"));
            }
            return forward("/error.jsp");
        } finally {
            putExitingLog();
        }
    }

    // /**
    // * ユーザ情報の表示
    // *
    // * @param uid
    // * ユーザID
    // */
    // @ActionPath("{uid}")
    // public Navigation view(@Var("uid") String uid) {
    // final String methodName =
    // Thread.currentThread().getStackTrace()[1].getMethodName();
    // logger.entering(this.getClass().getName(), methodName);
    // User user = service.get(uid);
    //
    // try {
    // if (user != null) {
    // User loginUser = getLoginUser();
    // super.request.setAttribute("user", user);
    // if (user instanceof Manager) {
    // // ユーザが提供者の場合
    // if (checkAdmin() || user.equals(loginUser)) {
    // // 管理者か本人しか表示できない
    // return forward("/manager/view.jsp");
    // }
    // errors.put(
    // "page",
    // ApplicationMessage.get("error.authority"));
    // } else if (user instanceof Editor) {
    // // ユーザが編集者の場合
    // Editor editor = (Editor) user;
    // if (checkAdmin()
    // || user.equals(loginUser)
    // || editor.getManagerRef().getModel().equals(loginUser)) {
    // // 管理者か本人か代表者しか表示できない
    // return forward("/user/view.jsp");
    // }
    // errors.put(
    // "page",
    // ApplicationMessage.get("error.authority"));
    // } else if (user instanceof Administrator) {
    // // ユーザが管理者の場合
    // if (checkAdmin()) {
    // return forward("/user/view.jsp");
    // }
    // errors.put(
    // "page",
    // ApplicationMessage.get("error.authority"));
    // } else {
    // // その他のユーザの場合
    // errors.put(
    // "page",
    // ApplicationMessage.get("error.invalid_user"));
    // }
    // }
    // return forward("/error.jsp");
    // } finally {
    // logger.entering(this.getClass().getName(), methodName);
    // }
    // }

    /**
     * デフォルト表示画面(
     *
     * @return
     */
    @Default
    public Navigation index() {
        putEnteringLog();
        try {
            if (checkAdmin() || checkManager()) {
                request.setAttribute(
                    "title",
                    ApplicationMessage.get("title.user_list"));
                List<? extends User> users;
                if (checkAdmin()) {
                    users = Datastore.query(UserMeta.get()).asList();
                } else  {
                    users = ((Manager)getLoginUser()).getEditorListRef().getModelList();
                }
                request.setAttribute("users", users);
                return forward("/user/list.jsp");
            }
            errors.put("page", ApplicationMessage.get("error.authority"));
            return forward("error.jsp");
        } finally {
            putExitingLog();
        }
    }

}
