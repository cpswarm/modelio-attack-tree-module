//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.8-b130911.1802
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a>
// Any modifications to this file will be lost upon recompilation of the source schema.
// Generated on: 2019.10.04 at 05:00:25 PM CEST
//
package org.modelio.module.attacktreedesigner.conversion.schema;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.XmlValue;
import com.modeliosoft.modelio.javadesigner.annotations.objid;

/**
 * <p>Java class for tagType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="tagType">
 * &lt;simpleContent>
 * &lt;extension base="&lt;http://www.w3.org/2001/XMLSchema>string">
 * &lt;attribute name="name" use="required" type="{http://www.w3.org/2001/XMLSchema}string" />
 * &lt;/extension>
 * &lt;/simpleContent>
 * &lt;/complexType>
 * </pre>
 */
@objid ("cbc46120-deaa-4f0a-a879-0c328e912db4")
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "tagType", propOrder = {
    "value"
})
public class TagType {
    @objid ("fd38b03c-2fc8-4cbd-a122-c2cf8447548e")
    @XmlValue
    protected String value;

    @objid ("d9bb6cee-8fb5-4c6f-9e0a-1dc974b81130")
    @XmlAttribute(name = "name", required = true)
    protected String name;

    /**
     * Gets the value of the value property.
     * @return
     * possible object is
     * {@link String }
     */
    @objid ("92f90146-270e-49bb-833c-703dc9c0510c")
    public String getValue() {
        return this.value;
    }

    /**
     * Sets the value of the value property.
     * 
     * @param value allowed object is
     * {@link String }
     */
    @objid ("2fbbcd03-3efc-4e1f-98f5-13c9a4faf368")
    public void setValue(String value) {
        this.value = value;
    }

    /**
     * Gets the value of the name property.
     * @return
     * possible object is
     * {@link String }
     */
    @objid ("0bbf2bac-e521-45a6-957c-9f5a1d2f9dcc")
    public String getName() {
        return this.name;
    }

    /**
     * Sets the value of the name property.
     * 
     * @param value allowed object is
     * {@link String }
     */
    @objid ("276e4719-9a35-4575-af8a-58931145db07")
    public void setName(String value) {
        this.name = value;
    }

}
