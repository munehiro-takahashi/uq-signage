package jp.co.nskint.uq.pd.signage.page;

import org.slim3.controller.Navigation;

import scenic3.annotation.ActionPath;
import scenic3.annotation.Default;
import scenic3.annotation.Page;
import scenic3.annotation.RequestParam;

/**
 * HTML
 * @author NAGASAWA Takahiro
 *
 */
@Page("/html")
public class HtmlPage extends BasePage {

    @Default
    public Navigation index(
            ) {

        putEnteringLog();
        try {
            return forward("/components/html.jsp");
        } finally {
            putExitingLog();
        }
    }
    /**
     * コーディネーターの表示
     *
     * @param id ID
     * @param url
     *            対象のURL
     * @param type
     *            タイプ:youtube/html5
     * @param width
     *            幅
     * @param height
     *            高さ
     * @param loop
     *            ループするかどうか true:ループする
     * @return ナビゲーション情報
     */
    @ActionPath("form")
    public Navigation coordinator(
            @RequestParam("id") String id,
            @RequestParam("value") String value,
            @RequestParam("width") int width,
            @RequestParam("height") int height) {
        putEnteringLog();
        try {
            request.setAttribute("id", id);
            request.setAttribute("value", value);
            request.setAttribute("width", width);
            request.setAttribute("height", height);
            return forward("/components/form/html.jsp");
        } finally {
            putExitingLog();
        }
    }
}
