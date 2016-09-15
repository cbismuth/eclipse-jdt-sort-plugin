package org.jdtsort.requestor;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.SubProgressMonitor;
import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jdt.core.dom.ASTRequestor;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jface.text.Document;
import org.eclipse.jface.text.IDocument;
import org.jdtsort.Logger;

public abstract class AbstractASTRequestor extends ASTRequestor {
    private IProgressMonitor subMonitor;
    private IProgressMonitor monitor;

    void postAcceptAST(ICompilationUnit unit, CompilationUnit root) {
        try {
            IDocument document = new Document(unit.getSource());
            unit.applyTextEdit(root.rewrite(document, null), null);
            unit.save(null, false);
        } catch (JavaModelException e) {
            Logger.log(e);
        }
        if (subMonitor != null) {
            subMonitor.worked(1);
            subMonitor.done();
        }
        if (monitor != null) {
            monitor.worked(1);
        }
    }

    void preAcceptAST(ICompilationUnit unit) {
        if (monitor != null) {
            subMonitor = new SubProgressMonitor(monitor, 1);
            subMonitor.setTaskName(String.format("%s on %s ...", getTaskName(), unit.getElementName()));
        }
    }

    protected abstract String getTaskName();

    public void setProgressMonitor(IProgressMonitor monitor) {
        this.monitor = monitor;
    }
}
