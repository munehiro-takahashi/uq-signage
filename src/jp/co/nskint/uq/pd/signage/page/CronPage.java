/** Copyright 2011 NIHON SYSTEM KAIHATSU LIMITED */
package jp.co.nskint.uq.pd.signage.page;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import jp.co.nskint.uq.pd.signage.meta.UserMeta;
import jp.co.nskint.uq.pd.signage.model.User;

import org.slim3.controller.Navigation;
import org.slim3.datastore.Datastore;

import scenic3.annotation.ActionPath;
import scenic3.annotation.Page;

/**
 * 定期実行するアクションを定義する。
 *
 * @author NAGASAWA takahiro <tnagasaw@nskint.co.jp>
 */
@Page("/cron")
public class CronPage extends BasePage {

    /**
     * 登録完了しないアカウントを削除
     */
    @ActionPath("clear")
    public Navigation clear() {
        putEnteringLog();
        try {
            UserMeta e = UserMeta.get();
            Calendar now = GregorianCalendar.getInstance();
            now.add(Calendar.DATE, -1);
            Date before24h = now.getTime();
            // 一時間以上前に生成され
            Iterable<User> users =
                Datastore
                    .query(e)
                    .filter(e.registeredDate.lessThan(before24h))
                    .filter(e.initialKey.isNotNull())
                    .asIterable();

            for (User user : users) {
                if (user.getRegisteredDate().equals(user.getUpdatedDate())) {
                    Datastore.delete(user.getUid());
                }
            }
            return null;
        } finally {
            putExitingLog();
        }
    }

}
