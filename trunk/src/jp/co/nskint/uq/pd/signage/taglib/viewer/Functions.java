/** Copyright 2011 NIHON SYSTEM KAIHATSU LIMITED */
package jp.co.nskint.uq.pd.signage.taglib.viewer;



/**
 * ビューア側共通 Taglib Functions
 */
public final class Functions {

    public static String ust_widthToHeight(int width) {
        return "" + ((int)(width * 0.56 + 26 + 0.9));
    }
    private Functions() {

    }
}
