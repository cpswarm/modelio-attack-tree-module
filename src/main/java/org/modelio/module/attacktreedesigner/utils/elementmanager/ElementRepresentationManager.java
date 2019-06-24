package org.modelio.module.attacktreedesigner.utils.elementmanager;

import java.util.List;
import com.modeliosoft.modelio.javadesigner.annotations.objid;
import org.modelio.api.modelio.diagram.IDiagramGraphic;
import org.modelio.api.modelio.diagram.IDiagramHandle;
import org.modelio.api.modelio.diagram.IDiagramNode;
import org.modelio.api.modelio.diagram.IDiagramService;
import org.modelio.api.module.context.IModuleContext;
import org.modelio.metamodel.diagrams.AbstractDiagram;
import org.modelio.metamodel.uml.infrastructure.Dependency;
import org.modelio.metamodel.uml.infrastructure.ModelElement;
import org.modelio.metamodel.uml.infrastructure.ModelTree;
import org.modelio.metamodel.uml.statik.Class;
import org.modelio.module.attacktreedesigner.api.AttackTreeStereotypes;
import org.modelio.module.attacktreedesigner.api.IAttackTreeDesignerPeerModule;
import org.modelio.module.attacktreedesigner.utils.AutoLayoutManager;
import org.modelio.module.attacktreedesigner.utils.DiagramElementStyle;
import org.modelio.module.attacktreedesigner.utils.Labels;
import org.modelio.vcore.smkernel.mapi.MObject;

@objid ("a35d8148-0ecb-46bd-bda1-1becf9977ed0")
public class ElementRepresentationManager {
    @objid ("c986fd17-ec42-4bdc-b3b6-543351e412cd")
    public static void maskChildren(IModuleContext moduleContext, IDiagramService diagramService, MObject element) {
        // get only children of type Class
        List<Class> elementChildren = ((ModelTree) element).getOwnedElement(Class.class);
        
        for(Class child: elementChildren) {
        
            List<AbstractDiagram> associatedDiagrams = child.getDiagramElement(AbstractDiagram.class);
            for(AbstractDiagram diagram: associatedDiagrams) {
        
                try(  IDiagramHandle diagramHandle = diagramService.getDiagramHandle(diagram);){
                    List<IDiagramGraphic> diagramGraphics = diagramHandle.getDiagramGraphics(child);
        
                    IDiagramGraphic diagramGraphic = diagramGraphics.get(0);
                    diagramGraphic.mask();
        
                    diagramHandle.save();
                    diagramHandle.close();
                }
            }
        
            // recursively call maskChildren to children
            maskChildren(moduleContext, diagramService, child);
        }
    }

    @objid ("db7a92bf-6ae6-484a-97c1-9194c3462b7c")
    public static void unmaskChildren(IModuleContext moduleContext, IDiagramService diagramService, IDiagramHandle diagramHandle, MObject element, int x, int y) {
        List<Class> elementChildren = ((ModelTree) element).getOwnedElement(Class.class);
        //        //        List<ModelTree> elementChildren = ((ModelTree) element).getOwnedElement();
        //        List<? extends MObject> elementChildren = element.getCompositionChildren();
        
        int newX = x;
        int newY = y + AutoLayoutManager.VERTICAL_AUTOSPACING;
        
        // unmask children
        for(MObject child: elementChildren) {
        
            // recursively call maskChildren to children
            unmaskChildren(moduleContext, diagramService, diagramHandle, child, newX, newY);
        
        
            // unmask current element
            List<IDiagramGraphic> graph = diagramHandle.unmask(child, newX, newY);
            if((graph != null) &&  (graph.size() > 0) && (graph.get(0) instanceof IDiagramNode)) {
                IDiagramNode graphNode = (IDiagramNode)graph.get(0);
                if(((Class) graphNode.getElement()).isStereotyped(IAttackTreeDesignerPeerModule.MODULE_NAME, AttackTreeStereotypes.ATTACK)) {
                    graphNode.setProperty(Labels.CLASS_SHOWNAME.name(), DiagramElementStyle.ATTACK.getShowNameProperty());
                    graphNode.setProperty(Labels.CLASS_REPRES_MODE.name(), DiagramElementStyle.ATTACK.getRepresentationMode());
                } else {
                    graphNode.setProperty(Labels.CLASS_SHOWNAME.name(), DiagramElementStyle.OPERATOR.getShowNameProperty());
                    graphNode.setProperty(Labels.CLASS_REPRES_MODE.name(), DiagramElementStyle.OPERATOR.getRepresentationMode());
                }
        
            }
        
            // increment x
            newX += AutoLayoutManager.HORIZONTAL_AUTOSPACING;
        }
        
        // unmask dependencies
        List<Dependency> elementDependsOnDependencies = ((ModelElement) element).getDependsOnDependency();
        for(Dependency dependency: elementDependsOnDependencies) {
            diagramHandle.unmask(dependency, 0, 0);
        }
    }

}
