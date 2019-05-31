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
@objid ("ba17069c-5478-4943-a589-c4e0a0dfd782")
public class EventTool extends DefaultBoxTool {
    /**
     * Default constructor
     */
    @objid ("494ecd4b-9b77-4a7a-a873-29b24a87aed7")
    public EventTool() {
        super();
    }

    @objid ("35340112-8f86-4ef9-8209-fe1dca397b72")
    @Override
    public boolean acceptElement(IDiagramHandle representation, IDiagramGraphic target) {
        MObject element = target.getElement();
        
        if (element instanceof AbstractDiagram) {
            element = ((AbstractDiagram) element).getOrigin();        
        }
        return (element instanceof org.modelio.metamodel.uml.statik.Package);
    }

    @objid ("40416ff9-78da-46bb-88cc-de8ccb82bfe9")
    @Override
    public void actionPerformed(IDiagramHandle representation, IDiagramGraphic target, Rectangle rect) {
        IModelingSession session = AttackTreeDesignerModule.getInstance().getModuleContext().getModelingSession();
        try( ITransaction transaction = session.createTransaction (Messages.getString ("Info.Session.Create", "Event"))){
            MObject owner = target.getElement();
        
            if (owner instanceof AbstractDiagram) {
                owner = ((AbstractDiagram) owner).getOrigin();
            }
        
            Element event = session.getModel().createClass("Event", (NameSpace) owner, IAttackTreeDesignerPeerModule.MODULE_NAME, AttackTreeStereotypes.EVENT);
            List<IDiagramGraphic> graph = representation.unmask(event, rect.x, rect.y);
        
            if((graph != null) &&  (graph.size() > 0) && (graph.get(0) instanceof IDiagramNode))
                ((IDiagramNode)graph.get(0)).setBounds(rect);
            
            representation.save();
            representation.close();
            transaction.commit ();
        }
    }

}
