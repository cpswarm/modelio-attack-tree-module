package org.modelio.module.attacktreedesigner.conversion.schema;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.XmlValue;
import com.modeliosoft.modelio.javadesigner.annotations.objid;

/**
 * <p>Java class for customTagType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="customTagType">
 * &lt;simpleContent>
 * &lt;extension base="&lt;http://www.w3.org/2001/XMLSchema>string">
 * &lt;attribute name="name" use="required" type="{http://www.w3.org/2001/XMLSchema}string" />
 * &lt;/extension>
 * &lt;/simpleContent>
 * &lt;/complexType>
 * </pre>
 */
@objid ("fdc91ac7-a43a-4831-930d-8373d9444e3a")
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "customTagType", propOrder = {
    "value"
})
public class CustomTagType {
    @objid ("354a582c-63a3-47b9-bb89-a80a2c5a061c")
    @XmlValue
    protected String value;

    @objid ("2437beb7-2972-4136-91b7-ae3a8b5c930e")
    @XmlAttribute(name = "name", required = true)
    protected String name;

    /**
     * Gets the value of the value property.
     * @return
     * possible object is
     * {@link String }
     */
    @objid ("a95fce1e-3634-46e4-8671-f649d10c39d9")
    public String getValue() {
        return this.value;
    }

    /**
     * Sets the value of the value property.
     * 
     * @param value allowed object is
     * {@link String }
     */
    @objid ("a343fb96-7f1e-4584-8807-f6a45edda225")
    public void setValue(String value) {
        this.value = value;
    }

    /**
     * Gets the value of the name property.
     * @return
     * possible object is
     * {@link String }
     */
    @objid ("d1f5006a-13da-4e3f-8387-50015fdcd1b3")
    public String getName() {
        return this.name;
    }

    /**
     * Sets the value of the name property.
     * 
     * @param value allowed object is
     * {@link String }
     */
    @objid ("8a4e9777-d9ca-43d4-b697-4de82cc58bf6")
    public void setName(String value) {
        this.name = value;
    }

}
