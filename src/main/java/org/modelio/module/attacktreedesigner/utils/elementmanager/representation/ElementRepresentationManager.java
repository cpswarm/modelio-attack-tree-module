package org.modelio.module.attacktreedesigner.utils.elementmanager.representation;

import java.util.List;
import com.modeliosoft.modelio.javadesigner.annotations.objid;
import org.modelio.api.modelio.diagram.IDiagramGraphic;
import org.modelio.api.modelio.diagram.IDiagramHandle;
import org.modelio.api.modelio.diagram.IDiagramNode;
import org.modelio.api.modelio.diagram.IDiagramService;
import org.modelio.api.module.context.IModuleContext;
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
import org.modelio.module.attacktreedesigner.impl.AttackTreeDesignerModule;
import org.modelio.module.attacktreedesigner.utils.elementmanager.CounterMeasureManager;
import org.modelio.module.attacktreedesigner.utils.elementmanager.Labels;
import org.modelio.vcore.smkernel.mapi.MObject;

@objid ("8d87749a-8242-406b-a1a8-9a84f227ed71")
public class ElementRepresentationManager {
    @objid ("bbdfeb9d-92e7-4e49-9871-787a3f28706a")
    public static final String DEFAULT_ATTACK_COLOR = "250, 240, 210";

    @objid ("c77d8c6e-671f-42a0-9e76-34b37bc7dab8")
    public static final String COUNTERED_ATTACK_COLOR = "220, 250, 210";

    @objid ("2e7eb0d3-339a-499b-8833-1cca9c83d496")
    public static void maskChildren(IModuleContext moduleContext, IDiagramService diagramService, MObject element, IDiagramHandle diagramHandle) {
        if(((Class)element).isStereotyped(IAttackTreeDesignerPeerModule.MODULE_NAME, AttackTreeStereotypes.ROOT)) {
        
            // get children of Root
            List<Dependency> elementDependencies = ((ModelTree) element).getDependsOnDependency();
        
            for(Dependency dependency:elementDependencies) {
                if(dependency.isStereotyped(IAttackTreeDesignerPeerModule.MODULE_NAME, AttackTreeStereotypes.CONNECTION)) {
                    ModelElement child = dependency.getDependsOn();
                    // recursively call maskChildren to children
                    maskChildren(moduleContext, diagramService, child, diagramHandle);
        
                    List<IDiagramGraphic> diagramGraphics = diagramHandle.getDiagramGraphics(child);
                    IDiagramGraphic diagramGraphic = diagramGraphics.get(0);
                    diagramGraphic.mask();
                }
            }
        
        } else {
            // get children of type Class
            List<Class> elementChildren = ((ModelTree) element).getOwnedElement(Class.class);
        
            for(Class child: elementChildren) {
                // recursively call maskChildren to children
                maskChildren(moduleContext, diagramService, child, diagramHandle);
        
                List<IDiagramGraphic> diagramGraphics = diagramHandle.getDiagramGraphics(child);
                IDiagramGraphic diagramGraphic = diagramGraphics.get(0);
                diagramGraphic.mask();
        
            }
        }
    }

    @objid ("dd2174dd-2428-4ea3-8971-66ddd6475428")
    public static void unmaskChildren(IModuleContext moduleContext, IDiagramService diagramService, IDiagramHandle diagramHandle, MObject element, int x, int y) {
        int newX = x;
        int newY = y + AutoLayoutManager.VERTICAL_AUTOSPACING;
        
        List<Class> elementChildren = ((ModelTree) element).getOwnedElement(Class.class);            
        
        // unmask children
        for(MObject child: elementChildren) {
        
            // recursively call maskChildren to children
            unmaskChildren(moduleContext, diagramService, diagramHandle, child, newX, newY);     
        
            // unmask current child
            unmaskElement(diagramHandle, newX, newY, child);                       
        
            // increment x
            newX += AutoLayoutManager.HORIZONTAL_AUTOSPACING;
        
        }
        //        }
    }

    @objid ("4e91463c-0100-4339-84bb-41c1ae661e26")
    public static void updateNodeStyle(IDiagramNode graphNode) {
        Class nodeElement = (Class) graphNode.getElement();
        if(nodeElement.isStereotyped(IAttackTreeDesignerPeerModule.MODULE_NAME, AttackTreeStereotypes.ATTACK)) {
            graphNode.setProperty(Labels.CLASS_SHOWNAME.name(), DiagramElementStyle.ATTACK.getShowNameProperty());
            graphNode.setProperty(Labels.CLASS_REPRES_MODE.name(), DiagramElementStyle.ATTACK.getRepresentationMode());
            if(CounterMeasureManager.isCountered(nodeElement, true))
                graphNode.setFillColor(COUNTERED_ATTACK_COLOR);
            else
                graphNode.setFillColor(DEFAULT_ATTACK_COLOR);
            
        } else if(nodeElement.isStereotyped(IAttackTreeDesignerPeerModule.MODULE_NAME, AttackTreeStereotypes.OPERATOR)) {
            graphNode.setProperty(Labels.CLASS_SHOWNAME.name(), DiagramElementStyle.OPERATOR.getShowNameProperty());
            graphNode.setProperty(Labels.CLASS_REPRES_MODE.name(), DiagramElementStyle.OPERATOR.getRepresentationMode());
            
        } else if(nodeElement.isStereotyped(IAttackTreeDesignerPeerModule.MODULE_NAME, AttackTreeStereotypes.TREE_REFERENCE)) {
            graphNode.setProperty(Labels.CLASS_SHOWNAME.name(), DiagramElementStyle.TREE_REFERENCE.getShowNameProperty());
            graphNode.setProperty(Labels.CLASS_SHOWNAME.name(), DiagramElementStyle.TREE_REFERENCE.getShowNameProperty());
            if(CounterMeasureManager.isCountered(nodeElement, true))
                graphNode.setFillColor(COUNTERED_ATTACK_COLOR);
            else
                graphNode.setFillColor(DEFAULT_ATTACK_COLOR);
        }
    }

    @objid ("e2bd70a7-67c9-4128-8f5f-ea72e758faed")
    public static void changeStyleToAttack(Classifier selectedElement, IModuleContext moduleContext) {
        IDiagramService diagramService = moduleContext.getModelioServices().getDiagramService();            
        
        List<AbstractDiagram> diagrams = ((Element) selectedElement).getDiagramElement(AbstractDiagram.class);
        
        for(AbstractDiagram diagram:diagrams) {
            try(  IDiagramHandle diagramHandle = diagramService.getDiagramHandle(diagram);){
        
                List<IDiagramGraphic> diagramGraphics = diagramHandle.getDiagramGraphics(selectedElement);
                if( diagramGraphics.size()>0 && diagramGraphics.get(0) instanceof IDiagramNode) {
        
                    IDiagramNode diagramNode = (IDiagramNode )diagramGraphics.get(0);
                    diagramNode.setProperty(Labels.CLASS_REPRES_MODE.name(), DiagramElementStyle.ATTACK.getRepresentationMode());
        
                }                        
        
                diagramHandle.save();
                diagramHandle.close();
            }
        
        }
    }

    @objid ("da98e348-8b04-4e90-b16b-452838052146")
    public static void changeStyleToReferencedTree(Classifier selectedElement, IModuleContext moduleContext, Attribute referenceTreeAttribute) {
        IDiagramService diagramService = moduleContext.getModelioServices().getDiagramService();            
        
        List<AbstractDiagram> diagrams = ((Element) selectedElement).getDiagramElement(AbstractDiagram.class);
        
        for(AbstractDiagram diagram:diagrams) {
            try(  IDiagramHandle diagramHandle = diagramService.getDiagramHandle(diagram);){
        
                List<IDiagramGraphic> diagramGraphics = diagramHandle.getDiagramGraphics(selectedElement);
                if( diagramGraphics.size()>0 && diagramGraphics.get(0) instanceof IDiagramNode) {
        
                    IDiagramNode diagramNode = (IDiagramNode )diagramGraphics.get(0);
                    diagramNode.setProperty(Labels.CLASS_REPRES_MODE.name(), DiagramElementStyle.TREE_REFERENCE.getRepresentationMode());
        
                }                        
        
                diagramHandle.unmask(referenceTreeAttribute,0,0);
        
                diagramHandle.save();
                diagramHandle.close();
            }
        
        }
    }

    @objid ("c6940168-17f7-472b-9148-4258ff3e488b")
    public static void unmaskElement(IDiagramHandle diagramHandle, int newX, int newY, MObject child) {
        List<IDiagramGraphic> graph = diagramHandle.unmask(child, newX, newY);
        
        
        
        // update node style
        if((graph != null) &&  (graph.size() > 0) && (graph.get(0) instanceof IDiagramNode)) {
            IDiagramNode graphNode = (IDiagramNode)graph.get(0);
            updateNodeStyle(graphNode);
        }
        
        // unmask dependencies
        List<Dependency> elementDependsOnDependencies = ((ModelElement) child).getDependsOnDependency();
        for(Dependency dependency: elementDependsOnDependencies) {
            if(dependency.isStereotyped(IAttackTreeDesignerPeerModule.MODULE_NAME, AttackTreeStereotypes.CONNECTION)) {
                diagramHandle.unmask(dependency, 0, 0);
            }
        }
        
        // unmask counter measure
        List<Note> elementNotes = ((ModelElement) child).getDescriptor();
        int noteSpacingX = newX;
        int noteSpacingY = newY;
        for(Note note:elementNotes) {
            if(note.getModel().getName().equals(AttackTreeNoteTypes.COUNTER_MEASURE)) {
                diagramHandle.unmask(note, noteSpacingX += (AutoLayoutManager.HORIZONTAL_AUTOSPACING / 4), noteSpacingY += AutoLayoutManager.VERTICAL_AUTOSPACING / 4);
            }
        }
        
        // unmask attribute Referenced tree
        List<Attribute> elementAttributes = ((Class) child).getOwnedAttribute();
        for(Attribute attribute:elementAttributes) {
            if(attribute.isStereotyped(IAttackTreeDesignerPeerModule.MODULE_NAME, AttackTreeStereotypes.TREE_REFERENCE_ATTRIBUTE)) {
                diagramHandle.unmask(attribute,0,0);
            }
        }
    }

    @objid ("a0b4c771-4e82-435f-8711-84f36cf71481")
    public static void setClassColor(Class element, IDiagramHandle diagramHandle, String color) {
        List<IDiagramGraphic> diagramGraphics = diagramHandle.getDiagramGraphics(element);
        if(! diagramGraphics.isEmpty()) {
            IDiagramNode diagramNode = (IDiagramNode) diagramGraphics.get(0);
            diagramNode.setFillColor(color);
        }
    }

    @objid ("ea9842d2-e462-4320-afd2-86dfa1dbf70b")
    public static void setClassColor(Class element, String color) {
        IDiagramService diagramService = AttackTreeDesignerModule.getInstance().getModuleContext().getModelioServices().getDiagramService();                    
        List<AbstractDiagram> diagrams = element.getDiagramElement(AbstractDiagram.class);
        for(AbstractDiagram diagram: diagrams) {
            try(  IDiagramHandle diagramHandle = diagramService.getDiagramHandle(diagram);){
                ElementRepresentationManager.setClassColor(element, diagramHandle, color);
        
                diagramHandle.save();
                diagramHandle.close();
            }
        }
    }

}
