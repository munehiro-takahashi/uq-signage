//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, vJAXB 2.1.10 in JDK 6 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2012.02.23 at 07:53:32 �ߌ� JST 
//


package jp.co.nskint.uq.pd.signage.model.xml;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElements;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for layout element declaration.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;element name="layout">
 *   &lt;complexType>
 *     &lt;complexContent>
 *       &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *         &lt;group ref="{http://uq.nskint.co.jp/uqSignage/layout}components" maxOccurs="unbounded" minOccurs="0"/>
 *       &lt;/restriction>
 *     &lt;/complexContent>
 *   &lt;/complexType>
 * &lt;/element>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
    "components"
})
@XmlRootElement(name = "layout")
public class LayoutXml {

    @XmlElements({
        @XmlElement(name = "video", type = Video.class),
        @XmlElement(name = "marquee", type = Marquee.class),
        @XmlElement(name = "text", type = Text.class),
        @XmlElement(name = "image", type = Image.class),
        @XmlElement(name = "line_graph", type = LineGraph.class),
        @XmlElement(name = "bar_graph", type = BarGraph.class),
        @XmlElement(name = "stream_video", type = StreamVideo.class),
        @XmlElement(name = "html", type = Html.class),
        @XmlElement(name = "table", type = Table.class),
        @XmlElement(name = "audio", type = Audio.class),
        @XmlElement(name = "pie_graph", type = PieGraph.class)
    })
    protected List<ComponentType> components;

    /**
     * Gets the value of the components property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the components property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getComponents().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link Video }
     * {@link Marquee }
     * {@link Text }
     * {@link Image }
     * {@link LineGraph }
     * {@link BarGraph }
     * {@link StreamVideo }
     * {@link Html }
     * {@link Table }
     * {@link Audio }
     * {@link PieGraph }
     * 
     * 
     */
    public List<ComponentType> getComponents() {
        if (components == null) {
            components = new ArrayList<ComponentType>();
        }
        return this.components;
    }

}