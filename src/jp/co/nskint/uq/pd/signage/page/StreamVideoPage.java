/** Copyright 2011 NIHON SYSTEM KAIHATSU LIMITED */
package jp.co.nskint.uq.pd.signage.page;

import org.slim3.controller.Navigation;

import scenic3.annotation.ActionPath;
import scenic3.annotation.Default;
import scenic3.annotation.Page;
import scenic3.annotation.RequestParam;

/**
 * ストリームビデオ 表示ページ
 * @author NAGASAWA takahiro <tnagasaw@nskint.co.jp>
 *
 */
@Page("/stream")
public class StreamVideoPage extends BasePage {

    @Default
    public Navigation index(@RequestParam("url") String url,
            @RequestParam("type") String type,
            @RequestParam("width") int width, @RequestParam("height") int height) {
        putEnteringLog();
        try {
            request.setAttribute("url", url);
            request.setAttribute("type", type);
            request.setAttribute("width", width);
            request.setAttribute("height", height);
            return forward("/components/stream_video.jsp");
        } finally {
            putExitingLog();
        }
    }

    @ActionPath("form")
    public Navigation form(
            @RequestParam("index") int index,
            @RequestParam("url") String url,
            @RequestParam("type") String type
            ){
        putEnteringLog();
        try {
            request.setAttribute("index", index);
            request.setAttribute("url", url);
            request.setAttribute("type", type);
        }
        finally {
            putExitingLog();
        }
        return forward("/components/form/stream_video.jsp");
    }
}
