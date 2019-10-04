package org.modelio.module.attacktreedesigner.property;

import java.util.ArrayList;
import java.util.List;
import com.modeliosoft.modelio.javadesigner.annotations.objid;
import org.modelio.api.module.propertiesPage.IModulePropertyTable;
import org.modelio.metamodel.uml.infrastructure.ModelElement;
import org.modelio.metamodel.uml.infrastructure.Note;
import org.modelio.metamodel.uml.infrastructure.TaggedValue;
import org.modelio.metamodel.uml.statik.Class;
import org.modelio.module.attacktreedesigner.api.AttackTreeNoteTypes;
import org.modelio.module.attacktreedesigner.api.AttackTreeStereotypes;
import org.modelio.module.attacktreedesigner.api.AttackTreeTagTypes;
import org.modelio.module.attacktreedesigner.api.IAttackTreeDesignerPeerModule;
import org.modelio.module.attacktreedesigner.i18n.Messages;
import org.modelio.module.attacktreedesigner.utils.TagsManager;
import org.modelio.module.attacktreedesigner.utils.elementmanager.ElementReferencing;

@objid ("c07b6144-87dc-44fc-ab74-b757ac83385b")
public class TreeReferencePropertyPage implements IPropertyContent {

    @objid ("e757e838-6b1d-4b74-a4a3-f44158a6f7f6")
    @Override
    public void changeProperty(ModelElement element, int row, String value) {
        if (row == 1) {
            ElementReferencing.updateReference((Class) element, value);
        }
    }

    @objid ("0ead49a6-f4d8-4b85-8627-a1ba8698ba53")
    @Override
    public void update(ModelElement element, IModulePropertyTable table) {
        Class referenceTree = (Class) element;
        ElementReferencing.updateListOfAvailableTrees(referenceTree);
        
        
        
        /*
         * add referenced tree and show available trees
         */
        List<String> trees = new ArrayList<>();
        trees.add("");
        trees.addAll(ElementReferencing.getAvailableTreesFullPath());
        
        String[] availableTreeNames = trees.toArray(new String[0]);
        
        Class referencedTree = ElementReferencing.getReferencedTree(referenceTree);
        String value = "";
        if(referencedTree != null) {
            value = ElementReferencing.getTreeFullPath(referencedTree);
        }
        table.addProperty (Messages.getString("Ui.Property.Reference.Name"), value, availableTreeNames);
       
        
        
        /*
         * Show referenced tree Root tags an counter measures (Show only)
         */
        if(referencedTree != null) {

            // Name
            table.addConsultProperty(Messages.getString("Ui.Property.Name.Name"), referencedTree.getName());

            // Severity property
            TaggedValue severityTag = referencedTree.getTag(IAttackTreeDesignerPeerModule.MODULE_NAME, AttackTreeStereotypes.ATTACK, AttackTreeTagTypes.SEVERITY);
            table.addConsultProperty (AttackTreeTagTypes.SEVERITY, TagsManager.getTagParameter(severityTag)); 
            
            // Probability property
            TaggedValue probabilityTag = referencedTree.getTag(IAttackTreeDesignerPeerModule.MODULE_NAME, AttackTreeStereotypes.ATTACK, AttackTreeTagTypes.PROBABILITY);
            table.addConsultProperty (AttackTreeTagTypes.PROBABILITY, TagsManager.getTagParameter(probabilityTag)); 
            
            // Risk Level Consult property (Read only, because it is calculated automatically based on severity and probability property)
            table.addConsultProperty (AttackTreeTagTypes.RISK_LEVEL, TagsManager.getElementRiskLevel(referencedTree));
            
            // Security related
            TaggedValue securityRelatedTag = referencedTree.getTag(IAttackTreeDesignerPeerModule.MODULE_NAME, AttackTreeStereotypes.ATTACK, AttackTreeTagTypes.SECURITY_RELATED);
            table.addConsultProperty(AttackTreeTagTypes.SECURITY_RELATED, TagsManager.getTagParameter(securityRelatedTag));
                    
            // Safety related
            TaggedValue safetyRelatedTag = referencedTree.getTag(IAttackTreeDesignerPeerModule.MODULE_NAME, AttackTreeStereotypes.ATTACK, AttackTreeTagTypes.SAFETY_RELATED);
            table.addConsultProperty(AttackTreeTagTypes.SAFETY_RELATED, TagsManager.getTagParameter(safetyRelatedTag));
            
            // Out of scope
            TaggedValue outOfScopeTag = referencedTree.getTag(IAttackTreeDesignerPeerModule.MODULE_NAME, AttackTreeStereotypes.ATTACK, AttackTreeTagTypes.OUT_OF_SCOPE);
            table.addConsultProperty(AttackTreeTagTypes.OUT_OF_SCOPE, TagsManager.getTagParameter(outOfScopeTag));
         
            // Counter measures
            List<Note> rootNotes = referencedTree.getDescriptor();
            for(Note rootNote:rootNotes) {
                if(rootNote.getModel().getName().equals(AttackTreeNoteTypes.COUNTER_MEASURE)) {
                    table.addConsultProperty (AttackTreeNoteTypes.COUNTER_MEASURE,rootNote.getContent());
                }
            }
        }        
    }

}
