package org.modelio.module.attacktreedesigner.command.tools;

import java.util.List;
import com.modeliosoft.modelio.javadesigner.annotations.objid;
import org.eclipse.draw2d.geometry.Rectangle;
import org.modelio.api.modelio.diagram.IDiagramGraphic;
import org.modelio.api.modelio.diagram.IDiagramHandle;
import org.modelio.api.modelio.diagram.IDiagramLink.LinkRouterKind;
import org.modelio.api.modelio.diagram.ILinkPath;
import org.modelio.api.modelio.diagram.tools.DefaultMultiLinkTool;
import org.modelio.metamodel.diagrams.AbstractDiagram;
import org.modelio.metamodel.uml.statik.Class;
import org.modelio.module.attacktreedesigner.api.AttackTreeStereotypes;
import org.modelio.module.attacktreedesigner.api.IAttackTreeDesignerPeerModule;
import org.modelio.vcore.smkernel.mapi.MObject;

@objid ("cb97a70e-ae73-4525-b1dd-eb1583ad95e0")
public class AndMultiLinkTool extends DefaultMultiLinkTool {
    @objid ("c64e00aa-e402-4343-9189-01f63424add3")
    @Override
    public boolean acceptFirstElement(IDiagramHandle diagramHandle, IDiagramGraphic targetNode) {
        MObject element = targetNode.getElement();
        return (element instanceof Class 
                && (((Class) element).isStereotyped(IAttackTreeDesignerPeerModule.MODULE_NAME, AttackTreeStereotypes.ROOT))) ;
    }

    @objid ("a3f48339-9019-414d-930e-1f89bf204f13")
    @Override
    public boolean acceptAdditionalElement(IDiagramHandle arg0, List<IDiagramGraphic> previousNodes, IDiagramGraphic targetNode) {
        MObject element = targetNode.getElement();
        return (element instanceof Class 
                && (((Class) element).isStereotyped(IAttackTreeDesignerPeerModule.MODULE_NAME, AttackTreeStereotypes.NODE))
                && !((Class) element).isStereotyped(IAttackTreeDesignerPeerModule.MODULE_NAME, AttackTreeStereotypes.ROOT) ) ;
    }

    @objid ("70ff79f3-7b2a-4c7b-8000-94fc0c9f8063")
    @Override
    public boolean acceptLastElement(IDiagramHandle arg0, List<IDiagramGraphic> otherNodes, IDiagramGraphic targetNode) {
        MObject element = targetNode.getElement();
        return (element instanceof AbstractDiagram);
    }

    @objid ("b4184827-33fa-4f24-ba1b-594286f23e35")
    @Override
    public void actionPerformed(IDiagramHandle diagramHandle, IDiagramGraphic lastNode, List<IDiagramGraphic> otherNodes, List<LinkRouterKind> routerKinds, List<ILinkPath> paths, Rectangle rectangle) {
        //        IModelingSession session = AttackTreeDesignerModule.getInstance().getModuleContext().getModelingSession();
        //        try( ITransaction transaction = session.createTransaction (Messages.getString ("Info.Session.Create", AttackTreeStereotypes.AND))){
        //        
        //            ModelTree parentElement = (ModelTree) otherNodes.get(0).getElement();
        //            MObject rootElement = diagramHandle.getDiagram().getOrigin().getCompositionOwner();
        //            //ModelElement lastElement = (ModelElement) lastNode.getElement();
        //        
        //        
        //            /*
        //             * Create and unmask AND Element
        //             */
        //            Class andElement = session.getModel().createClass(
        //                    PropertyLabel.DEFAULT_NAME.toString(), 
        //                    (NameSpace) rootElement, 
        //                    IAttackTreeDesignerPeerModule.MODULE_NAME, 
        //                    AttackTreeStereotypes.AND);
        //            List<IDiagramGraphic> graph = diagramHandle.unmask(andElement, rectangle.x, rectangle.y);
        //        //            if((graph != null) &&  (graph.size() > 0) && (graph.get(0) instanceof IDiagramNode)) {
        //        //                IDiagramNode graphNode = (IDiagramNode)graph.get(0);
        //        //                graphNode.setProperty(PropertyLabel.CLASS_SHOWNAME.name(), DiagramElementStyle.OPERATOR.getShowNameProperty());
        //        //                graphNode.setProperty(PropertyLabel.CLASS_REPRES_MODE.name(), DiagramElementStyle.OPERATOR.getRepresentationMode());
        //        //            }
        //        
        //            /*
        //             * Create Connection from first element to AND element
        //             */
        //            //            ModelElement connectionElement = session.getModel().createDependency(firstElement, andElement, IAttackTreeDesignerPeerModule.MODULE_NAME, AttackTreeStereotypes.CONNECTION); 
        //        
        //        
        //        
        //            diagramHandle.save();
        //            diagramHandle.close();
        //        
        //            andElement.setOwner(parentElement);
        //        
        //            session.getModel().getDefaultNameService()
        //            .setDefaultName(andElement, AttackTreeStereotypes.AND);
        //        
        //            transaction.commit ();
        //        }
    }

}
