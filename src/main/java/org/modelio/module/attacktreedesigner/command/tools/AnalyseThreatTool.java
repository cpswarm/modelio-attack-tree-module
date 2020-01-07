package org.modelio.module.attacktreedesigner.command.tools;

import com.modeliosoft.modelio.javadesigner.annotations.objid;
import org.modelio.api.modelio.diagram.IDiagramGraphic;
import org.modelio.api.modelio.diagram.IDiagramHandle;
import org.modelio.api.modelio.diagram.IDiagramLink.LinkRouterKind;
import org.modelio.api.modelio.diagram.ILinkPath;
import org.modelio.api.modelio.diagram.tools.DefaultLinkTool;
import org.modelio.api.modelio.model.IModelingSession;
import org.modelio.api.modelio.model.ITransaction;
import org.modelio.metamodel.uml.infrastructure.Dependency;
import org.modelio.metamodel.uml.infrastructure.ModelTree;
import org.modelio.metamodel.uml.statik.Class;
import org.modelio.module.attacktreedesigner.api.AttackTreeStereotypes;
import org.modelio.module.attacktreedesigner.api.IAttackTreeDesignerPeerModule;
import org.modelio.module.attacktreedesigner.i18n.Messages;
import org.modelio.module.attacktreedesigner.impl.AttackTreeDesignerModule;
import org.modelio.module.attacktreedesigner.utils.elementmanager.representation.ElementRepresentationManager;
import org.modelio.vcore.smkernel.mapi.MObject;

@objid ("c6403e03-57e0-47d9-b7cd-96222e112cfd")
public class AnalyseThreatTool extends DefaultLinkTool {
    @objid ("b6e164d2-8c72-4980-9990-7b96e38a044a")
    @Override
    public boolean acceptFirstElement(IDiagramHandle diagramHandle, IDiagramGraphic targetNode) {
        MObject firstElement = targetNode.getElement();
        return  (firstElement instanceof Class 
                && ((Class) firstElement).isStereotyped(IAttackTreeDesignerPeerModule.MODULE_NAME, AttackTreeStereotypes.ATTACK));
    }

    @objid ("8960d2a6-62bf-4294-b4cd-97c966d037f4")
    @Override
    public boolean acceptSecondElement(IDiagramHandle diagramHandle, IDiagramGraphic sourceNode, IDiagramGraphic targetNode) {
        return true;
    }

    @objid ("df149cfa-7157-4d6e-8174-b25e2e723014")
    @Override
    public void actionPerformed(IDiagramHandle diagramHandle, IDiagramGraphic originNode, IDiagramGraphic targetNode, LinkRouterKind touterType, ILinkPath path) {
        createThreatAnalysisDependency(diagramHandle, originNode, targetNode);
    }

    @objid ("a33eb854-2e9d-4da1-9551-e6a46c59333e")
    public static Dependency createThreatAnalysisDependency(IDiagramHandle diagramHandle, IDiagramGraphic originNode, IDiagramGraphic targetNode) {
        Dependency threatAnalysisDependency = null;
        
        IModelingSession session = AttackTreeDesignerModule.getInstance().getModuleContext().getModelingSession();
        try( ITransaction transaction = session.createTransaction (Messages.getString ("Info.Session.Create", AttackTreeStereotypes.THREAT_ANALYSIS))){
        
        
            ModelTree firstElement = (ModelTree) originNode.getElement();
            ModelTree secondElement = (ModelTree) targetNode.getElement();
                
            threatAnalysisDependency = session.getModel().createDependency(firstElement, 
                    secondElement, 
                    IAttackTreeDesignerPeerModule.MODULE_NAME, 
                    AttackTreeStereotypes.THREAT_ANALYSIS); 
            diagramHandle.unmask(threatAnalysisDependency, 0, 0);
        
        
            diagramHandle.save();
            diagramHandle.close();
        
            session.getModel().getDefaultNameService().setDefaultName(threatAnalysisDependency, AttackTreeStereotypes.THREAT_ANALYSIS);
        
            ElementRepresentationManager.setThreatAnalysisDependencyStyle(threatAnalysisDependency);
            transaction.commit ();
        }
        return threatAnalysisDependency;
    }

}
