package org.modelio.module.attacktreedesigner.utils;

import java.io.File;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import com.modeliosoft.modelio.javadesigner.annotations.objid;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.DirectoryDialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.FileDialog;
import org.modelio.module.attacktreedesigner.conversion.schema.AttackTreeType;
import org.modelio.module.attacktreedesigner.i18n.Messages;

@objid ("6d159f89-8a48-4267-9397-10134922be3b")
public class FileSystemManager {
    @objid ("10adea65-4deb-4025-a682-a99fe8ecb198")
    public static final String XML_FILE_EXTENSION = ".xml";

    @objid ("26722ba7-d008-40df-8db2-5dbdc545a808")
    public static final String PATH_SEPARATOR = "/";

    @objid ("31e97337-6289-47d2-ba4e-52fdb4c4fe8c")
    public static final String PATH_PREDECESSOR = "..";

    @objid ("e4580c8f-106a-4175-9d9a-d1c78bf3f018")
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
     * @return directory path or null if open dialog failed
     */
    @objid ("b3c3f28e-19ac-4c50-a0af-dc516d572e55")
    public static String getDialogDirectoryPath() {
        DirectoryDialog directoryDialog = new DirectoryDialog(Display.getDefault().getActiveShell());
        directoryDialog.setText(Messages.getString("Ui.Dialog.SelectDirectoryExport.Label"));
        return directoryDialog.open();
    }

    @objid ("0abac3d2-3692-427e-a77f-9395a26e9e8e")
    public static File createFile(String directoryPath, String fileName) {
        File directory = new File(directoryPath);
        directory.mkdirs();
        File file = new File(directory, fileName);
        //File file = new File(directoryPath+PATH_SEPARATOR+fileName);
        return file;
    }

    @objid ("22a7348f-632f-458c-8048-4915214ab5a0")
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
