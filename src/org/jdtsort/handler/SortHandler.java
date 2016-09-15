package org.jdtsort.handler;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.jdtsort.preferences.PreferenceConstants;
import org.jdtsort.preferences.PreferenceInitializer;
import org.jdtsort.requestor.AbstractASTRequestor;
import org.jdtsort.requestor.AnnotationSortRequestor;
import org.jdtsort.requestor.CaughtExceptionSortRequestor;
import org.jdtsort.requestor.CommentRemovalRequestor;
import org.jdtsort.requestor.ImplementedInterfaceSortRequestor;
import org.jdtsort.requestor.InterfaceCleanupRequestor;
import org.jdtsort.requestor.MethodSortRequestor;
import org.jdtsort.requestor.ModifierSortRequestor;
import org.jdtsort.requestor.ThrownExceptionSortRequestor;
import org.jdtsort.sorter.AnnotationSorter;
import org.jdtsort.sorter.CatchClauseSorter;
import org.jdtsort.sorter.MethodDeclarationSorter;
import org.jdtsort.sorter.ModifierSorter;
import org.jdtsort.sorter.ThrownExceptionSorter;
import org.jdtsort.sorter.TypeSorter;
import org.jdtsort.util.AggregatedField;

public class SortHandler extends AbstractProgressHandler implements PreferenceConstants {
    @Override
    public Collection<AbstractASTRequestor> getASTRequestors(Map<String, List<AggregatedField>> aggregatedFields) {
        Collection<AbstractASTRequestor> requestors = new LinkedList<AbstractASTRequestor>();
        if (PreferenceInitializer.getBoolean(P_CLEANUP_INTERFACES)) {
            requestors.add(new InterfaceCleanupRequestor());
        }
        if (PreferenceInitializer.getBoolean(P_SORT_ANNOTATIONS)) {
            requestors.add(new AnnotationSortRequestor(new AnnotationSorter()));
        }
        if (PreferenceInitializer.getBoolean(P_SORT_MODIFIERS)) {
            requestors.add(new ModifierSortRequestor(new ModifierSorter()));
        }
        if (PreferenceInitializer.getBoolean(P_SORT_IMPLEMENTED_INTERFACES)) {
            requestors.add(new ImplementedInterfaceSortRequestor(new TypeSorter()));
        }
        if (PreferenceInitializer.getBoolean(P_SORT_THROWN_EXCEPTIONS)) {
            requestors.add(new ThrownExceptionSortRequestor(new ThrownExceptionSorter()));
        }
        if (PreferenceInitializer.getBoolean(P_SORT_CATCH_BLOCKS)) {
            requestors.add(new CaughtExceptionSortRequestor(new CatchClauseSorter()));
        }
        if (PreferenceInitializer.getBoolean(P_REMOVE_COMMENTS)) {
            requestors.add(new CommentRemovalRequestor());
        }
        requestors.add(new MethodSortRequestor(new MethodDeclarationSorter(aggregatedFields)));
        return requestors;
    }

    @Override
    public String getTaskName() {
        return "Sort Code";
    }
}
