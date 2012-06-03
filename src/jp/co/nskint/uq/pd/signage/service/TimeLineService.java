/** Copyright 2011 NIHON SYSTEM KAIHATSU LIMITED */
package jp.co.nskint.uq.pd.signage.service;

import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Collections;
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
    public TimeLine get(long tlid) {
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
    protected TimeLine get(Transaction tx, long tlid) {
        TimeLine result;
        result =
            Datastore.getOrNull(
                tx,
                TimeLineMeta.get(),
                createKey(tlid));
        return result;
    }

    /**
     * キー生成
     * @param tlid タイムラインID
     * @return キー
     */
    public static Key createKey(long tlid) {
        return Datastore.createKey(TimeLineMeta.get(), tlid);
    }

    /**
     * タイムライン情報の新規IDを取得する
     * @return タイムライン情報の新規ID
     */
    public static Key createNewKey() {
        return Datastore.allocateId(TimeLineMeta.get());
    }

    /**
     * タイムライン情報を取得する。
     *
     * @param tlid
     *            タイムラインID
     * @param timelineXml
     *            タイムラインXMLモデル情報
     * @param manager
     *            代表者情報
     * @return タイムライン情報
     */
    public TimeLine cretateTimeLine(Manager manager, String lid, String name) {

        Key id = TimeLineService.createNewKey();
        long tlid = id.getId();
        // ブロック情報の作成
        final String blockId = "0";
        Block block = cretateBlock(blockId, lid);

        // スケジュール情報の作成
        final String scheduleId = "0";
        Schedule schedule = cretateSchedule(scheduleId, blockId);

        // タイムラインXMLモデルの作成
        TimeLineXml timelineXml = cretateTimeLineXml(tlid, name, block, schedule);

        TimeLine timeline = new TimeLine();
        timeline.setId(id);
        Date now = new Date();
        timeline.setRegisteredDate(now);
        timeline.setUpdatedDate(now);
        timeline.setXmlModel(timelineXml);
        timeline.getManagerRef().setModel(manager);

        return timeline;
    }

    /**
     * ブロック情報を取得する。
     *
     * @param blockId
     *            ブロックID
     * @param layoutId
     *            レイアウトID
     * @return ブロック情報
     */
    public Block cretateBlock(String blockId, String layoutId){
        Block block = new Block();
        block.setId(blockId);
        block.setLayoutId(layoutId);

        return block;
    }

    /**
     * スケジュール情報を取得する。
     *
     * @param scheduleId
     *            スケジュールID
     * @param blockId
     *            ブロックID
     * @return スケジュール情報
     */
    public Schedule cretateSchedule(String scheduleId, String blockId){
        Schedule schedule = new Schedule();
        schedule.setId(scheduleId);
        schedule.setBlockId(blockId);

        return schedule;
    }

    /**
     * タイムラインXMLモデル情報を取得する。
     *
     * @param ""+tlid
     *            タイムラインID
     * @param name
     * @param block
     *            ブロック情報
     * @param schedule
     *            スケジュール情報
     * @return タイムラインXMLモデル情報
     */
    public TimeLineXml cretateTimeLineXml(long tlid, String name, Block block,
            Schedule schedule) {
        TimeLineXml timelineXml = new TimeLineXml();
        timelineXml.setId(""+tlid);
        timelineXml.setName(name);
        timelineXml.getBlock().add(block);
        timelineXml.getSchedule().add(schedule);

        return timelineXml;
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
        ArrayList<Schedule> schedules = new ArrayList<Schedule>(model.getSchedule());
        Collections.reverse(schedules);
        for (Schedule schedule : schedules) {
            boolean before = true;
            if (schedule.getStart() != null) {
                final GregorianCalendar start = schedule.getStart().toGregorianCalendar();
                final int startHour = start.get(GregorianCalendar.HOUR_OF_DAY);
                final int startMin = start.get(GregorianCalendar.MINUTE);

                before = startHour < nowHour || startHour == nowHour && startMin <= nowMin;
            }
            boolean after = true;
            GregorianCalendar end = now;
            end.add(GregorianCalendar.DAY_OF_MONTH, 1);
            end.set(GregorianCalendar.HOUR_OF_DAY, 0);
            end.set(GregorianCalendar.MINUTE, 0);
            end.set(GregorianCalendar.SECOND, 0);
            end.set(GregorianCalendar.MILLISECOND, 0);
            if (schedule.getEnd() != null) {
                end = schedule.getEnd().toGregorianCalendar();
                final int endHour = end.get(GregorianCalendar.HOUR_OF_DAY);
                final int endMin = end.get(GregorianCalendar.MINUTE);

                after = endHour > nowHour || endHour == nowHour && endMin >= nowMin;
            }
            if (before && after) {
                for (Block block : model.getBlock()) {
                    if (block.getId().equals(schedule.getBlockId())) {
                        LayoutService layoutService = new LayoutService();
                        LayoutInfo result = new LayoutInfo();
                        result.layout = layoutService.get(timeline.getManagerRef().getModel(), Long.parseLong(block.getLayoutId()));
                        result.reloadTime = end.getTime();
                        return result;
                    }
                }
            }
        }
        return null;
    }

    /**
     * タイムラインの削除
     * @param tlid
     */
    public void delete(long tlid) {
        Datastore.delete(createKey(tlid));
    }
}
