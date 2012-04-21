package jp.co.nskint.uq.pd.signage.model;

import java.io.Serializable;
import java.io.StringReader;
import java.util.Date;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;

import jp.co.nskint.uq.pd.signage.model.xml.TimeLineXml;

import org.slim3.datastore.Attribute;
import org.slim3.datastore.CreationDate;
import org.slim3.datastore.Model;
import org.slim3.datastore.ModelRef;
import org.slim3.datastore.ModificationDate;

import com.google.appengine.api.datastore.Key;
/**
 * タイムライン情報
 */
@Model(schemaVersion = 1)
public class TimeLine implements Serializable  {
    /**
     *
     */
    private static final long serialVersionUID = 1L;
    /** ID */
    @Attribute(primaryKey = true)
    private Key id;
    /** タイムライン情報 XML */
    @Attribute(lob = true)
    private String xml;

    /** XMLモデル */
    @Attribute(persistent=false)
    private TimeLineXml xmlModel;

    /** 代表者(の参照) */
    private ModelRef<Manager> managerRef = new ModelRef<Manager>(Manager.class);

    /** 登録日 */
    @Attribute(listener = CreationDate.class)
    private Date registeredDate;
    /** 更新日 */
    @Attribute(listener = ModificationDate.class)
    private Date updatedDate;

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
     * タイムライン情報 XMLを取得します。
     * @return タイムライン情報 XML
     */
    public String getXml() {
        return xml;
    }

    /**
     * タイムライン情報 XMLを設定します。
     * @param xml タイムライン情報 XML
     */
    public void setXml(String xml) {
        this.xml = xml;
    }

    /**
     * XMLモデルを取得します。
     * @return XMLモデル
     */
    public TimeLineXml getXmlModel() {
        if (this.xmlModel == null) {
            try {
                StringReader reader = new StringReader(this.getXml());
                JAXBContext context =
                    JAXBContext.newInstance("jp.co.nskint.uq.pd.signage.model.xml");
                TimeLineXml xmlModel =
                    (TimeLineXml) context
                        .createUnmarshaller()
                        .unmarshal(reader);
                this.xmlModel = xmlModel;
            } catch (JAXBException e) {
                throw new IllegalStateException(e);
            }
        }
        return xmlModel;
    }

    /**
     * XMLモデルを設定します。
     * @param xmlModel XMLモデル
     */
    public void setXmlModel(TimeLineXml xmlModel) {
        this.xmlModel = xmlModel;
    }

    /**
     * 代表者(の参照)を取得します。
     * @return 代表者(の参照)
     */
    public ModelRef<Manager> getManagerRef() {
        return managerRef;
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

}
