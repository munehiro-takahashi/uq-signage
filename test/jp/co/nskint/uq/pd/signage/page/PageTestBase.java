package jp.co.nskint.uq.pd.signage.page;

import java.lang.reflect.Field;
import java.text.MessageFormat;
import java.util.ResourceBundle;

import org.junit.BeforeClass;
import org.slim3.datastore.Datastore;
import org.slim3.util.ApplicationMessage;
import org.slim3.util.LocaleLocator;

import scenic3.ScenicPage;
import scenic3.UrlsImpl;
import scenic3.tester.PageTestCase;

/**
 * Pageクラスのテストのスーパークラス。
 *
 * Slim3はDI機能が無いため、リフレクションを用いてコンテナ回りのオブジェクトを、
 * ページオブジェクトに注入する必要がある。
 * そのため、PageTestBaseにリフレクションでオブジェクトを注入するためのメソッドを用意している。
 * @author NAGASAWA takahiro <tnagasaw@nskint.co.jp>
 *
 */
public abstract class PageTestBase extends PageTestCase {


    private ResourceBundle resource = ResourceBundle.getBundle("application");

    public PageTestBase(Class<? extends UrlsImpl> appUrlClass,
            Class<? extends ScenicPage> pageClass) {
        super(appUrlClass, pageClass);
    }

    /**
     * メッセージバンドルを初期化する。
     */
    @BeforeClass
    public static void beforeClass() {
        ApplicationMessage.setBundle("application", LocaleLocator.get());
        Datastore.setGlobalCipherKey("testcipherkey123");
    }

//    /**
//     * Servletリクエストをページオブジェクトに注入する。
//     *
//     * @param request Servletリクエスト
//     */
//    protected void setRequestToPage(ScenicPage page,
//            final HttpServletRequest request) throws Exception {
//        setValueToField(ScenicPage.class, page, "request", request);
//    }

    /**
     * ページオブジェクトのアクセスできないメンバ変数に値を設定する。
     *
     * @param clazz 対象の(メンバが定義されている)クラス
     * @param target 対象のオブジェクト
     * @param fieldName 変数名
     * @param value 設定する値
     * @throws Exception
     */
    protected void setValueToField(final Class<?> clazz, Object target,
            final String fieldName, final Object value)
            throws Exception {
        Field field = clazz.getDeclaredField(fieldName);
        field.setAccessible(true);
        field.set(target, value);
    }

    /**
     * ページオブジェクトのアクセスできないメンバ変数の値を取得する。
     *
     * @param clazz 対象の(メンバが定義されている)クラス
     * @param target 対象のオブジェクト
     * @param fieldName 変数名
     * @return 変数値
     * @throws Exception
     */
    protected Object getValueFromField(final Class<?> clazz, Object target,
            final String fieldName)
            throws Exception {
        Field field = clazz.getDeclaredField(fieldName);
        field.setAccessible(true);
        return field.get(target);
    }

    /**
     * @return
     */
    protected String getMessage(String key, Object ...objects) {
        return MessageFormat.format(
            resource.getString(key), objects);
    }

//    /**
//     * ページオブジェクトの初期化
//     *
//     * pageオブジェクトが動作するのに必要なオブジェクトを注入する。
//     *
//     * @param page
//     * @throws Exception
//     */
//    protected void initializePage(final ScenicPage page) throws Exception {
//        setValueToField(
//            ScenicPage.class,
//            page,
//            "controller",
//            new ScenicController() {
//                @Override
//                protected Navigation run() throws Exception {
//                    // TODO 自動生成されたメソッド・スタブ
//                    return null;
//                }
//
//                @Override
//                public ScenicPage getPage() {
//                    // TODO 自動生成されたメソッド・スタブ
//                    return page;
//                }
//
//                @Override
//                public String getActionMethodName() {
//                    // TODO 自動生成されたメソッド・スタブ
//                    return null;
//                }
//            });
//        setValueToField(ScenicPage.class, page, "errors", new Errors());
//    }

}