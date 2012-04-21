package jp.co.nskint.uq.pd.signage.model;

import org.slim3.datastore.Attribute;
import org.slim3.datastore.InverseModelListRef;
import org.slim3.datastore.Model;

import com.google.appengine.api.datastore.PhoneNumber;
import com.google.appengine.api.datastore.PostalAddress;

/**
 * 代表者情報
 * @author NAGASAWA Takahiro<tnagasaw@nskint.co.jp>
 */
@Model(schemaVersion = 1)
public class Manager extends Editor {

    private static final long serialVersionUID = 1L;

    /** 連絡先 電話番号 */
    private PhoneNumber phone;

    /** 連絡先 住所 */
    private PostalAddress address;

    /** 連絡先 郵便番号 */
    private String zipcode;

    /** 編集者(の参照)リスト */
    @Attribute(persistent = false)
    private InverseModelListRef<Editor, Manager> editorListRef
        = new InverseModelListRef<Editor, Manager>(Editor.class, "managerRef", this);


    /** タイムライン情報(の参照)リスト */
    @Attribute(persistent = false)
    private InverseModelListRef<TimeLine, Manager> timelineRef = new InverseModelListRef<TimeLine, Manager>(TimeLine.class, "managerRef", this);
    /** レイアウト情報(の参照)リスト */
    @Attribute(persistent = false)
    private InverseModelListRef<Layout, Manager> layoutListRef = new InverseModelListRef<Layout, Manager>(Layout.class, "managerRef", this);
    /**
     * 連絡先 電話番号を取得します。
     * @return 連絡先 電話番号
     */
    public PhoneNumber getPhone() {
        return phone;
    }
    /**
     * 連絡先 電話番号を設定します。
     * @param phone 連絡先 電話番号
     */
    public void setPhone(PhoneNumber phone) {
        this.phone = phone;
    }
    /**
     * 連絡先 住所を取得します。
     * @return 連絡先 住所
     */
    public PostalAddress getAddress() {
        return address;
    }
    /**
     * 連絡先 住所を設定します。
     * @param address 連絡先 住所
     */
    public void setAddress(PostalAddress address) {
        this.address = address;
    }
    /**
     * 連絡先 郵便番号を取得します。
     * @return 連絡先 郵便番号
     */
    public String getZipcode() {
        return zipcode;
    }
    /**
     * 連絡先 郵便番号を設定します。
     * @param zipcode 連絡先 郵便番号
     */
    public void setZipcode(String zipcode) {
        this.zipcode = zipcode;
    }
    /**
     * 編集者(の参照)リストを取得します。
     * @return 編集者(の参照)リスト
     */
    public InverseModelListRef<Editor,Manager> getEditorListRef() {
        return editorListRef;
    }
    /**
     * タイムライン情報(の参照)リストを取得します。
     * @return タイムライン情報(の参照)リスト
     */
    public InverseModelListRef<TimeLine,Manager> getTimelineRef() {
        return timelineRef;
    }
    /**
     * レイアウト情報(の参照)リストを取得します。
     * @return レイアウト情報(の参照)リスト
     */
    public InverseModelListRef<Layout,Manager> getLayoutListRef() {
        return layoutListRef;
    }

    /**
     * ユーザ種別を取得する
     * @return ユーザ種別
     */
    @Override
    public String getType() {
        return TYPE_MANAGER;
    }
}
