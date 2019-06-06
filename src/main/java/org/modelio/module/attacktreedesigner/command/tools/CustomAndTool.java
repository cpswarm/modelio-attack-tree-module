package org.modelio.module.attacktreedesigner.command.tools;

import java.util.List;
import com.modeliosoft.modelio.javadesigner.annotations.objid;
import org.eclipse.draw2d.geometry.Rectangle;
import org.modelio.api.modelio.diagram.IDiagramGraphic;
import org.modelio.api.modelio.diagram.IDiagramHandle;
import org.modelio.api.modelio.diagram.IDiagramNode;
import org.modelio.api.modelio.diagram.tools.DefaultBoxTool;
import org.modelio.api.modelio.model.IModelingSession;
import org.modelio.api.modelio.model.ITransaction;
import org.modelio.metamodel.diagrams.AbstractDiagram;
import org.modelio.metamodel.uml.infrastructure.Element;
import org.modelio.metamodel.uml.statik.NameSpace;
import org.modelio.module.attacktreedesigner.api.AttackTreeStereotypes;
import org.modelio.module.attacktreedesigner.api.IAttackTreeDesignerPeerModule;
import org.modelio.module.attacktreedesigner.i18n.Messages;
import org.modelio.module.attacktreedesigner.impl.AttackTreeDesignerModule;
import org.modelio.module.attacktreedesigner.property.PropertyLabel;
import org.modelio.module.attacktreedesigner.utils.DiagramElementStyle;
import org.modelio.module.attacktreedesigner.utils.IAttackTreeCustomizerPredefinedField;
import org.modelio.vcore.smkernel.mapi.MObject;

@objid ("38603a9c-09b3-43a7-ae76-c6e926013433")
public class CustomAndTool extends DefaultBoxTool {
    @objid ("40a27424-7431-4578-8620-dd096b2c2c64")
    @Override
    public boolean acceptElement(final IDiagramHandle diagramHandle, final IDiagramGraphic targetNode) {
        MObject element = targetNode.getElement();
        
        if (element instanceof AbstractDiagram) {
            element = ((AbstractDiagram) element).getOrigin();        
        }
        return (element instanceof org.modelio.metamodel.uml.statik.Package);
    }

    @objid ("5e00ade5-293e-4352-a399-d4a91ae4884f")
    @Override
    public void actionPerformed(final IDiagramHandle diagramHandle, final IDiagramGraphic parent, final Rectangle rect) {
        IModelingSession session = AttackTreeDesignerModule.getInstance().getModuleContext().getModelingSession();
        try( ITransaction transaction = session.createTransaction (Messages.getString ("Info.Session.Create", IAttackTreeCustomizerPredefinedField.AND))){
            MObject owner = parent.getElement();
        
            if (owner instanceof AbstractDiagram) {
                owner = ((AbstractDiagram) owner).getOrigin();
            }
        
            Element andElement = session.getModel().createClass(IAttackTreeCustomizerPredefinedField.AND, (NameSpace) owner, IAttackTreeDesignerPeerModule.MODULE_NAME, AttackTreeStereotypes.AND);
            List<IDiagramGraphic> graph = diagramHandle.unmask(andElement, rect.x, rect.y);
        
            if((graph != null) &&  (graph.size() > 0) && (graph.get(0) instanceof IDiagramNode)) {
                IDiagramNode graphNode = (IDiagramNode)graph.get(0);
                graphNode.setProperty(PropertyLabel.CLASS_SHOWNAME.name(), DiagramElementStyle.OPERATOR.getShowNameProperty());
                graphNode.setProperty(PropertyLabel.CLASS_REPRES_MODE.name(), DiagramElementStyle.OPERATOR.getRepresentationMode());
            }
            
            diagramHandle.save();
            diagramHandle.close();
            transaction.commit ();
        }
    }

}
