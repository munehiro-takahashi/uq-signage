/** Copyright 2012 NIHON SYSTEM KAIHATSU LIMITED */
package jp.co.nskint.uq.pd.signage.page;

import org.slim3.controller.Navigation;

import scenic3.ScenicPage;
import scenic3.annotation.Default;
import scenic3.annotation.Page;

/**
 * 掲示板編集画面
 * @author NAGASAWA takahiro <tnagasaw@nskint.co.jp>
 */
@Page("/edit")
public class EditPage extends ScenicPage {

    @Default
    public Navigation index() {
            request.setAttribute("id", "1");
            return forward("/components/dummy_editor.jsp");
    }
}
