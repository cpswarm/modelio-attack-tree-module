package org.modelio.module.attacktreedesigner.utils;

import java.io.File;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.DirectoryDialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.FileDialog;
import org.modelio.module.attacktreedesigner.conversion.schema.AttackTreeType;
import org.modelio.module.attacktreedesigner.i18n.Messages;

public class FileSystemManager {

    public static final String XML_FILE_EXTENSION = ".xml";
    
    public static String getDialogPath() {
        FileDialog dialog = new FileDialog(Display.getDefault().getActiveShell(), SWT.MULTI);
        dialog.setText(Messages.getString("Ui.Dialog.SelectDirectoryExport.Label"));
        String[] extension = { "*.xml" };
        dialog.setFilterExtensions(extension);
        if (dialog.open() != null) {
            for (String filename : dialog.getFileNames()) {
                File file = new File(dialog.getFilterPath() + File.separatorChar + filename);
                String name = file.getName();
                name = file.getName();
            }
        }

        
        return null;
                
    }
    
    /**
     * 
     * @return directory path or null if open dialog failed
     */
    public static String getDialogDirectoryPath() {
                        
        DirectoryDialog directoryDialog = new DirectoryDialog(Display.getDefault().getActiveShell());
        directoryDialog.setText(Messages.getString("Ui.Dialog.SelectDirectoryExport.Label"));
        return directoryDialog.open();        

    }

    public static File createFile(String directoryPath, String fileName) {
        File file = new File(directoryPath+"/"+fileName);
        return file;
    }
    
    
    public static void marchallJaxbContentInFile(File file, AttackTreeType tree) {
        JAXBContext jaxbContext;
        try {
            jaxbContext = JAXBContext.newInstance(AttackTreeType.class);
            Marshaller jaxbMarshaller = jaxbContext.createMarshaller();
            jaxbMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);


            jaxbMarshaller.marshal(tree, file);

        } catch (JAXBException e) {
            e.printStackTrace();
        }
    }

}
