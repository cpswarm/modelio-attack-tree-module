package org.modelio.module.attacktreedesigner.command.tools;

import java.util.List;
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
import org.modelio.module.attacktreedesigner.utils.DiagramElementStyle;
import org.modelio.vcore.smkernel.mapi.MObject;

public class ElementCreationManager {

    public static void createOperatorElement(IDiagramHandle diagramHandle, 
            List<IDiagramGraphic> otherNodes, 
            Rectangle rectangle, 
            IModelingSession session,
            String stereotypeName) {
        ModelTree parentElement = (ModelTree) otherNodes.get(0).getElement();
        MObject rootElement = diagramHandle.getDiagram().getOrigin().getCompositionOwner();       
      
        /*
         * Create and unmask AND Element
         */
        Class andElement = session.getModel().createClass(
                PropertyLabel.DEFAULT_NAME.toString(), 
                (NameSpace) rootElement, 
                IAttackTreeDesignerPeerModule.MODULE_NAME, 
                stereotypeName);
        List<IDiagramGraphic> graph = diagramHandle.unmask(andElement, rectangle.x, rectangle.y);
        if((graph != null) &&  (graph.size() > 0) && (graph.get(0) instanceof IDiagramNode)) {
            IDiagramNode graphNode = (IDiagramNode)graph.get(0);
            graphNode.setProperty(PropertyLabel.CLASS_SHOWNAME.name(), DiagramElementStyle.OPERATOR.getShowNameProperty());
            graphNode.setProperty(PropertyLabel.CLASS_REPRES_MODE.name(), DiagramElementStyle.OPERATOR.getRepresentationMode());
        }
        andElement.setOwner(parentElement);
      
      
        /*
         * Create Connections
         */
        ModelElement connectionFirstElement = session.getModel().createDependency(parentElement, 
                andElement, 
                IAttackTreeDesignerPeerModule.MODULE_NAME, 
                AttackTreeStereotypes.CONNECTION); 
        diagramHandle.unmask(connectionFirstElement, 0, 0);
      
      
        int nodesSize = otherNodes.size();
        for (int i = 1; i < nodesSize; i++) {
            MObject element = otherNodes.get(i).getElement();
            ((ModelTree)element).setOwner(andElement);
            ModelElement connection = session.getModel().createDependency(andElement, 
                    (ModelElement) element, 
                    IAttackTreeDesignerPeerModule.MODULE_NAME, 
                    AttackTreeStereotypes.CONNECTION);
            diagramHandle.unmask(connection, 0, 0);
            andElement.setOwner(parentElement);
        }
      
      
        diagramHandle.save();
        diagramHandle.close();
      
      
        session.getModel().getDefaultNameService()
        .setDefaultName(andElement, stereotypeName);
    }
}
