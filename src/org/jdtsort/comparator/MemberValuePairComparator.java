package org.jdtsort.comparator;

import java.util.Comparator;

import org.eclipse.jdt.core.dom.MemberValuePair;

public class MemberValuePairComparator implements Comparator<MemberValuePair> {
    @Override
    public int compare(MemberValuePair o1, MemberValuePair o2) {
        return o1.getName().toString().compareTo(o2.toString());
    }
}
