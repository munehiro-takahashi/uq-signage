package jp.co.nskint.uq.pd.signage.page;

import jp.co.nskint.uq.pd.signage.model.xml.DirectionType;
import jp.co.nskint.uq.pd.signage.model.xml.OrientationType;

import org.slim3.controller.Navigation;

import scenic3.annotation.ActionPath;
import scenic3.annotation.Default;
import scenic3.annotation.Page;
import scenic3.annotation.RequestParam;

/**
 *
 * @author shori.soyama
 *
 */
@Page("/marquee")
public class MarqueePage extends BasePage {

    @Default
    public Navigation index(
            @RequestParam("id") String id,
    		@RequestParam("content") String content,
    		@RequestParam("fontFamily") String fontFamily,
            @RequestParam("fontSize") int fontSize,
            @RequestParam("fontColor") String fontColor,
            @RequestParam("fontStyle") String fontStyle,
    		@RequestParam("bounce") boolean bounce,
            @RequestParam("repeat") int repeat,
            @RequestParam("direction") String direction,
            @RequestParam("writtenDirection") String writtenDirection,
            @RequestParam("speed") int speed,
            @RequestParam("width") int width,
            @RequestParam("height") int height
            ) {

        putEnteringLog();
        try {
            request.setAttribute("id", getOrElse(id, "defaultMarquee"));
            request.setAttribute("content", getOrElse(content, "test"));
            request.setAttribute("fontFamily", getOrElse(fontFamily, "MS PGothic"));
            request.setAttribute("fontSize", getOrElse(fontSize, 10));
            request.setAttribute("fontColor", getOrElse(fontColor, "#000000"));
            request.setAttribute("fontStyle", getOrElse(fontStyle, "normal"));
            request.setAttribute("bounce", bounce);
            request.setAttribute("repeat", repeat);
            request.setAttribute("direction", getOrElse(direction, DirectionType.RIGHT.value()));
            request.setAttribute("writtenDirection", getOrElse(writtenDirection, OrientationType.HORIZONTAL.value()));
            request.setAttribute("speed", getOrElse(speed, 10));
            request.setAttribute("width", getOrElse(width, 200));
            request.setAttribute("height", getOrElse(height, 200));
            return forward("/components/marquee.jsp");
        } finally {
            putExitingLog();
        }
    }

    @ActionPath("form")
    public Navigation form(
            @RequestParam("index") String index,
            @RequestParam("content") String content,
            @RequestParam("fontFamily") String fontFamily,
            @RequestParam("fontSize") int fontSize,
            @RequestParam("fontColor") String fontColor,
            @RequestParam("fontStyle") String fontStyle,
            @RequestParam("bounce") boolean bounce,
            @RequestParam("repeat") int repeat,
            @RequestParam("direction") String direction,
            @RequestParam("writtenDirection") String writtenDirection,
            @RequestParam("speed") int speed,
            @RequestParam("width") int width,
            @RequestParam("height") int height
            ) {

        putEnteringLog();
        try {
            index = getOrElse(index, "defaultIndex");
            request.setAttribute("index", index);
            request.setAttribute("content", getOrElse(content, ""));
            request.setAttribute("fontFamily", getOrElse(fontFamily, "MS PGothic"));
            request.setAttribute("fontSize", getOrElse(fontSize, 10));
            request.setAttribute("fontColor", getOrElse(fontColor, "#000000"));
            request.setAttribute("fontStyle", getOrElse(fontStyle, "normal"));
            request.setAttribute("bounce", true);
            request.setAttribute("repeat", getOrElse(repeat, 0));
            request.setAttribute("direction", getOrElse(direction, DirectionType.RIGHT.value()));
            request.setAttribute("writtenDirection", getOrElse(writtenDirection, OrientationType.HORIZONTAL.value()));
            request.setAttribute("speed", getOrElse(speed, 10));
            request.setAttribute("width", getOrElse(width, 200));
            request.setAttribute("height", getOrElse(height, 200));
            return forward("/components/form/marquee.jsp");
        } finally {
            putExitingLog();
        }
    }

    private int getOrElse(int value, int defaultValue) {
        return value == 0 ? defaultValue : value;
    }

    private String getOrElse(String value, String defaultValue) {
        return value == null || value.isEmpty() ? defaultValue : value;
    }
}
