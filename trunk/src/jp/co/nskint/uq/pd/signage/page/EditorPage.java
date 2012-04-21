/** Copyright 2011 NIHON SYSTEM KAIHATSU LIMITED */
package jp.co.nskint.uq.pd.signage.page;

import jp.co.nskint.uq.pd.signage.model.Editor;
import jp.co.nskint.uq.pd.signage.model.Manager;
import jp.co.nskint.uq.pd.signage.model.User;
import jp.co.nskint.uq.pd.signage.service.EditorService;

import org.slim3.controller.Navigation;
import org.slim3.controller.validator.Validators;
import org.slim3.datastore.Datastore;
import org.slim3.util.ApplicationMessage;

import com.google.appengine.api.datastore.Transaction;

import scenic3.annotation.ActionPath;
import scenic3.annotation.Page;
import scenic3.annotation.RequestParam;
import scenic3.annotation.Var;

/**
 * 編集者情報 操作系画面
 *
 * @author NAGASAWA takahiro <tnagasaw@nskint.co.jp>
 */
@Page("/editor")
public class EditorPage extends BaseUserPage<EditorService> {

    private EditorService service = new EditorService();

    /**
     *
     */
    @Override
    public EditorService getService() {
        return service;
    }

    /**
     * 編集者登録画面
     *
     * @return
     */
    @ActionPath("regist")
    public Navigation regist() {
        putEnteringLog();
        try {
            if (checkManager()) {
                request.setAttribute(
                    "title",
                    ApplicationMessage.get("title.admin.regist"));
                return forward("/user/regist.jsp");
            } else {
                errors.put("page", ApplicationMessage.get("error.authority"));
            }
            return forward("/error.jsp");
        } finally {
            putExitingLog();
        }
    }

    /**
     * 編集者情報登録
     *
     * 未登録者が行うため、権限チェックは行わない。
     *
     * @param uid
     *            ユーザID
     * @param name
     *            氏名
     * @param mail
     *            メールアドレス
     */
    @ActionPath("insert")
    public Navigation insert(@RequestParam("uid") String uid,
            @RequestParam("name") String name, @RequestParam("mail") String mail) {
        putEnteringLog();
        request.setAttribute(
            "title",
            ApplicationMessage.get("title.editor.regist"));
        try {
            if (checkManager()) {
                Validators v = new Validators(request);
                v.add(
                    "uid",
                    v.required(),
                    v.maxlength(VALID_MAX_UID),
                    v.minlength(VALID_MIN_UID));
                v.add("name", v.required());
                v.add("mail", v.required(), v.regexp(VALID_PAT_MAILADDR));
                if (v.validate()) {
                    if (!validateInsertUser(uid)) {
                        // 登録画面を表示
                        return forward("/user/regist.jsp");
                    }
                    Transaction tx = Datastore.beginTransaction();
                    Editor user =
                        service.put(
                            tx,
                            uid,
                            name,
                            mail,
                            (Manager) getLoginUser());
                    // ユーザ登録メール送信
                    if (!sendConfirmationMail(user)) {
                        // メール送信失敗
                        tx.rollback();
                        // 登録画面を表示
                        return forward("/user/regist.jsp");
                    }
                    Datastore.put(user);
                    // エラーが無ければ登録
                    tx.commit();

                    // 登録終了画面(完了画面ではない)を表示
                    return redirect("/user/");
                } else {
                    return forward("/user/regist.jsp");
                }
            } else {
                errors.put("page", ApplicationMessage.get("error.authority"));
                return forward("/error.jsp");
            }
        } finally {
            putExitingLog();
        }
    }

    /**
     * 編集者編集画面
     *
     * @return
     */
    @ActionPath("{uid}/edit")
    public Navigation edit(@Var("uid") String uid) {
        putEnteringLog();
        try {
            User user = service.get(uid);
            if (user instanceof Editor) {
                if (checkEditRole(user)) {
                    request.setAttribute("uid", user.getUid().getName());
                    request.setAttribute("name", user.getName());
                    request.setAttribute("mail", user.getMail());
                    request.setAttribute(
                        "title",
                        ApplicationMessage.get("title.admin.edit"));
                    return forward("/user/edit.jsp");
                } else {
                    errors.put(
                        "page",
                        ApplicationMessage.get("error.authority"));
                }
            } else {
                errors
                    .put("page", ApplicationMessage.get("error.invalid_user"));
            }
            return forward("/error.jsp");
        } finally {
            putExitingLog();
        }
    }

    /**
     * 権限があるかチェックする。
     *
     * 対象編集者の属する代表者か、本人が編集できる。
     *
     * @param user
     * @return true:権限有り / false:権限無し
     */
    private boolean checkEditRole(User user) {
        User loginUser = getLoginUser();
        return (checkManager() && loginUser.getUid().equals(
            ((Editor) user).getManagerRef().getKey()))
            || loginUser.equals(user);
    }

    /**
     * 編集者情報更新
     *
     * @param name
     *            氏名
     * @param mail
     *            メールアドレス
     */
    @ActionPath("{uid}/update")
    public Navigation update(@Var("uid") String uid,
            @RequestParam("name") String name, @RequestParam("mail") String mail) {
        putEnteringLog();
        try {
            Validators v = new Validators(request);
            v.add("name", v.required());
            v.add("mail", v.required(), v.regexp(VALID_PAT_MAILADDR));
            if (v.validate()) {
                User tmp = service.get(uid);
                if (checkEditRole(tmp)) {
                    if (tmp instanceof Editor) {
                        Editor user = (Editor) tmp;
                        service.put(uid, name, mail, null);
                        user.setRegisteredDate(user.getUpdatedDate());
                        return redirect("/user/");
                    } else {
                        errors.put(
                            "page",
                            ApplicationMessage.get("error.invalid_user"));
                    }
                } else {
                    errors.put(
                        "page",
                        ApplicationMessage.get("error.authority"));
                }
            } else {
                return forward("/manager/edit.jsp");
            }
            // エラー画面を表示
            return forward("/error.jsp");
        } finally {
            putExitingLog();
        }
    }

}
