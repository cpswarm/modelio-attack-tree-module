package org.modelio.module.attacktreedesigner.command.explorer;

import java.io.File;
import java.util.List;
import com.modeliosoft.modelio.javadesigner.annotations.objid;
import org.modelio.api.module.IModule;
import org.modelio.api.module.command.DefaultModuleCommandHandler;
import org.modelio.metamodel.uml.statik.Class;
import org.modelio.metamodel.uml.statik.Package;
import org.modelio.module.attacktreedesigner.api.AttackTreeStereotypes;
import org.modelio.module.attacktreedesigner.api.IAttackTreeDesignerPeerModule;
import org.modelio.module.attacktreedesigner.conversion.ModelToJaxbConvertor;
import org.modelio.module.attacktreedesigner.utils.FileSystemManager;
import org.modelio.vcore.smkernel.mapi.MObject;

@objid ("286570d9-dd5d-4974-9067-813c6d3c8ff7")
public class ExportCommand extends DefaultModuleCommandHandler {
    @objid ("b18adffa-2cab-4db4-b632-4e0991478fb5")
    @Override
    public void actionPerformed(List<MObject> selectedElements, IModule module) {
        String directoryPath = FileSystemManager.getDialogDirectoryPath();
        if(directoryPath == null)
            return;
        
        MObject selectedElement = selectedElements.get(0);
        if(selectedElement instanceof Class) {
            Class modelTree = (Class) selectedElement;
            ModelToJaxbConvertor modelToJaxbConvertor = new ModelToJaxbConvertor(modelTree);
            
            File file = FileSystemManager.createFile(directoryPath, modelToJaxbConvertor.getModelTree().getName() + FileSystemManager.XML_FILE_EXTENSION);
        
            FileSystemManager.marchallJaxbContentInFile(file, modelToJaxbConvertor.convertModelToJaxb());
        
        }
    }

    @objid ("3870445b-8622-4a21-ad68-04f114f76a79")
    @Override
    public boolean accept(final List<MObject> selectedElements, final IModule module) {
        if ((selectedElements != null) && (selectedElements.size() == 1)){
            MObject selectedElement = selectedElements.get(0);
            return ((selectedElement != null) 
                    && (
                            ((selectedElement instanceof Class) 
                                    && ((Class)selectedElement).isStereotyped(IAttackTreeDesignerPeerModule.MODULE_NAME, AttackTreeStereotypes.ROOT)) 
                            || selectedElement instanceof Package)
                    );
        }
        return false;
    }

}
