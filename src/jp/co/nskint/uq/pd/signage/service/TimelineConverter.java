/** Copyright 2011 NIHON SYSTEM KAIHATSU LIMITED */
package jp.co.nskint.uq.pd.signage.service;

import java.io.StringReader;

import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamConstants;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;

/**
 * タイムラインをXMLからJavaScriptに変換する。
 * @author NAGASAWA takahiro <tnagasaw@nskint.co.jp>
 *
 */
public class TimelineConverter {

    public String convertXmlToScriptOutline(String src) {
        XMLInputFactory factory = XMLInputFactory.newInstance();
        try {
            XMLStreamReader reader = factory.createXMLStreamReader(new StringReader(src));
            StringBuffer buf = new StringBuffer();

            for (; reader.hasNext(); reader.next()) {
                if (XMLStreamConstants.START_ELEMENT == reader.getEventType()) {

                }
            }

            return buf.toString();
        } catch (XMLStreamException e) {
            // TODO 自動生成された catch ブロック
            e.printStackTrace();
        }
        return "";
    }
}
