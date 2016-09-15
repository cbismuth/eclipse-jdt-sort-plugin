package org.jdtsort.visitor;

import java.util.LinkedList;
import java.util.List;

import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.jdt.core.dom.BodyDeclaration;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.IExtendedModifier;
import org.eclipse.jdt.core.dom.Modifier;
import org.jdtsort.sorter.IASTNodeSorter;

import static org.eclipse.jdt.core.dom.ASTNode.copySubtree;

public class ModifierVisitor extends ASTVisitor {
    private final CompilationUnit root;
    private final IASTNodeSorter<Modifier> sorter;

    public ModifierVisitor(CompilationUnit root, IASTNodeSorter<Modifier> sorter) {
        this.root = root;
        this.sorter = sorter;
    }

    @Override
    public void postVisit(ASTNode node) {
        if (node instanceof BodyDeclaration) {
            BodyDeclaration declaration = (BodyDeclaration) node;
            List<Modifier> oldNodes = new LinkedList<Modifier>();
            List<Modifier> newNodes = new LinkedList<Modifier>();
            for (Object modifier : declaration.modifiers()) {
                if (((IExtendedModifier) modifier).isModifier()) {
                    oldNodes.add((Modifier) modifier);
                }
            }
            declaration.modifiers().removeAll(oldNodes);
            sorter.sort(oldNodes);
            for (Modifier oldNode : oldNodes) {
                newNodes.add((Modifier) copySubtree(root.getAST(), oldNode));
            }
            declaration.modifiers().addAll(newNodes);
        }
    }
}
