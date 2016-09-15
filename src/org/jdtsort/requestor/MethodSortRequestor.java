package org.jdtsort.requestor;

import java.util.List;

import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.dom.AbstractTypeDeclaration;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.MethodDeclaration;
import org.eclipse.text.edits.MalformedTreeException;
import org.jdtsort.sorter.IASTNodeSorter;
import org.jdtsort.visitor.MethodVisitor;

import static org.jdtsort.Logger.log;

public class MethodSortRequestor extends AbstractASTSortRequestor {
    private final IASTNodeSorter<MethodDeclaration> sorter;

    public MethodSortRequestor(IASTNodeSorter<MethodDeclaration> sorter) {
        this.sorter = sorter;
    }

    @Override
    public void acceptAST(ICompilationUnit unit, CompilationUnit root) {
        preAcceptAST(unit);
        try {
            root.recordModifications();
            List types = root.types();
            if (!types.isEmpty()) {
                ((AbstractTypeDeclaration) types.get(0)).accept(new MethodVisitor(root, sorter));
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
        return "Sort Methods";
    }
}
