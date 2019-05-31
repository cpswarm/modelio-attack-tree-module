package org.modelio.module.attacktreedesigner.impl;

import com.modeliosoft.modelio.javadesigner.annotations.objid;
import org.modelio.api.module.context.configuration.IModuleAPIConfiguration;
import org.modelio.module.attacktreedesigner.api.IAttackTreeDesignerPeerModule;
import org.modelio.vbasic.version.Version;

@objid ("b9938b7d-5f07-445a-ba5a-251f256f4742")
public class AttackTreeDesignerPeerModule implements IAttackTreeDesignerPeerModule {
    @objid ("a61990f2-9ae9-4083-bf13-cb78b3f1a1e6")
    private AttackTreeDesignerModule module = null;

    @objid ("d425f714-6690-40ee-b43c-faa3a65b10bf")
    private IModuleAPIConfiguration peerConfiguration;

    @objid ("426f03e0-8608-47eb-8750-08b0a479fbb2")
    public AttackTreeDesignerPeerModule(final AttackTreeDesignerModule module, final IModuleAPIConfiguration peerConfiguration) {
        this.module = module;
        this.peerConfiguration = peerConfiguration;
    }

    @objid ("a08ebdae-a18c-4fe7-9dd5-5631f0b98d79")
    @Override
    public IModuleAPIConfiguration getConfiguration() {
        return this.peerConfiguration;
    }

    @objid ("3ae0ebfb-8455-4ce2-8a47-43fc53277e35")
    @Override
    public String getDescription() {
        return this.module.getDescription();
    }

    @objid ("e5b3c43a-d81d-4087-be54-04464ae90d08")
    @Override
    public String getName() {
        return this.module.getName();
    }

    @objid ("0d41c7ad-3ff5-4138-9bf1-4acfeff33940")
    @Override
    public Version getVersion() {
        return this.module.getVersion();
    }

    @objid ("da953104-2737-4a29-ba2d-fe9703fefc68")
    void init() {
    }

}
