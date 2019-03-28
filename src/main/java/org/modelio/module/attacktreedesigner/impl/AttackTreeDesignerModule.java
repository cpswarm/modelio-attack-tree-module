package org.modelio.module.attacktreedesigner.impl;

import com.modeliosoft.modelio.javadesigner.annotations.objid;
import org.modelio.api.module.AbstractJavaModule;
import org.modelio.api.module.context.IModuleContext;
import org.modelio.api.module.lifecycle.IModuleLifeCycleHandler;
import org.modelio.api.module.parameter.IParameterEditionModel;

@objid ("324ff269-99e6-48dd-9b54-fc43ac763d14")
public class AttackTreeDesignerModule extends AbstractJavaModule {
    @objid ("5e45863d-b25a-46d4-b678-c42c0862d4b4")
    private static final String MODULE_IMAGE = "/res/icon/module.png";

    @objid ("6a80cdf4-904e-41b2-a7f7-1f76a9bd5264")
    private AttackTreeDesignerPeerModule peerModule = null;

    @objid ("372772c9-c3d0-444d-9282-f6284068b470")
    private AttackTreeDesignerLifeCycleHandler lifeCycleHandler = null;

    @objid ("6fdbc714-e8c5-4de7-a0df-68cdf9eb29e2")
    private static AttackTreeDesignerModule instance;

    @objid ("e2ce65d8-bb26-40db-bd26-562fe35822b5")
    public AttackTreeDesignerModule(final IModuleContext moduleContext) {
        super(moduleContext);
        
        AttackTreeDesignerModule.instance = this;
        
        this.lifeCycleHandler  = new AttackTreeDesignerLifeCycleHandler(this);
        this.peerModule = new AttackTreeDesignerPeerModule(this, moduleContext.getPeerConfiguration());
        init();
    }

    @objid ("108e48fa-593a-43c1-b9ee-01d325b557ac")
    @Override
    public AttackTreeDesignerPeerModule getPeerModule() {
        return this.peerModule;
    }

    /**
     * Return the LifeCycleHandler  attached to the current module. This handler is used to manage the module lifecycle by declaring the desired implementation for the start, select... methods.
     */
    @objid ("e9f121f7-82e5-4377-8240-9dc0679261a1")
    @Override
    public IModuleLifeCycleHandler getLifeCycleHandler() {
        return this.lifeCycleHandler;
    }

    /**
     * Method automatically called just after the creation of the module. The module is automatically instanciated at the beginning
     * of the MDA lifecycle and constructor implementation is not accessible to the module developer. The <code>init</code> method
     * allows the developer to execute the desired initialization.
     */
    @objid ("e72f2768-ac10-450a-b7eb-92df79b496ce")
    @Override
    public IParameterEditionModel getParametersEditionModel() {
           return super.getParametersEditionModel();
    }

    @objid ("2be751bb-dc4e-4605-90c3-232252cb8b51")
    @Override
    public String getModuleImagePath() {
        return AttackTreeDesignerModule.MODULE_IMAGE;
    }

    @objid ("5fdc918b-7269-46c3-8bea-2b68218319d2")
    public static AttackTreeDesignerModule getInstance() {
        // Automatically generated method. Please delete this comment before entering specific code.
        return instance;
    }

}
