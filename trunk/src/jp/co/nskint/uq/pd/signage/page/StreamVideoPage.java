/** Copyright 2011 NIHON SYSTEM KAIHATSU LIMITED */
package jp.co.nskint.uq.pd.signage.page;

import jp.co.nskint.uq.pd.signage.model.xml.StreamVideoType;

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
            @RequestParam("type") String type,
            @RequestParam("width") int width,
            @RequestParam("height") int height
            ){
        putEnteringLog();
        try {
            if(type == null || type.isEmpty()) {
                type = StreamVideoType.UST.value();
            }
            if(width < 1 ) {
                width = 400;
            }
            if(height < 1) {
                height = 300;
            }
            request.setAttribute("index", index);
            request.setAttribute("url", url);
            request.setAttribute("type", type);
            request.setAttribute("width", width);
            request.setAttribute("height", height);
        }
        finally {
            putExitingLog();
        }
        return forward("/components/form/stream_video.jsp");
    }
}
