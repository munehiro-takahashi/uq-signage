package jp.co.nskint.uq.pd.signage.model;

import org.slim3.datastore.Model;
import org.slim3.datastore.ModelRef;

/**
 * 提供者モデル
 * @author NAGASAWA Takahiro<tnagasaw@nskint.co.jp>
 */
@Model(schemaVersion = 1)
public class Editor extends User {

    private static final long serialVersionUID = 1L;

    /** 代表者(の参照) */
    private ModelRef<Manager> managerRef = new ModelRef<Manager>(Manager.class);

    /**
     * 代表者(の参照)を取得します。
     * @return 代表者(の参照)
     */
    public ModelRef<Manager> getManagerRef() {
        return managerRef;
    }

    /**
     * ユーザ種別を取得する
     * @return ユーザ種別
     */
    @Override
    public String getType() {
        return TYPE_EDITOR;
    }
}
