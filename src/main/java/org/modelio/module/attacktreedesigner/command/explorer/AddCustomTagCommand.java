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
import org.modelio.module.attacktreedesigner.ui.AddCustomTagDialog;
import org.modelio.vcore.smkernel.mapi.MObject;

@objid ("734d2e51-e541-495f-b2fb-f4d32850b393")
public class AddCustomTagCommand extends DefaultModuleCommandHandler {
    @objid ("0748b934-ae73-4b6e-a333-bdeb7bedbb66")
    @Override
    public void actionPerformed(List<MObject> selectedElements, IModule module) {
        AddCustomTagDialog addCustomTagDialog = new AddCustomTagDialog(Display.getCurrent().getActiveShell());
        addCustomTagDialog.setSelectedElement((ModelElement)selectedElements.get(0));
        addCustomTagDialog.open();
    }

    @objid ("947fb0c0-3c24-42b6-8b18-c789c3d04ec1")
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
