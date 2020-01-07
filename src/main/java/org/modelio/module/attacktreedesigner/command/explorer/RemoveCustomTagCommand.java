package org.modelio.module.attacktreedesigner.command.explorer;

import java.util.List;
import com.modeliosoft.modelio.javadesigner.annotations.objid;
import org.eclipse.swt.widgets.Display;
import org.modelio.api.module.IModule;
import org.modelio.api.module.command.DefaultModuleCommandHandler;
import org.modelio.metamodel.uml.infrastructure.ModelElement;
import org.modelio.metamodel.uml.statik.Class;
import org.modelio.module.attacktreedesigner.api.AttackTreeStereotypes;
import org.modelio.module.attacktreedesigner.api.IAttackTreeDesignerPeerModule;
import org.modelio.module.attacktreedesigner.ui.RemoveCustomTagDialog;
import org.modelio.vcore.smkernel.mapi.MObject;

@objid ("8f43f0b5-8feb-4296-b101-f9e4ee2da581")
public class RemoveCustomTagCommand extends DefaultModuleCommandHandler {
    @objid ("ae589711-75af-4d84-974a-9eb247a7853d")
    @Override
    public void actionPerformed(List<MObject> selectedElements, IModule module) {
        RemoveCustomTagDialog removeCustomTagDialog = new RemoveCustomTagDialog(Display.getCurrent().getActiveShell());
        removeCustomTagDialog.setSelectedElement((ModelElement)selectedElements.get(0));
        removeCustomTagDialog.open();
    }

    @objid ("651d63f9-7561-4355-af77-40061048cee1")
    @Override
    public boolean accept(List<MObject> selectedElements, IModule module) {
        if ((selectedElements != null) && (selectedElements.size() == 1)){
            return selectedElements.get(0) instanceof Class
                    && ((Class) selectedElements.get(0))
                    .isStereotyped(IAttackTreeDesignerPeerModule.MODULE_NAME, AttackTreeStereotypes.ATTACK);
        }
        return false;
    }

}
