package org.modelio.module.attacktreedesigner.api;

import com.modeliosoft.modelio.javadesigner.annotations.objid;
import org.modelio.api.module.IPeerModule;
import org.modelio.metamodel.diagrams.ClassDiagram;
import org.modelio.metamodel.uml.infrastructure.Dependency;
import org.modelio.metamodel.uml.infrastructure.ModelElement;
import org.modelio.metamodel.uml.infrastructure.Note;
import org.modelio.metamodel.uml.statik.Class;
import org.modelio.metamodel.uml.statik.Package;
import org.modelio.vcore.smkernel.mapi.MObject;

@objid ("bbd2491a-c005-46f1-80e2-5370d1fd7230")
public interface IAttackTreeDesignerPeerModule extends IPeerModule {
    @objid ("6e21714d-90ef-4676-87cb-baa9888b56f7")
    public static final String MODULE_NAME = "AttackTreeDesigner";

    @objid ("ba84000d-1a23-4ce5-a11b-f7de39ce2bb4")
    void exportModel(ModelElement selectedElement, String targetDirectoryPath);

    @objid ("f7819d05-47d0-4973-a336-bc6e5b583ee7")
    void importModel(Package targetPackage, String sourceElementPath);

    @objid ("1761e79e-34b9-456f-862f-03588326df15")
    ModelElement createNewTree(ModelElement owner);

    @objid ("36ea40f8-8a64-4cdf-a260-083f9290887c")
    Class createANDChild(Class owner, ClassDiagram diagram);

    @objid ("4103d4f7-2e21-4644-af65-f51c8cb29063")
    Class createORChild(Class owner, ClassDiagram diagram);

    @objid ("2cae7f7f-573e-4439-8700-a976ef8c5d28")
    Class createAttack(ClassDiagram diagram);

    @objid ("ed051488-9b4c-4c1a-9f41-129a7c15435d")
    Class createReference(ClassDiagram diagram);

    @objid ("1b659a6f-7bcd-435d-8594-3ff66bd9a8ec")
    Note createCounterMeasure(Class attack, ClassDiagram diagram);

    @objid ("bf82fcf8-66a5-4e5d-bdc5-9398466918f7")
    void updateTag(Class attack, String tagType, String TagValue);

    @objid ("05bcb59d-86eb-4c2a-8ef7-75838d41cab4")
    Dependency createConnection(Class source, Class target, ClassDiagram diagram);

    @objid ("22f26825-e5a9-408e-bb98-744de3eba2ab")
    String getElementFullPath(MObject element);

    @objid ("d29d7190-885c-4cb1-a3b5-1841fcb9e1d1")
    void updateReference(Class reference, String referencedTreeFullPath);

}
