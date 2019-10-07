package org.modelio.module.attacktreedesigner.impl;

import java.util.List;
import java.util.Set;
import com.modeliosoft.modelio.javadesigner.annotations.objid;
import org.modelio.api.modelio.model.IModelingSession;
import org.modelio.api.modelio.model.ITransaction;
import org.modelio.api.modelio.model.event.IElementDeletedEvent;
import org.modelio.api.modelio.model.event.IElementMovedEvent;
import org.modelio.api.modelio.model.event.IModelChangeEvent;
import org.modelio.api.modelio.model.event.IModelChangeHandler;
import org.modelio.metamodel.uml.infrastructure.Dependency;
import org.modelio.metamodel.uml.infrastructure.ModelElement;
import org.modelio.metamodel.uml.infrastructure.ModelTree;
import org.modelio.metamodel.uml.infrastructure.Note;
import org.modelio.metamodel.uml.infrastructure.TagParameter;
import org.modelio.metamodel.uml.infrastructure.TaggedValue;
import org.modelio.metamodel.uml.statik.Attribute;
import org.modelio.metamodel.uml.statik.Class;
import org.modelio.module.attacktreedesigner.api.AttackTreeNoteTypes;
import org.modelio.module.attacktreedesigner.api.AttackTreeStereotypes;
import org.modelio.module.attacktreedesigner.api.AttackTreeTagTypes;
import org.modelio.module.attacktreedesigner.api.IAttackTreeDesignerPeerModule;
import org.modelio.module.attacktreedesigner.i18n.Messages;
import org.modelio.module.attacktreedesigner.utils.elementmanager.CounterMeasureManager;
import org.modelio.module.attacktreedesigner.utils.elementmanager.ElementCreationManager;
import org.modelio.module.attacktreedesigner.utils.elementmanager.representation.ElementRepresentationManager;
import org.modelio.module.attacktreedesigner.utils.elementmanager.tags.TagsManager;
import org.modelio.vcore.smkernel.mapi.MObject;

@objid ("11497816-612d-40c7-9892-b8edaac4d924")
public class AttackTreeModelChangeHandler implements IModelChangeHandler {
    @objid ("f32a0b6f-acc3-43ad-8283-47e50175e432")
    @Override
    public void handleModelChange(IModelingSession session, IModelChangeEvent event) {
        /*
         * handle Created elements
         */
        Set<MObject> createdEvents = event.getCreationEvents();
        for ( MObject createdEvent : createdEvents){
            handleCreatedElement(session, createdEvent);
        }
        
        
        /*
         * Handle Updated elements
         */
        Set<MObject> updatedEvents = event.getUpdateEvents();
        for ( MObject updatedEvent : updatedEvents){
            handleUpdatedElement(session, updatedEvent);
        }
        
        
        /*
         * Handle Deleted Elements
         */
        List<IElementDeletedEvent> deleteEvents = event.getDeleteEvents();
        for (IElementDeletedEvent deleteEvent : deleteEvents){
            handleDeletedElement(session, deleteEvent);
        }
        
        
        
        /*
         * Handle Moved elements
         */
        List<IElementMovedEvent> moveEvents = event.getMoveEvents();
        for(IElementMovedEvent moveEvent:moveEvents) {
            // TODO 
            //(for changing the origin of a dependency) (update model, and tags)
        }
    }

    @objid ("0f37b455-4380-480d-9909-5c630a100844")
    private static void handleDeletedElement(IModelingSession session, IElementDeletedEvent deleteEvent) {
        try( ITransaction transaction = session.createTransaction (Messages.getString ("Info.Session.UpdateModel"))){
        
            MObject deletedElement = deleteEvent.getDeletedElement();
        
            if (deletedElement instanceof Dependency ) {
                Dependency deletedDependency = (Dependency) deletedElement;        
                handleDeletedDependency(session, deletedDependency); 
            }
        
            else if (deletedElement instanceof Class) {
                Class deletedClass = (Class) deletedElement;
                handleDeletedClass(session, deletedClass);
            }
        
            else if (deletedElement instanceof Note) {
                Note deletedNote = (Note) deletedElement;
                handleDeletedNote(deletedNote);
            }
        
            transaction.commit();
        }
    }

    @objid ("989ead6e-07ed-400e-86c7-36a6cfd5bb43")
    private static void handleUpdatedElement(IModelingSession session, MObject updatedEvent) {
        if( updatedEvent instanceof Class) {
            Class updatedClass = (Class)updatedEvent;
            handleUpdatedClass(session, updatedClass);
        } else if (updatedEvent instanceof TagParameter) {            
            TagParameter updatedTagParameter = (TagParameter) updatedEvent;
            handleUpdatedTagParameter(session, updatedTagParameter);
        } else if (updatedEvent instanceof Dependency) {
            // TODO
        }
    }

    @objid ("e8d3ee20-3885-49ca-8bb5-a318b916ceb2")
    private static void handleUpdatedTagParameter(IModelingSession session, TagParameter updatedTagParameter) {
        TaggedValue updatedTag = updatedTagParameter.getAnnoted();
        String tagName = updatedTag.getDefinition().getName();
        
        boolean severityTagIsUpdated = tagName.equals(AttackTreeTagTypes.SEVERITY);
        boolean probabilityTagIsUpdated = tagName.equals(AttackTreeTagTypes.PROBABILITY);
        boolean counteredTagIsUpdated = tagName.equals(AttackTreeTagTypes.COUNTERED_ATTACK);
        
        
        Class attack = (Class) updatedTag.getAnnoted();
        
        if(attack.isStereotyped(IAttackTreeDesignerPeerModule.MODULE_NAME, AttackTreeStereotypes.ROOT)) {
            // if the attack is a Root
            for(Attribute attribute : attack.getObject()) {
                if(attribute.isStereotyped(IAttackTreeDesignerPeerModule.MODULE_NAME, AttackTreeStereotypes.TREE_REFERENCE_ATTRIBUTE)) {
                    Class reference = (Class) attribute.getOwner();
                    if(!reference.isDeleted()) {
                        
                        /*
                         * Update reference Color to Countered
                         */
                        ElementRepresentationManager.setClassColor(reference, ElementRepresentationManager.COUNTERED_ATTACK_COLOR);                        
            
                        // if reference has a parent
                        for(Dependency parentDependency: reference.getImpactedDependency()) {
                            if(parentDependency.isStereotyped(IAttackTreeDesignerPeerModule.MODULE_NAME, AttackTreeStereotypes.CONNECTION)) {
                                Class firstAttackAscendant = getFirstNonDeletedAttackAscendant(parentDependency);
                                if(firstAttackAscendant != null) {
                                    try( ITransaction transaction = session.createTransaction (Messages.getString ("Info.Session.UpdateModel"))){
                                        updateAndPropagateAttackTags(firstAttackAscendant, severityTagIsUpdated, probabilityTagIsUpdated, counteredTagIsUpdated);
                                        transaction.commit();
                                    }
                                }
                            }
                        }
                    }
                }
            }
        
        
        } else {
            // if the attack has a parent
            for(Dependency parentDependency : attack.getImpactedDependency()) {
                if(parentDependency.isStereotyped(IAttackTreeDesignerPeerModule.MODULE_NAME, AttackTreeStereotypes.CONNECTION)) {
                    Class firstAttackAscendant = getFirstNonDeletedAttackAscendant(parentDependency);
                    if(firstAttackAscendant != null) {
                        try( ITransaction transaction = session.createTransaction (Messages.getString ("Info.Session.UpdateModel"))){
                            updateAndPropagateAttackTags(firstAttackAscendant, severityTagIsUpdated, probabilityTagIsUpdated, counteredTagIsUpdated);
                            transaction.commit();
                        }
                    }
                }
            }
        }
    }

    @objid ("6060df4c-6280-4398-a2eb-7711ac2ffb39")
    private static void handleUpdatedClass(IModelingSession session, Class updatedClass) {
        // if change in the type of the Operator (AND to OR or vice versa)
        if (updatedClass.isStereotyped(IAttackTreeDesignerPeerModule.MODULE_NAME, AttackTreeStereotypes.OPERATOR)) {
            Class firstAttackAscendant = getFirstNonDeletedAttackAscendant(updatedClass);
            if(firstAttackAscendant != null) {
                try( ITransaction transaction = session.createTransaction (Messages.getString ("Info.Session.UpdateModel"))){
                    updateAndPropagateAttackTags(firstAttackAscendant, true, true, true);
                    transaction.commit();
                }
            }
        }
    }

    @objid ("66b818dd-a80f-4d83-a038-feca0ad21ec6")
    private static void handleCreatedElement(IModelingSession session, MObject createdEvent) {
        // If Created Dependency
        if (createdEvent instanceof Dependency) {
            Dependency createdDependency = (Dependency) createdEvent;
            if(createdDependency.isStereotyped(IAttackTreeDesignerPeerModule.MODULE_NAME, AttackTreeStereotypes.CONNECTION)) {
                Class firstAttackAscendant = getFirstNonDeletedAttackAscendant(createdDependency);
                if(firstAttackAscendant != null) { 
        
                    try( ITransaction transaction = session.createTransaction (Messages.getString ("Info.Session.UpdateModel"))){
                        updateAndPropagateAttackTags(firstAttackAscendant, true, true, true);
                        transaction.commit();
                    }
                }
            }
        }
    }

    @objid ("a8aa3c42-1c0c-43c9-96bb-e5325e7cca19")
    public static void updateAndPropagateAttackTags(Class attack, boolean updateSeverity, boolean updateProbability, boolean updateCountered) {
        if(updateSeverity) {
            
            int minSeverityIndex = TagsManager.getMinSeverityIndex(attack);
            String attackSeverity = TagsManager.getElementTagParameter(attack, AttackTreeStereotypes.ATTACK, AttackTreeTagTypes.SEVERITY);
            int attackSeverityIndex = minSeverityIndex;
            
            for(int i=0; i < TagsManager.SEVERITY_VALUES.length; i++) {
                if(attackSeverity.equals(TagsManager.SEVERITY_VALUES[i])) {
                    attackSeverityIndex = i;
                    break;
                }
            }
            
            if(attackSeverityIndex < minSeverityIndex) {
                TagsManager.setElementTagValue(attack, AttackTreeStereotypes.ATTACK, AttackTreeTagTypes.SEVERITY, TagsManager.SEVERITY_VALUES[minSeverityIndex]);
            }
        }
        
        if(updateProbability) {
        
            int[] probabilityIndexBounds = TagsManager.getProbabilityIndexBounds(attack);
            String attackProbability = TagsManager.getElementTagParameter(attack, AttackTreeStereotypes.ATTACK, AttackTreeTagTypes.PROBABILITY);
        
            
            for(int i=0; i < TagsManager.PROBABILITY_VALUES.length; i++) {
                if(attackProbability.equals(TagsManager.PROBABILITY_VALUES[i])) {
                    int attackProbabilityIndex = i;
                    if(attackProbabilityIndex < probabilityIndexBounds[0]) {
                        TagsManager.setElementTagValue(attack, AttackTreeStereotypes.ATTACK, AttackTreeTagTypes.PROBABILITY, TagsManager.PROBABILITY_VALUES[probabilityIndexBounds[0]]);
                    } else if(attackProbabilityIndex > probabilityIndexBounds[1]) {
                        TagsManager.setElementTagValue(attack, AttackTreeStereotypes.ATTACK, AttackTreeTagTypes.PROBABILITY, TagsManager.PROBABILITY_VALUES[probabilityIndexBounds[1]]);
                    }
        
                    break;
                }
            }            
        }
        
        if(updateCountered) {
        
            if(CounterMeasureManager.isCountered(attack, false)) {
                ElementRepresentationManager.setClassColor(attack, ElementRepresentationManager.COUNTERED_ATTACK_COLOR);
                TagsManager.setElementTagValue(attack, AttackTreeStereotypes.ATTACK, AttackTreeTagTypes.COUNTERED_ATTACK, "true");
            } else {
                ElementRepresentationManager.setClassColor(attack, ElementRepresentationManager.DEFAULT_ATTACK_COLOR);
                TagsManager.setElementTagValue(attack, AttackTreeStereotypes.ATTACK, AttackTreeTagTypes.COUNTERED_ATTACK, "false");                    
            }
        
        }
        
        
        if(attack.isStereotyped(IAttackTreeDesignerPeerModule.MODULE_NAME, AttackTreeStereotypes.ROOT)) {
            /*
             * Propagate to references and their ascendants if it is a Referenced Root
             */
            for(Attribute attribute : attack.getObject()) {
                if(attribute.isStereotyped(IAttackTreeDesignerPeerModule.MODULE_NAME, AttackTreeStereotypes.TREE_REFERENCE_ATTRIBUTE)) {
                    Class reference = (Class) attribute.getOwner();
                    if(!reference.isDeleted()) {
                        
                        if(updateCountered) {
                            // update reference color
                            if(CounterMeasureManager.isCountered(reference, true)) {
                                ElementRepresentationManager.setClassColor(reference, ElementRepresentationManager.COUNTERED_ATTACK_COLOR);
                            } else {
                                ElementRepresentationManager.setClassColor(reference, ElementRepresentationManager.DEFAULT_ATTACK_COLOR);
                            }
                        }
                        
        
        
                        // propagate to references ascendants
                        for(Dependency parentDependency : reference.getImpactedDependency()) {
                            if(parentDependency.isStereotyped(IAttackTreeDesignerPeerModule.MODULE_NAME, AttackTreeStereotypes.CONNECTION)) {
                                Class firstAscendantAttack = getFirstNonDeletedAttackAscendant(parentDependency);
                                if(firstAscendantAttack != null)
                                    updateAndPropagateAttackTags(firstAscendantAttack, updateSeverity, updateProbability, updateCountered);
                            }
                        }
        
                    }
                }
            }
        } else {
            /*
             * Propagate to ascendants
             */
            for(Dependency parentDependency : attack.getImpactedDependency()) {
                if(parentDependency.isStereotyped(IAttackTreeDesignerPeerModule.MODULE_NAME, AttackTreeStereotypes.CONNECTION)) {
                    Class firstAscendantAttack = getFirstNonDeletedAttackAscendant(parentDependency);
                    if(firstAscendantAttack != null)
                        updateAndPropagateAttackTags(firstAscendantAttack, updateSeverity, updateProbability, updateCountered);
        
                }
            }
        }
    }

    @objid ("e9e2a866-9c46-404c-ae43-27c04851df8e")
    public static Class getFirstNonDeletedAttackAscendant(ModelElement element) {
        if(element instanceof Dependency) {
            return getFirstNonDeletedAttackAscendant( ((Dependency)element).getImpacted() );
        
        } else if (element instanceof Class && !element.isDeleted()) {
            Class node = (Class) element;
            if(node.isStereotyped(IAttackTreeDesignerPeerModule.MODULE_NAME, AttackTreeStereotypes.ATTACK)){
                return node;
            } else if (node.isStereotyped(IAttackTreeDesignerPeerModule.MODULE_NAME, AttackTreeStereotypes.OPERATOR)
                    || node.isStereotyped(IAttackTreeDesignerPeerModule.MODULE_NAME, AttackTreeStereotypes.TREE_REFERENCE)){
                for(Dependency dependency: node.getImpactedDependency()) {
                    if(dependency.isStereotyped(IAttackTreeDesignerPeerModule.MODULE_NAME, AttackTreeStereotypes.CONNECTION)){
                        return getFirstNonDeletedAttackAscendant(dependency);
                    }
                }
            }
        }
        return null;
    }

    @objid ("3fb79686-28ce-4548-a4d7-36c3bd051909")
    private static void handleDeletedDependency(IModelingSession session, Dependency deletedDependency) {
        if( deletedDependency.isStereotyped(IAttackTreeDesignerPeerModule.MODULE_NAME, AttackTreeStereotypes.CONNECTION)){
        
        
            MObject rootElement = deletedDependency.getDiagramElement().get(0).getOrigin(); 
        
        
            /*
             * Move Destination to Root (set Owner to Root)
             */
            ModelElement dependencyDependsOn = deletedDependency.getDependsOn();
            if  (!dependencyDependsOn.isDeleted()) {
        
                Class destinationNode = (Class) dependencyDependsOn;
                destinationNode.setOwner((ModelTree) rootElement);
                ElementCreationManager.renameElement(session, destinationNode); 
            }    
        
        
            /*
             * Update tags of ascendants
             */
            Class firstAttackAscendant = getFirstNonDeletedAttackAscendant(deletedDependency);
            if(firstAttackAscendant != null) 
                updateAndPropagateAttackTags(firstAttackAscendant, true, true, true);
        
        
        }
    }

    @objid ("10b948cf-b096-4002-8fd4-2d501ac0a50e")
    private static void handleDeletedClass(IModelingSession session, Class deletedClass) {
        if( (deletedClass).isStereotyped(IAttackTreeDesignerPeerModule.MODULE_NAME, AttackTreeStereotypes.ROOT)) {
        
        
            // Change attribute references type to undefined
            List<Attribute> referenceAttributesList = deletedClass.getObject();
            for(Attribute referenceAttribute:referenceAttributesList) {
                if(! referenceAttribute.isDeleted()) 
                    referenceAttribute.setType(session.getModel().getUmlTypes().getUNDEFINED());
        
                /*
                 *  update tags to ascendants
                 */
                Class reference = (Class) referenceAttribute.getOwner();
        
                // Update reference Color to Default
                ElementRepresentationManager.setClassColor(reference, ElementRepresentationManager.DEFAULT_ATTACK_COLOR);                        
        
                // Propagate to ascendants of reference
                for(Dependency parentDependency : reference.getImpactedDependency()) {
                    if(parentDependency.isStereotyped(IAttackTreeDesignerPeerModule.MODULE_NAME, AttackTreeStereotypes.CONNECTION)) {
                        Class firstAttackAscendant = getFirstNonDeletedAttackAscendant(parentDependency);
                        if(firstAttackAscendant != null) 
                            updateAndPropagateAttackTags(firstAttackAscendant, false, false, true);                         
                    }
                }
        
            }
        }
    }

    @objid ("78503f46-8107-4382-a8bd-142417d01e71")
    private static void handleDeletedNote(Note deletedNote) {
        if(deletedNote.getModel().getName().equals(AttackTreeNoteTypes.COUNTER_MEASURE)) {
            Class counteredAttack = (Class) deletedNote.getSubject();
            if(! counteredAttack.isDeleted()) {
        
                /*
                 * Update attack Tag to false and Color to Default
                 */
                ElementRepresentationManager.setClassColor(counteredAttack, ElementRepresentationManager.DEFAULT_ATTACK_COLOR);                        
                TagsManager.setElementTagValue(counteredAttack, AttackTreeStereotypes.ATTACK, AttackTreeTagTypes.COUNTERED_ATTACK, "false");
        
                /*
                 * Propagate to ascendants
                 */
                for(Dependency parentDependency : counteredAttack.getImpactedDependency()) {
                    if(parentDependency.isStereotyped(IAttackTreeDesignerPeerModule.MODULE_NAME, AttackTreeStereotypes.CONNECTION)) {
                        Class firstAttackAscendant = getFirstNonDeletedAttackAscendant(parentDependency);
                        if(firstAttackAscendant != null) 
                            updateAndPropagateAttackTags(firstAttackAscendant, false, false, true);                         
                    }
                }
        
                /*
                 * If counteredAttack is ROOT and is referenced
                 */
                if(counteredAttack.isStereotyped(IAttackTreeDesignerPeerModule.MODULE_NAME, AttackTreeStereotypes.ROOT)) {
                    for(Attribute attribute : counteredAttack.getObject()) {
                        if(attribute.isStereotyped(IAttackTreeDesignerPeerModule.MODULE_NAME, AttackTreeStereotypes.TREE_REFERENCE_ATTRIBUTE)) {
                            Class reference = (Class) attribute.getOwner();
        
                            /*
                             * Update reference Color to Default
                             */
                            ElementRepresentationManager.setClassColor(reference, ElementRepresentationManager.DEFAULT_ATTACK_COLOR);                        
        
                            /*
                             * Propagate to ascendants of reference
                             */
                            for(Dependency parentDependency : reference.getImpactedDependency()) {
                                if(parentDependency.isStereotyped(IAttackTreeDesignerPeerModule.MODULE_NAME, AttackTreeStereotypes.CONNECTION)) {
                                    Class firstAttackAscendant = getFirstNonDeletedAttackAscendant(parentDependency);
                                    if(firstAttackAscendant != null) 
                                        updateAndPropagateAttackTags(firstAttackAscendant, false, false, true);                         
                                }
                            }
                        }
                    }
                }
            }
        }
    }

}
