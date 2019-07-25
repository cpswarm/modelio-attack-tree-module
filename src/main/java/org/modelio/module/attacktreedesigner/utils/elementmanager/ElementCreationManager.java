package org.modelio.module.attacktreedesigner.utils.elementmanager;

import java.util.List;
import com.modeliosoft.modelio.javadesigner.annotations.objid;
import org.eclipse.draw2d.geometry.Rectangle;
import org.modelio.api.modelio.diagram.IDiagramGraphic;
import org.modelio.api.modelio.diagram.IDiagramHandle;
import org.modelio.api.modelio.diagram.IDiagramNode;
import org.modelio.api.modelio.model.IModelingSession;
import org.modelio.metamodel.uml.infrastructure.ModelElement;
import org.modelio.metamodel.uml.infrastructure.ModelTree;
import org.modelio.metamodel.uml.statik.Class;
import org.modelio.metamodel.uml.statik.NameSpace;
import org.modelio.module.attacktreedesigner.api.AttackTreeStereotypes;
import org.modelio.module.attacktreedesigner.api.IAttackTreeDesignerPeerModule;
import org.modelio.module.attacktreedesigner.utils.DiagramElementStyle;
import org.modelio.module.attacktreedesigner.utils.Labels;
import org.modelio.vcore.smkernel.mapi.MObject;

@objid ("9ff6243a-c266-42ed-8c3a-1fc351ac1cf9")
public class ElementCreationManager {
    @objid ("278a8f10-6f30-47a3-8eb2-a7b6e7b10570")
    public static void createOperatorElement(IDiagramHandle diagramHandle, List<IDiagramGraphic> otherNodes, Rectangle rectangle, IModelingSession session, String stereotypeName) {
        ModelTree parentElement = (ModelTree) otherNodes.get(0).getElement();
        MObject rootElement = diagramHandle.getDiagram().getOrigin().getCompositionOwner();       
              
        /*
         * Create and unmask OPERATOR Element
         */
        Class operatorElement = session.getModel().createClass(
                Labels.DEFAULT_NAME.toString(), 
                (NameSpace) rootElement, 
                IAttackTreeDesignerPeerModule.MODULE_NAME, 
                stereotypeName);
        List<IDiagramGraphic> graph = diagramHandle.unmask(operatorElement, rectangle.x, rectangle.y);
        if((graph != null) &&  (graph.size() > 0) && (graph.get(0) instanceof IDiagramNode)) {
            IDiagramNode graphNode = (IDiagramNode)graph.get(0);
            graphNode.setProperty(Labels.CLASS_SHOWNAME.name(), DiagramElementStyle.OPERATOR.getShowNameProperty());
            graphNode.setProperty(Labels.CLASS_REPRES_MODE.name(), DiagramElementStyle.OPERATOR.getRepresentationMode());
        }
        operatorElement.setOwner(parentElement);
              
              
        /*
         * Create Connections
         */
        ModelElement connectionFirstElement = session.getModel().createDependency(parentElement, 
                operatorElement, 
                IAttackTreeDesignerPeerModule.MODULE_NAME, 
                AttackTreeStereotypes.CONNECTION); 
        diagramHandle.unmask(connectionFirstElement, 0, 0);
              
              
        int nodesSize = otherNodes.size();
        for (int i = 1; i < nodesSize; i++) {
            MObject element = otherNodes.get(i).getElement();
            ((ModelTree)element).setOwner(operatorElement);
            ModelElement connection = session.getModel().createDependency(operatorElement, 
                    (ModelElement) element, 
                    IAttackTreeDesignerPeerModule.MODULE_NAME, 
                    AttackTreeStereotypes.CONNECTION);
            
            ElementCreationManager.renameElement(session, (ModelTree) element); 
            
            diagramHandle.unmask(connection, 0, 0);
        }
              
              
        diagramHandle.save();
        diagramHandle.close();
              
        session.getModel().getDefaultNameService()
        .setDefaultName(operatorElement, stereotypeName);
    }

    @objid ("37d184f1-c6b7-4e95-bfc3-541242c78ad8")
    public static void renameElement(IModelingSession session, ModelTree targetElement) {
        String targetElementName = targetElement.getName();
        targetElement.setName(Labels.DEFAULT_NAME.toString());
        session.getModel().getDefaultNameService().setDefaultName(targetElement, targetElementName);
    }

}
