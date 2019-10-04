//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.8-b130911.1802 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2019.10.01 at 03:57:10 PM CEST 
//


package org.modelio.module.attacktreedesigner.conversion.schema;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlElementDecl;
import javax.xml.bind.annotation.XmlRegistry;
import javax.xml.namespace.QName;


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
 * 
 */
@XmlRegistry
public class AttackTreeXMLObjectFactory {

    private final static QName _AttackTree_QNAME = new QName("", "attackTree");

    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: org.modelio.module.attacktreedesigner.conversion.schema
     * 
     */
    public AttackTreeXMLObjectFactory() {
    }

    /**
     * Create an instance of {@link AttackTreeType }
     * 
     */
    public AttackTreeType createAttackTreeType() {
        return new AttackTreeType();
    }

    /**
     * Create an instance of {@link AttackType }
     * 
     */
    public AttackType createAttackType() {
        return new AttackType();
    }

    /**
     * Create an instance of {@link TagType }
     * 
     */
    public TagType createTagType() {
        return new TagType();
    }

    /**
     * Create an instance of {@link OperatorType }
     * 
     */
    public OperatorType createOperatorType() {
        return new OperatorType();
    }

    /**
     * Create an instance of {@link TreeDiagramType }
     * 
     */
    public TreeDiagramType createTreeDiagramType() {
        return new TreeDiagramType();
    }

    /**
     * Create an instance of {@link CounterMeasureType }
     * 
     */
    public CounterMeasureType createCounterMeasureType() {
        return new CounterMeasureType();
    }

    /**
     * Create an instance of {@link TreeReferenceType }
     * 
     */
    public TreeReferenceType createTreeReferenceType() {
        return new TreeReferenceType();
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link AttackTreeType }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "", name = "attackTree")
    public JAXBElement<AttackTreeType> createAttackTree(AttackTreeType value) {
        return new JAXBElement<AttackTreeType>(_AttackTree_QNAME, AttackTreeType.class, null, value);
    }

}
