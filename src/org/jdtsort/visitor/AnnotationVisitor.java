package org.jdtsort.visitor;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.jdt.core.dom.Annotation;
import org.eclipse.jdt.core.dom.BodyDeclaration;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.IExtendedModifier;
import org.eclipse.jdt.core.dom.MemberValuePair;
import org.eclipse.jdt.core.dom.NormalAnnotation;
import org.jdtsort.comparator.MemberValuePairComparator;
import org.jdtsort.sorter.IASTNodeSorter;

import static org.eclipse.jdt.core.dom.ASTNode.copySubtree;

public class AnnotationVisitor extends ASTVisitor {
    private final CompilationUnit root;
    private final IASTNodeSorter<Annotation> sorter;
    private final MemberValuePairComparator comparator = new MemberValuePairComparator();

    public AnnotationVisitor(CompilationUnit root, IASTNodeSorter<Annotation> sorter) {
        this.root = root;
        this.sorter = sorter;
    }

    @Override
    public void postVisit(ASTNode node) {
        if (node instanceof BodyDeclaration) {
            BodyDeclaration declaration = (BodyDeclaration) node;
            List<Annotation> oldNodes = new LinkedList<Annotation>();
            List<Annotation> newNodes = new LinkedList<Annotation>();
            for (Object modifier : declaration.modifiers()) {
                if (((IExtendedModifier) modifier).isAnnotation()) {
                    oldNodes.add((Annotation) modifier);
                }
            }
            declaration.modifiers().removeAll(oldNodes);
            sorter.sort(oldNodes);
            for (Annotation oldNode : oldNodes) {
                if (oldNode.isNormalAnnotation()) {
                    NormalAnnotation annotation = (NormalAnnotation) oldNode;
                    List<MemberValuePair> pairs = new LinkedList<MemberValuePair>();
                    pairs.addAll(annotation.values());
                    annotation.values().clear();
                    Collections.sort(pairs, comparator);
                    for (MemberValuePair pair : pairs) {
                        annotation.values().add(copySubtree(root.getAST(), pair));
                    }
                }
                newNodes.add((Annotation) copySubtree(root.getAST(), oldNode));
            }
            declaration.modifiers().addAll(newNodes);
        }
    }
}
