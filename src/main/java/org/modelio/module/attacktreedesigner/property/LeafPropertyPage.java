package org.modelio.module.attacktreedesigner.property;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import com.modeliosoft.modelio.javadesigner.annotations.objid;
import org.modelio.api.modelio.diagram.IDiagramGraphic;
import org.modelio.api.modelio.diagram.IDiagramHandle;
import org.modelio.api.modelio.diagram.IDiagramNode;
import org.modelio.api.modelio.diagram.IDiagramService;
import org.modelio.api.module.context.IModuleContext;
import org.modelio.api.module.propertiesPage.IModulePropertyTable;
import org.modelio.metamodel.diagrams.AbstractDiagram;
import org.modelio.metamodel.uml.infrastructure.Element;
import org.modelio.metamodel.uml.infrastructure.ModelElement;
import org.modelio.metamodel.uml.infrastructure.ModelTree;
import org.modelio.metamodel.uml.statik.Attribute;
import org.modelio.metamodel.uml.statik.Class;
import org.modelio.metamodel.uml.statik.Classifier;
import org.modelio.metamodel.uml.statik.GeneralClass;
import org.modelio.module.attacktreedesigner.api.AttackTreeStereotypes;
import org.modelio.module.attacktreedesigner.api.IAttackTreeDesignerPeerModule;
import org.modelio.module.attacktreedesigner.i18n.Messages;
import org.modelio.module.attacktreedesigner.impl.AttackTreeDesignerModule;
import org.modelio.module.attacktreedesigner.impl.AttackTreeDesignerPeerModule;
import org.modelio.module.attacktreedesigner.utils.DiagramElementStyle;
import org.modelio.module.attacktreedesigner.utils.Labels;
import org.modelio.module.attacktreedesigner.utils.elementmanager.ElementNavigationManager;

@objid ("c07b6144-87dc-44fc-ab74-b757ac83385b")
public class LeafPropertyPage implements IPropertyContent {
    @objid ("84816d5d-f184-41cc-b99b-60734a124dea")
    private static final String REF_DEFAULT_NAME = "ref";

    @objid ("2f6d2394-45bb-46cd-ab6d-2320b7627afa")
    private static List<Class> _roots = null;

    @objid ("615dd24b-759f-4c1d-84de-a4195d62bc88")
    private List<String> getAvailableTreesNames() {
        List<String> result = new ArrayList<>();
        for(Class root : _roots) {           
            result.add(root.getOwner().getName() + "::" +  root.getName());        
        }
        return result;
    }

    @objid ("0433e36d-81f0-4470-9c10-d38aa5ac93f2")
    private String getReferencedTreeName(Class selectedElement) {
        List<Attribute> attributes = selectedElement.getOwnedAttribute();
        
        for(Attribute attribute : attributes) {
            if (attribute.isStereotyped(IAttackTreeDesignerPeerModule.MODULE_NAME, AttackTreeStereotypes.TREE_REFERENCE_ATTRIBUTE)) {
                GeneralClass type = attribute.getType();
                if ((type != null)
                        && (type.isStereotyped(IAttackTreeDesignerPeerModule.MODULE_NAME, AttackTreeStereotypes.ROOT))){
                    return getStandardName(type);
                }
            }
        }
        return "";
    }

    /**
     * @param selectedElement
     * @return List of trees roots contained in the same package as the root of the selectedElement
     */
    @objid ("e02263a0-514c-4838-a9de-ff7d78dfc4d1")
    private void getAvailableTrees(ModelTree selectedElement) {
        _roots = new ArrayList<>();
        
        ModelTree rootElement = ElementNavigationManager.getRootElement(selectedElement);
        
        for(Class clazz: AttackTreeDesignerModule.getInstance().getModuleContext().getModelingSession().findByClass(Class.class)) {
            if(clazz.isStereotyped(IAttackTreeDesignerPeerModule.MODULE_NAME, AttackTreeStereotypes.ROOT) 
                    && (!(clazz.equals(rootElement)))) {
                _roots.add(clazz);
            }
        }
    }

    @objid ("4f6829d3-d747-43cf-aa8b-497037a4fea1")
    private Attribute getRefAttribute(Classifier leaf) {
        for (Attribute attr : leaf.getOwnedAttribute()) {
            if (attr.isStereotyped(IAttackTreeDesignerPeerModule.MODULE_NAME, AttackTreeStereotypes.TREE_REFERENCE_ATTRIBUTE))
                return attr;
        }
        return null;
    }

    @objid ("d365e25a-81ea-4302-a0ed-dbe99059bc52")
    private void addReference(Classifier selectedElement, String referencedTreeName) {
        IModuleContext moduleContext = AttackTreeDesignerModule.getInstance().getModuleContext();
        
        //Get Referenced Tree
        GeneralClass root = null;       
        if (!(referencedTreeName.equals("")))
            root = getRootByName(referencedTreeName);
        
        if (root != null) {
        
            //Get reference attribute if exist
            Attribute ref = getRefAttribute(selectedElement);
            if (ref == null) {
        
                Attribute referenceTreeAttribute = moduleContext.getModelingSession().getModel().createAttribute(
                        REF_DEFAULT_NAME,
                        root, 
                        selectedElement,                      
                        IAttackTreeDesignerPeerModule.MODULE_NAME, 
                        AttackTreeStereotypes.TREE_REFERENCE_ATTRIBUTE); 
        
                
                selectedElement.addStereotype(AttackTreeDesignerPeerModule.MODULE_NAME, AttackTreeStereotypes.TREE_REFERENCE_DECORATION);
                
                
                changeStyleToReferencedTree(selectedElement, moduleContext, referenceTreeAttribute);
             
            }else { 
                ref.setType(root);
            }
        }else {
            List<Attribute> atts = selectedElement.getOwnedAttribute();
        
            Iterator<Attribute> iterator = atts.iterator();
        
            while ( iterator.hasNext() ) {
                Attribute att = iterator.next();
                if (att.isStereotyped( IAttackTreeDesignerPeerModule.MODULE_NAME, 
                        AttackTreeStereotypes.TREE_REFERENCE_ATTRIBUTE)) {
                    iterator.remove();
                    att.delete();
                }
            }
            
            selectedElement.removeStereotypes(IAttackTreeDesignerPeerModule.MODULE_NAME, AttackTreeStereotypes.TREE_REFERENCE_DECORATION);
            
            changeStyleToAttack(selectedElement, moduleContext);
        }
    }

    @objid ("137c2e63-838a-4482-ad05-1c1febf5a1a6")
    private GeneralClass getRootByName(String referencedTreeName) {
        for (Class root : _roots) {
            if (getStandardName(root).equals(referencedTreeName))
                return root;
        }
        return null;
    }

    @objid ("e1064e8b-c2e6-4bcb-92c7-d3afbbc87632")
    private String getStandardName(GeneralClass root) {
        return root.getOwner().getName() + "::" + root.getName();
    }

    @objid ("e757e838-6b1d-4b74-a4a3-f44158a6f7f6")
    @Override
    public void changeProperty(ModelElement element, int row, String value) {
        if (row == 1) {
            addReference((Classifier) element, value);
        }
    }

    @objid ("0ead49a6-f4d8-4b85-8627-a1ba8698ba53")
    @Override
    public void update(ModelElement element, IModulePropertyTable table) {
        Class leaf = (Class) element;
        getAvailableTrees(leaf);
        
        // add referenced tree and show available trees
        List<String> trees = new ArrayList<>();
        trees.add("");
        trees.addAll(getAvailableTreesNames());
        
        String[] availableTrees = trees.toArray(new String[0]);
        String value = getReferencedTreeName(leaf);
        table.addProperty (Messages.getString("Ui.Property.Reference.Name"), value, availableTrees);
    }

    @objid ("094160c1-5f18-444b-a31f-bb22040bba8d")
    private void changeStyleToAttack(Classifier selectedElement, IModuleContext moduleContext) {
        IDiagramService diagramService = moduleContext.getModelioServices().getDiagramService();            
        
        List<AbstractDiagram> diagrams = ((Element) selectedElement).getDiagramElement(AbstractDiagram.class);
        
        for(AbstractDiagram diagram:diagrams) {
            try(  IDiagramHandle diagramHandle = diagramService.getDiagramHandle(diagram);){
                
                List<IDiagramGraphic> diagramGraphics = diagramHandle.getDiagramGraphics(selectedElement);
                if( diagramGraphics.size()>0 && diagramGraphics.get(0) instanceof IDiagramNode) {
                    
                    IDiagramNode diagramNode = (IDiagramNode )diagramGraphics.get(0);
                    diagramNode.setProperty(Labels.CLASS_REPRES_MODE.name(), DiagramElementStyle.ATTACK.getRepresentationMode());
                    
                }                        
                
                diagramHandle.save();
                diagramHandle.close();
            }
        
        }
    }

    @objid ("5f02bade-e59b-4196-8e9d-9734fa78a00a")
    private void changeStyleToReferencedTree(Classifier selectedElement, IModuleContext moduleContext, Attribute referenceTreeAttribute) {
        IDiagramService diagramService = moduleContext.getModelioServices().getDiagramService();            
        
        List<AbstractDiagram> diagrams = ((Element) selectedElement).getDiagramElement(AbstractDiagram.class);
        
        for(AbstractDiagram diagram:diagrams) {
            try(  IDiagramHandle diagramHandle = diagramService.getDiagramHandle(diagram);){
                
                List<IDiagramGraphic> diagramGraphics = diagramHandle.getDiagramGraphics(selectedElement);
                if( diagramGraphics.size()>0 && diagramGraphics.get(0) instanceof IDiagramNode) {
                    
                    IDiagramNode diagramNode = (IDiagramNode )diagramGraphics.get(0);
                    diagramNode.setProperty(Labels.CLASS_REPRES_MODE.name(), DiagramElementStyle.REFERENCED_TREE.getRepresentationMode());
                    
                }                        
                
                diagramHandle.unmask(referenceTreeAttribute,0,0);
        
                diagramHandle.save();
                diagramHandle.close();
            }
        
        }
    }

}
