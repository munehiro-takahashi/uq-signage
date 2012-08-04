/** Copyright 2011 NIHON SYSTEM KAIHATSU LIMITED */
package jp.co.nskint.uq.pd.signage.taglib.editor;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.slim3.controller.ControllerConstants;
import org.slim3.util.ApplicationMessage;
import org.slim3.util.HtmlUtil;
import org.slim3.util.RequestLocator;

import com.google.appengine.api.datastore.Email;
import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;
import com.google.appengine.api.datastore.PhoneNumber;
import com.google.appengine.api.datastore.PostalAddress;


/**
 * エディタ側共通 Taglib Functions
 */
public final class Functions {

    private static String ARRAY_SUFFIX = "Array";

    /**
     * コピーライト表記を出力する。
     * @author NAGASAWA takahiro <tnagasaw@nskint.co.jp>
     */
    public static final String copyright() {
        return "Copyright &copy; 2011 NIHON SYSTEM KAIHATSU LIMITED.";
    }

    /**
     * キーに対応したラベルを出力する。
     * @param key キー
     * @return ラベル
     * @author NAGASAWA takahiro <tnagasaw@nskint.co.jp>
     */
    public static final String label(String key) {
        return org.slim3.jsp.Functions.h(ApplicationMessage.get("label."+key));
    }

    /**
     * エラー表示
     * @param name フィールド名
     * @return 修飾つきメッセージ
     * @author NAGASAWA takahiro <tnagasaw@nskint.co.jp>
     */
    public static final String error(String name) {
        String result = "";
        HttpServletRequest request = request();
        @SuppressWarnings("unchecked")
        Map<String, String> errors =
            (Map<String, String>) request
                .getAttribute(ControllerConstants.ERRORS_KEY);
        if (errors != null && errors.containsKey(name)) {
            String message = errors.get(name);
            result = "<span class='error'>" + org.slim3.jsp.Functions.h(message) + "</span>";
        }
        return result;
    }

    /**
     * Returns the current request.
     *
     * @return the current request
     * @throws IllegalStateException
     *             if the current request does not exists
     */
    private static HttpServletRequest request() throws IllegalStateException {
        HttpServletRequest request = RequestLocator.get();
        if (request == null) {
            throw new IllegalStateException(
                "JSP should be called via FrontController.");
        }
        return request;
    }

    /**
     * Encodes the input object. If the object is a string, it is escaped as
     * HTML. If the object is a key, it is encoded as Base64. Anything else is
     * converted to a string using toString() method.
     *
     * @param input
     *            the input value
     * @return the escaped value
     */
    public static String h(Object input) {
        if (input == null || "".equals(input)) {
            return "";
        }
        if (input.getClass() == String.class) {
            return HtmlUtil.escape(input.toString());
        }
        if (input.getClass() == Key.class) {
            return KeyFactory.keyToString((Key) input);
        }
        if (input instanceof PhoneNumber) {
            return ((PhoneNumber)input).getNumber();
        }
        if (input instanceof PostalAddress) {
            return ((PostalAddress)input).getAddress();
        }
        if (input instanceof Email) {
            return ((Email)input).getEmail();
        }
        return input.toString();
    }

    /**
     * Returns the text tag representation.
     *
     * @param name
     *            the property name
     * @return the text tag representation
     * @throws IllegalArgumentException
     *             if the property name ends with "Array"
     */
    public static String text(String name) throws IllegalArgumentException {
        if (name.endsWith(ARRAY_SUFFIX)) {
            throw new IllegalArgumentException("The property name("
                + name
                + ") must not end with \"Array\".");
        }
        HttpServletRequest request = request();
        return "name=\""
            + name
            + "\" value=\""
            + h(request.getAttribute(name))
            + "\"";
    }

    public static String url(String url) {
        HttpServletRequest request = RequestLocator.get();
        StringBuffer buf = new StringBuffer();
        int port = request.getLocalPort();
        final String scheme = request.getScheme();
        final String serverName = request.getServerName();
        if (port < 0) {
            port = 80; // Work around java.net.URL bug
        }
        buf.append(scheme);
        buf.append("://");
        buf.append(serverName);
        if ((scheme.equals("http") && (port != 80 || port != 0)) || (scheme.equals("https") && (port != 443))) {
            buf.append(':');
            buf.append(port);
        }
        buf.append(request.getContextPath()).append(url);
        return buf.toString();

    }
    private Functions() {

    }
}
