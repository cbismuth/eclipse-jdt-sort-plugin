package org.jdtsort.visitor;

import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.jdt.core.dom.Comment;

public class CommentRemovalVisitor extends ASTVisitor {
    @Override
    public void postVisit(ASTNode node) {
        if (node instanceof Comment) {
            ((Comment) node).delete();
        }
    }
}
