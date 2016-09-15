package org.jdtsort.sorter;

import java.util.Collections;
import java.util.List;

import org.eclipse.jdt.core.dom.Modifier;
import org.jdtsort.comparator.ModifierComparator;

@SuppressWarnings("rawtypes")
public class ModifierSorter implements IASTNodeSorter<Modifier> {
    @Override
    public void sort(List nodes) {
        Collections.sort(nodes, new ModifierComparator());
    }
}
