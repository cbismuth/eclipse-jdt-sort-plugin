package org.jdtsort.sorter;

import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.eclipse.jdt.core.dom.MethodDeclaration;
import org.jdtsort.comparator.MethodDeclarationComparator;
import org.jdtsort.util.AggregatedField;

public class MethodDeclarationSorter implements IASTNodeSorter<MethodDeclaration> {
    private final Map<String, List<AggregatedField>> aggregatedFields;

    public MethodDeclarationSorter(Map<String, List<AggregatedField>> aggregatedFields) {
        this.aggregatedFields = aggregatedFields;
    }

    @Override
    public void sort(List<MethodDeclaration> nodes) {
        MethodDeclarationComparator cmp = new MethodDeclarationComparator(aggregatedFields);
        Collections.sort(nodes, cmp);
        cmp.clearCache();
    }
}
