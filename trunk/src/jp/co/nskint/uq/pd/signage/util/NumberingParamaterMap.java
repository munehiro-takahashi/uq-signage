/** Copyright 2011 NIHON SYSTEM KAIHATSU LIMITED */
package jp.co.nskint.uq.pd.signage.util;

import java.util.Enumeration;
import java.util.HashSet;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;

import org.slim3.util.RequestMap;
import org.slim3.util.StringUtil;

/**
 * 採番されたリクエストパラメータをマップとして扱うクラス
 *
 */
public class NumberingParamaterMap extends RequestMap {
    /** シリアルバージョン */
    private static final long serialVersionUID = -9169270334087867016L;
    
    enum RegExpMatchGroup {
        NUMBER(1),
        NAME(2);
        
        private int index;
        
        RegExpMatchGroup(int index) {
            this.index = index;
        }
        
        public int getIndex() {
            return this.index;
        }
    };
    /** 番号付きパラメータに一致する正規表現 */
    private static final Pattern pattern = Pattern.compile("^([0-9]+)_([a-zA-Z]+)$");
    
    /** 現在取り出したい番号 */
    protected int number = -1;
    
    /**
     * 引数付きコンストラクタ
     * @param request リクエスト
     */
    public NumberingParamaterMap(HttpServletRequest request) {
        super(request);
    }
    
    /**
     * 番号を設定する。
     * @param number 番号
     */
    public void setNumber( int number) {
        this.number = number;
    }
    
    /**
     * 指定されたキーと内部に保持している番号から番号付きキーを取得する。
     * @param key キー
     * @return 番号付きキー
     */
    protected String getNumberingKey(Object key) {
        return this.number + "_" + StringUtil.toString(key);
    }
    
    /**
     * キーが「(番号)_(キー)」の形式であるかどうかを確認する。
     * @param key
     * @return true: 番号付きのパラメータ形式 / false: その他の形式
     */
    protected boolean isNumberingKey(Object key) {
        // キーが「^([1-9][0-9]*)_([a-zA-Z]+)$」の形式であるかどうかを確認する。
        Matcher matcher = pattern.matcher(StringUtil.toString(key));
        
        // 番号付きパラメータの形式に一致する場合
        if(matcher.find()) {
            return true;
        }
        
        return false;
    }
    
    /**
     * 番号付きパラメータ名からパラメータ名のみを抽出する。
     * @param key 番号付きパラメータ
     * @return パラメータ名
     */
    protected String extractParamaterName(String key) {
        Matcher matcher = pattern.matcher(StringUtil.toString(key));
        
        // 番号付きパラメータの形式に一致する場合
        if(matcher.find()) {
            return matcher.group(RegExpMatchGroup.NAME.getIndex());
        }
        
        return "";
    }
    
    @Override
    public boolean containsKey(Object key) {
        return super.containsKey(this.getNumberingKey(key));
    }

    @Override
    public Set<java.util.Map.Entry<String, Object>> entrySet() {
        Set<java.util.Map.Entry<String, Object>> set = new HashSet<java.util.Map.Entry<String, Object>>();
        for (Enumeration<?> e = request.getAttributeNames(); e.hasMoreElements();) {
            String key = StringUtil.toString(e.nextElement());
            set.add(new EntryImpl(key, request.getAttribute(this.getNumberingKey(key))));
        }
        return set;
    }

    @Override
    public Object get(Object key) {
        return super.get(this.getNumberingKey(key));
    }


    @Override
    public Set<String> keySet() {
        Set<String> set = new HashSet<String>();
        for(Enumeration<?> e = request.getAttributeNames(); e.hasMoreElements();) {
            String key = StringUtil.toString(e.nextElement());
            // 番号付きパラメータ以外の場合
            if(!this.isNumberingKey(key)) {
                // キーの対象にしない
                continue;
            }
            set.add(this.extractParamaterName(key));
        }
        return set;
    }

    @Override
    public Object put(String key, Object value) {
        return super.put(this.getNumberingKey(key), value);
    }

    @Override
    public Object remove(Object key) {
        return super.remove(this.getNumberingKey(key));
    }

    @Override
    public int size() {
        int size = 0;
        for (Enumeration<?> e = request.getAttributeNames(); e.hasMoreElements();) {
            // 番号付きパラメータ以外の場合
            if(!this.isNumberingKey(StringUtil.toString(e.nextElement()))) {
                // カウントの対象にしない
                continue;
            }
            size++;
        }
        return size;
    }
}
