package org.modelio.module.attacktreedesigner.command.wizard;

import com.modeliosoft.modelio.javadesigner.annotations.objid;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Display;
import org.modelio.api.modelio.diagram.IDiagramHandle;
import org.modelio.api.modelio.diagram.IDiagramService;
import org.modelio.api.modelio.diagram.dg.IDiagramDG;
import org.modelio.api.modelio.diagram.style.IStyleHandle;
import org.modelio.api.modelio.model.IMetamodelExtensions;
import org.modelio.api.modelio.model.IModelingSession;
import org.modelio.api.modelio.model.ITransaction;
import org.modelio.api.modelio.model.IUmlModel;
import org.modelio.api.module.context.IModuleContext;
import org.modelio.api.module.contributor.AbstractWizardContributor;
import org.modelio.api.module.contributor.ElementDescriptor;
import org.modelio.api.module.contributor.diagramcreation.IDiagramWizardContributor;
import org.modelio.metamodel.diagrams.AbstractDiagram;
import org.modelio.metamodel.diagrams.StaticDiagram;
import org.modelio.metamodel.mmextensions.infrastructure.ExtensionNotFoundException;
import org.modelio.metamodel.uml.infrastructure.ModelElement;
import org.modelio.metamodel.uml.infrastructure.Profile;
import org.modelio.metamodel.uml.infrastructure.Stereotype;
import org.modelio.metamodel.uml.statik.Package;
import org.modelio.module.attacktreedesigner.api.AttackTreeStereotypes;
import org.modelio.module.attacktreedesigner.api.IAttackTreeDesignerPeerModule;
import org.modelio.module.attacktreedesigner.i18n.Messages;
import org.modelio.module.attacktreedesigner.impl.AttackTreeDesignerModule;
import org.modelio.module.attacktreedesigner.utils.AttackTreeResourcesManager;
import org.modelio.vcore.smkernel.mapi.MClass;
import org.modelio.vcore.smkernel.mapi.MMetamodel;
import org.modelio.vcore.smkernel.mapi.MObject;

/**
 * @author ebrosse
 */
@objid ("ab8184c4-c658-4aa4-9af4-bb754d6478ea")
public class AttackTreeDiagramWizard extends AbstractWizardContributor implements IDiagramWizardContributor {
    @objid ("ee93155e-9134-488d-8ba6-30ca1da31d92")
    @Override
    public AbstractDiagram actionPerformed(ModelElement owner, String diagramName, String description) {
        IModuleContext moduleContext = AttackTreeDesignerModule.getInstance().getModuleContext();
        IModelingSession session = moduleContext.getModelingSession();
        String name = Messages.getString ("Ui.Command.AttackTreeDiagramExplorerCommand.Label");
        StaticDiagram diagram = null;
        
        try( ITransaction transaction = session.createTransaction(Messages.getString ("Info.Session.Create", "AttackTree Diagram"))){
          
            diagram = session.getModel().createStaticDiagram(name, owner, IAttackTreeDesignerPeerModule.MODULE_NAME, AttackTreeStereotypes.ATTACK_TREE_DIAGRAM);
            
            IUmlModel model = session.getModel();                   
            
        //            model.createNote(IModelerModulePeerModule.MODULE_NAME, IModelerModuleNoteTypes.MODELELEMENT_DESCRIPTION, diagram, description);
        
            if (diagram != null) {
                IDiagramService ds = moduleContext.getModelioServices().getDiagramService();
                try(  IDiagramHandle handler = ds.getDiagramHandle(diagram);){
                    IDiagramDG dg = handler.getDiagramNode();
        
                    for (IStyleHandle style : ds.listStyles()){
                        if (style.getName().equals("sysml")){
                            dg.setStyle(style);
                            break;
                        }
                    }
        
                    handler.save();
                    handler.close();
                }
        
                moduleContext.getModelioServices().getEditionService().openEditor(diagram);
            }
        
            transaction.commit ();
        } catch (ExtensionNotFoundException e) {
        //            AttackTreeDesignerModule.logService.error(e);
        }
        return diagram;
    }

    @objid ("a83b5cc0-0afa-4813-8822-e77c4a477056")
    @Override
    public String getLabel() {
        return Messages.getString ("Ui.Command.AttackTreeDiagramExplorerCommand.Label");
    }

    @objid ("4080b445-ce40-4d81-8292-428c23bd3312")
    @Override
    public String getDetails() {
        return Messages.getString ("Ui.Command.AttackTreeDiagramExplorerCommand.Details");
    }

    @objid ("a5398042-0820-4c92-9836-1ea2070f604b")
    @Override
    public String getHelpUrl() {
        return null;
    }

    @objid ("cc036a87-a0c4-441d-bf62-a075581f857a")
    @Override
    public Image getIcon() {
        return new Image(Display.getDefault(),AttackTreeResourcesManager.getInstance().getImage("attacktreediagram.png"));
    }

    @objid ("da1a686a-a7c9-4c63-ba74-3a339196dbf6")
    @Override
    public String getInformation() {
        return Messages.getString ("Ui.Command.AttackTreeDiagramExplorerCommand.Information");
    }

    @objid ("dc6e2f82-d2d1-46bd-874c-f9ab1ab55807")
    @Override
    public boolean accept(MObject selectedElt) {
        return  ((selectedElt != null) 
                && (selectedElt.getStatus().isModifiable())
                &&  (((selectedElt instanceof Package) 
                        && !(selectedElt instanceof Profile))));
    }

    @objid ("9442eb3a-0f6e-4d4e-9045-bed2836c8ff8")
    @Override
    public ElementDescriptor getCreatedElementType() {
        IModuleContext moduleContext = getModule().getModuleContext();
        MMetamodel metamodel = moduleContext.getModelioServices().getMetamodelService().getMetamodel();
        MClass mClass = metamodel.getMClass(StaticDiagram.class);
        IMetamodelExtensions extensions = moduleContext.getModelingSession().getMetamodelExtensions();
        Stereotype stereotype = extensions.getStereotype(IAttackTreeDesignerPeerModule.MODULE_NAME, AttackTreeStereotypes.ATTACK_TREE_DIAGRAM, mClass);
        return stereotype != null ? new ElementDescriptor(mClass, stereotype) : null;
    }

}
