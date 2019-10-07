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
        
        // selectedElement is a Tree
        if(selectedElement instanceof Class) {
            Class modelTree = (Class) selectedElement;
            exportTree(directoryPath, modelTree);        
        } 
        // selected element is a package
        else if (selectedElement instanceof Package) {
            Package pkg = (Package) selectedElement;
            exportPackageTrees(directoryPath, pkg);
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

    @objid ("3305ebb0-7e96-4806-8e8a-8bd47f90bdc7")
    private void exportPackageTrees(String directoryPath, Package pkg) {
        for(MObject element : pkg.getCompositionChildren()) {
            if(element instanceof Class) {
                Class node = (Class) element;
                if(node.isStereotyped(IAttackTreeDesignerPeerModule.MODULE_NAME, AttackTreeStereotypes.ROOT)) {
                    exportTree(directoryPath + FileSystemManager.PATH_SEPARATOR + pkg.getName(), node);
                }
            } else if (element instanceof Package) {
                Package childPackage = (Package) element;
                exportPackageTrees(directoryPath + FileSystemManager.PATH_SEPARATOR + pkg.getName(), childPackage);
            }
        }
    }

    @objid ("1f6c3069-cbbf-4875-8bc7-beac1a409a26")
    private void exportTree(String directoryPath, Class modelTree) {
        ModelToJaxbConvertor modelToJaxbConvertor = new ModelToJaxbConvertor(modelTree);
        File file = FileSystemManager.createFile(directoryPath, modelToJaxbConvertor.getModelTree().getName() + FileSystemManager.XML_FILE_EXTENSION);
        FileSystemManager.marchallJaxbContentInFile(file, modelToJaxbConvertor.convertModelToJaxb());
    }

}
