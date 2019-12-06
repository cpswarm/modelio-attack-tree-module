package org.modelio.module.attacktreedesigner.utils.elementmanager.tags;

import com.modeliosoft.modelio.javadesigner.annotations.objid;
import org.modelio.metamodel.uml.infrastructure.Dependency;
import org.modelio.metamodel.uml.statik.Class;
import org.modelio.module.attacktreedesigner.api.AttackTreeStereotypes;
import org.modelio.module.attacktreedesigner.api.AttackTreeTagTypes;
import org.modelio.module.attacktreedesigner.api.IAttackTreeDesignerPeerModule;
import org.modelio.module.attacktreedesigner.utils.elementmanager.ElementReferencing;

@objid ("98175c32-cdba-444f-8be5-f9b8afee9649")
public class SeverityTagManager {
    @objid ("c852642d-3f4d-41df-b616-c25b41c370c7")
    public static int getMinSeverityIndex(Class node) {
        if(node.isStereotyped(IAttackTreeDesignerPeerModule.MODULE_NAME, AttackTreeStereotypes.ATTACK)) {
        
            // may have at most 1 child Operator (i.e. AND/OR)
            for(Dependency dependency: node.getDependsOnDependency()) {
                if(dependency.isStereotyped(IAttackTreeDesignerPeerModule.MODULE_NAME, AttackTreeStereotypes.CONNECTION)) {
        
                    return getMinSeverityIndex((Class) dependency.getDependsOn()); 
                }
            }
        
        } else if (node.isStereotyped(IAttackTreeDesignerPeerModule.MODULE_NAME, AttackTreeStereotypes.OR)) {
        
            return getMinSeverityIndexOROperator(node);
        
        } else if (node.isStereotyped(IAttackTreeDesignerPeerModule.MODULE_NAME, AttackTreeStereotypes.AND)) {
        
            return getMinSeverityIndexANDOperator(node);
        
        } else if (node.isStereotyped(IAttackTreeDesignerPeerModule.MODULE_NAME, AttackTreeStereotypes.TREE_REFERENCE)) {
        
            // return the minSeverityIndex of the reference
            Class referencedTree = ElementReferencing.getReferencedTree(node);
            if(referencedTree  != null)
                return getMinSeverityIndex(referencedTree);
            else
                return 0;
        }
        return 0;
    }

    @objid ("c55d27ca-c0e8-48e0-935c-b0921f1dcfa7")
    private static int getMinSeverityIndexANDOperator(Class node) {
        int minSeverityLevel = 0;
        
        for(Dependency dependency: node.getDependsOnDependency()) {
            if(dependency.isStereotyped(IAttackTreeDesignerPeerModule.MODULE_NAME, AttackTreeStereotypes.CONNECTION)) {
                Class child = (Class) dependency.getDependsOn();
                int currentMinSeverityIndex;
        
                /*
                 * Calculate currentMinSeverityIndex depending on the child nature (Attack, reference, or else operator)
                 */
                if(child.isStereotyped(IAttackTreeDesignerPeerModule.MODULE_NAME, AttackTreeStereotypes.ATTACK)) {
                    String childSeverity = TagsManager.getElementTagParameter(child, AttackTreeStereotypes.ATTACK, AttackTreeTagTypes.SEVERITY);
                    currentMinSeverityIndex = 0;
                    for(int i=0; i<TagsManager.SEVERITY_VALUES.length; i++) {
                        if(childSeverity.equals(TagsManager.SEVERITY_VALUES[i])) {
                            currentMinSeverityIndex = i;
                            break;
                        }
                    }
                } else if(child.isStereotyped(IAttackTreeDesignerPeerModule.MODULE_NAME, AttackTreeStereotypes.TREE_REFERENCE)) {
                    currentMinSeverityIndex = 0;
                    Class childReference = ElementReferencing.getReferencedTree(child) ;
                    if(childReference != null) {
                        String childReferenceSeverity = TagsManager.getElementTagParameter(childReference, AttackTreeStereotypes.ATTACK, AttackTreeTagTypes.SEVERITY);
                        for(int i=0; i<TagsManager.SEVERITY_VALUES.length; i++) {
                            if(childReferenceSeverity.equals(TagsManager.SEVERITY_VALUES[i])) {
                                currentMinSeverityIndex = i;
                                break;
                            }
                        }
                    }
        
                } else {
                    currentMinSeverityIndex = getMinSeverityIndex(child);
                }
        
                /*
                 * update minSeverityIndex
                 */
                if( currentMinSeverityIndex > minSeverityLevel) 
                    minSeverityLevel = currentMinSeverityIndex;
        
            }
        }
        return minSeverityLevel;
    }

    @objid ("4d33ed9e-478b-48a4-b535-018d28deff5b")
    private static int getMinSeverityIndexOROperator(Class node) {
        boolean nodeHasChildren = false;
        
        int minSeverityIndex = TagsManager.SEVERITY_VALUES.length-1;
        
        for(Dependency dependency: node.getDependsOnDependency()) {
            if(dependency.isStereotyped(IAttackTreeDesignerPeerModule.MODULE_NAME, AttackTreeStereotypes.CONNECTION)) {
                nodeHasChildren = true;
                Class child = (Class) dependency.getDependsOn();
        
                int currentMinSeverityIndex; 
        
                /*
                 * Calculate currentMinSeverityIndex depending on the child nature (Attack, reference, or else operator)
                 */
                if(child.isStereotyped(IAttackTreeDesignerPeerModule.MODULE_NAME, AttackTreeStereotypes.ATTACK)) {
                    String childSeverity = TagsManager.getElementTagParameter(child, AttackTreeStereotypes.ATTACK, AttackTreeTagTypes.SEVERITY);
                    currentMinSeverityIndex = 0;
                    for(int i=0; i<TagsManager.SEVERITY_VALUES.length; i++) {
                        if(childSeverity.equals(TagsManager.SEVERITY_VALUES[i])) {
                            currentMinSeverityIndex = i;
                            break;
                        }
                    }
                } else if(child.isStereotyped(IAttackTreeDesignerPeerModule.MODULE_NAME, AttackTreeStereotypes.TREE_REFERENCE)) {
                    Class childReference = ElementReferencing.getReferencedTree(child) ;
                    currentMinSeverityIndex = 0;
                    if(childReference != null) {
                        String childReferenceSeverity = TagsManager.getElementTagParameter(childReference, AttackTreeStereotypes.ATTACK, AttackTreeTagTypes.SEVERITY);
                        for(int i=0; i<TagsManager.SEVERITY_VALUES.length; i++) {
                            if(childReferenceSeverity.equals(TagsManager.SEVERITY_VALUES[i])) {
                                currentMinSeverityIndex = i;
                                break;
                            }
                        }
                    }
        
                } else {
                    currentMinSeverityIndex = getMinSeverityIndex(child);
                }
        
                /*
                 * update minSeverityIndex
                 */
                if( currentMinSeverityIndex < minSeverityIndex)
                    minSeverityIndex = currentMinSeverityIndex;
        
        
            }
        }
        
        if(nodeHasChildren)
            return minSeverityIndex;
        else
            return 0;
    }

}
