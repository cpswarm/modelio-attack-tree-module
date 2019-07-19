package org.modelio.module.attacktreedesigner.property;

import java.util.ArrayList;
import java.util.List;
import com.modeliosoft.modelio.javadesigner.annotations.objid;
import org.modelio.api.modelio.model.IModelingSession;
import org.modelio.api.modelio.model.ITransaction;
import org.modelio.api.module.IModule;
import org.modelio.api.module.context.IModuleContext;
import org.modelio.api.module.propertiesPage.AbstractModulePropertyPage;
import org.modelio.api.module.propertiesPage.IModulePropertyTable;
import org.modelio.metamodel.uml.infrastructure.Dependency;
import org.modelio.metamodel.uml.infrastructure.ModelElement;
import org.modelio.metamodel.uml.infrastructure.ModelTree;
import org.modelio.metamodel.uml.infrastructure.TaggedValue;
import org.modelio.metamodel.uml.statik.Class;
import org.modelio.module.attacktreedesigner.api.AttackTreeStereotypes;
import org.modelio.module.attacktreedesigner.api.AttackTreeTagTypes;
import org.modelio.module.attacktreedesigner.api.IAttackTreeDesignerPeerModule;
import org.modelio.module.attacktreedesigner.i18n.Messages;
import org.modelio.module.attacktreedesigner.impl.AttackTreeDesignerModule;
import org.modelio.module.attacktreedesigner.utils.AttackTreeResourcesManager;
import org.modelio.module.attacktreedesigner.utils.TagsManager;
import org.modelio.module.attacktreedesigner.utils.elementmanager.ElementNavigationManager;
import org.modelio.vcore.smkernel.mapi.MObject;

@objid ("23bf4eaa-6db6-4138-8664-6b16f27ded1e")
public class AttackTreePropertyPage extends AbstractModulePropertyPage {
    @objid ("dcfd9796-8fbf-438d-b23b-905ef12af0a2")
    public static final String NAME_PROPERTY = "Name";

    @objid ("50df2a0f-9be2-4a9f-8b25-6ceabfa60293")
    private static final String REFERENCING_A_TREE_LABEL = "Referencing a tree";

    @objid ("6e0f839a-3fec-4180-b2bd-de72fb2384d0")
    private static final String REFERENCED_TREE_LABEL = "Referenced tree";

    @objid ("14a6ab03-bf87-4a77-987d-5e8ca9eb245c")
    private static final String UNKNOWN_TREE_LABEL = "Unknown referenced tree";

    @objid ("801a3c4e-e87e-4923-82de-facf9fce16e6")
    public AttackTreePropertyPage(IModule module, String name, String label, String bitmap) {
        super (module, name, label, bitmap);
    }

    /**
     * This method is called when the current selection changes and that the property box contents requires an update.
     * The ?selectedElements? parameter contains the list of the newly selected elements. The ?table? parameter is the
     * table that must be filled with the updated contents of the property box before returning.
     */
    @objid ("d0ab204e-02c5-4689-aa0d-b68be90eaa15")
    @Override
    public void update(List<MObject> selectedElements, IModulePropertyTable table) {
        if ((selectedElements != null) && (selectedElements.size() > 0)) {
            MObject selectedElement = selectedElements.get(0);
            if( (selectedElement != null)
                    && (selectedElement instanceof Class)){
        
                if(((Class) selectedElement).isStereotyped(IAttackTreeDesignerPeerModule.MODULE_NAME, AttackTreeStereotypes.ATTACK)) {
                    // add Name property
                    table.addProperty (AttackTreeResourcesManager.getInstance().getPropertyName(NAME_PROPERTY), selectedElement.getName());
        
                    // add tags properties
                    List<TaggedValue> listTags = ((Class) selectedElement).getTag();
        
                    for(TaggedValue tag:listTags) {
                        if(tag.getDefinition().getName().equals(AttackTreeTagTypes.SEVERITY)) {
        
                            table.addProperty (tag.getDefinition().getName(), 
                                    TagsManager.getParameter(tag, tag.getDefinition().getName()),
                                    TagsManager.SEVERITY_VALUES);
        
                        } else if (tag.getDefinition().getName().equals(AttackTreeTagTypes.PROBABILITY)){
                            table.addProperty (tag.getDefinition().getName(), 
                                    TagsManager.getParameter(tag, tag.getDefinition().getName()),
                                    TagsManager.PROBABILITY_VALUES);
                        } else if (tag.getDefinition().getName().equals(AttackTreeTagTypes.RISK_LEVEL)) {
                            table.addConsultProperty (tag.getDefinition().getName(), 
                                    TagsManager.getParameter(tag, tag.getDefinition().getName()));
                        } else if (tag.getDefinition().getName().equals(AttackTreeTagTypes.SECURITY_RELATED)) {
                            table.addProperty(tag.getDefinition().getName(), 
                                    TagsManager.getParameter(tag, tag.getDefinition().getName()).equals("true"));
                        } else if (tag.getDefinition().getName().equals(AttackTreeTagTypes.SAFETY_RELATED)) {
                            table.addProperty(tag.getDefinition().getName(), 
                                    TagsManager.getParameter(tag, tag.getDefinition().getName()).equals("true"));
                        } else if (tag.getDefinition().getName().equals(AttackTreeTagTypes.OUT_OF_SCOPE)) {
                            table.addProperty(tag.getDefinition().getName(), 
                                    TagsManager.getParameter(tag, tag.getDefinition().getName()).equals("true"));
                        } else {
                            table.addProperty (tag.getDefinition().getName(), 
                                    TagsManager.getParameter(tag, tag.getDefinition().getName()));
                        }
        
        
        
                    }
                }
        
        
                /*
                 * Add the possibility of referencing a tree for leaves
                 */
                List<Dependency> elementDependencies = ((Class) selectedElement).getDependsOnDependency();
        
                if(((Class) selectedElement).isStereotyped(IAttackTreeDesignerPeerModule.MODULE_NAME, AttackTreeStereotypes.TREE_REFERENCE_DECORATION)) {
                    // add check box for referencing tree
                    table.addProperty(REFERENCING_A_TREE_LABEL, true);
        
        
                    // add referenced tree and show available trees
                    String[] availableTrees = getAvailableTreesNames((Class) selectedElement).toArray(new String[0]);
        
                    if( elementDependencies.isEmpty()) {
        
                        table.addProperty (REFERENCED_TREE_LABEL, 
                                UNKNOWN_TREE_LABEL,
                                availableTrees);
        
        
                    } else if(elementDependencies.size() == 1){
                        Dependency elementDependency = elementDependencies.get(0);
                        if(elementDependency.isStereotyped(IAttackTreeDesignerPeerModule.MODULE_NAME, AttackTreeStereotypes.TREE_REFERENCE_DEPENDENCY)) {
        
        
        
                            table.addProperty (REFERENCED_TREE_LABEL, 
                                    elementDependencies.get(0).getDependsOn().getName(),
                                    availableTrees);
                        }
                    }
        
                } else if(elementDependencies.isEmpty()) {
                    // add check box for referencing tree
                    table.addProperty(REFERENCING_A_TREE_LABEL, false);
                } 
        
        
            }
        }
    }

    /**
     * This method is called when a value has been edited in the property box in the row ?row?. The ?selectedElements?
     * parameter contains the list of the currently selected elements. The ?row? parameter is the row number of the
     * modified value. The ?value? parameter is the new value the user has set to the given row.
     */
    @objid ("5cf28d81-b746-4c5c-acd9-c075bb6c4468")
    @Override
    public void changeProperty(List<MObject> selectedElements, int row, String value) {
        if ((selectedElements != null) && (selectedElements.size() > 0)) {
            MObject selectedElement = selectedElements.get(0);
            if( (selectedElement != null)
                    && (selectedElement instanceof Class)
                    && ((Class) selectedElement).isStereotyped(IAttackTreeDesignerPeerModule.MODULE_NAME, AttackTreeStereotypes.ATTACK)){
        
        
                if(row==1) {
                    // row=1 -> Name property
                    selectedElement.setName(value);
        
                } else if (row == 2) {
                    // row=2 -> Severity property
                    TagsManager.setTagValue((Class)selectedElement, AttackTreeTagTypes.SEVERITY, value);
        
                    // calculate Risk Level
                    TagsManager.updateRiskLevelTagValue(selectedElement);
        
        
                } else if (row == 3) {
                    // row=3 -> Probability property
                    TagsManager.setTagValue((Class)selectedElement, AttackTreeTagTypes.PROBABILITY, value);
        
                    // calculate Risk Level
                    TagsManager.updateRiskLevelTagValue(selectedElement);
        
                } else if (row == 5) {
                    // row=5 -> Security related
                    TagsManager.setTagValue((Class)selectedElement, AttackTreeTagTypes.SECURITY_RELATED, value);
        
        
                } else if (row == 6) {
                    // row=6 -> Safety related
                    TagsManager.setTagValue((Class)selectedElement, AttackTreeTagTypes.SAFETY_RELATED, value);
        
        
                } else if (row == 7) {
                    // row=7 -> Out of scope
                    TagsManager.setTagValue((Class)selectedElement, AttackTreeTagTypes.OUT_OF_SCOPE, value);
        
        
                } else if (row == 8) {
                    // row=8 - > Referencing a tree
                    IModuleContext moduleContext = AttackTreeDesignerModule.getInstance().getModuleContext();
                    IModelingSession session = moduleContext.getModelingSession();
        
        
                    try( ITransaction transaction = session.createTransaction(Messages.getString ("Info.Session.UpdateModel"))){
        
                        if(value.equals("true")) {
                            
                            ((Class) selectedElement).addStereotype(IAttackTreeDesignerPeerModule.MODULE_NAME, AttackTreeStereotypes.TREE_REFERENCE_DECORATION);
                            session.getModel().getDefaultNameService().setDefaultName(((Class) selectedElement), UNKNOWN_TREE_LABEL);
                            
                        } else {
                            ((Class) selectedElement).removeStereotypes(IAttackTreeDesignerPeerModule.MODULE_NAME, AttackTreeStereotypes.TREE_REFERENCE_DECORATION);
                            List<Dependency> elementDependencies = ((Class) selectedElement).getDependsOnDependency();
        
        
                            if(!elementDependencies.isEmpty()) {
                                if(elementDependencies.get(0).isStereotyped(IAttackTreeDesignerPeerModule.MODULE_NAME, AttackTreeStereotypes.TREE_REFERENCE_DEPENDENCY)){
                                    elementDependencies.get(0).delete();
                                }
                            }  
                            
                            session.getModel().getDefaultNameService().setDefaultName(((Class) selectedElement), AttackTreeStereotypes.ATTACK);
                        }
        
                        transaction.commit ();
                    }
        
        
                } else if (row == 9) {
                    // row=9 - > Select Referenced Tree
        
                    addReferenceDependencyToReferencedTree((ModelElement) selectedElement, value);
        
        
                }
            }
        
        
            //ModelElement element = ((ModelElement) selectedElements.get (0));
        
        
            //AttackTreePropertyManager.changeProperty(element, row, value);
        
        }
    }

    @objid ("8c950848-bd0c-4ee6-a014-35235ca64ab5")
    private List<String> getAvailableTreesNames(Class selectedElement) {
        ModelTree rootElement = ElementNavigationManager.getRootElement(selectedElement);
        
        ModelTree modelPackage = rootElement.getOwner();
        
        ArrayList<String> availableTreesNames= new ArrayList<>();
        availableTreesNames.add(UNKNOWN_TREE_LABEL);
        for(Class packageElement: modelPackage.getOwnedElement(Class.class)) {
            if(packageElement.isStereotyped(IAttackTreeDesignerPeerModule.MODULE_NAME, AttackTreeStereotypes.ROOT) && ! packageElement.equals(rootElement)) {
                availableTreesNames.add(packageElement.getName());
            }
        }
        return availableTreesNames;
    }

    @objid ("2a83035a-1000-4112-afc8-4e951cf95f38")
    private void addReferenceDependencyToReferencedTree(ModelElement selectedElement, String referencedTreeName) {
        IModelingSession session = AttackTreeDesignerModule.getInstance().getModuleContext().getModelingSession();
        try( ITransaction transaction = session.createTransaction (Messages.getString ("Info.Session.Create", AttackTreeStereotypes.TREE_REFERENCE_DEPENDENCY))){
        
        
            List<Dependency> elementDependencies = ((Class) selectedElement).getDependsOnDependency();
            if(elementDependencies.isEmpty() ) {
        
                Class referencedTreeRoot = getReferencedTreeByName(selectedElement, referencedTreeName);
                if(referencedTreeRoot != null) {
                    session.getModel().createDependency(selectedElement, 
                            referencedTreeRoot, 
                            IAttackTreeDesignerPeerModule.MODULE_NAME, 
                            AttackTreeStereotypes.TREE_REFERENCE_DEPENDENCY); 
                }
                selectedElement.setName(referencedTreeName);
        
            } else  {
                Dependency elementDependency = elementDependencies.get(0);
        
                if(elementDependency.isStereotyped(IAttackTreeDesignerPeerModule.MODULE_NAME, AttackTreeStereotypes.TREE_REFERENCE_DEPENDENCY)
                        && ! elementDependency.getDependsOn().getName().equals(referencedTreeName)) {
        
                    elementDependency.delete();
        
                    if(! referencedTreeName.equals(UNKNOWN_TREE_LABEL)){
                        Class referencedTreeRoot = getReferencedTreeByName(selectedElement, referencedTreeName);
                        if(referencedTreeRoot != null) {
                            session.getModel().createDependency(selectedElement, 
                                    referencedTreeRoot, 
                                    IAttackTreeDesignerPeerModule.MODULE_NAME, 
                                    AttackTreeStereotypes.TREE_REFERENCE_DEPENDENCY); 
                        }
                        selectedElement.setName(referencedTreeName);
                    } else {
                        session.getModel().getDefaultNameService().setDefaultName(selectedElement, UNKNOWN_TREE_LABEL);
                    }
                }
            }
        
        
        
        
            transaction.commit ();
        }
    }

    @objid ("093ce0a6-bdb9-4623-9f73-52dcc19416bd")
    public Class getReferencedTreeByName(ModelElement selectedElement, String referencedTreeName) {
        List<Class> availableTrees = getAvailableTrees((Class) selectedElement);
        for(Class availableTree : availableTrees) {
            if(availableTree.getName().equals(referencedTreeName)) {
                return availableTree;
            }
        }
        return null;
    }

    /**
     * @param selectedElement
     * @return List of trees roots contained in the same package as the root of the selectedElement
     */
    @objid ("48b036b1-28fe-40a9-b07d-38651a3d33bd")
    private List<Class> getAvailableTrees(Class selectedElement) {
        ModelTree rootElement = ElementNavigationManager.getRootElement(selectedElement);
        
        ModelTree modelPackage = rootElement.getOwner();
        
        ArrayList<Class> availableTreesRoots= new ArrayList<>();
        for(Class packageElement: modelPackage.getOwnedElement(Class.class)) {
            if(packageElement.isStereotyped(IAttackTreeDesignerPeerModule.MODULE_NAME, AttackTreeStereotypes.ROOT) && ! packageElement.equals(rootElement)) {
                availableTreesRoots.add(packageElement);
            }
        }
        return availableTreesRoots;
    }

}
