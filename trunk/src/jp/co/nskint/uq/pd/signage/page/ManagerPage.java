package jp.co.nskint.uq.pd.signage.page;

import jp.co.nskint.uq.pd.signage.model.Manager;
import jp.co.nskint.uq.pd.signage.model.User;
import jp.co.nskint.uq.pd.signage.service.ManagerService;

import org.slim3.controller.Navigation;
import org.slim3.controller.validator.Validators;
import org.slim3.datastore.Datastore;
import org.slim3.util.ApplicationMessage;

import scenic3.annotation.ActionPath;
import scenic3.annotation.Page;
import scenic3.annotation.RequestParam;
import scenic3.annotation.Var;

import com.google.appengine.api.datastore.Transaction;

/**
 * 代表者情報 操作系画面
 *
 * @author NAGASAWA takahiro <tnagasaw@nskint.co.jp>
 */
@Page("/manager")
public class ManagerPage extends BaseUserPage<ManagerService> {

    private ManagerService service = new ManagerService();
    /* (非 Javadoc)
     * @see jp.co.nskint.uq.pd.signage.page.BaseUserPage#getService()
     */
    @Override
    public ManagerService getService() {
        return service;
    }

    /**
     * 代表者情報登録画面表示
     *
     * 未登録者が行うため、権限チェックは行わない。
     */
    @ActionPath("regist")
    public Navigation regist() {
        request.setAttribute(
            "title",
            ApplicationMessage.get("title.manager.regist"));
        // 登録画面を表示
        return forward("/manager/regist.jsp");
    }

    /**
     * 代表者情報登録
     *
     * 未登録者が行うため、権限チェックは行わない。
     *
     * @param uid
     *            ユーザID
     * @param name
     *            氏名
     * @param mail
     *            メールアドレス
     * @param phone
     *            電話番号
     * @param zipcode
     *            郵便番号
     * @param address
     *            住所
     */
    @ActionPath("insert")
    public Navigation insert(@RequestParam("uid") String uid,
            @RequestParam("name") String name,
            @RequestParam("mail") String mail,
            @RequestParam("phone") String phone,
            @RequestParam("zipcode") String zipcode,
            @RequestParam("address") String address) {
        putEnteringLog();
        request.setAttribute(
            "title",
            ApplicationMessage.get("title.manager.regist"));
        try {
            if (!validateInsertUser(uid)) {
                // 登録画面を表示
                return forward("/manager/regist.jsp");
            }
            Validators v = getValidator();
            v.add(
                "uid",
                v.required(),
                v.maxlength(VALID_MAX_UID),
                v.minlength(VALID_MIN_UID));
            if (v.validate()) {
                Transaction tx= Datastore.beginTransaction();
                User user =
                    service.put(tx, uid, name, mail, phone, zipcode, address);
                // ユーザ登録メール送信
                if (!sendConfirmationMail(user)) {
                    // メール送信失敗
                    tx.rollback();
                    return forward("/manager/regist.jsp");
                }

                Datastore.put(user);
                // エラーが無ければ登録
                tx.commit();

                // 登録終了画面(完了画面ではない)を表示
                return forward("/manager/registered.jsp");
            } else {
                return forward("/manager/regist.jsp");
            }
        } finally {
            putExitingLog();
        }
    }

    /**
     * 代表者情報更新画面表示
     *
     * @param uid
     */
    @ActionPath("{uid}/edit")
    public Navigation edit(@Var("uid") String uid) {
        putEnteringLog();
        try {
            User user = service.get(uid);
            if (roleCheck(user) && user instanceof Manager) {
                Manager manager = (Manager) user;
                request.setAttribute("uid", manager.getUid().getName());
                request.setAttribute("name", manager.getName());
                request.setAttribute("mail", manager.getMail());
                request.setAttribute("phone", manager.getPhone());
                request.setAttribute("zipcode", manager.getZipcode());
                request.setAttribute("address", manager.getAddress());
                request.setAttribute(
                    "title",
                    ApplicationMessage.get("title.manager.regist"));

                // 登録画面を表示
                return forward("/manager/edit.jsp");
            }
            // エラー画面を表示
            return forward("/error.jsp");
        } finally {
            putExitingLog();
        }
    }

    /**
     * 代表者情報更新
     *
     * @param name
     *            氏名
     * @param mail
     *            メールアドレス
     * @param phone
     *            電話番号
     * @param zipcode
     *            郵便番号
     * @param address
     *            住所
     */
    @ActionPath("{uid}/update")
    public Navigation update(@Var("uid") String uid,
            @RequestParam("name") String name,
            @RequestParam("mail") String mail,
            @RequestParam("phone") String phone,
            @RequestParam("zipcode") String zipcode,
            @RequestParam("address") String address) {
        putEnteringLog();
        try {
            User tmp = service.get(uid);
            if (roleCheck(tmp)) {
                Validators v = getValidator();
                if (v.validate()) {
                    service.put(uid, name, mail, phone, zipcode, address);
                    return redirect("/user/" + uid);
                } else {
                    return forward("/manager/edit.jsp");
                }
            }
            // エラー画面を表示
            return forward("/error.jsp");
        } finally {
            putExitingLog();
        }
    }

    /**
     * バリデータを取得する。
     *
     * @return バリデータ
     */
    private Validators getValidator() {
        Validators v = new Validators(request);
        v.add("name", v.required());
        v.add("mail", v.required(), v.regexp(VALID_PAT_MAILADDR));
        v.add("phone", v.required(), v.regexp("0\\d+[\\-]\\d+[\\-]\\d+"));
        v.add("zipcode", v.required(), v.regexp("\\d{3}-\\d{4}"));
        v.add("address", v.required());
        return v;
    }

    /**
     * 権限チェックをする。
     *
     * @param user
     *            操作対象ユーザ
     * @return true:OK / false:NG
     */
    private boolean roleCheck(User user) {
        if (user != null
            && user instanceof Manager
            && (checkAdmin() || user.equals(getLoginUser()))) {
            return true;
        } else {
            errors.put("page", ApplicationMessage.get("error.authority"));
            return false;
        }
    }
}