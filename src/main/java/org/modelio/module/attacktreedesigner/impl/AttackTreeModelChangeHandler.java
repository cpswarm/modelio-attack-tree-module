package org.modelio.module.attacktreedesigner.impl;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import com.modeliosoft.modelio.javadesigner.annotations.objid;
import org.modelio.api.modelio.diagram.IDiagramGraphic;
import org.modelio.api.modelio.diagram.IDiagramHandle;
import org.modelio.api.modelio.diagram.IDiagramNode;
import org.modelio.api.modelio.diagram.IDiagramService;
import org.modelio.api.modelio.model.IModelingSession;
import org.modelio.api.modelio.model.ITransaction;
import org.modelio.api.modelio.model.event.IElementDeletedEvent;
import org.modelio.api.modelio.model.event.IModelChangeEvent;
import org.modelio.api.modelio.model.event.IModelChangeHandler;
import org.modelio.metamodel.diagrams.AbstractDiagram;
import org.modelio.metamodel.uml.infrastructure.Dependency;
import org.modelio.metamodel.uml.infrastructure.Element;
import org.modelio.metamodel.uml.infrastructure.ModelElement;
import org.modelio.metamodel.uml.infrastructure.ModelTree;
import org.modelio.metamodel.uml.infrastructure.Note;
import org.modelio.metamodel.uml.statik.Attribute;
import org.modelio.metamodel.uml.statik.Class;
import org.modelio.metamodel.uml.statik.Classifier;
import org.modelio.module.attacktreedesigner.api.AttackTreeNoteTypes;
import org.modelio.module.attacktreedesigner.api.AttackTreeStereotypes;
import org.modelio.module.attacktreedesigner.api.IAttackTreeDesignerPeerModule;
import org.modelio.module.attacktreedesigner.i18n.Messages;
import org.modelio.module.attacktreedesigner.utils.elementmanager.ElementCreationManager;
import org.modelio.module.attacktreedesigner.utils.elementmanager.representation.ElementRepresentationManager;
import org.modelio.vcore.smkernel.mapi.MObject;

@objid ("11497816-612d-40c7-9892-b8edaac4d924")
public class AttackTreeModelChangeHandler implements IModelChangeHandler {
    @objid ("4a269eed-c402-4cd4-94b4-4f81b4e4db37")
    private static final String DEFAULT_ATTACK_COLOR = "250, 240, 210";

    @objid ("0a2e6dc5-227b-4540-8c5a-b84189766ebd")
    private static final String COUNTERED_ATTACK_COLOR = "220, 250, 210";

    @objid ("f32a0b6f-acc3-43ad-8283-47e50175e432")
    @Override
    public void handleModelChange(IModelingSession session, IModelChangeEvent event) {
        Set<AbstractDiagram> setOfDiagramsToUpdateColorsOfAttacks = new HashSet<>();
        
        
        
        /*
         * Deleted Elements
         */
        List<IElementDeletedEvent> deleteEvents = event.getDeleteEvents();
        if(! deleteEvents.isEmpty()) {
            try( ITransaction transaction = session.createTransaction (
                    //"update after deleted elements"
                    Messages.getString ("Info.Session.UpdateModel")
                    )){
        
                for (IElementDeletedEvent deleteEvent : deleteEvents){
                    handleDeletedElement(session, setOfDiagramsToUpdateColorsOfAttacks, deleteEvent);
                }
        
                transaction.commit();
            }
        }
        
        
        
        /*
         * Created elements
         */
        Set<MObject> createdEvents = event.getCreationEvents();
        for ( MObject createdEvent : createdEvents){
        
            if(createdEvent instanceof Note ||
                    createdEvent instanceof Class ||
                    (createdEvent instanceof Dependency && 
                            ((Dependency) createdEvent).isStereotyped(IAttackTreeDesignerPeerModule.MODULE_NAME, 
                                    AttackTreeStereotypes.CONNECTION))) {
        
                // add diagrams to setOfDiagramsToUpdateColorsOfAttacks
                List<AbstractDiagram> diagrams = ((Element) createdEvent).getDiagramElement(AbstractDiagram.class);
                setOfDiagramsToUpdateColorsOfAttacks.addAll(diagrams);
            }
        }
        
        
        
        /*
         * Updated elements
         */
        Set<MObject> updatedEvents = event.getUpdateEvents();
        for ( MObject updatedEvent : updatedEvents){
            if( updatedEvent instanceof Class
                    && ((Class)updatedEvent).isStereotyped(IAttackTreeDesignerPeerModule.MODULE_NAME, AttackTreeStereotypes.NODE)) {
        
                // add diagrams to setOfDiagramsToUpdateColorsOfAttacks
                List<AbstractDiagram> diagrams = ((Element) updatedEvent).getDiagramElement(AbstractDiagram.class);
                setOfDiagramsToUpdateColorsOfAttacks.addAll(diagrams);
            }
        }
        
        
        
        /*
         * Update Colors of attacks (Counterd/Uncountered)
         */
        if(!setOfDiagramsToUpdateColorsOfAttacks.isEmpty()) {
            try( ITransaction transaction = session.createTransaction(
                    //"update doagrams colors of attacks"
                    Messages.getString ("Info.Session.UpdateModel")
                    )){
        
                IDiagramService diagramService = AttackTreeDesignerModule.getInstance().getModuleContext()
                        .getModelioServices().getDiagramService();  
        
                for(AbstractDiagram diagram:setOfDiagramsToUpdateColorsOfAttacks) {
                    try(  IDiagramHandle diagramHandle = diagramService.getDiagramHandle(diagram);){
        
                        updateAttacksColors(diagram, diagramHandle);
                        diagramHandle.save();
                        diagramHandle.close();
                    }
                }
                transaction.commit();
            }
        }
    }

    @objid ("dd0a931b-1e55-4087-a9b1-859edbc0d378")
    private void updateAttacksColors(AbstractDiagram diagram, IDiagramHandle diagramHandle) {
        MObject owner = diagram.getCompositionOwner();
        if(owner instanceof Class 
                && ((Class) owner ).isStereotyped(IAttackTreeDesignerPeerModule.MODULE_NAME, AttackTreeStereotypes.ROOT)){
        
        
            Class rootElement = (Class) owner;
        
            List<Class> subTreesToAnalyse = new ArrayList<>();
            subTreesToAnalyse.add(rootElement);
        
            List<Class> rootOwnedClasses = ((ModelTree) rootElement).getOwnedElement(Class.class);
            for(Class element:rootOwnedClasses) {
                if(element.isStereotyped(IAttackTreeDesignerPeerModule.MODULE_NAME, AttackTreeStereotypes.NODE)){
                    List<Dependency> elementImpactedDependencies = element.getImpactedDependency();
                    if( elementImpactedDependencies.isEmpty()) {
                        subTreesToAnalyse.add(element);
                    } else {                
                        for(Dependency dependency:elementImpactedDependencies) {
                            if (dependency.isStereotyped(IAttackTreeDesignerPeerModule.MODULE_NAME, AttackTreeStereotypes.CONNECTION)) {
                                subTreesToAnalyse.add(element);
                                break;
                            }
                        }
                    }
                }
            }
        
            /*
             * recursively update colors of subTrees
             */
            for(Class subTree:subTreesToAnalyse) {
                updateCounteredAttackColors(subTree, diagramHandle);
            }
        
        }
    }

    @objid ("4f7d6b68-cba3-4e19-ad6f-0acf38213d7d")
    private static Boolean updateCounteredAttackColors(Class subTree, IDiagramHandle diagramHandle) {
        boolean hasChildren = false;
        boolean allChildrenAreCountered = true;
        boolean atLeastOneChildIsCountered = false;
        
        /*
         * update children
         */
        for(Dependency dependency: subTree.getDependsOnDependency()) {
            if(dependency.isStereotyped(IAttackTreeDesignerPeerModule.MODULE_NAME, AttackTreeStereotypes.CONNECTION)) {
        
                ModelElement child = dependency.getDependsOn();
                //if(child instanceof Class) {
                hasChildren = true;
                boolean counteredChild = updateCounteredAttackColors((Class)child, diagramHandle); // boolean local variable at beginning
                if(counteredChild ) {
                    atLeastOneChildIsCountered = true;
                } else {
                    allChildrenAreCountered = false;
                }
                //}
            }
        }
        
        
        /*
         * update current element depending on children
         */
        if (subTree.isStereotyped(IAttackTreeDesignerPeerModule.MODULE_NAME, AttackTreeStereotypes.ATTACK)) {
            if( (hasChildren && allChildrenAreCountered) 
                    || attackHasCounterMeasure(subTree)) {
                setClassColor(subTree, diagramHandle,COUNTERED_ATTACK_COLOR);
                return true;
            } else { 
                setClassColor(subTree, diagramHandle,DEFAULT_ATTACK_COLOR);
                return false;
            }
        } else if  (subTree.isStereotyped(IAttackTreeDesignerPeerModule.MODULE_NAME, AttackTreeStereotypes.AND)) {
            return atLeastOneChildIsCountered;
        } else if  (subTree.isStereotyped(IAttackTreeDesignerPeerModule.MODULE_NAME, AttackTreeStereotypes.OR)) {
            return (hasChildren && allChildrenAreCountered);
        }
        return null;
    }

    @objid ("16d98f10-b4d5-440d-bd88-ded2d63278ff")
    private static boolean attackHasCounterMeasure(Class attack) {
        List<Note> attackNotes = attack.getDescriptor();
        for(Note note:attackNotes) {
            if(note.getModel().getName().equals(AttackTreeNoteTypes.COUNTER_MEASURE)) {
                return true;
            }
        }
        return false;
    }

    @objid ("87d18e22-1bd0-4ca6-8c9d-5434e3159e88")
    public static void setClassColor(Class subTree, IDiagramHandle diagramHandle, String color) {
        List<IDiagramGraphic> diagramGraphics = diagramHandle.getDiagramGraphics(subTree);
        if(! diagramGraphics.isEmpty()) {
            IDiagramNode diagramNode = (IDiagramNode) diagramGraphics.get(0);
            diagramNode.setFillColor(color);
        }
    }

    @objid ("0f37b455-4380-480d-9909-5c630a100844")
    public void handleDeletedElement(IModelingSession session, Set<AbstractDiagram> diagramSet, IElementDeletedEvent deleteEvent) {
        MObject deletedElement = deleteEvent.getDeletedElement();
        
        /*
         * If the deleted element is a dependency
         */
        if (deletedElement instanceof Dependency 
                && ((Dependency) deletedElement).isStereotyped(IAttackTreeDesignerPeerModule.MODULE_NAME, AttackTreeStereotypes.CONNECTION)){
        
            List<AbstractDiagram> diagrams = ((Element) deletedElement).getDiagramElement(AbstractDiagram.class);
            for(AbstractDiagram diagram:diagrams) {
                diagramSet.add(diagram);
            }
        
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
        else if (deletedElement instanceof Class) {
        
            List<AbstractDiagram> diagrams = ((Element) deletedElement).getDiagramElement(AbstractDiagram.class);
            for(AbstractDiagram diagram:diagrams) {
                diagramSet.add(diagram);
            }
        
        
            /*
             * If the deleted Element is a Root, verify if it is referenced by other trees
             */
            if (((Class) deletedElement).isStereotyped(IAttackTreeDesignerPeerModule.MODULE_NAME, AttackTreeStereotypes.ROOT)){
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
        
        
        
        /*
         * If the deleted element is a note
         */
        else if (deletedElement instanceof Note) {
        
            List<AbstractDiagram> diagrams = ((Element) deletedElement).getDiagramElement(AbstractDiagram.class);
            for(AbstractDiagram diagram:diagrams) {
                diagramSet.add(diagram);
            }
        }
    }

}
