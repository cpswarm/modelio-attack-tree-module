//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.8-b130911.1802
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a>
// Any modifications to this file will be lost upon recompilation of the source schema.
// Generated on: 2019.10.04 at 05:00:25 PM CEST
//
package org.modelio.module.attacktreedesigner.conversion.schema;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlType;
import com.modeliosoft.modelio.javadesigner.annotations.objid;

/**
 * <p>Java class for operationType.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="operationType">
 * &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string">
 * &lt;enumeration value="AND"/>
 * &lt;enumeration value="OR"/>
 * &lt;/restriction>
 * &lt;/simpleType>
 * </pre>
 */
@objid ("e29a4a82-eadd-42c0-87df-a9c8c169f084")
@XmlType(name = "operationType")
@XmlEnum
public enum OperationType {
    AND,
    OR;

    @objid ("4e9d809b-2a35-4ac7-9cd8-780c61f92b66")
    public String value() {
        return name();
    }

    @objid ("1a1b4f2f-3f36-4793-9005-afc93040d15f")
    public static OperationType fromValue(String v) {
        return valueOf(v);
    }

}
