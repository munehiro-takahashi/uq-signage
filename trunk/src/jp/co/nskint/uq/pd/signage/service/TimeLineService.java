/** Copyright 2011 NIHON SYSTEM KAIHATSU LIMITED */
package jp.co.nskint.uq.pd.signage.service;

import java.io.StringWriter;
import java.util.Date;
import java.util.GregorianCalendar;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;

import jp.co.nskint.uq.pd.signage.meta.TimeLineMeta;
import jp.co.nskint.uq.pd.signage.model.Layout;
import jp.co.nskint.uq.pd.signage.model.Manager;
import jp.co.nskint.uq.pd.signage.model.TimeLine;
import jp.co.nskint.uq.pd.signage.model.xml.Block;
import jp.co.nskint.uq.pd.signage.model.xml.Schedule;
import jp.co.nskint.uq.pd.signage.model.xml.TimeLineXml;

import org.slim3.datastore.Datastore;

import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.Transaction;

/**
 * タイムライン情報 サービス
 *
 * @author NAGASAWA takahiro <tnagasaw@nskint.co.jp>
 *
 */
public class TimeLineService extends Service {
    /**
     * タイムライン情報の保存
     * @param manager 
     *
     * @param timeline
     *            タイムライン情報
     */
    public void put(Manager manager, TimeLine timeline) {
        Transaction tx = Datastore.beginTransaction();
        // XMLモデルを文字列に変換
        TimeLineXml xmlModel = timeline.getXmlModel();
        if (xmlModel != null) {
            try {
                StringWriter writer = new StringWriter();
                JAXBContext context =
                    JAXBContext.newInstance("jp.co.nskint.uq.pd.signage.model.xml");
                context.createMarshaller().marshal(xmlModel, writer);
                timeline.setXml(writer.toString());
            } catch (JAXBException e) {
                throw new IllegalStateException(e);
            }
        }
        timeline.getManagerRef().setModel(manager);
        Datastore.put(tx, timeline);
        tx.commit();
    }

    /**
     * タイムライン情報の取得
     *
     * @param tlid
     *            タイムラインID
     * @return タイムライン情報
     */
    public TimeLine get(String tlid) {
        Transaction tx = Datastore.beginTransaction();
        TimeLine result = this.get(tx, tlid);
        tx.commit();
        return result;
    }

    /**
     * タイムライン情報の取得
     *
     * @param tx
     *            トランザクション
     * @param tlid
     *            タイムラインID
     * @return タイムライン情報
     */
    protected TimeLine get(Transaction tx, String tlid) {
        TimeLine result;
        result =
            Datastore.getOrNull(
                tx,
                TimeLineMeta.get(),
                TimeLineService.createKey(tlid));
        return result;
    }

    /**
     * キー生成
     * @param tlid タイムラインID
     * @return キー
     */
    public static Key createKey(String tlid) {
        return Datastore.createKey(TimeLineMeta.get(), tlid);
    }

    public static class LayoutInfo {
        public Layout layout;
        public Date reloadTime;
    }
    /**
     * 現在表示すべきレイアウトを取得する。
     */
    public LayoutInfo getViewLayout(TimeLine timeline) {
        GregorianCalendar now = new GregorianCalendar();
        final int nowHour = now.get(GregorianCalendar.HOUR_OF_DAY);
        final int nowMin = now.get(GregorianCalendar.MINUTE);

        TimeLineXml model = timeline.getXmlModel();
        for (Schedule schedule : model.getSchedule()) {
            boolean before = true;
            if (schedule.getStart() != null) {
                final GregorianCalendar start = schedule.getStart().toGregorianCalendar();
                final int startHour = start.get(GregorianCalendar.HOUR_OF_DAY);
                final int startMin = start.get(GregorianCalendar.MINUTE);

                before = startHour < nowHour || startHour == nowHour && startMin <= nowMin;
            }
            boolean after = true;
            if (schedule.getEnd() != null) {
                final GregorianCalendar end = schedule.getEnd().toGregorianCalendar();
                final int endHour = end.get(GregorianCalendar.HOUR_OF_DAY);
                final int endMin = end.get(GregorianCalendar.MINUTE);
                before = endHour > nowHour || endHour == nowHour && endMin >= nowMin;
            }
            if (before && after) {
                for (Block block : model.getBlock()) {
                    if (block.getId().equals(schedule.getBlockId())) {
                        LayoutService layoutService = new LayoutService();
                        LayoutInfo result = new LayoutInfo();
                        result.layout = layoutService.get(timeline.getManagerRef().getModel(), Long.parseLong(block.getLayoutId()));
                        result.reloadTime = null;
                        return result;
                    }
                }
                break;
            }

        }
        return null;
    }
}
