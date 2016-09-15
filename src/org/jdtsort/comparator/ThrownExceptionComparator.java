package org.jdtsort.comparator;

import java.util.Comparator;

import org.eclipse.jdt.core.dom.Name;

public class ThrownExceptionComparator implements Comparator<Name> {
    @Override
    public int compare(Name o1, Name o2) {
        return o1.getFullyQualifiedName().compareTo(o2.getFullyQualifiedName());
    }
}
