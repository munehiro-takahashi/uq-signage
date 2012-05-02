/** Copyright 2011 NIHON SYSTEM KAIHATSU LIMITED */
package jp.co.nskint.uq.pd.signage.page;

import java.util.Date;

import jp.co.nskint.uq.pd.signage.model.Layout;
import jp.co.nskint.uq.pd.signage.model.Manager;
import jp.co.nskint.uq.pd.signage.model.TimeLine;
import jp.co.nskint.uq.pd.signage.model.xml.Block;
import jp.co.nskint.uq.pd.signage.model.xml.EffectType;
import jp.co.nskint.uq.pd.signage.model.xml.Image;
import jp.co.nskint.uq.pd.signage.model.xml.LayoutXml;
import jp.co.nskint.uq.pd.signage.model.xml.Marquee;
import jp.co.nskint.uq.pd.signage.model.xml.Schedule;
import jp.co.nskint.uq.pd.signage.model.xml.StreamVideo;
import jp.co.nskint.uq.pd.signage.model.xml.StreamVideoType;
import jp.co.nskint.uq.pd.signage.model.xml.TimeLineXml;
import jp.co.nskint.uq.pd.signage.service.LayoutService;
import jp.co.nskint.uq.pd.signage.service.ManagerService;
import jp.co.nskint.uq.pd.signage.service.TimeLineService;
import jp.co.nskint.uq.pd.signage.service.TimeLineService.LayoutInfo;

import org.slim3.controller.Navigation;

import scenic3.annotation.ActionPath;
import scenic3.annotation.Default;
import scenic3.annotation.Page;
import scenic3.annotation.RequestParam;
import scenic3.annotation.Var;

/**
 * 掲示板を表示するページ
 * @author NAGASAWA takahiro <tnagasaw@nskint.co.jp>
 *
 */
@Page("/view")
public class ViewPage extends BasePage {
    private TimeLineService tlService = new TimeLineService();
    /**
     * 表示
     */
    @ActionPath("{tlid}")
    public Navigation view(@Var("tlid") String tlid) {
        putEnteringLog();
        try {
            TimeLine timeline = tlService.get(tlid);
            LayoutInfo layout = tlService.getViewLayout(timeline);
            request.setAttribute("title", timeline.getXmlModel().getName());
            request.setAttribute("layout", layout.layout.getXmlModel());
            request.setAttribute("title", "テスト");
            return forward("/view/view.jsp");
        } finally {
            putExitingLog();
        }
    }

    /**
     * 指定されたIDで最低限のデータを登録する。
     * @param id
     * @return
     */
    @Default
    public Navigation setDummyData(@RequestParam("mid") String mid, @RequestParam("id") String id) {

        Layout layout = new Layout();

        ManagerService managerService = new ManagerService();
        Manager manager = (Manager)managerService.get(mid);

        final LayoutXml xmlModel = new LayoutXml();
        final Marquee marquee = new Marquee();
        marquee.setValue("デジタルサイネージ　デモ");
        marquee.setX(0);
        marquee.setY(0);
        marquee.setWidth(1024);
        marquee.setHeight(60);
        marquee.setFontColor("000");
        marquee.setFontSize(50);
        marquee.setSpeed(100);
        marquee.setId("1");
        xmlModel.getComponents().add(marquee);

        final StreamVideo svideo = new StreamVideo();
        svideo.setX(520);
        svideo.setY(60);
        svideo.setWidth(480);
        svideo.setHeight(386);
        svideo.setType(StreamVideoType.UST);
        svideo.setUrl("http://www.ustream.tv/embed/9755653");
        svideo.setId("2");
        xmlModel.getComponents().add(svideo);

        final Image image = new Image();
        image.setAlbum("Joints2012");
        image.setUser("drmashu");
        image.setEffect(EffectType.FADE);
        image.setX(0);
        image.setY(60);
        image.setWidth(500);
        image.setHeight(500);
        image.setId("3");
        xmlModel.getComponents().add(image);

        long lid = Long.parseLong(id);
        layout.setId(LayoutService.createKey(lid));
        final Date now = new Date();
        layout.setRegisteredDate(now);
        layout.setUpdatedDate(now);
        layout.setXmlModel(xmlModel);

        LayoutService layoutService = new LayoutService();
        layoutService.put(manager, layout);

        final String tlid = id;

        TimeLine timeline = new TimeLine();
        timeline.setId(TimeLineService.createKey(tlid));
        TimeLineXml tlXml = new TimeLineXml();
        tlXml.setName(id);

        Block block = new Block();
        final String blockId = "block_"+id;
        block.setId(blockId);
        block.setLayoutId(""+lid);
        tlXml.getBlock().add(block);

        Schedule schedule = new Schedule();

        schedule.setBlockId(blockId);
        schedule.setId("schedule_"+id);
        tlXml.getSchedule().add(schedule);
        timeline.setXmlModel(tlXml);
        tlService.put(manager, timeline);

        return redirect(tlid);
    }
}
