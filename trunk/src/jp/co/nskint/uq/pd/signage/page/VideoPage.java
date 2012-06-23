/** Copyright 2011 NIHON SYSTEM KAIHATSU LIMITED */
package jp.co.nskint.uq.pd.signage.page;

import org.slim3.controller.Navigation;

import scenic3.annotation.ActionPath;
import scenic3.annotation.Default;
import scenic3.annotation.Page;
import scenic3.annotation.RequestParam;

/**
 * 動画 表示ページ
 *
 * @author NAGASAWA takahiro <tnagasaw@nskint.co.jp>
 *
 */
@Page("/video")
public class VideoPage extends BasePage {

    /**
     * 表示
     *
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
    @Default
    public Navigation index(@RequestParam("url") String url,
            @RequestParam("type") String type,
            @RequestParam("width") int width,
            @RequestParam("height") int height,
            @RequestParam("loop") boolean loop) {
        putEnteringLog();
        try {
            request.setAttribute("url", url);
            request.setAttribute("type", type);
            request.setAttribute("loop", loop);
            request.setAttribute("width", width);
            request.setAttribute("height", height);
            return forward("/components/video.jsp");
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
            @RequestParam("url") String url,
            @RequestParam("type") String type,
            @RequestParam("width") int width,
            @RequestParam("height") int height,
            @RequestParam("loop") boolean loop) {
        putEnteringLog();
        try {
            request.setAttribute("id", id);
            request.setAttribute("url", url);
            request.setAttribute("type", type);
            request.setAttribute("loop", loop);
            request.setAttribute("width", width);
            request.setAttribute("height", height);
            return forward("/components/video_coordinator.jsp");
        } finally {
            putExitingLog();
        }
    }
    /**
     * 編集
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
    @ActionPath("edit")
    public Navigation edit(
            @RequestParam("id") String id,
            @RequestParam("url") String url,
            @RequestParam("type") String type,
            @RequestParam("width") int width,
            @RequestParam("height") int height,
            @RequestParam("loop") boolean loop) {
        putEnteringLog();
        try {
            request.setAttribute("id", id);
            request.setAttribute("url", url);
            request.setAttribute("type", type);
            request.setAttribute("loop", loop);
            request.setAttribute("width", width);
            request.setAttribute("height", height);
            return forward("/components/form/video.jsp");
        } finally {
            putExitingLog();
        }
    }
    }
