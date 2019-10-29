package org.modelio.module.attacktreedesigner.impl;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import com.modeliosoft.modelio.javadesigner.annotations.objid;
import org.modelio.api.modelio.diagram.IDiagramGraphic;
import org.modelio.api.modelio.diagram.IDiagramHandle;
import org.modelio.api.modelio.diagram.IDiagramService;
import org.modelio.api.module.context.configuration.IModuleAPIConfiguration;
import org.modelio.metamodel.diagrams.ClassDiagram;
import org.modelio.metamodel.uml.infrastructure.ModelElement;
import org.modelio.metamodel.uml.statik.Class;
import org.modelio.metamodel.uml.statik.Package;
import org.modelio.module.attacktreedesigner.api.AttackTreeStereotypes;
import org.modelio.module.attacktreedesigner.api.IAttackTreeDesignerPeerModule;
import org.modelio.module.attacktreedesigner.command.explorer.AttackTreeDiagramCommand;
import org.modelio.module.attacktreedesigner.command.explorer.ExportCommand;
import org.modelio.module.attacktreedesigner.command.explorer.ImportCommand;
import org.modelio.module.attacktreedesigner.command.explorer.ImportPackageCommand;
import org.modelio.module.attacktreedesigner.command.tools.ConnectionTool;
import org.modelio.module.attacktreedesigner.command.tools.CreateAttackTool;
import org.modelio.module.attacktreedesigner.command.tools.CreateTreeReferenceTool;
import org.modelio.module.attacktreedesigner.utils.elementmanager.ElementCreationManager;
import org.modelio.module.attacktreedesigner.utils.elementmanager.representation.DiagramElementBounds;
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
    public ModelElement createNewTree(ModelElement owner) {
        return AttackTreeDiagramCommand.createNewTree(this.module, owner);
    }

    @objid ("f5e84e59-3ad7-48d5-970d-40302f975171")
    @Override
    public Class createANDChild(Class owner, ClassDiagram diagram) {
        Class andChild = null;
        if(owner.isStereotyped(IAttackTreeDesignerPeerModule.MODULE_NAME, AttackTreeStereotypes.ATTACK)
                || owner.isStereotyped(IAttackTreeDesignerPeerModule.MODULE_NAME, AttackTreeStereotypes.OPERATOR)) {
            IDiagramService diagramService = this.module.getModuleContext().getModelioServices().getDiagramService();
            try(  IDiagramHandle diagramHandle = diagramService.getDiagramHandle(diagram);){
                List<IDiagramGraphic> diagramGraphics = diagramHandle.getDiagramGraphics(owner);
                if( !diagramGraphics.isEmpty()) {
                    List<IDiagramGraphic> andElementList = new ArrayList<>();
                    andElementList.add(diagramGraphics.get(0));
                    andChild = ElementCreationManager.createOperatorElement(diagramHandle, andElementList, DiagramElementBounds.ROOT.createRectangleBounds(), AttackTreeStereotypes.AND);        
                }
            }
        }
        return andChild;
    }

    @objid ("0006f3a1-e871-4243-ad0b-02972dc2c5ed")
    @Override
    public Class createORChild(Class owner, ClassDiagram diagram) {
        Class orChild = null;
        if(owner.isStereotyped(IAttackTreeDesignerPeerModule.MODULE_NAME, AttackTreeStereotypes.ATTACK)
                || owner.isStereotyped(IAttackTreeDesignerPeerModule.MODULE_NAME, AttackTreeStereotypes.OPERATOR)) {
            IDiagramService diagramService = this.module.getModuleContext().getModelioServices().getDiagramService();
            try(  IDiagramHandle diagramHandle = diagramService.getDiagramHandle(diagram);){
                List<IDiagramGraphic> diagramGraphics = diagramHandle.getDiagramGraphics(owner);
                if( !diagramGraphics.isEmpty()) {
                    List<IDiagramGraphic> orElementList = new ArrayList<>();
                    orElementList.add(diagramGraphics.get(0));
                    orChild = ElementCreationManager.createOperatorElement(diagramHandle, orElementList, DiagramElementBounds.ROOT.createRectangleBounds(), AttackTreeStereotypes.OR);        
                }
            }
        }
        return orChild;
    }

    @objid ("bec93347-e527-49f2-928f-faa485685a04")
    @Override
    public Class createAttack(ClassDiagram diagram) {
        Class attackElement = null;
        IDiagramService diagramService = this.module.getModuleContext().getModelioServices().getDiagramService();
        try(  IDiagramHandle diagramHandle = diagramService.getDiagramHandle(diagram);){
            attackElement =CreateAttackTool.createAttack(diagramHandle, diagramHandle.getDiagramNode(), DiagramElementBounds.ROOT.createRectangleBounds());
        }
        return attackElement;
    }

    @objid ("143142a3-ac52-4198-af94-1631d08c2490")
    @Override
    public Class createReference(ClassDiagram diagram) {
        Class referenceElement = null;
        IDiagramService diagramService = this.module.getModuleContext().getModelioServices().getDiagramService();
        try(  IDiagramHandle diagramHandle = diagramService.getDiagramHandle(diagram);){
            referenceElement = CreateTreeReferenceTool.createReference(diagramHandle, diagramHandle.getDiagramNode(), DiagramElementBounds.ROOT.createRectangleBounds());
        }
        return referenceElement;
    }

    @objid ("3b5c2457-310e-4948-8b3a-f6cde12c2f0a")
    @Override
    public void createCounterAttack(Class attack, ClassDiagram diagram) {
        // TODO Auto-generated method stub
    }

    @objid ("a8b529c0-fd14-4522-9be3-4469aa3c9925")
    @Override
    public void updateTag(Class attack, String tagType, String TagValue) {
        // TODO Auto-generated method stub
    }

    @objid ("934c8f36-b5a7-4f94-8587-d69d6cd694dd")
    @Override
    public void createConnection(Class source, Class target, ClassDiagram diagram) {
        IDiagramService diagramService = this.module.getModuleContext().getModelioServices().getDiagramService();
        try(  IDiagramHandle diagramHandle = diagramService.getDiagramHandle(diagram);){
        
            List<IDiagramGraphic> sourceGraphics = diagramHandle.getDiagramGraphics(source);
            List<IDiagramGraphic> targetGraphics = diagramHandle.getDiagramGraphics(target);
            if( !sourceGraphics.isEmpty() && !targetGraphics.isEmpty() ) {
                ConnectionTool.createConnection(diagramHandle, sourceGraphics.get(0), targetGraphics.get(0));        
            }
        }
    }

}
