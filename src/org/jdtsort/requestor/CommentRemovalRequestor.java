package org.jdtsort.requestor;

import java.util.List;

import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.dom.AbstractTypeDeclaration;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.text.edits.MalformedTreeException;
import org.jdtsort.visitor.CommentRemovalVisitor;

import static org.jdtsort.Logger.log;

/**
 * This class represents an AST requestor dedicated to remove comments. It can be useful while working on legacy projects with inconsistent documentation. This class can't remove orphan comment child nodes.
 * 
 * @author Christophe Bismuth (christophe.bismuth@gmail.com)
 */
public class CommentRemovalRequestor extends AbstractASTSortRequestor {
    @Override
    public void acceptAST(ICompilationUnit unit, CompilationUnit root) {
        preAcceptAST(unit);
        try {
            root.recordModifications();
            List types = root.types();
            if (!types.isEmpty()) {
                ((AbstractTypeDeclaration) types.get(0)).accept(new CommentRemovalVisitor());
            }
        } catch (IllegalArgumentException e) {
            log(e);
        } catch (MalformedTreeException e) {
            log(e);
        }
        postAcceptAST(unit, root);
    }

    @Override
    public String getTaskName() {
        return "Remove Comments";
    }
}
