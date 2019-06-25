package org.modelio.module.attacktreedesigner.command;

import java.util.List;
import com.modeliosoft.modelio.javadesigner.annotations.objid;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.widgets.Display;
import org.modelio.api.module.IModule;
import org.modelio.api.module.command.DefaultModuleCommandHandler;
import org.modelio.api.module.context.IModuleContext;
import org.modelio.vcore.smkernel.mapi.MObject;

@objid ("f1debdc5-b35e-4358-8737-25a832c4cd07")
public class UnmaskSubTreeCommand extends DefaultModuleCommandHandler {
    @objid ("33cddea2-4e46-4ef2-9dd0-5809966869c6")
    @Override
    public void actionPerformed(final List<MObject> selectedElements, final IModule module) {
        // Module context is the link to many Modelio services
        IModuleContext ctx = module.getModuleContext();
        
        // TODO replace the following dumb code by the real implementation of the command
        MessageDialog.openInformation(Display.getDefault().getActiveShell(), "", "Command not implemented!");
    }

    @objid ("cc6c9be3-a86e-4537-907c-69442cf4f07b")
    @Override
    public boolean accept(final List<MObject> selectedElements, final IModule module) {
        // Generated call to the super method will check the scope conditions defined in Studio.
        // DO NOT REMOVE this call unless you need to take full control on the checks to be carried out.
        // However you can safely extends the checked conditions by adding custom code.
        if (super.accept(selectedElements, module) == false) {
            return false;
        }
        return true;
    }

}
