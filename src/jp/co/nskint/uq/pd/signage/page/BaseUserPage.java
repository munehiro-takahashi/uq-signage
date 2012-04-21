/** Copyright 2011 NIHON SYSTEM KAIHATSU LIMITED */
package jp.co.nskint.uq.pd.signage.page;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.logging.Level;

import jp.co.nskint.uq.pd.signage.model.User;
import jp.co.nskint.uq.pd.signage.service.UserService;

import org.apache.commons.lang3.RandomStringUtils;
import org.slim3.util.ApplicationMessage;

import com.google.appengine.api.mail.MailService;
import com.google.appengine.api.mail.MailService.Message;
import com.google.appengine.api.mail.MailServiceFactory;

/**
 * ユーザ情報を扱うページに共通する処理を集めたクラス
 *
 * @author NAGASAWA takahiro <tnagasaw@nskint.co.jp>
 */
public abstract class BaseUserPage<T extends UserService> extends BasePage {

    /**
     * サービスを取得する。
     * @return サービス
     */
    public abstract T getService();

    /**
     * ユーザが登録可能かをチェックする。
     * <ul>
     * <li>既に登録済みのユーザか</li>
     * <li>ユーザIDに使用できないキーワードでないか</li>
     * </ul>
     *
     * @param uid
     *            対象のUID
     * @return true:登録可 / false:登録不可
     */
    protected boolean validateInsertUser(String uid) {
        boolean result = true;
        if (!getService().checkDuplicationUid(uid)) {
            // すでにIDが使われている場合
            errors.put(
                "uid",
                ApplicationMessage.get(
                    "error.id",
                    ApplicationMessage.get("label.uid"),
                    uid));
            result = false;
        }
        return result;
    }

    /**
     * ユーザ登録確認メール送信
     *
     * @param user
     * @throws IOException
     */
    protected boolean sendConfirmationMail(User user) {
        final String methodName =
            Thread.currentThread().getStackTrace()[1].getMethodName();
        // 初期化キーを設定
        user.setInitialKey(RandomStringUtils.randomAlphanumeric(16));

        // RequestURLを分解して、
        String url = "";
        try {
            URI reqUrl = new URI(this.request.getRequestURL().toString());
            url =
                reqUrl.getScheme()
                    + "://"
                    + reqUrl.getHost()
                    + ((reqUrl.getPort() != 80)? ":" + reqUrl.getPort() : "")  // 80以外のポートが使われていたら、ポート番号をつける
                    + "/"
                    + request.getContextPath()
                    + "user/"
                    + user.getUid().getName()
                    + "/initial/"
                    + user.getInitialKey();
        } catch (URISyntaxException e) {
        }

        String content =
            ApplicationMessage.get(
                "mail.confirmation.content",
                user.getName(),
                url);
        String from = ApplicationMessage.get("mail.noreply");
        String to = user.getMail().getEmail();
        Message message =
            new Message(
                from,
                to,
                ApplicationMessage.get("mail.confirmation.subject"),
                content);

        logger.logp(
            Level.FINEST,
            this.getClass().getName(),
            methodName,
            "from :" + from);
        logger.logp(Level.FINEST, this.getClass().getName(), methodName, "to :"
            + to);
        logger.logp(
            Level.FINEST,
            this.getClass().getName(),
            methodName,
            "content :" + content);

        try {
            getMailService().send(message);
            return true;
        } catch (IOException e) {
            errors.put(
                "page",
                ApplicationMessage.get(
                    "error.mail",
                    user.getUid().getName(),
                    user.getMail().getEmail()));
            return false;
        }
    }

    /**
     * @return
     */
    protected MailService getMailService() {
        return MailServiceFactory.getMailService();
    }

}
