package org.jdtsort.visitor;

import java.util.LinkedList;
import java.util.List;

import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.Type;
import org.eclipse.jdt.core.dom.TypeDeclaration;
import org.jdtsort.sorter.IASTNodeSorter;

import static org.eclipse.jdt.core.dom.ASTNode.copySubtree;

public class ImplementedInterfaceVisitor extends ASTVisitor {
    private final CompilationUnit root;
    private final IASTNodeSorter<Type> sorter;

    public ImplementedInterfaceVisitor(CompilationUnit root, IASTNodeSorter<Type> sorter) {
        this.root = root;
        this.sorter = sorter;
    }

    @Override
    public void endVisit(TypeDeclaration node) {
        List<Type> oldNodes = new LinkedList<Type>();
        List<Type> newNodes = new LinkedList<Type>();
        oldNodes.addAll(node.superInterfaceTypes());
        node.superInterfaceTypes().clear();
        sorter.sort(oldNodes);
        for (Type oldNode : oldNodes) {
            newNodes.add((Type) copySubtree(root.getAST(), oldNode));
        }
        node.superInterfaceTypes().addAll(newNodes);
    }
}
