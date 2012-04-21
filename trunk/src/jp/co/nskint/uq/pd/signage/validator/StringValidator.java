/**
 *
 */
package jp.co.nskint.uq.pd.signage.validator;

import java.util.Map;

import org.slim3.controller.validator.AbstractValidator;
import org.slim3.util.ApplicationMessage;

/**
 * 指定した文字列と比較を行うバリデータ
 * @author NAGASAWA Takahiro<tnagasaw@nskint.co.jp>
 */
public class StringValidator extends AbstractValidator {

    private String string;
    private boolean not = false;

    /**
     * コンストラクタ
     * @param string 比較対象の文字列
     */
    public StringValidator(String string) {
        this(string, null, false);
    }

    /**
     * コンストラクタ
     * @param string 比較対象の文字列
     * @param not true:一致しないこと / false:一致すること
     */
    public StringValidator(String string, boolean not) {
        this(string, null, not);
    }

    /**
     * コンストラクタ
     * @param string 比較対象の文字列
     * @param message 出力メッセージ
     */
    public StringValidator(String string, String message) {
        this(string, message, false);
    }

    /**
     * コンストラクタ
     * @param string 比較対象の文字列
     * @param message 出力メッセージ
     * @param not true:一致しないこと / false:一致すること
     */
    public StringValidator(String string, String message, boolean not) {
        super(message);
        this.string = string;
        this.not = not;
    }

    /* (非 Javadoc)
     * @see org.slim3.controller.validator.Validator#validate(java.util.Map, java.lang.String)
     */
    public String validate(Map<String, Object> parameters, String name) {
        Object value = parameters.get(name);
        if ( value == null || "".equals(value) || string == null || "".equals(string) ) {
            return null;
        }
        try {
            String s = (String) value;
            if ( not ) {
                if ( !string.equals(s)  ) {
                    return null;
                }
            } else {
                if ( string.equals(s)  ) {
                    return null;
                }
            }
        } catch (Throwable ignore) {
        }
        if (message != null) {
            return message;
        }
        return ApplicationMessage.get(
            getMessageKey(),
            getLabel(name));
    }

    /* (非 Javadoc)
     * @see org.slim3.controller.validator.AbstractValidator#getMessageKey()
     */
    @Override
    protected String getMessageKey() {
        return "validator.invalid";
    }

}
