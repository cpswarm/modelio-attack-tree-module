package org.modelio.module.attacktreedesigner.api;

import com.modeliosoft.modelio.javadesigner.annotations.objid;

/**
 * @author ebrosse
 */
@objid ("aa25de17-6718-4ec6-9d23-ae1ce1473250")
public interface AttackTreeStereotypes {
    @objid ("721a8d08-a034-4904-844c-aaea957179fb")
    public static final String AND = "And";

    @objid ("213e0e68-d354-4b3d-95ee-c3592c60dfa1")
    public static final String OR = "Or";

    @objid ("e64a0f5f-b1b8-4f9b-80ee-3a1dd8df3d7f")
    public static final String EVENT = "Event";

    @objid ("8b0ed670-d6fe-4425-9cf9-d081d93fee4d")
    public static final String ATTACKTREEDIAGRAM = "AttackTreeDiagram";

    @objid ("e3052503-69ab-47a0-8072-e9f29356d0b0")
    public static final String GOAL = "Goal";

    @objid ("f557d12b-bb25-4519-aa75-643b3d14cf68")
    public static final String SUBGOAL = "SubGoal";

}
