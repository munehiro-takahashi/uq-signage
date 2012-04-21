/** Copyright 2011 NIHON SYSTEM KAIHATSU LIMITED */
package jp.co.nskint.uq.pd.signage.service;

import java.util.Date;
import java.util.List;

import jp.co.nskint.uq.pd.signage.meta.BoardMeta;
import jp.co.nskint.uq.pd.signage.model.Board;

import org.slim3.datastore.Datastore;
import org.slim3.datastore.ModelQuery;

import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.Transaction;

/**
 * 掲示板情報 サービス
 * @author NAGASAWA takahiro <tnagasaw@nskint.co.jp>
 *
 */
public class BoardService extends Service {

    public List<Board> list() {
        ModelQuery<Board> query = Datastore.query(Board.class);
        return query.asList();
    }
    /**
     * 掲示板情報の取得
     * @param bid 掲示板ID
     * @return 掲示板情報
     */
    public Board get(long bid) {
        Transaction tx = Datastore.beginTransaction();
        Board result = this.get(tx, bid);
        tx.commit();
        return result;
    }

    /**
     * 掲示板情報の取得
     * @param tx トランザクション
     * @param bid 掲示板ID
     * @return 掲示板情報
     */
    public Board get(Transaction tx, long bid) {
        Board result;
        result = Datastore.getOrNull(tx, BoardMeta.get(), BoardService.createKey(bid));
        return result;
    }

    /**
     * @param bid 掲示板ID
     * @param title タイトル
     * @return
     */
    public Board put(long bid, String title) {
        Transaction tx = Datastore.beginTransaction();
        Board result = put(tx, bid, title);

        tx.commit();

        return result;
    }

    /**
     * @param tx
     * @param bid 掲示板ID
     * @param title タイトル
     * @return
     */
    protected Board put(Transaction tx, long bid, String title) {
        Board result = null;
        if (bid == 0) {
            result = new Board();
            // 初期化キーを設定
            result.setUpdatedDate(new Date());
            result.setRegisteredDate(result.getUpdatedDate());
        } else {
            result = get(tx, bid);
            result.setUpdatedDate(new Date());
        }
        Datastore.put(tx, result);
        return result;
    }

    /**
     * キー生成 (自動生成のため非公開)
     * @param bid 掲示板ID
     * @return キー
     */
    private static Key createKey(long bid) {
        return Datastore.createKey(BoardMeta.get(), bid);
    }

}
