package org.modelio.module.attacktreedesigner.utils;

import java.util.List;
import com.modeliosoft.modelio.javadesigner.annotations.objid;
import org.eclipse.draw2d.geometry.Rectangle;
import org.modelio.api.modelio.diagram.IDiagramGraphic;
import org.modelio.api.modelio.diagram.IDiagramHandle;
import org.modelio.api.modelio.diagram.IDiagramNode;
import org.modelio.metamodel.uml.infrastructure.ModelTree;
import org.modelio.metamodel.uml.statik.Class;
import org.modelio.vcore.smkernel.mapi.MObject;

@objid ("d5db8112-c267-4c13-8caf-155044ac5883")
public class AutoLayoutManager {
    @objid ("6ff0e552-b437-4437-8c65-38c5b1b8a97e")
    public static final int VERTICAL_AUTOSPACING = 120;

    @objid ("72f31cc0-6a29-48af-ba01-951baf54a63e")
    public static final int HORIZONTAL_AUTOSPACING = 240;

    @objid ("0e85846e-1532-46f6-8c7f-0bd53ff0d6b4")
    public static void autolayoutChildren(IDiagramHandle diagramHandle, MObject element, IDiagramNode nodeGraphic, Rectangle bounds) {
        nodeGraphic.setBounds(bounds);
        
        
        
        
        
        //        List<? extends MObject> elementChildren = element.getCompositionChildren();
        List<Class> elementChildren = ((ModelTree)element).getOwnedElement(Class.class);
        //        int numberOfChildren = elementChildren.size();
        int numberOfChildren = elementChildren.size();
        for(int i=0; i<numberOfChildren ; i++) {
            MObject child = elementChildren.get(i);
            List<IDiagramGraphic> childDiagramGraphics = diagramHandle.getDiagramGraphics(child);
        
            if(! childDiagramGraphics.isEmpty()) {
        
                IDiagramNode childNodeGraphic = (IDiagramNode) childDiagramGraphics.get(0);
        
        
                Rectangle newBounds = childNodeGraphic.getBounds().getCopy();
                newBounds.setY(bounds.y + VERTICAL_AUTOSPACING - (newBounds.height/2));
                newBounds.setX(bounds.x 
                        + ((i-(numberOfChildren/2)) * HORIZONTAL_AUTOSPACING) - (newBounds.width/2) 
                        + (bounds.width/2));
                autolayoutChildren(diagramHandle, child, childNodeGraphic, newBounds) ;
        
            }
        
        
        }
    }

}
