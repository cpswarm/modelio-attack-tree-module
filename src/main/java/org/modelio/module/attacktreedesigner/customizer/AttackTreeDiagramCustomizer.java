package org.modelio.module.attacktreedesigner.customizer;

import java.util.List;
import java.util.Map;
import com.modeliosoft.modelio.javadesigner.annotations.objid;
import org.eclipse.gef.palette.MarqueeToolEntry;
import org.eclipse.gef.palette.PaletteDrawer;
import org.eclipse.gef.palette.PaletteEntry;
import org.eclipse.gef.palette.PaletteRoot;
import org.eclipse.gef.palette.SelectionToolEntry;
import org.modelio.api.modelio.diagram.IDiagramCustomizer;
import org.modelio.api.modelio.diagram.IDiagramService;
import org.modelio.api.module.IModule;
import org.modelio.module.attacktreedesigner.i18n.Messages;
import org.modelio.module.attacktreedesigner.impl.AttackTreeDesignerModule;
import org.modelio.module.attacktreedesigner.utils.IAttackTreeCustomizerPredefinedField;
import org.modelio.module.attacktreedesigner.utils.IDiagramCustomizerPredefinedField;

/**
 * This class handles the palette configuration of SysML block diagram
 * @author ebrosse
 */
@objid ("5a0c2f27-1450-435c-8e32-0baf5103b679")
public class AttackTreeDiagramCustomizer implements IDiagramCustomizer {
    @objid ("b763e0bc-5e9b-486c-896b-1e79ff2ae4ca")
    @Override
    public void fillPalette(PaletteRoot paletteRoot) {
        IDiagramService toolRegistry = AttackTreeDesignerModule.getInstance().getModuleContext().getModelioServices().getDiagramService();
        
        final PaletteDrawer commonGroup = new PaletteDrawer("Default", null);
        commonGroup.setInitialState(PaletteDrawer.INITIAL_STATE_OPEN);
        commonGroup.add(new SelectionToolEntry());
        commonGroup.add(new MarqueeToolEntry());
        paletteRoot.add(commonGroup);
        
        paletteRoot.add(this.createTreeGroup(toolRegistry));
        paletteRoot.add(this.createDefaultNotesGroup(toolRegistry));
        paletteRoot.add(this.createDefaultFreeDrawingGroup(toolRegistry));
    }

    @objid ("afed292a-8eec-4200-8d61-c57146011c8d")
    protected PaletteEntry createDefaultFreeDrawingGroup(final IDiagramService toolRegistry) {
        final PaletteDrawer group = new PaletteDrawer(Messages.getString("SysMLPaletteGroup.Freedrawing"), null);
        
        group.add(toolRegistry.getRegisteredTool(IDiagramCustomizerPredefinedField.DrawingRectangle));
        group.add(toolRegistry.getRegisteredTool(IDiagramCustomizerPredefinedField.DrawingEllipse));
        group.add(toolRegistry.getRegisteredTool(IDiagramCustomizerPredefinedField.DrawingText));
        group.add(toolRegistry.getRegisteredTool(IDiagramCustomizerPredefinedField.DrawingLine));
        
        group.setInitialState(PaletteDrawer.INITIAL_STATE_OPEN);
        return group;
    }

    @objid ("8e0c2a1a-b308-4084-87b2-2eb56d6515b9")
    protected PaletteEntry createDefaultNotesGroup(final IDiagramService toolRegistry) {
        final PaletteDrawer group = new PaletteDrawer(Messages.getString("SysMLPaletteGroup.NotesAndConstraints"), null);
        
        group.add(toolRegistry.getRegisteredTool(IDiagramCustomizerPredefinedField.Note));
        group.add(toolRegistry.getRegisteredTool(IDiagramCustomizerPredefinedField.Constraint));
        group.add(toolRegistry.getRegisteredTool(IDiagramCustomizerPredefinedField.ExternDocument));
        group.setInitialState(PaletteDrawer.INITIAL_STATE_OPEN);
        return group;
    }

    @objid ("5c0fdacf-103f-4b1d-af08-68727854c72e")
    private PaletteEntry createTreeGroup(final IDiagramService toolRegistry) {
        final PaletteDrawer group = new PaletteDrawer(Messages.getString("AttackTreePaletteGroup.Tree"), null);
        group.add(toolRegistry.getRegisteredTool(IAttackTreeCustomizerPredefinedField.EVENT));
        group.add(toolRegistry.getRegisteredTool(IAttackTreeCustomizerPredefinedField.AND));
        group.add(toolRegistry.getRegisteredTool(IAttackTreeCustomizerPredefinedField.OR));
        group.add(toolRegistry.getRegisteredTool(IDiagramCustomizerPredefinedField.Dependency));
        
        group.setInitialState(PaletteDrawer.INITIAL_STATE_OPEN);
        return group;
    }

    @objid ("02f9f7c0-c2ad-4fc1-8840-c2099e523a51")
    @Override
    public boolean keepBasePalette() {
        return false;
    }

    @objid ("f6c8d9a3-169a-4a25-b88c-df34d5499d2a")
    @Override
    public void initialize(IModule module, List<org.modelio.api.modelio.diagram.tools.PaletteEntry> tools, Map<String, String> hParameters, boolean keepBasePalette) {
    }

    @objid ("a19426f8-075c-4d7a-a023-6675ebd40492")
    @Override
    public Map<String, String> getParameters() {
        return null;
    }

}
