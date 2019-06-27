package org.modelio.module.attacktreedesigner.property;

import java.util.List;
import com.modeliosoft.modelio.javadesigner.annotations.objid;
import org.modelio.api.module.propertiesPage.IModulePropertyTable;
import org.modelio.metamodel.uml.infrastructure.ModelElement;
import org.modelio.metamodel.uml.infrastructure.TaggedValue;
import org.modelio.metamodel.uml.statik.Class;
import org.modelio.module.attacktreedesigner.api.AttackTreeStereotypes;
import org.modelio.module.attacktreedesigner.api.IAttackTreeDesignerPeerModule;
import org.modelio.module.attacktreedesigner.utils.AttackTreeResourcesManager;
import org.modelio.module.attacktreedesigner.utils.TagsManager;

@objid ("f97fbaac-1f51-49d9-88e4-4d123d39b79a")
public class AttackTreePropertyManager {
    @objid ("5399b40d-3a99-43e0-a9b1-508456c59a5b")
    public static final String NAME_PROPERTY = "Name";

    /**
     * @param MObject
     * : the selected MObject
     * @param row : the row of the property
     * @param value : the new value of the property
     * @return the new value of the row
     */
    @objid ("686020f8-7fb4-414c-8694-d7f3fe888320")
    public static int changeProperty(ModelElement element, int row, String value) {
        return row;
    }

    /**
     * build the property table of the selected Elements
     * @param MObject
     * : the selected element
     * @param table : the property table
     */
    @objid ("60d3e597-4f70-4498-840f-69a3140a067e")
    public static void update(ModelElement element, IModulePropertyTable table) {
        table.addProperty (AttackTreeResourcesManager.getInstance().getPropertyName(NAME_PROPERTY), element.getName());
        //table.addConsultProperty(key, value);
        //IModulePropertyTable.
        //        String[] enumValues = {"llllll", "kkkk", "ldddd"};
        //        table.addProperty("hh", "llllll", enumValues );
        
        
        //IModuleContext moduleContext = AttackTreeDesignerModule.getInstance().getModuleContext();
        //IMetamodelExtensions extensions = moduleContext.getModelingSession().getMetamodelExtensions();
        //MMetamodel metamodel = moduleContext.getModelioServices().getMetamodelService().getMetamodel();
        
        
        if((element instanceof Class) 
                && ((Class)element).isStereotyped(IAttackTreeDesignerPeerModule.MODULE_NAME, AttackTreeStereotypes.ATTACK)) {
            
            List<TaggedValue> listTags = element.getTag();
            
            
            for(TaggedValue tag:listTags) {
                
        
                
                table.addProperty (tag.getDefinition().getName(), 
                        //tag.get
                        TagsManager.getParameter(tag, tag.getDefinition().getName())
                        //element.getTagValue(IAttackTreeDesignerPeerModule.MODULE_NAME, AttackTreeStereotypes.ATTACK, AttackTreeTagTypes.SEVERITY)
                        );
                
        
            }
            
        
            
        }
    }

}
