package org.jdtsort.visitor;

import java.util.LinkedList;
import java.util.List;

import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.MethodDeclaration;
import org.eclipse.jdt.core.dom.Name;
import org.jdtsort.sorter.IASTNodeSorter;

import static org.eclipse.jdt.core.dom.ASTNode.copySubtree;

public class ThrownExceptionVisitor extends ASTVisitor {
    private final CompilationUnit root;
    private final IASTNodeSorter<Name> sorter;

    public ThrownExceptionVisitor(CompilationUnit root, IASTNodeSorter<Name> sorter) {
        this.root = root;
        this.sorter = sorter;
    }

    @Override
    public void endVisit(MethodDeclaration node) {
        List<Name> oldNodes = new LinkedList<Name>();
        List<Name> newNodes = new LinkedList<Name>();
        oldNodes.addAll(node.thrownExceptions());
        node.thrownExceptions().clear();
        sorter.sort(oldNodes);
        for (Name oldNode : oldNodes) {
            newNodes.add((Name) copySubtree(root.getAST(), oldNode));
        }
        node.thrownExceptions().addAll(newNodes);
    }
}
