package org.modelio.module.attacktreedesigner.utils.elementmanager.representation;

import com.modeliosoft.modelio.javadesigner.annotations.objid;
import org.eclipse.draw2d.geometry.Rectangle;

@objid ("c9135aa7-0b4c-43bc-9f63-cd1b9e64c9e6")
public enum DiagramElementBounds {
    ROOT (300, 40, 140, 40),
    COUNTER_MEASURE (300,40,140,60);

    @objid ("f38fc103-ce11-4168-8bf2-9d012c5ae4e0")
    private int x;

    @objid ("0336a2e8-dd3f-41d7-a624-d17949093551")
    private int y;

    @objid ("7cfdab0f-5139-47ec-8e98-a1e26b8b4c66")
    private int width;

    @objid ("392fabef-af64-4f67-918f-35a4f20cc4f1")
    private int height;

    @objid ("55b18318-512f-4020-bce0-93437030f47c")
    DiagramElementBounds(int x, int y, int width, int height) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
    }

    @objid ("ac6566ba-ae70-4ac6-818c-6d8e99bc935c")
    public Rectangle createRectangleBounds() {
        return new Rectangle(this.x,this.y,this.width,this.height);
    }

    @objid ("68bc9356-2dd5-4eef-951d-b6dd303f00b7")
    public int getX() {
        return this.x;
    }

    @objid ("5021cb2f-8328-4fe4-bcbd-0bb6996d186b")
    public int getY() {
        return this.y;
    }

    @objid ("196d5807-8cfb-4c54-b3a5-5053df4599e4")
    public int getWidth() {
        return this.width;
    }

    @objid ("b42c3284-1152-42ca-9089-6d925d322538")
    public int getHeight() {
        return this.height;
    }

}
