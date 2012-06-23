package jp.co.nskint.uq.pd.signage.controller;

import jp.co.nskint.uq.pd.signage.controller.matcher.AdminPageMatcher;
import jp.co.nskint.uq.pd.signage.controller.matcher.BarGraphPageMatcher;
import jp.co.nskint.uq.pd.signage.controller.matcher.CronPageMatcher;
import jp.co.nskint.uq.pd.signage.controller.matcher.EditPageMatcher;
import jp.co.nskint.uq.pd.signage.controller.matcher.EditorPageMatcher;
import jp.co.nskint.uq.pd.signage.controller.matcher.FrontPageMatcher;
import jp.co.nskint.uq.pd.signage.controller.matcher.HtmlPageMatcher;
import jp.co.nskint.uq.pd.signage.controller.matcher.ImagePageMatcher;
import jp.co.nskint.uq.pd.signage.controller.matcher.LayoutPageMatcher;
import jp.co.nskint.uq.pd.signage.controller.matcher.ManagerPageMatcher;
import jp.co.nskint.uq.pd.signage.controller.matcher.MarqueePageMatcher;
import jp.co.nskint.uq.pd.signage.controller.matcher.StreamVideoPageMatcher;
import jp.co.nskint.uq.pd.signage.controller.matcher.TimeLinePageMatcher;
import jp.co.nskint.uq.pd.signage.controller.matcher.UserPageMatcher;
import jp.co.nskint.uq.pd.signage.controller.matcher.VideoPageMatcher;
import jp.co.nskint.uq.pd.signage.controller.matcher.ViewPageMatcher;
import scenic3.UrlsImpl;

public class AppUrls extends UrlsImpl {

    public AppUrls() {
        excludes("/css/*");
        excludes("/_ah/*");
        excludes("/ktrwjr/*");
        add(ViewPageMatcher.get());
        add(ManagerPageMatcher.get());
        add(AdminPageMatcher.get());
        add(EditorPageMatcher.get());
        add(CronPageMatcher.get());
        add(UserPageMatcher.get());
        add(EditPageMatcher.get());
        add(StreamVideoPageMatcher.get());
        add(VideoPageMatcher.get());
        add(MarqueePageMatcher.get());
        add(BarGraphPageMatcher.get());
        add(ImagePageMatcher.get());
        add(HtmlPageMatcher.get());
        add(LayoutPageMatcher.get());
        add(TimeLinePageMatcher.get());
        add(FrontPageMatcher.get());
        // TODO Add your own new PageMatcher

    }
}