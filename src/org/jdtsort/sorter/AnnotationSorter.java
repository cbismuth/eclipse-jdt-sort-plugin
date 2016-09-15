package org.jdtsort.sorter;

import java.util.Collections;
import java.util.List;

import org.eclipse.jdt.core.dom.Annotation;
import org.jdtsort.comparator.AnnotationComparator;

@SuppressWarnings("rawtypes")
public class AnnotationSorter implements IASTNodeSorter<Annotation> {
    @Override
    public void sort(List nodes) {
        Collections.sort(nodes, new AnnotationComparator());
    }
}
