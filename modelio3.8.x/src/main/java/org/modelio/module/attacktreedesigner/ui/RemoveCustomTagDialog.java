package org.modelio.module.attacktreedesigner.ui;

import com.modeliosoft.modelio.javadesigner.annotations.objid;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Dialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.List;
import org.eclipse.swt.widgets.Shell;
import org.modelio.api.modelio.model.IModelingSession;
import org.modelio.api.modelio.model.ITransaction;
import org.modelio.metamodel.uml.infrastructure.ModelElement;
import org.modelio.metamodel.uml.infrastructure.TaggedValue;
import org.modelio.module.attacktreedesigner.api.AttackTreeTagTypes;
import org.modelio.module.attacktreedesigner.i18n.Messages;
import org.modelio.module.attacktreedesigner.impl.AttackTreeDesignerModule;

@objid ("b198d528-4358-43a5-af8b-ea548a040eab")
public class RemoveCustomTagDialog extends Dialog {
    @objid ("80c5caa7-b5b4-43b7-8890-d2f19856aaba")
    private static final int DEFAULT_MARGIN = 20;

    @objid ("ffaab374-a6c5-4399-ae8d-5d3bff47c3cf")
    private List customTagsList;

    @objid ("cd99800a-b911-4f4c-a144-a4a494c1ec5f")
    private Button cancelButton;

    @objid ("cb14f6f4-f6ba-40e9-95fa-cceb6f67a178")
    private Button okButton;

    @objid ("0c136a23-bbdf-4373-9c0d-a3208a9a572e")
    private ModelElement selectedElement = null;

    @objid ("2a29e6dc-8205-4e3c-969e-40552379d262")
    private Shell shell;

    @objid ("24dcfe9b-26f2-4f32-8bc7-413abf3994da")
    public RemoveCustomTagDialog(Shell parent, int style) {
        super (parent, style);
    }

    @objid ("91d1b09c-a011-49a0-848e-5b1c830322a2")
    public RemoveCustomTagDialog(Shell activeShell) {
        this (activeShell, SWT.NONE);
    }

    @objid ("d84e60b6-0a68-4715-9aa3-5d78fc6c59f4")
    public void setSelectedElement(ModelElement selectedElement) {
        this.selectedElement = selectedElement;
    }

    @objid ("ddb1013e-1079-43aa-981f-1810d02479b4")
    public void open() {
        Shell parent = getParent();
        Display display = parent.getDisplay();
        
        this.shell = new Shell(parent, SWT.DIALOG_TRIM | SWT.APPLICATION_MODAL);
        
        
        this.shell.setText("Add a new Custom Tag property");
        this.shell.setLocation(parent.getBounds().x + (parent.getBounds().width/2), parent.getBounds().y + (parent.getBounds().height/2));
        // Layout with 2 columns
        GridLayout gridLayout = new GridLayout(2, false);
        this.shell.setLayout(gridLayout);
        
        gridLayout.marginTop = DEFAULT_MARGIN/2;
        gridLayout.marginRight = DEFAULT_MARGIN;
        gridLayout.marginLeft = DEFAULT_MARGIN;
        gridLayout.marginBottom = DEFAULT_MARGIN/2;
        gridLayout.verticalSpacing = DEFAULT_MARGIN;
        
        
        // First row : label 
        Label label = new Label(this.shell, SWT.NONE);
        label.setText("Select the custom properties that you want to remove");
        GridData gridData = new GridData(SWT.FILL, SWT.TOP, true, false);
        gridData.horizontalSpan = 2;
        label.setLayoutData(gridData);
        
        // Second row : label used as a separator
        label = new Label(this.shell, SWT.SEPARATOR | SWT.HORIZONTAL);
        gridData = new GridData(SWT.FILL, SWT.TOP, true, false);
        gridData.horizontalSpan = 2;
        label.setLayoutData(gridData);
        
        // 3rd row
        label = new Label(this.shell, SWT.NULL);
        label.setText("Property name: ");
        this.customTagsList = new List (this.shell, SWT.BORDER | SWT.MULTI | SWT.V_SCROLL);
        
        for(TaggedValue attackTag : this.selectedElement.getTag()) {
            String tagDefinitionName = attackTag.getDefinition().getName();
            if(tagDefinitionName.equals(AttackTreeTagTypes.CUSTOM_TAG)) {
                this.customTagsList.add (attackTag.getActual().get(0).getValue());
            }
        }
        
        gridData = new GridData(GridData.HORIZONTAL_ALIGN_FILL);
        gridData.widthHint = 120;
        gridData.heightHint = 200;
        this.customTagsList.setLayoutData(gridData);
        
        // 4th row
        //  --> Cancel Button
        this.cancelButton = new Button(this.shell, SWT.PUSH);
        this.cancelButton.setText(Messages.getString ("Ui.Button.Cancel.Label"));
        gridData = new GridData();
        gridData.horizontalAlignment = GridData.BEGINNING;
        gridData.widthHint = 100;
        this.cancelButton.setLayoutData(gridData);
        this.cancelButton.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent e) {
                RemoveCustomTagDialog.this.shell.close();
            }
        });
        
        //  --> OK Button
        this.okButton = new Button(this.shell, SWT.PUSH);
        this.okButton.setText(Messages.getString ("Ui.Button.OK.Label"));
        gridData = new GridData();
        gridData.horizontalAlignment = GridData.END;
        gridData.widthHint = 100;
        this.okButton.setLayoutData(gridData);
        this.okButton.addSelectionListener(new SelectionAdapter() {
        
            @Override
            public void widgetSelected(SelectionEvent e) {
                if(RemoveCustomTagDialog.this.selectedElement == null) {
                    MessageDialog.openInformation(Display.getDefault().getActiveShell(), 
                            Messages.getString ("Ui.Dialog.NoSelectedElement.Label"), 
                            Messages.getString ("Ui.Dialog.noSelectedTagRemove.Message"));
                    RemoveCustomTagDialog.this.shell.close();
                } else  {
                    removeSelectedCustomTag();
                }
            }
        
        });
        
        this.shell.pack();
        
        this.shell.open();
        
        while (!this.shell.isDisposed()) {
            if (!display.readAndDispatch()) display.sleep();
        }
    }

    @objid ("cf761a1f-3c7f-4d5f-8ee6-f65c303ac274")
    private void removeSelectedCustomTag() {
        String[] selectedItems = this.customTagsList.getSelection();
        if (selectedItems.length == 0) {
            MessageDialog.openInformation(Display.getDefault().getActiveShell(), 
                    Messages.getString ("Ui.Dialog.NoSelectedElement.Label"), 
                    Messages.getString ("Ui.Dialog.NoSelectedTagRemove.Message"));
        } else {
        
            IModelingSession modelingSession = AttackTreeDesignerModule.getInstance().getModuleContext().getModelingSession ();
            
            //java.util.List<TaggedValue> tagsList = this.selectedElement.getTag();
            
            for(String selectedItem:selectedItems) {
                for(TaggedValue attackTag : this.selectedElement.getTag()) {
                    String tagDefinitionName = attackTag.getDefinition().getName();
                    if(tagDefinitionName.equals(AttackTreeTagTypes.CUSTOM_TAG)
                            && attackTag.getActual().get(0).getValue().equals(selectedItem)) {
                        try( ITransaction transaction = modelingSession.createTransaction(Messages.getString ("Info.Session.Delete", TaggedValue.MNAME))){
        
                        attackTag.delete();
                        transaction.commit ();
                    }
                        break;
                    }
                }
            }
        //            for(TaggedValue attackTag : tagsList) {
        //                String tagDefinitionName = attackTag.getDefinition().getName();
        //                if(tagDefinitionName.equals(AttackTreeTagTypes.CUSTOM_TAG)) {
        //                    for(String selectedItem:selectedItems) {
        //                        if(selectedItem.equals(attackTag.getActual().get(0).getValue())){
        //                            try( ITransaction transaction = modelingSession.createTransaction(Messages.getString ("Info.Session.Delete", TaggedValue.MNAME))){
        //
        //                                attackTag.delete();
        //                                transaction.commit ();
        //                            }
        //                            break;
        //                        }
        //                    }
        //                }
        //            }
        
            this.shell.close();
        }
    }

}
