package org.modelio.module.attacktreedesigner.utils.elementmanager.representation;

import com.modeliosoft.modelio.javadesigner.annotations.objid;

@objid ("3e8931b9-f960-4114-9cc5-f7f89fb94884")
public enum DiagramElementStyle {
    OPERATOR ("NONE", "IMAGE"),
    ATTACK ("SIMPLE", "SIMPLE"),
    TREE_REFERENCE ("SIMPLE", "STRUCTURED");

    @objid ("94a929ff-c998-4705-b409-0a0d70ac3828")
    private String showNameProperty;

    @objid ("68cbadd8-3bc2-47c0-a525-f9e2c59bce62")
    private String representationMode;

    @objid ("8c8cbbc4-cd79-438d-a215-60fc0caad7b8")
    DiagramElementStyle(String showNameProperty, String representationMode) {
        this.setShowNameProperty(showNameProperty);
        this.setRepresentationMode(representationMode);
    }

    @objid ("5ffefbd3-9fc3-45a2-9218-e123322e3370")
    public String getShowNameProperty() {
        return this.showNameProperty;
    }

    @objid ("fbf8b316-b40a-40ec-8996-ba1ea0712758")
    public void setShowNameProperty(String showNameProperty) {
        this.showNameProperty = showNameProperty;
    }

    @objid ("f839e669-d94f-4de0-96dc-06d2b4723a11")
    public String getRepresentationMode() {
        return this.representationMode;
    }

    @objid ("6ed6f309-e864-4fde-9e4e-ed2c19016ed0")
    public void setRepresentationMode(String representationMode) {
        this.representationMode = representationMode;
    }

}
