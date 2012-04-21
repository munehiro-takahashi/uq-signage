package jp.co.nskint.uq.pd.signage.page;

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
@Page("/bar_graph")
public class BarGraphPage extends BasePage {

    @Default
    public Navigation index(
            @RequestParam("id") String id,
    		@RequestParam("data") String data,
    		@RequestParam("data_caption") String data_caption,
            @RequestParam("scale_caption") String scale_caption,
            @RequestParam("scale_max") int scale_max,
            @RequestParam("scale_min") int scale_min,
    		@RequestParam("scale_step") int scale_step,
            @RequestParam("orientation") String orientation,
            @RequestParam("bar_width") int bar_width,
            @RequestParam("bar_margin") int bar_margin,
            @RequestParam("width") int width,
            @RequestParam("height") int height
            ) {
        putEnteringLog();
        try {
            request.setAttribute("id", getOrElse(id, "defaultBarGraph"));
            request.setAttribute("data", data);
            request.setAttribute("data_caption", data_caption);
            request.setAttribute("scale_caption", scale_caption);
            request.setAttribute("scale_max", scale_max);
            request.setAttribute("scale_min", scale_min);
            request.setAttribute("scale_step", scale_step);
            request.setAttribute("orientation", getOrElse(orientation, OrientationType.VERTICAL.value()));
            request.setAttribute("bar_width", bar_width);
            request.setAttribute("bar_margin", bar_margin);
            request.setAttribute("width", getOrElse(width, 400));
            request.setAttribute("height", getOrElse(height, 400));
            return forward("/components/bar_graph.jsp");
        } finally {
            putExitingLog();
        }
    }

    @ActionPath("form")
    public Navigation form(
            @RequestParam("index") String index,
            @RequestParam("data") String data,
            @RequestParam("data_caption") String data_caption,
            @RequestParam("scale_caption") String scale_caption,
            @RequestParam("scale_max") int scale_max,
            @RequestParam("scale_min") int scale_min,
            @RequestParam("scale_step") int scale_step,
            @RequestParam("orientation") String orientation,
            @RequestParam("bar_width") int bar_width,
            @RequestParam("bar_margin") int bar_margin,
            @RequestParam("width") int width,
            @RequestParam("height") int height
            ) {

        putEnteringLog();
        try {
            index = getOrElse(index, "defaultIndex");
            request.setAttribute("index", index);
            request.setAttribute("data", data);
            request.setAttribute("dataCaption", data_caption);
            request.setAttribute("scaleCaption", scale_caption);
            request.setAttribute("scaleMax", scale_max);
            request.setAttribute("scaleMin", scale_min);
            request.setAttribute("scaleStep", scale_step);
            request.setAttribute("orientation", getOrElse(orientation, OrientationType.VERTICAL.value()));
            request.setAttribute("barWidth", bar_width);
            request.setAttribute("barMargin", bar_margin);
            request.setAttribute("width", getOrElse(width, 400));
            request.setAttribute("height", getOrElse(height, 400));
            return forward("/components/form/bar_graph.jsp");
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
