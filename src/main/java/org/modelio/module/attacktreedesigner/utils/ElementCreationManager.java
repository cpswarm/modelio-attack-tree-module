package org.modelio.module.attacktreedesigner.utils;

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
import org.modelio.module.attacktreedesigner.property.PropertyLabel;
import org.modelio.vcore.smkernel.mapi.MObject;

@objid ("ceef5962-40ca-4125-a5ed-cac05d971ab5")
public class ElementCreationManager {
    @objid ("576a3c4c-44be-4a80-880f-6be3f8f202ce")
    public static void createOperatorElement(IDiagramHandle diagramHandle, List<IDiagramGraphic> otherNodes, Rectangle rectangle, IModelingSession session, String stereotypeName) {
        ModelTree parentElement = (ModelTree) otherNodes.get(0).getElement();
        MObject rootElement = diagramHandle.getDiagram().getOrigin().getCompositionOwner();       
              
        /*
         * Create and unmask AND Element
         */
        Class operatorElement = session.getModel().createClass(
                PropertyLabel.DEFAULT_NAME.toString(), 
                (NameSpace) rootElement, 
                IAttackTreeDesignerPeerModule.MODULE_NAME, 
                stereotypeName);
        List<IDiagramGraphic> graph = diagramHandle.unmask(operatorElement, rectangle.x, rectangle.y);
        if((graph != null) &&  (graph.size() > 0) && (graph.get(0) instanceof IDiagramNode)) {
            IDiagramNode graphNode = (IDiagramNode)graph.get(0);
            graphNode.setProperty(PropertyLabel.CLASS_SHOWNAME.name(), DiagramElementStyle.OPERATOR.getShowNameProperty());
            graphNode.setProperty(PropertyLabel.CLASS_REPRES_MODE.name(), DiagramElementStyle.OPERATOR.getRepresentationMode());
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

    @objid ("a8b0f785-5203-4ca1-91f4-50285a0ed077")
    public static void renameElement(IModelingSession session, ModelTree targetElement) {
        String targetElementName = targetElement.getName();
        targetElement.setName(PropertyLabel.DEFAULT_NAME.toString());
        session.getModel().getDefaultNameService().setDefaultName(targetElement, targetElementName);
    }

}
