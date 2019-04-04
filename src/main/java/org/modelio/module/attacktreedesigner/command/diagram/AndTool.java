package org.modelio.module.attacktreedesigner.command.diagram;

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
import org.modelio.vcore.smkernel.mapi.MObject;

/**
 * @author ebrosse
 */
@objid ("a97178e2-7d56-4360-a2d9-f9714bbebb85")
public class AndTool extends DefaultBoxTool {
    /**
     * Default constructor
     */
    @objid ("66507bc0-cd61-440c-8c3c-2ca28ef794d4")
    public AndTool() {
        super();
    }

    @objid ("1ac6010f-9cf6-4ec5-8c77-0e2897ef2ab5")
    @Override
    public boolean acceptElement(IDiagramHandle representation, IDiagramGraphic target) {
        MObject element = target.getElement();
        
        if (element instanceof AbstractDiagram) {
            element = ((AbstractDiagram) element).getOrigin();        
        }
        return (element instanceof org.modelio.metamodel.uml.statik.Package);
    }

    @objid ("a03883b0-3bda-44f0-ae5f-de5d4a8cb99e")
    @Override
    public void actionPerformed(IDiagramHandle representation, IDiagramGraphic target, Rectangle rect) {
        IModelingSession session = AttackTreeDesignerModule.getInstance().getModuleContext().getModelingSession();
        try( ITransaction transaction = session.createTransaction (Messages.getString ("Info.Session.Create", "And"))){
            MObject owner = target.getElement();
        
            if (owner instanceof AbstractDiagram) {
                owner = ((AbstractDiagram) owner).getOrigin();
            }
        
            Element event = session.getModel().createClass("And", (NameSpace) owner, IAttackTreeDesignerPeerModule.MODULE_NAME, AttackTreeStereotypes.AND);
            List<IDiagramGraphic> graph = representation.unmask(event, rect.x, rect.y);
        
            if((graph != null) &&  (graph.size() > 0) && (graph.get(0) instanceof IDiagramNode))
                ((IDiagramNode)graph.get(0)).setBounds(rect);
            
            representation.save();
            representation.close();
            transaction.commit ();
        }
    }

}
