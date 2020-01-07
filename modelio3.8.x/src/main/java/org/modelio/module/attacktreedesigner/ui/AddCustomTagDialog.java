package org.modelio.module.attacktreedesigner.ui;

import java.util.List;
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
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.modelio.api.modelio.model.IModelingSession;
import org.modelio.api.modelio.model.ITransaction;
import org.modelio.metamodel.uml.infrastructure.ModelElement;
import org.modelio.metamodel.uml.infrastructure.TagParameter;
import org.modelio.metamodel.uml.infrastructure.TaggedValue;
import org.modelio.module.attacktreedesigner.api.AttackTreeTagTypes;
import org.modelio.module.attacktreedesigner.i18n.Messages;
import org.modelio.module.attacktreedesigner.impl.AttackTreeDesignerModule;
import org.modelio.module.attacktreedesigner.utils.elementmanager.tags.TagsManager;

@objid ("fa5ccbc2-b004-464c-9d3b-e5988e41ddf0")
public class AddCustomTagDialog extends Dialog {
    @objid ("44c04157-b290-418f-b57a-f4b3f64a6de1")
    private static final int DEFAULT_MARGIN = 20;

    @objid ("d3b847ac-9a2f-41a9-9097-e43ad94d4b81")
    private Text propertyNameText;

    @objid ("605f0966-f49b-49d8-91cc-0d9c82b55fbf")
    private Text propertyValueText;

    @objid ("53d1de15-3bd9-4080-ba82-427c8bde0e5f")
    private Button cancelButton;

    @objid ("7565279b-6bf6-4b79-b35f-689386ade49c")
    private Button okButton;

    @objid ("2349e348-6e45-4f52-bdf3-3709a363968b")
    private ModelElement selectedElement = null;

    @objid ("b0646f29-0272-4b85-8b9a-1f1af0228bed")
    private Shell shell;

    @objid ("a146e292-6fc7-4b85-8999-b48e70306552")
    public AddCustomTagDialog(Shell parent, int style) {
        super (parent, style);
    }

    @objid ("5feba6d3-0113-4600-98df-5135f9ac554d")
    public AddCustomTagDialog(Shell activeShell) {
        this (activeShell, SWT.NONE);
    }

    @objid ("28eeda15-8a11-4246-9291-ad2d48e76639")
    public void setSelectedElement(ModelElement selectedElement) {
        this.selectedElement = selectedElement;
    }

    @objid ("d8ba66d4-d5b9-4d29-88c7-b811e195902a")
    public void open() {
        Shell parent = getParent();
        Display display = parent.getDisplay();
        
        this.shell = new Shell(parent, SWT.DIALOG_TRIM | SWT.APPLICATION_MODAL);
        
        
        this.shell.setText(Messages.getString ("Ui.Dialog.AddCustomTag.Label"));
        this.shell.setLocation(parent.getBounds().x + (parent.getBounds().width/2), parent.getBounds().y + (parent.getBounds().height/2));
        // Layout with 2 columns
        GridLayout gridLayout = new GridLayout(2, false);
        this.shell.setLayout(gridLayout);
        
        gridLayout.marginTop = DEFAULT_MARGIN/2;
        gridLayout.marginRight = DEFAULT_MARGIN;
        gridLayout.marginLeft = DEFAULT_MARGIN;
        gridLayout.marginBottom = DEFAULT_MARGIN/2;
        gridLayout.verticalSpacing = DEFAULT_MARGIN/2;
        
        
        // First row : label 
        Label label = new Label(this.shell, SWT.NONE);
        label.setText(Messages.getString ("Ui.Dialog.CustomTagName.Label"));
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
        label.setText(Messages.getString ("Ui.Field.CustomTagName.Label"));
        this.propertyNameText = new Text(this.shell, SWT.SINGLE | SWT.BORDER);
        gridData = new GridData(GridData.HORIZONTAL_ALIGN_FILL);
        gridData.widthHint = 200;
        this.propertyNameText.setLayoutData(gridData);
        
        
        // 4th row
        label = new Label(this.shell, SWT.NULL);
        label.setText(Messages.getString ("Ui.Field.CustomTagValue.Label"));
        this.propertyValueText = new Text(this.shell, SWT.SINGLE | SWT.BORDER);
        gridData = new GridData(GridData.HORIZONTAL_ALIGN_FILL);
        gridData.widthHint = 200;
        this.propertyValueText.setLayoutData(gridData);
        
        // 5th row
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
                AddCustomTagDialog.this.shell.close();
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
                if(AddCustomTagDialog.this.selectedElement == null) {
                    MessageDialog.openInformation(Display.getDefault().getActiveShell(), 
                            Messages.getString ("Ui.Dialog.NoSelectedElement.Label"), 
                            Messages.getString ("Ui.Dialog.NoSelectedElementAdd.Message"));
                    AddCustomTagDialog.this.shell.close();
                } else if (AddCustomTagDialog.this.propertyNameText.getText().isEmpty()) {
                    MessageDialog.openInformation(Display.getDefault().getActiveShell(), 
                            Messages.getString ("Ui.Dialog.EmptyName.Label"), 
                            Messages.getString ("Ui.Dialog.EmptyName.Message"));
                    
                } else if (AddCustomTagDialog.this.propertyValueText.getText().isEmpty()) {
                    MessageDialog.openInformation(Display.getDefault().getActiveShell(), 
                            Messages.getString ("Ui.Dialog.EmptyValue.Label"), 
                            Messages.getString ("Ui.Dialog.EmptyValue.Message"));
                } else  {
                    addNewCustomTag();
                }
            }
        
        });
        
        this.shell.pack();
        
        this.shell.open();
        
        while (!this.shell.isDisposed()) {
            if (!display.readAndDispatch()) display.sleep();
        }
    }

    @objid ("08fbc412-e6c1-42eb-8fd6-0b144e7f84b8")
    private void addNewCustomTag() {
        IModelingSession modelingSession = AttackTreeDesignerModule.getInstance().getModuleContext().getModelingSession ();
        
        boolean alreadyExistingTag = false;
        List<TaggedValue> attackTags = AddCustomTagDialog.this.selectedElement.getTag();
        for(TaggedValue attackTag:attackTags) {
            String tagDefinitionName = attackTag.getDefinition().getName();
            if(tagDefinitionName.equals(AttackTreeTagTypes.CUSTOM_TAG)) {
                List<TagParameter> tagParameters = attackTag.getActual();
                if(tagParameters.get(0).getValue().equals(AddCustomTagDialog.this.propertyNameText.getText())) {
                    alreadyExistingTag = true;
                    break;
                }
            }
        }
        if(alreadyExistingTag) {
            MessageDialog.openInformation(Display.getDefault().getActiveShell(), 
                    Messages.getString ("Ui.Dialog.existingName.Label"), 
                    Messages.getString ("Ui.Dialog.existingName.Message"));
        } else {
            try( ITransaction transaction = modelingSession.createTransaction(Messages.getString ("Info.Session.Create", TaggedValue.MNAME))){
        
                TagsManager.createCustomTag(modelingSession, AddCustomTagDialog.this.selectedElement, 
                        AddCustomTagDialog.this.propertyNameText.getText(), 
                        AddCustomTagDialog.this.propertyValueText.getText());
        
                transaction.commit ();
            }
            AddCustomTagDialog.this.shell.close();
        }
    }

}
