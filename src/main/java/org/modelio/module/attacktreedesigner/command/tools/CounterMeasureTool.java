package org.modelio.module.attacktreedesigner.command.tools;

import com.modeliosoft.modelio.javadesigner.annotations.objid;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.draw2d.geometry.Rectangle;
import org.modelio.api.modelio.diagram.IDiagramGraphic;
import org.modelio.api.modelio.diagram.IDiagramHandle;
import org.modelio.api.modelio.diagram.IDiagramLink.LinkRouterKind;
import org.modelio.api.modelio.diagram.ILinkPath;
import org.modelio.api.modelio.diagram.tools.DefaultAttachedBoxTool;
import org.modelio.api.modelio.model.IModelingSession;
import org.modelio.api.modelio.model.ITransaction;
import org.modelio.api.modelio.model.IUmlModel;
import org.modelio.metamodel.uml.infrastructure.ModelElement;
import org.modelio.metamodel.uml.infrastructure.Note;
import org.modelio.metamodel.uml.statik.Class;
import org.modelio.module.attacktreedesigner.api.AttackTreeNoteTypes;
import org.modelio.module.attacktreedesigner.api.AttackTreeStereotypes;
import org.modelio.module.attacktreedesigner.api.IAttackTreeDesignerPeerModule;
import org.modelio.module.attacktreedesigner.i18n.Messages;
import org.modelio.module.attacktreedesigner.impl.AttackTreeDesignerModule;
import org.modelio.vcore.smkernel.mapi.MObject;

@objid ("44e9eff9-fdf9-424a-8b26-2eee8557bb47")
public class CounterMeasureTool extends DefaultAttachedBoxTool {
    @objid ("bbeee311-cb98-4a26-a18c-9ada8cd12713")
    @Override
    public boolean acceptElement(IDiagramHandle diagramHandle, IDiagramGraphic targetNode) {
        MObject targetElement = targetNode.getElement();
        return (targetElement instanceof Class 
                && ((Class) targetElement).isStereotyped(IAttackTreeDesignerPeerModule.MODULE_NAME, AttackTreeStereotypes.ATTACK)
                && targetElement.getStatus().isModifiable ()
                );
    }

    @objid ("c2df77ce-c32a-4e4c-8db8-0e4d3d849ae8")
    @Override
    public void actionPerformed(IDiagramHandle diagramHandle, IDiagramGraphic originNode, LinkRouterKind routerType, ILinkPath path, Point point) {
        IModelingSession session = AttackTreeDesignerModule.getInstance().getModuleContext().getModelingSession();
        IUmlModel model = session.getModel();                
        try( ITransaction transaction = session.createTransaction (Messages.getString("Info.Session.Create", "Counter Measure"))){
            ModelElement owner = (ModelElement) originNode.getElement();
            
            Note note = model.createNote(IAttackTreeDesignerPeerModule.MODULE_NAME,AttackTreeNoteTypes.COUNTER_MEASURE, owner, AttackTreeNoteTypes.COUNTER_MEASURE);   
        
            diagramHandle.unmask(note, point.x, point.y );
            
            diagramHandle.save();
            diagramHandle.close();
            transaction.commit();
        }
    }

    @objid ("b8da66a5-09f8-40f9-8a6f-5e654955d597")
    @Override
    public void actionPerformedInDiagram(final IDiagramHandle diagramHandle, final Rectangle rect) {
    }

}
