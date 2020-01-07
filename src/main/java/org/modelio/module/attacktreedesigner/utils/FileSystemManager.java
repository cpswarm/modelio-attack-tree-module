package org.modelio.module.attacktreedesigner.utils;

import java.io.File;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import com.modeliosoft.modelio.javadesigner.annotations.objid;
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

    @objid ("46ffe6e4-70b3-4b52-ae51-55a16dcb7887")
    public static final String XML_FILE_WILDCARD = "*.xml";

    @objid ("e4580c8f-106a-4175-9d9a-d1c78bf3f018")
    public static String getXMLFileDialogPath() {
        FileDialog fileDialog = new FileDialog(Display.getDefault().getActiveShell());
        fileDialog.setText(Messages.getString("Ui.Dialog.SelectXMLFileImport.Label"));
        String[] extension = { XML_FILE_WILDCARD };
        fileDialog.setFilterExtensions(extension);
        return fileDialog.open();
    }

    /**
     * @return directory path or null if open dialog failed
     */
    @objid ("b3c3f28e-19ac-4c50-a0af-dc516d572e55")
    public static String getDialogDirectoryPath(String dialogText) {
        DirectoryDialog directoryDialog = new DirectoryDialog(Display.getDefault().getActiveShell());
        directoryDialog.setText(dialogText);
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
    public static void marshallJaxbContentInFile(File file, AttackTreeType tree) {
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

    @objid ("fe3be8c8-a80a-46a9-86b2-e755c7ab923a")
    public static AttackTreeType unmarshallFileToJaxb(File file) {
        JAXBContext jaxbContext;
        try {
            jaxbContext = JAXBContext.newInstance(AttackTreeType.class);
            Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
            return (AttackTreeType) jaxbUnmarshaller.unmarshal(file);
            
        } catch (JAXBException e) {
            e.printStackTrace();
        }
        return null;
    }

}
