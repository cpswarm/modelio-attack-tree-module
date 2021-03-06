//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.8-b130911.1802
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a>
// Any modifications to this file will be lost upon recompilation of the source schema.
// Generated on: 2019.10.04 at 05:00:25 PM CEST
//
package org.modelio.module.attacktreedesigner.conversion.schema;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlElementDecl;
import javax.xml.bind.annotation.XmlRegistry;
import javax.xml.namespace.QName;
import com.modeliosoft.modelio.javadesigner.annotations.objid;

/**
 * This object contains factory methods for each
 * Java content interface and Java element interface
 * generated in the org.modelio.module.attacktreedesigner.conversion.schema package.
 * <p>An ObjectFactory allows you to programatically
 * construct new instances of the Java representation
 * for XML content. The Java representation of XML
 * content can consist of schema derived interfaces
 * and classes representing the binding of schema
 * type definitions, element declarations and model
 * groups.  Factory methods for each of these are
 * provided in this class.
 */
@objid ("59457d53-a9a3-4b12-9900-6def9c69eb8d")
@XmlRegistry
public class AttackTreeXMLObjectFactory {
    @objid ("9b1f05c2-80fb-4d71-9971-e48b549a6ebd")
    private static final QName _AttackTree_QNAME = new QName("", "attackTree");

    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: org.modelio.module.attacktreedesigner.conversion.schema
     */
    @objid ("ac8102ce-135a-4b0e-b973-d8916baa4a70")
    public AttackTreeXMLObjectFactory() {
    }

    /**
     * Create an instance of {@link AttackTreeType }
     */
    @objid ("5ecc05ea-5121-4b3b-8cbf-20056bdaef2e")
    public AttackTreeType createAttackTreeType() {
        return new AttackTreeType();
    }

    /**
     * Create an instance of {@link AttackType }
     */
    @objid ("243af1c5-d9bf-47af-bbe6-bc497d6ec76c")
    public AttackType createAttackType() {
        return new AttackType();
    }

    /**
     * Create an instance of {@link TagType }
     */
    @objid ("13e7294d-b739-42c1-bbe2-a5f64ea01fdc")
    public TagType createTagType() {
        return new TagType();
    }

    /**
     * Create an instance of {@link OperatorType }
     */
    @objid ("8b20e231-e366-40c0-8361-94561ca9c07c")
    public OperatorType createOperatorType() {
        return new OperatorType();
    }

    /**
     * Create an instance of {@link TreeDiagramType }
     */
    @objid ("6f0a5782-408b-4a67-b37f-7a2275d0fa5d")
    public TreeDiagramType createTreeDiagramType() {
        return new TreeDiagramType();
    }

    /**
     * Create an instance of {@link CounterMeasureType }
     */
    @objid ("da26acc8-e42a-499c-b33c-1592a15e5934")
    public CounterMeasureType createCounterMeasureType() {
        return new CounterMeasureType();
    }

    /**
     * Create an instance of {@link TreeReferenceType }
     */
    @objid ("b31b69f1-cc30-4a54-8840-f562e4a11025")
    public TreeReferenceType createTreeReferenceType() {
        return new TreeReferenceType();
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link AttackTreeType }{@code >}}
     */
    @objid ("0b68dab5-8a65-490f-a0e9-380b6f0fed04")
    @XmlElementDecl(namespace = "", name = "attackTree")
    public JAXBElement<AttackTreeType> createAttackTree(AttackTreeType value) {
        return new JAXBElement<>(_AttackTree_QNAME, AttackTreeType.class, null, value);
    }

    /**
     * Create an instance of {@link CustomTagType }
     */
    @objid ("08ec36d0-16a6-4675-b9bb-102096b68df8")
    public CustomTagType createCustomTagType() {
        return new CustomTagType();
    }

}
