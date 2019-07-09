package org.modelio.module.attacktreedesigner.command.explorer;

import java.util.List;
import com.modeliosoft.modelio.javadesigner.annotations.objid;
import org.eclipse.draw2d.geometry.Rectangle;
import org.modelio.api.modelio.diagram.IDiagramGraphic;
import org.modelio.api.modelio.diagram.IDiagramHandle;
import org.modelio.api.modelio.diagram.IDiagramNode;
import org.modelio.api.modelio.diagram.IDiagramService;
import org.modelio.api.modelio.model.IModelingSession;
import org.modelio.api.modelio.model.ITransaction;
import org.modelio.api.module.IModule;
import org.modelio.api.module.command.DefaultModuleCommandHandler;
import org.modelio.api.module.context.IModuleContext;
import org.modelio.metamodel.diagrams.AbstractDiagram;
import org.modelio.metamodel.uml.infrastructure.Dependency;
import org.modelio.metamodel.uml.infrastructure.Element;
import org.modelio.metamodel.uml.infrastructure.ModelElement;
import org.modelio.metamodel.uml.infrastructure.ModelTree;
import org.modelio.metamodel.uml.statik.Class;
import org.modelio.module.attacktreedesigner.api.AttackTreeStereotypes;
import org.modelio.module.attacktreedesigner.api.IAttackTreeDesignerPeerModule;
import org.modelio.module.attacktreedesigner.i18n.Messages;
import org.modelio.module.attacktreedesigner.impl.AttackTreeDesignerModule;
import org.modelio.module.attacktreedesigner.utils.AutoLayoutManager;
import org.modelio.module.attacktreedesigner.utils.DiagramElementStyle;
import org.modelio.module.attacktreedesigner.utils.Labels;
import org.modelio.module.attacktreedesigner.utils.elementmanager.ElementRepresentationManager;
import org.modelio.vcore.smkernel.mapi.MObject;

@objid ("fc2698b1-fc2d-4a0c-99d8-c38b18503d7e")
public class UnmaskSubTreeCommand extends DefaultModuleCommandHandler {
    @objid ("bf6048fb-b21c-4bb3-b690-0ef3fec6e9fe")
    @Override
    public void actionPerformed(final List<MObject> selectedElements, final IModule module) {
        IModuleContext moduleContext = AttackTreeDesignerModule.getInstance().getModuleContext();
        IModelingSession session = moduleContext.getModelingSession();
        MObject selectedElement = selectedElements.get(0);  
        
        IDiagramService diagramService = moduleContext.getModelioServices().getDiagramService();
        
        
        //List<Class> displacedChildren = new ArrayList<>();
        
        try( ITransaction transaction = session.createTransaction(Messages.getString ("Info.Session.UpdateModel"))){
        
        
            /*
             *  Remove SubTree stereotype from "selectedElement"
             */
            ((Class) selectedElement).removeStereotypes(IAttackTreeDesignerPeerModule.MODULE_NAME, AttackTreeStereotypes.SUBTREE);
        
        
        
            List<AbstractDiagram> diagrams = ((Element) selectedElement).getDiagramElement(AbstractDiagram.class);
            for(AbstractDiagram diagram: diagrams) {
                try(  IDiagramHandle diagramHandle = diagramService.getDiagramHandle(diagram);){
        
        
                    List<IDiagramGraphic> diagramGraphics = diagramHandle.getDiagramGraphics(selectedElement);
                    IDiagramGraphic diagramGraphic = diagramGraphics.get(0);
        
        
                    Rectangle elementBounds = ((IDiagramNode) diagramGraphic).getBounds();
        
        
                    /*
                     * Unmask children of "Operator element" child of Selected element ("Operator element" not included)
                     */
                    List<Class> elementChildren = ((ModelTree) selectedElement).getOwnedElement(Class.class);
                    for(Class operatorEltChildOfSelectedElt:elementChildren) {
        
                        ElementRepresentationManager.unmaskChildren(moduleContext, diagramService, diagramHandle , operatorEltChildOfSelectedElt, elementBounds.x, elementBounds.y + AutoLayoutManager.VERTICAL_AUTOSPACING);
        
        
                        /* 
                         * setOwner of Operator element child of Selected element to update representation
                         */
                        MObject root = diagram.getOrigin().getCompositionOwner();
                        operatorEltChildOfSelectedElt.setOwner((ModelTree) root);
        
        
                        //displacedChildren.add(operatorChild);
                    }
        
        
                    diagramHandle.save();
                    //diagramHandle.close();
                    //}
                    //}
        
                    //-->transaction.commit ();
                    //-->}
        
        
        
                    //-->try( ITransaction transaction = session.createTransaction(Messages.getString ("Info.Session.UpdateModel"))){
        
        
        
                    //-->List<AbstractDiagram> diagrams = ((Element) selectedElement).getDiagramElement(AbstractDiagram.class);
                    //for(AbstractDiagram diagram: diagrams) {
                    //try(  IDiagramHandle diagramHandle = diagramService.getDiagramHandle(diagram);){
        
                    for(Class displacedOperatorElement: elementChildren) {
        
                        //List<IDiagramGraphic> diagramGraphics = diagramHandle.getDiagramGraphics(selectedElement);
                        //IDiagramGraphic diagramGraphic = diagramGraphics.get(0);
        
                        //Rectangle elementBounds = ((IDiagramNode) diagramGraphic).getBounds();
        
        
                        /*
                         *  Unmask Operator element child of Selected element 
                         */
                        List<IDiagramGraphic> graph = diagramHandle.unmask(displacedOperatorElement, elementBounds.x, elementBounds.y + AutoLayoutManager.VERTICAL_AUTOSPACING);
        
        
                        /*
                         * Update Operator element child's style
                         */
                        if((graph != null) &&  (graph.size() > 0) && (graph.get(0) instanceof IDiagramNode)) {
                            IDiagramNode graphNode = (IDiagramNode)graph.get(0);
                            updateNodeStyle(graphNode);
                        }
        
        
                        /*
                         *  unmask dependencies related to displaced operator element
                         */
                        List<Dependency> elementDependsOnDependencies = ((ModelElement) displacedOperatorElement).getDependsOnDependency();
                        for(Dependency dependency: elementDependsOnDependencies) {
                            diagramHandle.unmask(dependency, 0, 0);
                        }
                        List<Dependency> elementImpactedDependency = ((ModelElement) displacedOperatorElement).getImpactedDependency();
                        for(Dependency dependency: elementImpactedDependency) {
                            diagramHandle.unmask(dependency, 0, 0);
                        }
        
                        
                        /*
                         * update owner of displaced operator element Child of selected element
                         */
                        displacedOperatorElement.setOwner((ModelTree) selectedElement);
                
                    }
        
                    diagramHandle.save();
                    diagramHandle.close();
                }
        
            }
        
            transaction.commit ();
        }
    }

    @objid ("7d0beca4-ff4e-4484-9a27-c3d6aec6beca")
    @Override
    public boolean accept(final List<MObject> selectedElements, final IModule module) {
        if ((selectedElements != null) && (selectedElements.size() == 1)){
            MObject selectedElement = selectedElements.get(0);
            return ((selectedElement != null) 
                    && (selectedElement instanceof Class)
                    && (((Class) selectedElement).isStereotyped(IAttackTreeDesignerPeerModule.MODULE_NAME, AttackTreeStereotypes.SUBTREE))
                    && selectedElement.getStatus().isModifiable());
        }
        return false;
    }

    @objid ("db62b280-a883-4dc4-8298-c90e40835b14")
    public void updateNodeStyle(IDiagramNode graphNode) {
        if(((Class) graphNode.getElement()).isStereotyped(IAttackTreeDesignerPeerModule.MODULE_NAME, AttackTreeStereotypes.ATTACK)) {
            graphNode.setProperty(Labels.CLASS_SHOWNAME.name(), DiagramElementStyle.ATTACK.getShowNameProperty());
            graphNode.setProperty(Labels.CLASS_REPRES_MODE.name(), DiagramElementStyle.ATTACK.getRepresentationMode());
        } else {
            graphNode.setProperty(Labels.CLASS_SHOWNAME.name(), DiagramElementStyle.OPERATOR.getShowNameProperty());
            graphNode.setProperty(Labels.CLASS_REPRES_MODE.name(), DiagramElementStyle.OPERATOR.getRepresentationMode());
        }
    }

}
