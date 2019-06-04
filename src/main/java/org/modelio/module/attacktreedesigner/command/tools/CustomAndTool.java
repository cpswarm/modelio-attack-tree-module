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
import org.modelio.module.attacktreedesigner.utils.IAttackTreeCustomizerPredefinedField;
import org.modelio.vcore.smkernel.mapi.MObject;

@objid ("b750192e-2db9-49d6-bde8-958b48be5ce7")
public class CustomAndTool extends DefaultBoxTool {
    @objid ("ee4fed66-7af5-4237-9a8d-c872b21617e8")
    @Override
    public void actionPerformed(IDiagramHandle diagramHandle, IDiagramGraphic diagramGraphic, Rectangle rectangle) {
        IModelingSession session = AttackTreeDesignerModule.getInstance().getModuleContext().getModelingSession();
        try( ITransaction transaction = session.createTransaction (Messages.getString ("Info.Session.Create", IAttackTreeCustomizerPredefinedField.AND))){
            MObject owner = diagramGraphic.getElement();
        
            if (owner instanceof AbstractDiagram) {
                owner = ((AbstractDiagram) owner).getOrigin();
            }
        
            Element event = session.getModel().createClass(IAttackTreeCustomizerPredefinedField.AND, (NameSpace) owner, IAttackTreeDesignerPeerModule.MODULE_NAME, AttackTreeStereotypes.AND);
            List<IDiagramGraphic> graph = diagramHandle.unmask(event, rectangle.x, rectangle.y);
        
            if((graph != null) &&  (graph.size() > 0) && (graph.get(0) instanceof IDiagramNode))
                ((IDiagramNode)graph.get(0)).setBounds(rectangle);
        
            /*
             * Style A faire
             */
            
            diagramHandle.save();
            diagramHandle.close();
            transaction.commit ();
        }
    }

    @objid ("370e5af0-8616-4816-91b2-b3fb23556a60")
    @Override
    public boolean acceptElement(IDiagramHandle diagramHandle, IDiagramGraphic diagramGraphic) {
        MObject element = diagramGraphic.getElement();
        
        if (element instanceof AbstractDiagram) {
            element = ((AbstractDiagram) element).getOrigin();        
        }
        return (element instanceof org.modelio.metamodel.uml.statik.Package);
    }

}
