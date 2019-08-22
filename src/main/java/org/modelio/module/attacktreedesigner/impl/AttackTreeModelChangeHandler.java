package org.modelio.module.attacktreedesigner.impl;

import java.util.List;
import com.modeliosoft.modelio.javadesigner.annotations.objid;
import org.modelio.api.modelio.model.IModelingSession;
import org.modelio.api.modelio.model.ITransaction;
import org.modelio.api.modelio.model.event.IElementDeletedEvent;
import org.modelio.api.modelio.model.event.IModelChangeEvent;
import org.modelio.api.modelio.model.event.IModelChangeHandler;
import org.modelio.metamodel.uml.infrastructure.Dependency;
import org.modelio.metamodel.uml.infrastructure.ModelElement;
import org.modelio.metamodel.uml.infrastructure.ModelTree;
import org.modelio.metamodel.uml.statik.Attribute;
import org.modelio.metamodel.uml.statik.Class;
import org.modelio.metamodel.uml.statik.Classifier;
import org.modelio.module.attacktreedesigner.api.AttackTreeStereotypes;
import org.modelio.module.attacktreedesigner.api.IAttackTreeDesignerPeerModule;
import org.modelio.module.attacktreedesigner.i18n.Messages;
import org.modelio.module.attacktreedesigner.utils.elementmanager.ElementCreationManager;
import org.modelio.module.attacktreedesigner.utils.elementmanager.ElementRepresentationManager;
import org.modelio.vcore.smkernel.mapi.MObject;

@objid ("11497816-612d-40c7-9892-b8edaac4d924")
public class AttackTreeModelChangeHandler implements IModelChangeHandler {
    @objid ("f32a0b6f-acc3-43ad-8283-47e50175e432")
    @Override
    public void handleModelChange(IModelingSession session, IModelChangeEvent event) {
        List<IElementDeletedEvent> deleteEvents = event.getDeleteEvents();
        if(! deleteEvents.isEmpty()) {
            try( ITransaction transaction = session.createTransaction (Messages.getString ("Info.Session.Create", "Synchronize Attack Tree Model"))){
        
                /*
                 * Go through deleted elements
                 */
                for (IElementDeletedEvent deleteEvent : deleteEvents){
                    MObject deletedElement = deleteEvent.getDeletedElement();
        
                    /*
                     * If the deleted element is a dependency
                     */
                    if (deletedElement instanceof Dependency 
                            && ((Dependency) deletedElement).isStereotyped(IAttackTreeDesignerPeerModule.MODULE_NAME, AttackTreeStereotypes.CONNECTION)){
        
                        Dependency deletedDependency = (Dependency) deletedElement;        
                        MObject rootElement = deletedDependency.getDiagramElement().get(0).getOrigin(); 
                        ModelElement depElement = deletedDependency.getDependsOn();
        
                        if ((depElement != null) 
                                && (!(depElement.isDeleted()))
                                && (rootElement instanceof ModelTree)
                                && (depElement instanceof ModelTree)) {
        
                            ModelTree targetElement = (ModelTree) depElement;
                            targetElement.setOwner((ModelTree) rootElement);
                            ElementCreationManager.renameElement(session, targetElement); 
                        }              
                    } 
                    /*
                     * If the deleted element is a class
                     */
                    else if (deletedElement instanceof Class
                            && ((Class) deletedElement).isStereotyped(IAttackTreeDesignerPeerModule.MODULE_NAME, AttackTreeStereotypes.ROOT)){
                        Class deletedRoot = (Class) deletedElement;
        
                        /*
                         * If the deleted class is a type for the attribute referenced tree
                         */
                        List<Attribute> referenceAttributesList = deletedRoot.getObject();
                        for(Attribute referenceAttribute:referenceAttributesList) {
        
                            if(! referenceAttribute.isDeleted()) {
                                Classifier attributeOwner = referenceAttribute.getOwner();
        
                                referenceAttribute.delete();
        
                                if(! attributeOwner.isDeleted() && attributeOwner.isStereotyped(IAttackTreeDesignerPeerModule.MODULE_NAME, 
                                        AttackTreeStereotypes.TREE_REFERENCE_DECORATION)) {
                                    attributeOwner.removeStereotypes(IAttackTreeDesignerPeerModule.MODULE_NAME, 
                                            AttackTreeStereotypes.TREE_REFERENCE_DECORATION);
        
                                    ElementRepresentationManager.changeStyleToAttack(attributeOwner,
                                            AttackTreeDesignerModule.getInstance().getModuleContext());
                                }
                            }
                        }
                    }
                }
        
                transaction.commit();
            }
        }
    }

}
