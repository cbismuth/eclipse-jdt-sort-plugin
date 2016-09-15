package org.jdtsort.sorter;

import java.util.Collections;
import java.util.List;

import org.eclipse.jdt.core.dom.Name;
import org.jdtsort.comparator.ThrownExceptionComparator;

public class ThrownExceptionSorter implements IASTNodeSorter<Name> {
    @Override
    public void sort(List<Name> nodes) {
        Collections.sort(nodes, new ThrownExceptionComparator());
    }
}
