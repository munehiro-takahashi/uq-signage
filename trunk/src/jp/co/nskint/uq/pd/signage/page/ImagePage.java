package jp.co.nskint.uq.pd.signage.page;

import jp.co.nskint.uq.pd.signage.model.xml.DirectionType;
import jp.co.nskint.uq.pd.signage.model.xml.OrientationType;

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
}
