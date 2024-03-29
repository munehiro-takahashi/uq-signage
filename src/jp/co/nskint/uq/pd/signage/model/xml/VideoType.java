//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, vJAXB 2.1.10 in JDK 6 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2012.02.02 at 07:43:37 �ߌ� JST 
//


package jp.co.nskint.uq.pd.signage.model.xml;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlEnumValue;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for VideoType.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="VideoType">
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 *     &lt;enumeration value="html5"/>
 *     &lt;enumeration value="youtube"/>
 *   &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 * 
 */
@XmlType(name = "VideoType")
@XmlEnum
public enum VideoType {

    @XmlEnumValue("html5")
    HTML_5("html5"),
    @XmlEnumValue("youtube")
    YOUTUBE("youtube");
    private final String value;

    VideoType(String v) {
        value = v;
    }

    public String value() {
        return value;
    }

    public static VideoType fromValue(String v) {
        for (VideoType c: VideoType.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }

}
