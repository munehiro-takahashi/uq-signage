package jp.co.nskint.uq.pd.signage.page;

import org.slim3.controller.Navigation;

import scenic3.annotation.ActionPath;
import scenic3.annotation.Default;
import scenic3.annotation.Page;
import scenic3.annotation.RequestParam;

/**
 * 静止画スライドショー
 * @author NAGASAWA Takahiro
 *
 */
@Page("/image")
public class ImagePage extends BasePage {

    @Default
    public Navigation index(
            ) {

        putEnteringLog();
        try {
            return forward("/components/image.jsp");
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
    @ActionPath("coordinator")
    public Navigation coordinator(
            @RequestParam("id") String id,
            @RequestParam("type") String type,
            @RequestParam("effect") String effect,
            @RequestParam("interval") Integer interval,
            @RequestParam("user") String user,
            @RequestParam("album") String album,
            @RequestParam("width") int width,
            @RequestParam("height") int height) {
        putEnteringLog();
        try {
            request.setAttribute("id", id);
            request.setAttribute("effect", effect);
            request.setAttribute("type", type);
            request.setAttribute("interval", interval);
            request.setAttribute("user", user);
            request.setAttribute("album", album);
            request.setAttribute("width", width);
            request.setAttribute("height", height);
            return forward("/components/video_coordinator.jsp");
        } finally {
            putExitingLog();
        }
    }
}
