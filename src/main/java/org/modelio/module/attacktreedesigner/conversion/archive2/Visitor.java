package org.modelio.module.attacktreedesigner.conversion.archive2;

public interface Visitor {

    public void visitANDNode(ANDNode andNode);

    public void visitORNode(ORNode orNode);

    public void visitRootNode(RootNode rootNode);

    public void visitAttackNode(AttackNode attackNode);

    public void visitTreeReferenceNode(TreeReferenceNode treeReferenceNode);
    
    
}
