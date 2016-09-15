package org.jdtsort.handler;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jdt.core.dom.ASTParser;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.text.edits.MalformedTreeException;
import org.eclipse.ui.IEditorPart;
import org.jdtsort.preferences.PreferenceConstants;
import org.jdtsort.preferences.PreferenceInitializer;
import org.jdtsort.requestor.AbstractASTRequestor;
import org.jdtsort.requestor.FieldAggregationRequestor;
import org.jdtsort.util.AggregatedField;

import static java.lang.String.format;
import static org.eclipse.jdt.core.dom.AST.JLS4;
import static org.eclipse.jdt.core.dom.ASTParser.newParser;
import static org.eclipse.ui.handlers.HandlerUtil.getActiveEditor;
import static org.eclipse.ui.handlers.HandlerUtil.getCurrentSelection;
import static org.jdtsort.Logger.log;
import static org.jdtsort.handler.AbstractProgressHandler.formatCompilationUnit;

class RunnableWithProgress implements IRunnableWithProgress, PreferenceConstants {
    private final AbstractProgressHandler handler;
    private final IEditorPart editor;
    private final ISelection selection;

    RunnableWithProgress(AbstractProgressHandler handler, ExecutionEvent event) {
        this.handler = handler;
        editor = getActiveEditor(event);
        selection = getCurrentSelection(event);
    }

    @Override
    public void run(IProgressMonitor monitor) {
        handler.units.clear();
        handler.units.putAll(SelectionAnalyzer.getCompilationUnitsFromSelection(editor, selection));
        monitor.beginTask(handler.getTaskName(), handler.getTotalWork());
        Map<String, List<AggregatedField>> aggregatedFields = new HashMap<String, List<AggregatedField>>();
        for (Entry<IJavaProject, Collection<ICompilationUnit>> units : handler.units.entrySet()) {
            if (!monitor.isCanceled()) {
                IJavaProject project = units.getKey();
                createASTs(project, units, new FieldAggregationRequestor(aggregatedFields), monitor);
            }
        }
        for (Entry<IJavaProject, Collection<ICompilationUnit>> units : handler.units.entrySet()) {
            if (!monitor.isCanceled()) {
                IJavaProject project = units.getKey();
                for (AbstractASTRequestor requestor : handler.getASTRequestors(aggregatedFields)) {
                    createASTs(project, units, requestor, monitor);
                }
            }
        }
        monitor.done();
        if (PreferenceInitializer.getBoolean(P_FORMAT_SOURCE_CODE) && !monitor.isCanceled()) {
            monitor.beginTask("Format Code", handler.getTotalCompilationUnits());
            for (Collection<ICompilationUnit> units : handler.units.values()) {
                for (ICompilationUnit unit : units) {
                    try {
                        monitor.setTaskName(format("Format %s ...", unit.getElementName()));
                        formatCompilationUnit(unit);
                    } catch (JavaModelException e) {
                        log(e);
                    } catch (MalformedTreeException e) {
                        log(e);
                    }
                    monitor.worked(1);
                }
            }
            monitor.done();
        }
    }

    private void createASTs(IJavaProject project, Entry<IJavaProject, Collection<ICompilationUnit>> units, AbstractASTRequestor requestor, IProgressMonitor monitor) {
        ASTParser parser = newParser(JLS4);
        parser.setProject(project);
        parser.setResolveBindings(true);
        requestor.setProgressMonitor(monitor);
        parser.createASTs(units.getValue().toArray(new ICompilationUnit[units.getValue().size()]), new String[0], requestor, null);
    }
}
