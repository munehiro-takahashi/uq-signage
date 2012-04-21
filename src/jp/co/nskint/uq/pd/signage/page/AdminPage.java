/** Copyright 2011 NIHON SYSTEM KAIHATSU LIMITED */
package jp.co.nskint.uq.pd.signage.page;

import jp.co.nskint.uq.pd.signage.model.Administrator;
import jp.co.nskint.uq.pd.signage.model.User;
import jp.co.nskint.uq.pd.signage.service.AdminService;

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
 * 管理者情報 操作系画面
 *
 * @author NAGASAWA takahiro <tnagasaw@nskint.co.jp>
 */
@Page("/admin")
public class AdminPage extends BaseUserPage<AdminService> {

    private AdminService service = new AdminService();

    /*
     * (非 Javadoc)
     *
     * @see jp.co.nskint.uq.pd.signage.page.BaseUserPage#getService()
     */
    @Override
    public AdminService getService() {
        return service;
    }

    /**
     * 管理者作成. 最初の一人の管理者(administrator)を作る。 最初の一人が作られて以降は、何も行わない。
     *
     * @return
     */
    @ActionPath("create")
    public Navigation create() {
        putEnteringLog();
        try {
            final String uid = "administrator";
            User admin = getService().get(uid);

            if (admin == null) {
                // "administrator"が未登録の時のみ
                getService().put(
                    uid,
                    ApplicationMessage.get("admin.name"),
                    ApplicationMessage.get("admin.mail"));
                getService().savePassword(uid, "password");
            }
            return this.forward("/index.jsp");
        } finally {
            putExitingLog();
        }
    }

    /**
     * 管理者登録画面
     *
     * @return
     */
    @ActionPath("regist")
    public Navigation regist() {
        putEnteringLog();
        try {
            if (checkAdmin()) {
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
     * 管理者情報登録
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
            ApplicationMessage.get("title.admin.regist"));
        try {
            if (checkAdmin()) {
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
                    User user = getService().put(tx, uid, name, mail);
                    // ユーザ登録メール送信
                    if (!sendConfirmationMail(user)) {
                        // メール送信失敗
                        // 失敗したら、ユーザ登録は取りやめ
                        tx.rollback();
                        // 登録画面を表示
                        return forward("/user/regist.jsp");
                    }
                    
                    Datastore.put(user);
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
     * 管理者編集画面
     *
     * @return
     */
    @ActionPath("{uid}/edit")
    public Navigation edit(@Var("uid") String uid) {
        putEnteringLog();
        try {
            if (checkAdmin()) {
                User user = getService().get(uid);
                if (user != null && user instanceof Administrator) {
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
                        ApplicationMessage.get("error.invalid_user"));
                }
            } else {
                errors.put("page", ApplicationMessage.get("error.authority"));
            }
            return forward("/error.jsp");
        } finally {
            putExitingLog();
        }
    }

    /**
     * 管理者情報更新
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
            if (checkAdmin()) {
                User tmp = getService().get(uid);
                if (tmp != null && tmp instanceof Administrator) {
                    Validators v = new Validators(request);
                    v.add("name", v.required());
                    v.add("mail", v.required(), v.regexp(VALID_PAT_MAILADDR));
                    if (v.validate()) {
                        getService().put(uid, name, mail);
                        return redirect("/user/");
                    } else {
                        return forward("/user/edit.jsp");
                    }
                } else {
                    errors.put(
                        "page",
                        ApplicationMessage.get("error.invalid_user"));
                }
            } else {
                errors.put("page", ApplicationMessage.get("error.authority"));
            }
            // エラー画面を表示
            return forward("/error.jsp");
        } finally {
            putExitingLog();
        }
    }
}
