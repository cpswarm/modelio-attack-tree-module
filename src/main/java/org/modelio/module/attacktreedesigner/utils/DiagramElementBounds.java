package org.modelio.module.attacktreedesigner.utils;

import com.modeliosoft.modelio.javadesigner.annotations.objid;
import org.eclipse.draw2d.geometry.Rectangle;

@objid ("4937636a-2bda-46e1-afbb-7eef33bce98d")
public enum DiagramElementBounds {
    ROOT (300, 40, 140, 40);

    @objid ("f0493680-fe2c-4f66-99a6-87b11643b374")
    private int x;

    @objid ("0d59140f-1926-457f-a69f-b4801c2b731a")
    private int y;

    @objid ("58ed11be-c45e-4614-a617-5a7da85d6db5")
    private int width;

    @objid ("b046db78-f840-4ccf-aa69-63ad37510f8a")
    private int height;

    @objid ("85a6e04d-a6a6-4e95-a1ce-ad7ef0b69bf8")
    DiagramElementBounds(int x, int y, int width, int height) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
    }

    @objid ("bc93e606-1c2b-43ab-9b97-8b21e1f341da")
    public Rectangle createRectangleBounds() {
        return new Rectangle(this.x,this.y,this.width,this.height);
    }

    @objid ("d111ba94-c279-4afe-8b30-70d3089d3859")
    public int getX() {
        return this.x;
    }

    @objid ("74e972fa-29c6-4570-848d-ba0ec3bbdc24")
    public int getY() {
        return this.y;
    }

}
