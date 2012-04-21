package jp.co.nskint.uq.pd.signage.model;

import org.slim3.datastore.Model;

@Model(schemaVersion = 1)
public class Administrator extends User {

    private static final long serialVersionUID = 1L;

    /**
     * ユーザ種別を取得する
     * @return ユーザ種別
     */
    public String getType() {
        return TYPE_ADMINISTRATOR;
    }
}
