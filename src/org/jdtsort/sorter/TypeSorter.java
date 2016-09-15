package org.jdtsort.sorter;

import java.util.Collections;
import java.util.List;

import org.eclipse.jdt.core.dom.Type;
import org.jdtsort.comparator.TypeComparator;

public class TypeSorter implements IASTNodeSorter<Type> {
    @Override
    public void sort(List<Type> nodes) {
        Collections.sort(nodes, new TypeComparator());
    }
}
