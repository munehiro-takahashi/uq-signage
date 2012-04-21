package jp.co.nskint.uq.pd.signage.model;

import java.io.Serializable;
import java.util.Date;


import org.slim3.datastore.Attribute;
import org.slim3.datastore.InverseModelListRef;
import org.slim3.datastore.Model;
import org.slim3.datastore.ModelRef;

import com.google.appengine.api.datastore.Key;
/**
 * 掲示板情報
 */
@Model(schemaVersion = 1)
public class Board implements Serializable {
    /**
     *
     */
    private static final long serialVersionUID = 1L;

    /** ID */
    @Attribute(primaryKey = true)
    private Key id;

    /** 代表者(の参照) */
    private ModelRef<Manager> managerRef = new ModelRef<Manager>(Manager.class);
    /** タイムライン情報(の参照) */
    @Attribute(persistent = false)
    private InverseModelListRef<TimeLine, Board> timelineRef = new InverseModelListRef<TimeLine, Board>(TimeLine.class, "boardRef", this);
    /** レイアウト情報(の参照)リスト */
    @Attribute(persistent = false)
    private InverseModelListRef<Layout, Board> layoutListRef = new InverseModelListRef<Layout, Board>(Layout.class, "boardRef", this);

    /** 登録日 */
    private Date registeredDate;
    /** 更新日 */
    private Date updatedDate;

    /** タイトル */
    private String title;

    /**
     * IDを取得します。
     * @return ID
     */
    public Key getId() {
        return id;
    }

    /**
     * IDを設定します。
     * @param id ID
     */
    public void setId(Key id) {
        this.id = id;
    }

    /**
     * 代表者(の参照)を取得します。
     * @return 代表者(の参照)
     */
    public ModelRef<Manager> getManagerRef() {
        return managerRef;
    }

    /**
     * タイムライン情報(の参照)を取得します。
     * @return タイムライン情報(の参照)
     */
    public InverseModelListRef<TimeLine,Board> getTimelineRef() {
        return timelineRef;
    }

    /**
     * レイアウト情報(の参照)リストを取得します。
     * @return layoutListRef レイアウト情報(の参照)リスト
     */
    public InverseModelListRef<Layout, Board> getLayoutListRef() {
        return layoutListRef;
    }

    /**
     * 登録日を取得します。
     * @return 登録日
     */
    public Date getRegisteredDate() {
        return registeredDate;
    }

    /**
     * 登録日を設定します。
     * @param registeredDate 登録日
     */
    public void setRegisteredDate(Date registeredDate) {
        this.registeredDate = registeredDate;
    }

    /**
     * 更新日を取得します。
     * @return 更新日
     */
    public Date getUpdatedDate() {
        return updatedDate;
    }

    /**
     * 更新日を設定します。
     * @param updatedDate 更新日
     */
    public void setUpdatedDate(Date updatedDate) {
        this.updatedDate = updatedDate;
    }

    /**
     * タイトルを取得します。
     * @return タイトル
     */
    public String getTitle() {
        return title;
    }

    /**
     * タイトルを設定します。
     * @param title タイトル
     */
    public void setTitle(String title) {
        this.title = title;
    }

}
