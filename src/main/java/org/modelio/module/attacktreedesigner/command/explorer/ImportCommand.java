package org.modelio.module.attacktreedesigner.command.explorer;

import java.io.File;
import java.util.List;
import com.modeliosoft.modelio.javadesigner.annotations.objid;
import org.modelio.api.module.IModule;
import org.modelio.api.module.command.DefaultModuleCommandHandler;
import org.modelio.metamodel.uml.statik.Package;
import org.modelio.module.attacktreedesigner.conversion.JaxbToModelConvertor;
import org.modelio.module.attacktreedesigner.conversion.schema.AttackTreeType;
import org.modelio.module.attacktreedesigner.utils.FileSystemManager;
import org.modelio.vcore.smkernel.mapi.MObject;

@objid ("f7a702eb-8393-4b88-9a6e-6364ec849ec0")
public class ImportCommand extends DefaultModuleCommandHandler {
    @objid ("336aa945-1551-403e-94a6-3d5b5b1f6842")
    @Override
    public void actionPerformed(List<MObject> selectedElements, IModule module) {
        String xmlFilePath = FileSystemManager.getXMLFileDialogPath();
        if(xmlFilePath == null)
            return;        
        
        AttackTreeType jaxbTree = FileSystemManager.unmarshallFileToJaxb(new File(xmlFilePath));
        JaxbToModelConvertor modelToJaxbConvertor = new JaxbToModelConvertor(jaxbTree);
        
        Package destinationPackage = (Package) selectedElements.get(0); 
        modelToJaxbConvertor.createTreeModel(module, destinationPackage);
        JaxbToModelConvertor.updatePostponedReferences();
    }

    @objid ("2e25b03b-87c0-412c-b7be-96654411cfd6")
    @Override
    public boolean accept(List<MObject> selectedElements, IModule module) {
        if ((selectedElements != null) && (selectedElements.size() == 1)){
            return selectedElements.get(0) instanceof Package ;
        }
        return false;
    }

}
