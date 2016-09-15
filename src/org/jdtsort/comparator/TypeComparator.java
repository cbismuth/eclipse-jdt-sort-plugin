package org.jdtsort.comparator;

import java.util.Comparator;

import org.eclipse.jdt.core.dom.Type;

public class TypeComparator implements Comparator<Type> {
    @Override
    public int compare(Type o1, Type o2) {
        return o1.toString().compareTo(o2.toString());
    }
}
