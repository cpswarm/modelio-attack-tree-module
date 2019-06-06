package org.modelio.module.attacktreedesigner.api;

import com.modeliosoft.modelio.javadesigner.annotations.objid;

/**
 * @author ebrosse
 */
@objid ("aa25de17-6718-4ec6-9d23-ae1ce1473250")
public interface AttackTreeStereotypes {
    @objid ("8b0ed670-d6fe-4425-9cf9-d081d93fee4d")
    public static final String ATTACKTREEDIAGRAM = "AttackTreeDiagram";

    @objid ("9ed47491-0e71-4898-b628-a07583d88201")
    public static final String ROOT = "Root";

    @objid ("13509460-cf81-4611-9e0a-20cc54a11b9e")
    public static final String AND = "AND";

    @objid ("6d01b8a6-7e7d-4dd7-9f2e-3ed1609bff3c")
    public static final String OR = "OR";

    @objid ("756f9e99-259e-4626-8dee-fed9de59b869")
    public static final String ABSTRACTATTACK = "AbstractAttack";

    @objid ("54ff413b-d4e0-4417-ab93-32952ef79a85")
    public static final String ATTACK = "Attack";

    @objid ("5be35f6d-1348-41cd-8e2f-5c48719c1b91")
    public static final String CONNECTION = "Connection";

    @objid ("4bf4c6bb-0e17-4afd-8f70-d74fec27ef40")
    public static final String SUBTREE = "SubTree";

    @objid ("25390562-acd5-4c73-8fac-42a2f97fa1ad")
    public static final String NODE = "Node";

    @objid ("61d17d69-3dec-4f33-878d-e8f37655ff63")
    public static final String OPERATOR = "Operator";

    @objid ("1d412940-e0f7-4fdf-91cc-2ea45bca17b3")
    public static final String NODEWITHMASKABLEDESCENDANTS = "NodeWithMaskableDescendants";

}
