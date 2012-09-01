/** Copyright 2011 NIHON SYSTEM KAIHATSU LIMITED */
package jp.co.nskint.uq.pd.signage.page;

import java.util.Date;

import jp.co.nskint.uq.pd.signage.model.Layout;
import jp.co.nskint.uq.pd.signage.model.TimeLine;
import jp.co.nskint.uq.pd.signage.model.User;
import jp.co.nskint.uq.pd.signage.service.LayoutService;
import jp.co.nskint.uq.pd.signage.service.TimeLineService;
import jp.co.nskint.uq.pd.signage.service.TimeLineService.LayoutInfo;

import org.slim3.controller.Navigation;

import scenic3.annotation.ActionPath;
import scenic3.annotation.Page;
import scenic3.annotation.Var;

/**
 * 掲示板を表示するページ
 * @author NAGASAWA takahiro <tnagasaw@nskint.co.jp>
 *
 */
@Page("/view")
public class ViewPage extends BasePage {
    private TimeLineService tlService = new TimeLineService();
    private LayoutService lService = new LayoutService();
    /**
     * 表示
     */
    @ActionPath("{tlid}")
    public Navigation view(@Var("tlid") long tlid) {
        putEnteringLog();
        try {
            TimeLine timeline = tlService.get(tlid);
            LayoutInfo layout = tlService.getViewLayout(timeline);
            request.setAttribute("title", timeline.getXmlModel().getName());
            request.setAttribute("layout", layout.layout.getXmlModel());
            request.setAttribute("reloadTime", layout.reloadTime.getTime() - new Date().getTime());
            request.setAttribute("title", "テスト");
            return forward("/view/view.jsp");
        } finally {
            putExitingLog();
        }
    }

    /**
     * 表示
     */
    @ActionPath("{lid}/preview")
    public Navigation preview(@Var("lid") long lid) {
        putEnteringLog();
        try {
            User user = (User)request.getSession().getAttribute(SESS_KEY_LOGIN_USER);
            Layout layout = lService.get(user, lid);
            request.setAttribute("title", "プレビュー");
            request.setAttribute("layout", layout.getXmlModel());
            request.setAttribute("title", "テスト");
            return forward("/view/view.jsp");
        } finally {
            putExitingLog();
        }
    }

}
