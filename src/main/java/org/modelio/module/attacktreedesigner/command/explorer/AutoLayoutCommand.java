package org.modelio.module.attacktreedesigner.command.explorer;

import java.util.List;
import com.modeliosoft.modelio.javadesigner.annotations.objid;
import org.modelio.api.modelio.diagram.IDiagramGraphic;
import org.modelio.api.modelio.diagram.IDiagramHandle;
import org.modelio.api.modelio.diagram.IDiagramNode;
import org.modelio.api.modelio.diagram.IDiagramService;
import org.modelio.api.modelio.model.IModelingSession;
import org.modelio.api.modelio.model.ITransaction;
import org.modelio.api.module.IModule;
import org.modelio.api.module.command.DefaultModuleCommandHandler;
import org.modelio.api.module.context.IModuleContext;
import org.modelio.metamodel.diagrams.ClassDiagram;
import org.modelio.module.attacktreedesigner.api.AttackTreeStereotypes;
import org.modelio.module.attacktreedesigner.api.IAttackTreeDesignerPeerModule;
import org.modelio.module.attacktreedesigner.i18n.Messages;
import org.modelio.module.attacktreedesigner.impl.AttackTreeDesignerModule;
import org.modelio.module.attacktreedesigner.utils.AutoLayoutManager;
import org.modelio.vcore.smkernel.mapi.MObject;

@objid ("2b416d6a-09f7-4f10-a756-f1bd7f901d69")
public class AutoLayoutCommand extends DefaultModuleCommandHandler {
    @objid ("9af6b559-1381-47af-a5a4-76899a689b2c")
    @Override
    public void actionPerformed(List<MObject> selectedElements, IModule module) {
        IModuleContext moduleContext = AttackTreeDesignerModule.getInstance().getModuleContext();
        IModelingSession session = moduleContext.getModelingSession();
        
        ClassDiagram selectedDiagram = (ClassDiagram) selectedElements.get(0);
        //ModelTree haha = ((ModelTree)selectedDiagram).getOwner();
        MObject rootElement = selectedDiagram.getCompositionOwner();
        
        
        IDiagramService diagramService = moduleContext.getModelioServices().getDiagramService();
        
        try( ITransaction transaction = session.createTransaction(Messages.getString ("Info.Session.UpdateModel"))){
        
        
        
            try(  IDiagramHandle diagramHandle = diagramService.getDiagramHandle(selectedDiagram);){
        
                List<IDiagramGraphic> diagramGraphics = diagramHandle.getDiagramGraphics(rootElement);
        
                if(! diagramGraphics.isEmpty()) {
        
                    IDiagramNode nodeGraphic = (IDiagramNode) diagramGraphics.get(0);
                    AutoLayoutManager.autolayoutChildren(diagramHandle, rootElement, nodeGraphic, nodeGraphic.getBounds().getCopy());
                    
                    diagramHandle.save();
                    diagramHandle.close();
        
                }
            }
        
            transaction.commit ();
        }
    }

    @objid ("deeca976-dc65-45b7-8107-98d420f3b196")
    @Override
    public boolean accept(final List<MObject> selectedElements, final IModule module) {
        if ((selectedElements != null) && (selectedElements.size() == 1)){
            MObject selectedElement = selectedElements.get(0);
            return ((selectedElement != null) 
                    && (selectedElement instanceof ClassDiagram)
                    && ((ClassDiagram) selectedElement).isStereotyped(IAttackTreeDesignerPeerModule.MODULE_NAME, AttackTreeStereotypes.ATTACK_TREE_DIAGRAM)
                    && selectedElement.getStatus().isModifiable());
        }
        return false;
    }

}
