package org.modelio.module.attacktreedesigner.property;

import com.modeliosoft.modelio.javadesigner.annotations.objid;
import org.modelio.api.module.propertiesPage.IModulePropertyTable;
import org.modelio.metamodel.uml.infrastructure.ModelElement;
import org.modelio.metamodel.uml.infrastructure.Note;
import org.modelio.module.attacktreedesigner.api.AttackTreeNoteTypes;

@objid ("0ce3473c-f1fb-4fd1-9270-02f8b6c698e4")
public class NotePropertyPage implements IPropertyContent {
    @objid ("de2ae4d8-249f-4a1f-a3d2-c783263b6589")
    @Override
    public void changeProperty(ModelElement element, int row, String value) {
        if (row == 1) {
            ((Note) element).setContent(value);
        }
    }

    @objid ("e864df24-e168-4e55-9bed-c3b5088ee833")
    @Override
    public void update(ModelElement element, IModulePropertyTable table) {
        table.addProperty (AttackTreeNoteTypes.COUNTER_MEASURE,((Note)element).getContent());
    }

}
