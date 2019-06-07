package org.modelio.module.attacktreedesigner.command.explorer;

import java.util.List;
import com.modeliosoft.modelio.javadesigner.annotations.objid;
import org.modelio.api.modelio.diagram.IDiagramGraphic;
import org.modelio.api.modelio.diagram.IDiagramHandle;
import org.modelio.api.modelio.diagram.IDiagramNode;
import org.modelio.api.modelio.diagram.IDiagramService;
import org.modelio.api.modelio.diagram.dg.IDiagramDG;
import org.modelio.api.modelio.diagram.style.IStyleHandle;
import org.modelio.api.modelio.model.IModelingSession;
import org.modelio.api.modelio.model.ITransaction;
import org.modelio.api.module.IModule;
import org.modelio.api.module.command.DefaultModuleCommandHandler;
import org.modelio.api.module.context.IModuleContext;
import org.modelio.metamodel.diagrams.ClassDiagram;
import org.modelio.metamodel.uml.infrastructure.ModelElement;
import org.modelio.metamodel.uml.infrastructure.Profile;
import org.modelio.metamodel.uml.infrastructure.Stereotype;
import org.modelio.metamodel.uml.statik.NameSpace;
import org.modelio.metamodel.uml.statik.Package;
import org.modelio.module.attacktreedesigner.api.AttackTreeStereotypes;
import org.modelio.module.attacktreedesigner.api.IAttackTreeDesignerPeerModule;
import org.modelio.module.attacktreedesigner.i18n.Messages;
import org.modelio.module.attacktreedesigner.impl.AttackTreeDesignerModule;
import org.modelio.module.attacktreedesigner.utils.DiagramElementBounds;
import org.modelio.vcore.smkernel.mapi.MClass;
import org.modelio.vcore.smkernel.mapi.MObject;

/**
 * @author ebrosse
 */
@objid ("1bdbb2e0-7f1a-46d7-ab20-04f90652f854")
public class AttackTreeDiagramCommand extends DefaultModuleCommandHandler {
    @objid ("289b34c8-b854-4b43-ba33-6c907c5e23b7")
    @Override
    public void actionPerformed(List<MObject> selectedElements, IModule module) {
        IModuleContext moduleContext = AttackTreeDesignerModule.getInstance().getModuleContext();
        IModelingSession session = moduleContext.getModelingSession();
        ModelElement owner = (ModelElement) selectedElements.get(0);    
        
        String name = Messages.getString ("Ui.Command.AttackTreeDiagramExplorerCommand.Name", owner.getName());
        
        try( ITransaction transaction = session.createTransaction(Messages.getString ("Info.Session.Create", "AttackTree Diagram"))){
        
            /*
             * Create default Root node in the Attack Tree
             */
            ModelElement rootElement = session.getModel().createClass(AttackTreeStereotypes.ROOT, (NameSpace) owner, IAttackTreeDesignerPeerModule.MODULE_NAME, AttackTreeStereotypes.ROOT);
        
        
            /*
             * Create attack tree diagram
             */
            MClass mclass = moduleContext.getModelioServices().getMetamodelService().getMetamodel().getMClass(ClassDiagram.class);
            Stereotype ster = session.getMetamodelExtensions().getStereotype(IAttackTreeDesignerPeerModule.MODULE_NAME, AttackTreeStereotypes.ATTACKTREEDIAGRAM, mclass);
            ClassDiagram diagram = session.getModel().createClassDiagram(name, rootElement, ster);
        
        
            /*
             * Unmask default Root node in the diagram
             */
        
            IDiagramService diagramService = moduleContext.getModelioServices().getDiagramService();
            try(  IDiagramHandle diagramHandle = diagramService.getDiagramHandle(diagram);){
        
                IDiagramDG dg = diagramHandle.getDiagramNode();
        
                for (IStyleHandle style : diagramService.listStyles()){
                    if (style.getName().equals("attacktree")){
                        dg.setStyle(style);
                        break;
                    }
                }
        
                List<IDiagramGraphic> diagramGraphics = diagramHandle.unmask(rootElement, 0, 0);
                for (IDiagramGraphic diagramGraphic : diagramGraphics) {
                    if(diagramGraphic.getElement().equals(rootElement)){
                        ((IDiagramNode) diagramGraphic).setBounds(DiagramElementBounds.ROOT.createRectangleBounds());
                    }
                }
                diagramHandle.save();
                diagramHandle.close();
            }
        
            moduleContext.getModelioServices().getEditionService().openEditor(diagram);
        
            transaction.commit ();
        }
    }

    @objid ("2375ec67-7c56-4d00-a193-52f2683549a1")
    @Override
    public boolean accept(List<MObject> selectedElements, IModule module) {
        if ((selectedElements != null) && (selectedElements.size() == 1)){
            MObject selectedElt = selectedElements.get(0);
            return ((selectedElt != null) &&
                    (((selectedElt instanceof Package) 
                            && !(selectedElt instanceof Profile)
                            && selectedElt.getStatus().isModifiable())));
        }
        return false;
    }

}
