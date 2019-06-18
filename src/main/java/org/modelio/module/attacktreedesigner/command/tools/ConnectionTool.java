package org.modelio.module.attacktreedesigner.command.tools;

import com.modeliosoft.modelio.javadesigner.annotations.objid;
import org.modelio.api.modelio.diagram.IDiagramGraphic;
import org.modelio.api.modelio.diagram.IDiagramHandle;
import org.modelio.api.modelio.diagram.IDiagramLink.LinkRouterKind;
import org.modelio.api.modelio.diagram.ILinkPath;
import org.modelio.api.modelio.diagram.tools.DefaultLinkTool;
import org.modelio.api.modelio.model.IModelingSession;
import org.modelio.api.modelio.model.ITransaction;
import org.modelio.metamodel.uml.infrastructure.ModelElement;
import org.modelio.metamodel.uml.infrastructure.ModelTree;
import org.modelio.metamodel.uml.statik.Class;
import org.modelio.module.attacktreedesigner.api.AttackTreeStereotypes;
import org.modelio.module.attacktreedesigner.api.IAttackTreeDesignerPeerModule;
import org.modelio.module.attacktreedesigner.i18n.Messages;
import org.modelio.module.attacktreedesigner.impl.AttackTreeDesignerModule;
import org.modelio.module.attacktreedesigner.utils.ElementCreationManager;
import org.modelio.module.attacktreedesigner.utils.ElementNavigationManager;
import org.modelio.vcore.smkernel.mapi.MObject;

@objid ("2a0856e5-abef-4f86-ae8a-6609fa50c760")
public class ConnectionTool extends DefaultLinkTool {
    @objid ("51dd6bb6-90ef-4d03-8c37-c8c7c2a41a34")
    @Override
    public boolean acceptFirstElement(IDiagramHandle diagramHandle, IDiagramGraphic targetNode) {
        MObject firstElement = targetNode.getElement();
        return (firstElement instanceof Class 
                && (((Class) firstElement).isStereotyped(IAttackTreeDesignerPeerModule.MODULE_NAME, AttackTreeStereotypes.OPERATOR)
                        || (((Class) firstElement).isStereotyped(IAttackTreeDesignerPeerModule.MODULE_NAME, AttackTreeStereotypes.ABSTRACTATTACK)
                                && ((ModelElement) firstElement).getDependsOnDependency().size() == 0))) ;
    }

    @objid ("29f80b38-9ce1-482b-980b-2ed98b5f2730")
    @Override
    public boolean acceptSecondElement(IDiagramHandle diagramHandle, IDiagramGraphic sourceNode, IDiagramGraphic targetNode) {
        MObject firstElement = sourceNode.getElement();
        MObject secondElement = targetNode.getElement();
        return (secondElement instanceof Class 
                && (((Class) secondElement).isStereotyped(IAttackTreeDesignerPeerModule.MODULE_NAME, AttackTreeStereotypes.NODE))
                && !(((Class) secondElement).isStereotyped(IAttackTreeDesignerPeerModule.MODULE_NAME, AttackTreeStereotypes.ROOT))
                && ((ModelElement) secondElement).getImpactedDependency().size()==0  
                && (((Class) firstElement).isStereotyped(IAttackTreeDesignerPeerModule.MODULE_NAME, AttackTreeStereotypes.OPERATOR) 
                        || ((Class) secondElement).isStereotyped(IAttackTreeDesignerPeerModule.MODULE_NAME, AttackTreeStereotypes.OPERATOR))
                && ! ElementNavigationManager.haveSameSecondLevelAncestor(sourceNode, targetNode)) ;
    }

    @objid ("e019777d-2d6c-4712-bec4-d46af7e96675")
    @Override
    public void actionPerformed(IDiagramHandle diagramHandle, IDiagramGraphic originNode, IDiagramGraphic targetNode, LinkRouterKind touterType, ILinkPath path) {
        IModelingSession session = AttackTreeDesignerModule.getInstance().getModuleContext().getModelingSession();
        try( ITransaction transaction = session.createTransaction (Messages.getString ("Info.Session.Create", AttackTreeStereotypes.CONNECTION))){
        
        
            ModelTree firstElement = (ModelTree) originNode.getElement();
            ModelTree secondElement = (ModelTree) targetNode.getElement();
        
            secondElement.setOwner(firstElement);
        
            ModelElement connection = session.getModel().createDependency(firstElement, 
                    secondElement, 
                    IAttackTreeDesignerPeerModule.MODULE_NAME, 
                    AttackTreeStereotypes.CONNECTION); 
            diagramHandle.unmask(connection, 0, 0);
        
        
            diagramHandle.save();
            diagramHandle.close();
        
        
            ElementCreationManager.renameElement(session, secondElement); 
        
            transaction.commit ();
        }
    }

}
