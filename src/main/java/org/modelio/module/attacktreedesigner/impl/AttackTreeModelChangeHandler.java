package org.modelio.module.attacktreedesigner.impl;

import java.util.List;
import com.modeliosoft.modelio.javadesigner.annotations.objid;
import org.modelio.api.modelio.model.IModelingSession;
import org.modelio.api.modelio.model.ITransaction;
import org.modelio.api.modelio.model.event.IElementDeletedEvent;
import org.modelio.api.modelio.model.event.IModelChangeEvent;
import org.modelio.api.modelio.model.event.IModelChangeHandler;
import org.modelio.metamodel.uml.infrastructure.Dependency;
import org.modelio.metamodel.uml.infrastructure.ModelTree;
import org.modelio.module.attacktreedesigner.api.AttackTreeStereotypes;
import org.modelio.module.attacktreedesigner.api.IAttackTreeDesignerPeerModule;
import org.modelio.module.attacktreedesigner.i18n.Messages;
import org.modelio.module.attacktreedesigner.utils.elementmanager.ElementCreationManager;
import org.modelio.vcore.smkernel.mapi.MObject;

@objid ("11497816-612d-40c7-9892-b8edaac4d924")
public class AttackTreeModelChangeHandler implements IModelChangeHandler {
    @objid ("f32a0b6f-acc3-43ad-8283-47e50175e432")
    @Override
    public void handleModelChange(IModelingSession session, IModelChangeEvent event) {
        //DeletedEvents
        List<IElementDeletedEvent> deleteEvents = event.getDeleteEvents();
        for (IElementDeletedEvent deleteEvent : deleteEvents){
            MObject deletedElement = deleteEvent.getDeletedElement();
            if (deletedElement instanceof Dependency 
                    && ((Dependency) deletedElement).isStereotyped(IAttackTreeDesignerPeerModule.MODULE_NAME, AttackTreeStereotypes.CONNECTION)){
        
                Dependency deletedDependency = (Dependency) deletedElement;
                try( ITransaction transaction = session.createTransaction (Messages.getString ("Info.Session.Create", "Synchronize Attack Tree Model"))){
        
                    MObject rootElement = deletedDependency.getDiagramElement().get(0).getOrigin(); 
                    ModelTree targetElement = (ModelTree) deletedDependency.getDependsOn();
        
                    targetElement.setOwner((ModelTree) rootElement);
        
                    ElementCreationManager.renameElement(session, targetElement); 
        
                    transaction.commit();
                }
            }
        
        }
    }

}
