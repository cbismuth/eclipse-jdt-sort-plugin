package org.jdtsort.handler;

import java.lang.reflect.InvocationTargetException;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jdt.core.formatter.CodeFormatter;
import org.eclipse.jface.dialogs.ProgressMonitorDialog;
import org.eclipse.jface.text.Document;
import org.eclipse.text.edits.MalformedTreeException;
import org.eclipse.text.edits.TextEdit;
import org.eclipse.ui.handlers.HandlerUtil;
import org.jdtsort.Logger;

import static org.eclipse.jdt.core.ToolFactory.createCodeFormatter;
import static org.eclipse.jdt.core.formatter.CodeFormatter.F_INCLUDE_COMMENTS;
import static org.eclipse.jdt.core.formatter.CodeFormatter.K_COMPILATION_UNIT;

public abstract class AbstractProgressHandler extends AbstractHandler implements IASTRequestorsHandler {
    final Map<IJavaProject, Collection<ICompilationUnit>> units = new HashMap<IJavaProject, Collection<ICompilationUnit>>();

    static void formatCompilationUnit(ICompilationUnit unit) throws JavaModelException, MalformedTreeException {
        Document document = new Document(unit.getSource());
        String text = document.get();
        CodeFormatter formatter = createCodeFormatter(unit.getJavaProject().getOptions(true));
        TextEdit edit = formatter.format(K_COMPILATION_UNIT | F_INCLUDE_COMMENTS, text, 0, text.length(), 0, null);
        unit.applyTextEdit(edit, null);
        unit.save(null, false);
    }

    @Override
    public int getTotalCompilationUnits() {
        int totalCompilationUnits = 0;
        for (Collection<ICompilationUnit> c : units.values()) {
            totalCompilationUnits += c.size();
        }
        return totalCompilationUnits;
    }

    @Override
    public int getTotalWork() {
        int totalWork = 0;
        for (Collection<ICompilationUnit> c : units.values()) {
            totalWork += c.size();
        }
        totalWork *= getASTRequestors(null).size();
        return totalWork;
    }

    @Override
    public Object execute(ExecutionEvent event) throws ExecutionException {
        try {
            new ProgressMonitorDialog(HandlerUtil.getActiveShell(event).getShell()).run(true, true, new RunnableWithProgress(this, event));
        } catch (InterruptedException e) {
            Logger.log(e);
        } catch (InvocationTargetException e) {
            Logger.log(e);
        }
        return null;
    }
}
