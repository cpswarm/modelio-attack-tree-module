package org.modelio.module.attacktreedesigner.command.explorer;

import java.util.ArrayList;
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
        
        
        List<Class> displacedChildren = new ArrayList<>();
        
        try( ITransaction transaction = session.createTransaction(Messages.getString ("Info.Session.UpdateModel"))){
        
        
            // Remove SubTree stereotype from "selectedElement"
            ((Class) selectedElement).removeStereotypes(IAttackTreeDesignerPeerModule.MODULE_NAME, AttackTreeStereotypes.SUBTREE);
        
        
        
        
        
            List<AbstractDiagram> diagrams = ((Element) selectedElement).getDiagramElement(AbstractDiagram.class);
            for(AbstractDiagram diagram: diagrams) {
                try(  IDiagramHandle diagramHandle = diagramService.getDiagramHandle(diagram);){
        
        
                    List<IDiagramGraphic> diagramGraphics = diagramHandle.getDiagramGraphics(selectedElement);
                    IDiagramGraphic diagramGraphic = diagramGraphics.get(0);
        
        
                    Rectangle elementBounds = ((IDiagramNode) diagramGraphic).getBounds();
        
                    List<Class> elementChildren = ((ModelTree) selectedElement).getOwnedElement(Class.class);
                    for(Class child:elementChildren) {
        
                        ElementRepresentationManager.unmaskChildren(moduleContext, diagramService, diagramHandle , child, elementBounds.x, elementBounds.y + AutoLayoutManager.VERTICAL_AUTOSPACING);
        
                        // Refresh owner of direct children (Operator AND or OR) to update representation
                        MObject root = diagram.getOrigin().getCompositionOwner();
                        //                    List<Class> elementChildren = ((ModelTree) selectedElement).getOwnedElement(Class.class);
                        //                    for(Class child: elementChildren) {
                        child.setOwner((ModelTree) root);
        
                        displacedChildren.add(child);
        
        
        
                    }
        
        
                    // Why it didnt work
        //                    try (FileWriter writer = new FileWriter("/home/kchaabouni/kais2/p07_cpswarm/debugging_out.txt", true);
        //                            BufferedWriter bw = new BufferedWriter(writer)) {
        //                        bw.write("displaced.size : " + displacedChildren.size()
        //
        //                                );
        //                        bw.newLine();
        //                    } catch (IOException e) {
        //                        System.err.format("IOException: %s%n", e);
        //                    }
                    //
        
        
                    diagramHandle.save();
                    diagramHandle.close();
                }
            }
        
            transaction.commit ();
        }
        
        
        
        try( ITransaction transaction = session.createTransaction(Messages.getString ("Info.Session.UpdateModel"))){
        
        
        
            List<AbstractDiagram> diagrams = ((Element) selectedElement).getDiagramElement(AbstractDiagram.class);
            for(AbstractDiagram diagram: diagrams) {
                try(  IDiagramHandle diagramHandle = diagramService.getDiagramHandle(diagram);){
        
                    for(Class displacedChild: displacedChildren) {
        
                        List<IDiagramGraphic> diagramGraphics = diagramHandle.getDiagramGraphics(selectedElement);
                        IDiagramGraphic diagramGraphic = diagramGraphics.get(0);
        
                        Rectangle elementBounds = ((IDiagramNode) diagramGraphic).getBounds();
        
        
                        // Unmask child 
                        List<IDiagramGraphic> graph = diagramHandle.unmask(displacedChild, elementBounds.x, elementBounds.y + AutoLayoutManager.VERTICAL_AUTOSPACING);
        
                        // Why it didnt work
        //                        try (FileWriter writer = new FileWriter("/home/kchaabouni/kais2/p07_cpswarm/debugging_out.txt", true);
        //                                BufferedWriter bw = new BufferedWriter(writer)) {
        //                            bw.write("graph != null: " + (graph != null)
        //                                    + ", graph.size() " + graph.size()
        //                                    + ", (graph.get(0) instanceof IDiagramNode)" 
        //                                    );
        //                            bw.newLine();
        //                        } catch (IOException e) {
        //                            System.err.format("IOException: %s%n", e);
        //                        }
                        //
        
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
        
                        // unmask related dependencies
                        List<Dependency> elementDependsOnDependencies = ((ModelElement) displacedChild).getDependsOnDependency();
                        for(Dependency dependency: elementDependsOnDependencies) {
                            diagramHandle.unmask(dependency, 0, 0);
                        }
                        List<Dependency> elementImpactedDependency = ((ModelElement) displacedChild).getImpactedDependency();
                        for(Dependency dependency: elementImpactedDependency) {
                            diagramHandle.unmask(dependency, 0, 0);
                        }
        
                        displacedChild.setOwner((ModelTree) selectedElement);
        
        
                        //        
                        //        
                        //                        List<IDiagramGraphic> graph = diagramHandle.unmask(child, elementBounds.x, elementBounds.y + AutoLayoutManager.VERTICAL_AUTOSPACING);
                        //        
                        //        
                        //        
                        //                        if((graph != null) &&  (graph.size() > 0) && (graph.get(0) instanceof IDiagramNode)) {
                        //                            IDiagramNode graphNode = (IDiagramNode)graph.get(0);
                        //                            graphNode.setProperty(Labels.CLASS_SHOWNAME.name(), DiagramElementStyle.OPERATOR.getShowNameProperty());
                        //                            graphNode.setProperty(Labels.CLASS_REPRES_MODE.name(), DiagramElementStyle.OPERATOR.getRepresentationMode());
                        //        
                        //        
                        //        
                        //                        }
                        //        
                        //                        // update owner
                        //                        child.setOwner((ModelTree) selectedElement);
                        //        
                        //                    }
        
        
        
        
        
        
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

}
