package jp.co.nskint.uq.pd.signage.page;

import jp.co.nskint.uq.pd.signage.model.User;
import jp.co.nskint.uq.pd.signage.service.UserService;

import org.slim3.controller.Navigation;
import org.slim3.util.ApplicationMessage;

import scenic3.annotation.ActionPath;
import scenic3.annotation.Default;
import scenic3.annotation.Page;
import scenic3.annotation.RequestParam;

@Page("/")
public class FrontPage extends BasePage {

    private static final String ATTR_URL = "url";
    private UserService service = new UserService();

    /**
     * ログイン後初期表示
     *
     * @return
     */
    @ActionPath("menu")
    public Navigation menu() {
        putEnteringLog();
        try {
            User loginUser = this.getLoginUser();
            logger.fine("login user : " + loginUser.getUid().getName() + " / " + loginUser.getName() + " / " + loginUser.getType());
            if (checkManager()) {
                // ユーザが代表者の場合
                return redirect("/layout/?mid="+loginUser.getUid().getName());
            } else if (checkEditor()) {
                // ユーザが編集者の場合
                return redirect("/timeline/");
            } else if (checkAdmin()) {
                // ユーザが管理者の場合
                return redirect("/user/");
            } else {
                // その他のユーザの場合
                return forward("/error.jsp");
            }
        } finally {
            putExitingLog();
        }
    }

    /**
     * ログイン画面
     *
     * @param url
     *            元URL
     * @return
     */
    @ActionPath("login")
    public Navigation login(@RequestParam("url") String url) {
        putEnteringLog();
        try {
            request.setAttribute(ATTR_URL, url);
            return forward("/login.jsp");
        } finally {
            putExitingLog();
        }
    }

    /**
     * ログイン実施
     *
     * @param uid
     * @param password
     * @param url
     * @return
     */
    @ActionPath("doLogin")
    public Navigation doLogin(@RequestParam("uid") String uid,
            @RequestParam("password") String password,
            @RequestParam("url") String url) {
        putEnteringLog();
        try {
            User user = service.get(uid);
            if (null != user) {
                logger.finest("doLogin " + password + " / " + user.getPassword());
                if (password.equals(user.getPassword())) {
                    setLoginUser(user);
                    if (null == url || "".equals(url)) {
                        return redirect("/menu");
                    } else {
                        return forward(url);
                    }
                }
            }
            request.setAttribute("url", url);
            errors.put("uid", ApplicationMessage.get("error.login"));
            return forward("/login.jsp");
        } finally {
            putExitingLog();
        }
    }

    /**
     * デフォルト表示画面
     *
     * @return
     */
    @Default
    public Navigation index() {
        putEnteringLog();
        try {
            startSession();
            return forward("/index.jsp");
        } finally {
            putExitingLog();
        }
    }
}