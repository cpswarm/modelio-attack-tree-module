package org.modelio.module.attacktreedesigner.utils.elementmanager.representation;

import java.util.List;
import com.modeliosoft.modelio.javadesigner.annotations.objid;
import org.eclipse.draw2d.geometry.Rectangle;
import org.modelio.api.modelio.diagram.IDiagramGraphic;
import org.modelio.api.modelio.diagram.IDiagramHandle;
import org.modelio.api.modelio.diagram.IDiagramNode;
import org.modelio.metamodel.uml.infrastructure.ModelTree;
import org.modelio.metamodel.uml.statik.Class;
import org.modelio.module.attacktreedesigner.api.AttackTreeStereotypes;
import org.modelio.module.attacktreedesigner.api.IAttackTreeDesignerPeerModule;

@objid ("5e8afa52-c9aa-4a15-aaea-58d24a7df45b")
public class AutoLayoutManager {
    @objid ("48ab984d-acde-4187-beee-1a1a8212870a")
    public static final int VERTICAL_AUTOSPACING = 120;

    @objid ("40e45c6d-d487-449a-b733-3753d3102bf3")
    public static final int HORIZONTAL_AUTOSPACING = 120;

    @objid ("37a5e19b-9765-45a9-a037-0b90df94706a")
    private static int max(int n1, int n2) {
        if( n1 > n2)
            return n1;
        else 
            return n2;
    }

    @objid ("d66e46be-48c2-4eb6-a3de-798c27e69a40")
    public static Rectangle autolayoutSubTree(IDiagramHandle diagramHandle, Class element, IDiagramNode elementNodeGraphic, Rectangle elementBounds, List<Integer> levelsLeftLimitX, int level) {
        int childrenWidth = elementBounds.width;
        int firstChildrenX = elementBounds.x;
        
        List<Class> elementChildren = ((ModelTree)element).getOwnedElement(Class.class);
        int numberOfChildren = elementChildren.size();
        
        /*
         * loop children
         */
        if(! elementChildren.isEmpty()) {
            Class child = elementChildren.get(0);
        
            List<IDiagramGraphic> childDiagramGraphics = diagramHandle.getDiagramGraphics(child);
            if(! childDiagramGraphics.isEmpty()) {
                IDiagramNode childNodeGraphic = (IDiagramNode) childDiagramGraphics.get(0);
                
                Rectangle childNodeBounds = childNodeGraphic.getBounds();
                
                updateChildBounds(elementBounds, levelsLeftLimitX, level, numberOfChildren, childNodeBounds);
        
                firstChildrenX = childNodeBounds.x;
                Rectangle newBounds = autolayoutSubTree(diagramHandle, child, childNodeGraphic, childNodeBounds, levelsLeftLimitX, level + 1);
                childrenWidth = newBounds.width;
            }
            
            for(int i = 1; i < numberOfChildren; i++) {
        
                child = elementChildren.get(i);
                childDiagramGraphics = diagramHandle.getDiagramGraphics(child);
                
                if(! childDiagramGraphics.isEmpty()) {
                    IDiagramNode childNodeGraphic = (IDiagramNode) childDiagramGraphics.get(0);
                    
                    Rectangle childNodeBounds = childNodeGraphic.getBounds();
                    
                    
                    updateChildBounds(elementBounds, levelsLeftLimitX, level, numberOfChildren, childNodeBounds);
                    
                    
                    
                    Rectangle newBounds = autolayoutSubTree(diagramHandle, child, childNodeGraphic, childNodeBounds, levelsLeftLimitX, level + 1);
                    
                    childrenWidth = childrenWidth + HORIZONTAL_AUTOSPACING + newBounds.width ;
                    
                }
            }
            
        }
        
        
        if(! element.isStereotyped(IAttackTreeDesignerPeerModule.MODULE_NAME, AttackTreeStereotypes.ROOT)) {
            elementBounds.setX( firstChildrenX + ((childrenWidth - elementBounds.width)/2) );
        }
        
        
        elementNodeGraphic.setBounds(elementBounds);
        return elementBounds;
    }

    @objid ("992247c3-8d3b-4b49-9833-9c63a0e81506")
    public static void updateChildBounds(Rectangle elementBounds, List<Integer> levelsLeftLimitX, int level, int numberOfChildren, Rectangle childNodeBounds) {
        // Child SetY
        childNodeBounds.setY(elementBounds.y + VERTICAL_AUTOSPACING - (childNodeBounds.height/2));
              
        
        // child setX
        if (levelsLeftLimitX.size() <= level) {
            
            // leftLimitX has not been set yet in this level
            childNodeBounds.setX(elementBounds.x + (elementBounds.width / 2) - 
                    ((
                            HORIZONTAL_AUTOSPACING * (numberOfChildren - 1) 
                            + 
                            childNodeBounds.width 
                            * numberOfChildren 
                            ) / 2));
              
            levelsLeftLimitX.add(level, childNodeBounds.x + childNodeBounds.width + HORIZONTAL_AUTOSPACING);
        } else {
            
            // leftLimitX has been set in this level
            childNodeBounds.setX(
                    max(
                            levelsLeftLimitX.get(level)
                            , 
                            elementBounds.x + (elementBounds.width / 2) - 
                            ((
                                    HORIZONTAL_AUTOSPACING * (numberOfChildren - 1) + 
                                    childNodeBounds.width 
                                    * numberOfChildren 
                                    ) / 2))
                    );
            levelsLeftLimitX.set(level, childNodeBounds.x + childNodeBounds.width + HORIZONTAL_AUTOSPACING);
        }
    }

}
