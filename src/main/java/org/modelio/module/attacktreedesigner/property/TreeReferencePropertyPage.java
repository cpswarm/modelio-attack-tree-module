package org.modelio.module.attacktreedesigner.property;

import java.util.ArrayList;
import java.util.List;
import com.modeliosoft.modelio.javadesigner.annotations.objid;
import org.modelio.api.module.propertiesPage.IModulePropertyTable;
import org.modelio.metamodel.uml.infrastructure.ModelElement;
import org.modelio.metamodel.uml.statik.Class;
import org.modelio.metamodel.uml.statik.Classifier;
import org.modelio.module.attacktreedesigner.i18n.Messages;
import org.modelio.module.attacktreedesigner.utils.elementmanager.ElementReferencing;

@objid ("c07b6144-87dc-44fc-ab74-b757ac83385b")
public class TreeReferencePropertyPage implements IPropertyContent {

    @objid ("e757e838-6b1d-4b74-a4a3-f44158a6f7f6")
    @Override
    public void changeProperty(ModelElement element, int row, String value) {
        if (row == 1) {
            ElementReferencing.updateReference((Classifier) element, value);
        }
    }

    @objid ("0ead49a6-f4d8-4b85-8627-a1ba8698ba53")
    @Override
    public void update(ModelElement element, IModulePropertyTable table) {
        Class referenceTree = (Class) element;
        ElementReferencing.getAvailableTrees(referenceTree);
        
        // add referenced tree and show available trees
        List<String> trees = new ArrayList<>();
        trees.add("");
        trees.addAll(ElementReferencing.getAvailableTreesNames());
        
        String[] availableTreeNames = trees.toArray(new String[0]);
        String value = ElementReferencing.getReferencedTreeName(referenceTree);
        table.addProperty (Messages.getString("Ui.Property.Reference.Name"), value, availableTreeNames);
    }

}
