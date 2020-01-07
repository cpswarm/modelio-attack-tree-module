package org.modelio.module.attacktreedesigner.utils.elementmanager.representation;

import com.modeliosoft.modelio.javadesigner.annotations.objid;

@objid ("35595e54-c666-4514-a1c1-fc99c7062489")
public enum DiagramElementStyle {
    OPERATOR ("NONE", "IMAGE"),
    ATTACK ("SIMPLE", "SIMPLE"),
    TREE_REFERENCE ("SIMPLE", "STRUCTURED");

    @objid ("ba8b6174-d142-4e19-91a7-ac0be5ae6732")
    private String showNameProperty;

    @objid ("68c55436-6917-4cb4-b757-53906a321a83")
    private String representationMode;

    @objid ("fb4e66a9-b610-449c-bd01-c5e1fcfe95ef")
    DiagramElementStyle(String showNameProperty, String representationMode) {
        this.setShowNameProperty(showNameProperty);
        this.setRepresentationMode(representationMode);
    }

    @objid ("1aface22-4015-4858-bbda-3d9d597e830d")
    public String getShowNameProperty() {
        return this.showNameProperty;
    }

    @objid ("cc9eba86-1ecf-418b-8018-feedf07388c4")
    public void setShowNameProperty(String showNameProperty) {
        this.showNameProperty = showNameProperty;
    }

    @objid ("82214f16-633c-49d7-8d90-485fd2e445ce")
    public String getRepresentationMode() {
        return this.representationMode;
    }

    @objid ("08b13c80-3a80-4e10-b1fd-04cd41cf7836")
    public void setRepresentationMode(String representationMode) {
        this.representationMode = representationMode;
    }

}
