package org.modelio.module.attacktreedesigner.command.explorer;

import java.util.List;
import com.modeliosoft.modelio.javadesigner.annotations.objid;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.widgets.Display;
import org.modelio.api.module.IModule;
import org.modelio.api.module.command.DefaultModuleCommandHandler;
import org.modelio.api.module.context.IModuleContext;
import org.modelio.metamodel.diagrams.AbstractDiagram;
import org.modelio.metamodel.uml.statik.Attribute;
import org.modelio.metamodel.uml.statik.Class;
import org.modelio.module.attacktreedesigner.api.AttackTreeStereotypes;
import org.modelio.module.attacktreedesigner.api.IAttackTreeDesignerPeerModule;
import org.modelio.module.attacktreedesigner.impl.AttackTreeDesignerModule;
import org.modelio.vcore.smkernel.mapi.MObject;

@objid ("764185fa-1588-4b59-b6f8-b09a28253fb9")
public class OpenReferencedTreeDiagram extends DefaultModuleCommandHandler {
    @objid ("20d7aef8-1f05-45af-8908-21dbe8830e60")
    @Override
    public void actionPerformed(final List<MObject> selectedElements, final IModule module) {
        IModuleContext moduleContext = AttackTreeDesignerModule.getInstance().getModuleContext();
        MObject selectedElement = selectedElements.get(0);  
        
        AbstractDiagram referencedTreeDiagram = getReferencedTreeDiagram(selectedElement);
        
        if(referencedTreeDiagram != null) {
            moduleContext.getModelioServices().getEditionService().openEditor(referencedTreeDiagram);
        } else {
            MessageDialog.openInformation(Display.getDefault().getActiveShell(), "No Diagram Found!", "Sorry! We could not find the diagram of the referenced tree");
        }
    }

    @objid ("a681e25d-55b2-4b38-86bc-c096e4dd7aeb")
    @Override
    public boolean accept(final List<MObject> selectedElements, final IModule module) {
        if ((selectedElements != null) && (selectedElements.size() == 1)){
            MObject selectedElement = selectedElements.get(0);
            return ((selectedElement != null) 
                    && (
                            ((selectedElement instanceof Class)
                                    && ((Class) selectedElement).isStereotyped(IAttackTreeDesignerPeerModule.MODULE_NAME, AttackTreeStereotypes.TREE_REFERENCE))
                            
                            || ((selectedElement instanceof Attribute)
                                    && ((Attribute) selectedElement).isStereotyped(IAttackTreeDesignerPeerModule.MODULE_NAME, AttackTreeStereotypes.TREE_REFERENCE_ATTRIBUTE))
                            )
                    );
        }
        return false;
    }

    @objid ("0616cac0-990a-40cb-bf74-8fdbfb982538")
    public AbstractDiagram getReferencedTreeDiagram(MObject selectedElement) {
        AbstractDiagram referencedTreeDiagram = null;
                
        Attribute referenceTreeAttribute = null;        
        
        if(selectedElement instanceof Class) {
            List<Attribute> attributes = ((Class) selectedElement).getOwnedAttribute();
            if(!attributes.isEmpty()) {
                referenceTreeAttribute = attributes.get(0);
        
            }
        } else if (selectedElement instanceof Attribute ) {
            referenceTreeAttribute = ((Attribute) selectedElement);
        }
        
        if (referenceTreeAttribute != null) {
            List<? extends MObject> referencedTreeRootChildren = referenceTreeAttribute.getType().getCompositionChildren();
            for(MObject child : referencedTreeRootChildren) {
                if(child instanceof AbstractDiagram) {
                    return (AbstractDiagram) child;
                }
            }
        }
        return referencedTreeDiagram;
    }

}
