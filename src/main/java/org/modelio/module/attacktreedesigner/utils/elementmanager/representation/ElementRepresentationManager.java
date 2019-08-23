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
import org.modelio.module.attacktreedesigner.utils.DiagramElementStyle;
import org.modelio.module.attacktreedesigner.utils.Labels;
import org.modelio.vcore.smkernel.mapi.MObject;

@objid ("8d87749a-8242-406b-a1a8-9a84f227ed71")
public class ElementRepresentationManager {
    @objid ("2e7eb0d3-339a-499b-8833-1cca9c83d496")
    public static void maskChildren(IModuleContext moduleContext, IDiagramService diagramService, MObject element, IDiagramHandle diagramHandle) {
        // get only children of type Class
        List<Class> elementChildren = ((ModelTree) element).getOwnedElement(Class.class);
        
        for(Class child: elementChildren) {
        
            // recursively call maskChildren to children
            maskChildren(moduleContext, diagramService, child, diagramHandle);
        
            List<IDiagramGraphic> diagramGraphics = diagramHandle.getDiagramGraphics(child);
            IDiagramGraphic diagramGraphic = diagramGraphics.get(0);
            diagramGraphic.mask();
        
        }
    }

    @objid ("dd2174dd-2428-4ea3-8971-66ddd6475428")
    public static void unmaskChildren(IModuleContext moduleContext, IDiagramService diagramService, IDiagramHandle diagramHandle, MObject element, int x, int y) {
        List<Class> elementChildren = ((ModelTree) element).getOwnedElement(Class.class);
        
        int newX = x;
        int newY = y + AutoLayoutManager.VERTICAL_AUTOSPACING;
        
        // unmask children
        for(MObject child: elementChildren) {
        
            // recursively call maskChildren to children
            unmaskChildren(moduleContext, diagramService, diagramHandle, child, newX, newY);
        
        
        
            // unmask current child
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
            
            
            // increment x
            newX += AutoLayoutManager.HORIZONTAL_AUTOSPACING;
            
        }
    }

    @objid ("4e91463c-0100-4339-84bb-41c1ae661e26")
    public static void updateNodeStyle(IDiagramNode graphNode) {
        if(((Class) graphNode.getElement()).isStereotyped(IAttackTreeDesignerPeerModule.MODULE_NAME, AttackTreeStereotypes.ATTACK)) {
            graphNode.setProperty(Labels.CLASS_SHOWNAME.name(), DiagramElementStyle.ATTACK.getShowNameProperty());
            graphNode.setProperty(Labels.CLASS_REPRES_MODE.name(), DiagramElementStyle.ATTACK.getRepresentationMode());
        } else {
            graphNode.setProperty(Labels.CLASS_SHOWNAME.name(), DiagramElementStyle.OPERATOR.getShowNameProperty());
            graphNode.setProperty(Labels.CLASS_REPRES_MODE.name(), DiagramElementStyle.OPERATOR.getRepresentationMode());
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
                    diagramNode.setProperty(Labels.CLASS_REPRES_MODE.name(), DiagramElementStyle.REFERENCED_TREE.getRepresentationMode());
                    
                }                        
                
                diagramHandle.unmask(referenceTreeAttribute,0,0);
        
                diagramHandle.save();
                diagramHandle.close();
            }
        
        }
    }

}
