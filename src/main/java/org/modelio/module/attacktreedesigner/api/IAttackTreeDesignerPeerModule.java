package org.modelio.module.attacktreedesigner.api;

import com.modeliosoft.modelio.javadesigner.annotations.objid;
import org.modelio.api.module.IPeerModule;
import org.modelio.metamodel.uml.infrastructure.ModelElement;
import org.modelio.metamodel.uml.statik.Package;

@objid ("bbd2491a-c005-46f1-80e2-5370d1fd7230")
public interface IAttackTreeDesignerPeerModule extends IPeerModule {
    @objid ("6e21714d-90ef-4676-87cb-baa9888b56f7")
    public static final String MODULE_NAME = "AttackTreeDesigner";

    @objid ("ba84000d-1a23-4ce5-a11b-f7de39ce2bb4")
    void exportModel(ModelElement selectedElement, String targetDirectoryPath);

    @objid ("f7819d05-47d0-4973-a336-bc6e5b583ee7")
    void importModel(Package targetPackage, String sourceElementPath);

}
