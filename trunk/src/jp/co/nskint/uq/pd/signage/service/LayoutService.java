/** Copyright 2011 NIHON SYSTEM KAIHATSU LIMITED */
package jp.co.nskint.uq.pd.signage.service;

import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;

import jp.co.nskint.uq.pd.signage.meta.LayoutMeta;
import jp.co.nskint.uq.pd.signage.model.Layout;
import jp.co.nskint.uq.pd.signage.model.Manager;
import jp.co.nskint.uq.pd.signage.model.User;
import jp.co.nskint.uq.pd.signage.model.xml.LayoutXml;

import org.slim3.datastore.Datastore;

import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.Transaction;

/**
 * レイアウト情報 サービス
 *
 * @author NAGASAWA takahiro <tnagasaw@nskint.co.jp>
 *
 */
public class LayoutService extends Service {
    /**
     * レイアウト情報の保存
     *
     * @param layout
     *            レイアウト情報
     */
    public void put(Manager manager, Layout layout) {
        // XMLモデルを文字列に変換
        LayoutXml xmlModel = layout.getXmlModel();
        if (xmlModel != null) {
            try {
                StringWriter writer = new StringWriter();
                JAXBContext context =
                    JAXBContext.newInstance("jp.co.nskint.uq.pd.signage.model.xml");
                context.createMarshaller().marshal(xmlModel, writer);
                layout.setXml(writer.toString());
            } catch (JAXBException e) {
                throw new IllegalStateException(e);
            }
        }

        layout.setManagerRef(manager);

        Transaction tx = Datastore.beginTransaction();
        layout.setManagerRef(manager);
        Datastore.put(tx, layout);
        tx.commit();
    }

    /**
     * レイアウト情報の取得
     *
     * @param lid
     *            レイアウトID
     * @return レイアウト情報
     */
    public Layout get(User user, long lid) {
        Transaction tx = Datastore.beginTransaction();
        Layout result = this.get(tx, user, lid);
        tx.commit();
        return result;
    }

    /**
     * レイアウト情報の取得
     *
     * @param tx
     *            トランザクション
     * @param lid
     *            レイアウトID
     * @return レイアウト情報
     */
    protected Layout get(Transaction tx, User user, long lid) {
        Key key = LayoutService.createKey(lid);
        if( key == null) {
            return null;
        }
        Layout result;

        result =
            Datastore.getOrNull(tx, LayoutMeta.get(), key);
        if(User.TYPE_ADMINISTRATOR.equals(user.getType())
                || user.equals(result.getManagerRef().getModel())) {
            return result;
        }
        return null;
    }

    public void delete(User user, List<Long> lids) {
        Transaction tx = Datastore.beginTransaction();
        this.delete(tx, user, lids);
        tx.commit();
    }
    /**
     * レイアウト情報の削除
     * @param tx トランザクション
     * @param manager 代表者情報
     * @param lids 削除対象のレイアウトIDを格納したリスト
     */
    protected void delete(Transaction tx, User user, List<Long> lids) {
        if(lids == null) {
            return ;
        }
        List<Key> keys = new ArrayList<Key>();
        Iterator<Long> ite = lids.iterator();

        while (ite.hasNext()) {
            Long lid = (Long) ite.next();
            Key key = LayoutService.createKey(lid);
            if(key == null) {
                continue ;
            }
            keys.add(key);
        }

        Datastore.delete(tx, keys);
    }


    /**
     * @param lid
     * @return
     */
    public static Key createKey(long lid) {
        if(lid < 1) {
            return null;
        }
        return Datastore.createKey(LayoutMeta.get(), lid);
    }

    /**
     * レイアウト情報の新規IDを取得する
     * @return レイアウト情報の新規ID
     */
    public static Key createNewKey() {
        return Datastore.allocateId(LayoutMeta.get());
    }
}
