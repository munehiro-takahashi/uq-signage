package jp.co.nskint.uq.pd.signage.page;

import java.util.List;

import jp.co.nskint.uq.pd.signage.model.Layout;
import jp.co.nskint.uq.pd.signage.model.Manager;
import jp.co.nskint.uq.pd.signage.model.TimeLine;
import jp.co.nskint.uq.pd.signage.model.xml.TimeLineXml;
import jp.co.nskint.uq.pd.signage.service.ManagerService;
import jp.co.nskint.uq.pd.signage.service.TimeLineService;

import org.slim3.controller.Navigation;

import scenic3.annotation.ActionPath;
import scenic3.annotation.Default;
import scenic3.annotation.Page;
import scenic3.annotation.RequestParam;

/**
 * タイムライン操作画面
 *
 */
@Page("/timeline")
public class TimeLinePage extends BasePage {

    private TimeLineService tlService = new TimeLineService();
    private ManagerService mService = new ManagerService();

    /**
     * タイムラインの一覧画面を表示するアクション
     *
     * @param mid 代表者ＩＤ
     * @return
     */
    @Default
    public Navigation list(@RequestParam("mid") String mid) {
        final String methodName =
            Thread.currentThread().getStackTrace()[1].getMethodName();
        logger.entering(this.getClass().getName(), methodName);

        try {
            // 代表者IDが与えられていない場合
            if(mid == null || mid.isEmpty()) {
                errors.put("page", "マネージャIDが指定されていません。");
                return forward("/error.jsp");
            }

            Manager manager = (Manager)mService.get(mid);

            if(manager == null) {
                errors.put("page", "指定されたマネージャは存在しません。");
                return forward("/error.jsp");
            }

            List<TimeLine> timelineList = manager.getTimelineRef().getModelList();

            List<Layout> layoutList = manager.getLayoutListRef().getModelList();

            request.setAttribute("timelineList", timelineList);
            request.setAttribute("manager", manager);
            request.setAttribute("layoutList", layoutList);

            // 画面表示
//            request.setAttribute("title", "タイムライン管理");
            return forward("/timeline/list.jsp");
        }
        finally {
            logger.exiting(this.getClass().getName(), methodName);
        }
    }

    /**
     * タイムラインを追加するアクション
     *
     * @param mid
     *            代表者ＩＤ
     * @param lid
     *            デフォルトのレイアウトＩＤ
     * @return
     */
    @ActionPath("add")
    public Navigation add(@RequestParam("mid") String mid,
            @RequestParam("lid") String lid, @RequestParam("name") String name) {
        final String methodName =
                Thread.currentThread().getStackTrace()[1].getMethodName();
        logger.entering(this.getClass().getName(), methodName);

        logger.fine("add " + name);

        Manager manager = (Manager) mService.get(mid);
        if (manager == null) {
            errors.put("page", "指定されたマネージャは存在しません。");
            return forward("/error.jsp");
        }

        // タイムライン情報の作成
        TimeLine timeline = tlService.cretateTimeLine(manager, lid, name);

        // 保存
        tlService.put(manager, timeline);

        // 一覧画面に遷移する。
        return redirectToList(mid);
    }

    /**
     * タイムライン編集画面を表示するアクション
     *
     * @param mid 代表者ＩＤ
     * @param tlid タイムラインＩＤ
     * @param lid レイアウトＩＤ
     * @return
     */
    @ActionPath("edit")
    public Navigation edit(@RequestParam("mid") String mid, @RequestParam("tlid") long tlid) {

        final String methodName =
            Thread.currentThread().getStackTrace()[1].getMethodName();
        logger.entering(this.getClass().getName(), methodName);

        try {
            Manager manager = (Manager)mService.get(mid);
            if(manager == null) {
                errors.put("page", "指定されたマネージャは存在しません。");
                return forward("/error.jsp");
            }

            TimeLine timeline = tlService.get(tlid);
            if(timeline == null) {
                errors.put("page", "指定されたタイムラインは存在しません。");
                return forward("/error.jsp");
            }

            // タイムラインXML情報を取得
            TimeLineXml timelineXml = timeline.getXmlModel();

            List<Layout> layoutList = manager.getLayoutListRef().getModelList();
            this.request.setAttribute("layoutList", layoutList);

//            String defaultLayoutId = timeline.getLayoutId();
//            this.request.setAttribute("defaultLayoutId", defaultLayoutId);

            this.request.setAttribute("mid", mid);
            this.request.setAttribute("tlid", tlid);
            this.request.setAttribute("timeline", timelineXml);
            this.request.setAttribute("title", "タイムライン編集");

            return forward("/timeline/edit.jsp");
        }
        finally {
            logger.exiting(this.getClass().getName(), methodName);
        }
    }

    /**
     * タイムラインを保存するアクション
     *
     * @param mid 代表者ＩＤ
     * @param tlid タイムラインＩＤ
     * @return
     */
    @ActionPath("save")
    public Navigation save(
            @RequestParam("mid") String mid,
            @RequestParam("tlid") long tlid,
            @RequestParam("xml") String xml) {

        final String methodName =
            Thread.currentThread().getStackTrace()[1].getMethodName();
        logger.entering(this.getClass().getName(), methodName);

        try {
            Manager manager = (Manager)mService.get(mid);
            if(manager == null) {
                errors.put("page", "指定されたマネージャは存在しません。");
                return forward("/error.jsp");
            }

            TimeLine timeline = tlService.get(tlid);
            if(timeline == null) {
                errors.put("page", "指定されたタイムラインは存在しません。");
                return forward("/error.jsp");
            }

//            try {
//                JAXBContext context =
//                    JAXBContext.newInstance("jp.co.nskint.uq.pd.signage.model.xml");
                timeline.setXml(xml);
//                TimeLineXml tlXml = (TimeLineXml) context.createUnmarshaller().unmarshal(new StreamSource(new StringReader(xml)));
//                timeline.setXmlModel(tlXml);
//            } catch (JAXBException e) {
//                throw new IllegalStateException(e);
//            }

            tlService.put(manager, timeline);

            return redirectToList(mid);
        }
        finally {
            logger.exiting(this.getClass().getName(), methodName);
        }
    }

    /**
     * タイムラインを削除するアクション
     *
     * @param mid 代表者ＩＤ
     * @param tlid タイムラインＩＤ
     * @return
     */
    @ActionPath("delete")
    public Navigation delete(
            @RequestParam("mid") String mid,
            @RequestParam("tlids") String[] tlids) {

        final String methodName =
            Thread.currentThread().getStackTrace()[1].getMethodName();
        logger.entering(this.getClass().getName(), methodName);

        try {
            Manager manager = (Manager)mService.get(mid);
            if(manager == null) {
                errors.put("page", "指定されたマネージャは存在しません。");
                return forward("/error.jsp");
            }

            for (String tlid : tlids) {
                logger.fine("delete Timeline '" + tlid + "'");
                tlService.delete(Long.valueOf(tlid));
            }

            return redirectToList(mid);
        }
        finally {
            logger.exiting(this.getClass().getName(), methodName);
        }
    }

    /**
     * @param mid
     * @return
     */
    private Navigation redirectToList(String mid) {
        return redirect("/timeline/?mid=" + mid);
    }
}
