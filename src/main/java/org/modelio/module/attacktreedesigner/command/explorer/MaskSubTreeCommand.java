package org.modelio.module.attacktreedesigner.command.explorer;

import java.util.List;
import com.modeliosoft.modelio.javadesigner.annotations.objid;
import org.modelio.api.modelio.diagram.IDiagramHandle;
import org.modelio.api.modelio.diagram.IDiagramService;
import org.modelio.api.modelio.model.IModelingSession;
import org.modelio.api.modelio.model.ITransaction;
import org.modelio.api.module.IModule;
import org.modelio.api.module.command.DefaultModuleCommandHandler;
import org.modelio.api.module.context.IModuleContext;
import org.modelio.metamodel.diagrams.AbstractDiagram;
import org.modelio.metamodel.uml.infrastructure.Dependency;
import org.modelio.metamodel.uml.infrastructure.Element;
import org.modelio.metamodel.uml.infrastructure.ModelTree;
import org.modelio.metamodel.uml.statik.Class;
import org.modelio.module.attacktreedesigner.api.AttackTreeStereotypes;
import org.modelio.module.attacktreedesigner.api.IAttackTreeDesignerPeerModule;
import org.modelio.module.attacktreedesigner.i18n.Messages;
import org.modelio.module.attacktreedesigner.impl.AttackTreeDesignerModule;
import org.modelio.module.attacktreedesigner.utils.elementmanager.representation.ElementRepresentationManager;
import org.modelio.vcore.smkernel.mapi.MObject;

@objid ("6528192d-24c6-477e-b7c8-3fabb14aacf6")
public class MaskSubTreeCommand extends DefaultModuleCommandHandler {
    @objid ("093217f9-c7fa-456d-8c32-aff51cf34765")
    @Override
    public void actionPerformed(final List<MObject> selectedElements, final IModule module) {
        IModuleContext moduleContext = AttackTreeDesignerModule.getInstance().getModuleContext();
        IModelingSession session = moduleContext.getModelingSession();
        MObject selectedElement = selectedElements.get(0);  
        
        try( ITransaction transaction = session.createTransaction(Messages.getString ("Info.Session.UpdateModel"))){
        
            ((Class) selectedElement).addStereotype(IAttackTreeDesignerPeerModule.MODULE_NAME, AttackTreeStereotypes.SUBTREE);
            
            // Mask children of newly modified attack to subtree
            IDiagramService diagramService = moduleContext.getModelioServices().getDiagramService();
        
            
            List<AbstractDiagram> associatedDiagrams = ((Element) selectedElement).getDiagramElement(AbstractDiagram.class);
            for(AbstractDiagram diagram: associatedDiagrams) {
        
                try(  IDiagramHandle diagramHandle = diagramService.getDiagramHandle(diagram);){
        
                    ElementRepresentationManager.maskChildren(moduleContext, diagramService, selectedElement, diagramHandle);
        
                    diagramHandle.save();
                    diagramHandle.close();
                }
            }
        
            transaction.commit ();
        }
    }

    @objid ("7168cd67-7184-4d1c-8e77-58d362ef1137")
    @Override
    public boolean accept(final List<MObject> selectedElements, final IModule module) {
        if ((selectedElements != null) && (selectedElements.size() == 1)){
            MObject selectedElement = selectedElements.get(0);
            if((selectedElement instanceof Class)
                    && ((Class) selectedElement).isStereotyped(IAttackTreeDesignerPeerModule.MODULE_NAME, AttackTreeStereotypes.ATTACK)
                    && !((Class) selectedElement).isStereotyped(IAttackTreeDesignerPeerModule.MODULE_NAME, AttackTreeStereotypes.SUBTREE)
                    && selectedElement.getStatus().isModifiable()
                    && (((ModelTree) selectedElement)).getDependsOnDependency().size()>0) {
                for(Dependency dependency:(((ModelTree) selectedElement)).getDependsOnDependency()) {
                    if(dependency.isStereotyped(IAttackTreeDesignerPeerModule.MODULE_NAME, AttackTreeStereotypes.CONNECTION)) {
                        return true;
                    }
                }
            }
            return ((selectedElement != null) 
                    && (selectedElement instanceof Class)
                    && ((Class) selectedElement).isStereotyped(IAttackTreeDesignerPeerModule.MODULE_NAME, AttackTreeStereotypes.ATTACK)
                    && !((Class) selectedElement).isStereotyped(IAttackTreeDesignerPeerModule.MODULE_NAME, AttackTreeStereotypes.SUBTREE)
                    && selectedElement.getStatus().isModifiable()
                    && (((ModelTree) selectedElement)).getDependsOnDependency().size()>0);
        
        }
        return false;
    }

}
