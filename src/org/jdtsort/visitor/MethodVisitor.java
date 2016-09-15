package org.jdtsort.visitor;

import java.util.LinkedList;
import java.util.List;

import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.jdt.core.dom.AbstractTypeDeclaration;
import org.eclipse.jdt.core.dom.BodyDeclaration;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.MethodDeclaration;
import org.jdtsort.sorter.IASTNodeSorter;

import static org.eclipse.jdt.core.dom.ASTNode.copySubtree;

public class MethodVisitor extends ASTVisitor {
    private final CompilationUnit root;
    private final IASTNodeSorter<MethodDeclaration> sorter;

    public MethodVisitor(CompilationUnit root, IASTNodeSorter<MethodDeclaration> sorter) {
        this.root = root;
        this.sorter = sorter;
    }

    @Override
    public void postVisit(ASTNode node) {
        if (node instanceof AbstractTypeDeclaration) {
            List<MethodDeclaration> oldNodes = new LinkedList<MethodDeclaration>();
            List<MethodDeclaration> newNodes = new LinkedList<MethodDeclaration>();
            for (Object oldNode : ((AbstractTypeDeclaration) node).bodyDeclarations()) {
                if (oldNode instanceof MethodDeclaration) {
                    oldNodes.add((MethodDeclaration) oldNode);
                }
            }
            for (MethodDeclaration oldNode : oldNodes) {
                ((AbstractTypeDeclaration) node).bodyDeclarations().remove(oldNode);
            }
            sorter.sort(oldNodes);
            for (BodyDeclaration oldNode : oldNodes) {
                newNodes.add((MethodDeclaration) copySubtree(root.getAST(), oldNode));
            }
            ((AbstractTypeDeclaration) node).bodyDeclarations().addAll(newNodes);
        }
    }
}
