package org.jdtsort.visitor;

import java.util.LinkedList;
import java.util.List;

import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.jdt.core.dom.CatchClause;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.TryStatement;
import org.jdtsort.sorter.IASTNodeSorter;

import static org.eclipse.jdt.core.dom.ASTNode.copySubtree;

public class CaughtExceptionVisitor extends ASTVisitor {
    private final CompilationUnit root;
    private final IASTNodeSorter<CatchClause> sorter;

    public CaughtExceptionVisitor(CompilationUnit root, IASTNodeSorter<CatchClause> sorter) {
        this.root = root;
        this.sorter = sorter;
    }

    @Override
    public void endVisit(TryStatement node) {
        List<CatchClause> oldNodes = new LinkedList<CatchClause>();
        List<CatchClause> newNodes = new LinkedList<CatchClause>();
        oldNodes.addAll(node.catchClauses());
        node.catchClauses().clear();
        sorter.sort(oldNodes);
        for (CatchClause oldNode : oldNodes) {
            newNodes.add((CatchClause) copySubtree(root.getAST(), oldNode));
        }
        node.catchClauses().addAll(newNodes);
    }
}
