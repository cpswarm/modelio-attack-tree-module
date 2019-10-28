package org.modelio.module.attacktreedesigner.impl;

import java.io.File;
import com.modeliosoft.modelio.javadesigner.annotations.objid;
import org.modelio.api.module.context.configuration.IModuleAPIConfiguration;
import org.modelio.metamodel.uml.infrastructure.ModelElement;
import org.modelio.metamodel.uml.statik.Class;
import org.modelio.metamodel.uml.statik.Package;
import org.modelio.module.attacktreedesigner.api.IAttackTreeDesignerPeerModule;
import org.modelio.module.attacktreedesigner.command.explorer.AttackTreeDiagramCommand;
import org.modelio.module.attacktreedesigner.command.explorer.ExportCommand;
import org.modelio.module.attacktreedesigner.command.explorer.ImportCommand;
import org.modelio.module.attacktreedesigner.command.explorer.ImportPackageCommand;
import org.modelio.vbasic.version.Version;

@objid ("b9938b7d-5f07-445a-ba5a-251f256f4742")
public class AttackTreeDesignerPeerModule implements IAttackTreeDesignerPeerModule {
    @objid ("73c4d441-2e48-4307-9885-315d7f1a0280")
    private IModuleAPIConfiguration peerConfiguration;

    @objid ("a61990f2-9ae9-4083-bf13-cb78b3f1a1e6")
    private AttackTreeDesignerModule module = null;

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

    @objid ("d62f3325-2366-4de8-9780-f21b2cd1136c")
    @Override
    public void exportModel(ModelElement selectedElement, String targetDirectoryPath) {
        // selectedElement is a Tree
        if(selectedElement instanceof Class) {
            Class modelTree = (Class) selectedElement;
            ExportCommand.exportTree(targetDirectoryPath, modelTree);        
        } 
        // selected element is a package
        else if (selectedElement instanceof Package) {
            Package pkg = (Package) selectedElement;
            ExportCommand.exportPackageTrees(targetDirectoryPath, pkg);
        }
    }

    @objid ("f249428d-8ea2-45d8-8c98-47bbc0fe9b65")
    @Override
    public void importModel(Package targetPackage, String sourceElementPath) {
        File file = new File(sourceElementPath);
        
        if(file.isDirectory()) {
            ImportPackageCommand.importDirectory(this.module, sourceElementPath, targetPackage);
        
        } else if(file.isFile()) {
            ImportCommand.importXMLFile( this.module, sourceElementPath, targetPackage);
        
        }
    }

    @objid ("f20d74e7-3cd2-44a3-915e-698d87a8bcb8")
    @Override
    public void createNewTree(ModelElement owner) {
        AttackTreeDiagramCommand.createNewTree(this.module, owner);
    }

}
