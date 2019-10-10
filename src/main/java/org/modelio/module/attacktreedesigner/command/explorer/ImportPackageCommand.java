package org.modelio.module.attacktreedesigner.command.explorer;

import java.util.List;
import com.modeliosoft.modelio.javadesigner.annotations.objid;
import org.modelio.api.module.IModule;
import org.modelio.api.module.command.DefaultModuleCommandHandler;
import org.modelio.metamodel.uml.statik.Package;
import org.modelio.module.attacktreedesigner.utils.FileSystemManager;
import org.modelio.vcore.smkernel.mapi.MObject;

@objid ("7dec2c56-ca0f-427c-a31c-4a43ebd1bc8c")
public class ImportPackageCommand extends DefaultModuleCommandHandler {
    @objid ("90ebe09b-0be3-45fb-be7d-092f56a0eeb2")
    @Override
    public void actionPerformed(final List<MObject> selectedElements, final IModule module) {
        String directoryPath = FileSystemManager.getDialogDirectoryPath();
        if(directoryPath == null)
            return;
        
        Package destinationPackage = (Package) selectedElements.get(0);
    }

    @objid ("55818d2f-f714-4233-b8cd-fe6af41c74cc")
    @Override
    public boolean accept(final List<MObject> selectedElements, final IModule module) {
        if ((selectedElements != null) && (selectedElements.size() == 1)){
            return selectedElements.get(0) instanceof Package ;
        }
        return false;
    }

}
