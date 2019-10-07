package org.modelio.module.attacktreedesigner.command.tools;

import com.modeliosoft.modelio.javadesigner.annotations.objid;
import org.eclipse.draw2d.geometry.Rectangle;
import org.modelio.api.modelio.diagram.IDiagramGraphic;
import org.modelio.api.modelio.diagram.IDiagramHandle;
import org.modelio.api.modelio.diagram.tools.DefaultBoxTool;
import org.modelio.api.modelio.model.IModelingSession;
import org.modelio.api.modelio.model.ITransaction;
import org.modelio.metamodel.diagrams.AbstractDiagram;
import org.modelio.metamodel.uml.infrastructure.ModelTree;
import org.modelio.metamodel.uml.statik.Class;
import org.modelio.metamodel.uml.statik.NameSpace;
import org.modelio.module.attacktreedesigner.api.AttackTreeStereotypes;
import org.modelio.module.attacktreedesigner.api.IAttackTreeDesignerPeerModule;
import org.modelio.module.attacktreedesigner.i18n.Messages;
import org.modelio.module.attacktreedesigner.impl.AttackTreeDesignerModule;
import org.modelio.module.attacktreedesigner.utils.elementmanager.Labels;
import org.modelio.module.attacktreedesigner.utils.elementmanager.tags.TagsManager;
import org.modelio.vcore.smkernel.mapi.MObject;

@objid ("65f215dc-552b-42fb-b461-cbc086de074b")
public class CreateAttackTool extends DefaultBoxTool {
    @objid ("8736ada6-b08a-4677-80dc-01e18e615607")
    @Override
    public boolean acceptElement(IDiagramHandle diagramHandle, IDiagramGraphic targetNode) {
        MObject targetNodeElement = targetNode.getElement();
        return (targetNodeElement instanceof AbstractDiagram) && 
                ((Class)((AbstractDiagram) targetNodeElement).getOrigin())
                .isStereotyped(IAttackTreeDesignerPeerModule.MODULE_NAME, AttackTreeStereotypes.ROOT);
    }

    @objid ("5ce94569-dd8e-49a1-88c9-8a8e79488a83")
    @Override
    public void actionPerformed(IDiagramHandle diagramHandle, IDiagramGraphic targetDiagramGraphic, Rectangle rect) {
        IModelingSession session = AttackTreeDesignerModule.getInstance().getModuleContext().getModelingSession();
        try( ITransaction transaction = session.createTransaction (Messages.getString ("Info.Session.Create", AttackTreeStereotypes.ATTACK))){
        
            MObject rootElement = diagramHandle.getDiagram().getOrigin().getCompositionOwner();
        
            // create stereotyped Attack Class
            Class attackElement = session.getModel().createClass(
                    Labels.DEFAULT_NAME.toString(), 
                    (NameSpace) rootElement, 
                    IAttackTreeDesignerPeerModule.MODULE_NAME, 
                    AttackTreeStereotypes.ATTACK);
            
            // create Default tags
            TagsManager.createAttackDefaultTags(session, attackElement);
            
            // unmask Attack and save diagram
            diagramHandle.unmask(attackElement, rect.x, rect.y);            
            
            diagramHandle.save();
            diagramHandle.close();
            
            attackElement.setOwner((ModelTree) targetDiagramGraphic.getElement().getCompositionOwner());
            
            session.getModel().getDefaultNameService()
            .setDefaultName(attackElement, AttackTreeStereotypes.ATTACK); 
        
            transaction.commit ();
        }
    }

}
