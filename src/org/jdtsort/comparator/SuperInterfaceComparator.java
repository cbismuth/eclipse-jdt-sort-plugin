package org.jdtsort.comparator;

import java.util.Comparator;

import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.core.JavaModelException;

import static java.lang.Integer.MAX_VALUE;
import static java.lang.Integer.MIN_VALUE;
import static org.jdtsort.Logger.log;

public class SuperInterfaceComparator implements Comparator<IType> {
    @Override
    public int compare(IType o1, IType o2) {
        int rval = 0;
        try {
            String[] names = o1.getSuperInterfaceNames();
            for (int i = 0; i < names.length && rval == 0; i++) {
                if (o2.getElementName().equals(names[i])) {
                    rval = MAX_VALUE;
                }
            }
            if (rval == 0) {
                rval = MIN_VALUE;
            }
        } catch (JavaModelException e) {
            log(e);
        }
        return rval;
    }
}
