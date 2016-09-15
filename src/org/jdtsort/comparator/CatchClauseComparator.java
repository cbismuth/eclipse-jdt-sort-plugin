package org.jdtsort.comparator;

import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;

import org.eclipse.jdt.core.dom.CatchClause;
import org.eclipse.jdt.core.dom.ITypeBinding;
import org.eclipse.jdt.core.dom.Type;
import org.jdtsort.util.BindingUtils;

import static java.lang.Integer.MAX_VALUE;
import static java.lang.Integer.MIN_VALUE;
import static org.jdtsort.util.BindingUtils.resolveBinding;

public class CatchClauseComparator implements Comparator<CatchClause>, ICachedComparator {
    private final Map<Integer, ITypeBinding> cache = new HashMap<Integer, ITypeBinding>();

    @Override
    public int compare(CatchClause o1, CatchClause o2) {
        ITypeBinding type1 = getBinding(o1.getException().getType());
        ITypeBinding type2 = getBinding(o2.getException().getType());
        int rval;
        if (BindingUtils.isSubType(type1, type2)) {
            rval = MIN_VALUE;
        } else if (BindingUtils.isSubType(type2, type1)) {
            rval = MAX_VALUE;
        } else {
            rval = type1.getJavaElement().getElementName().compareTo(type2.getJavaElement().getElementName());
        }
        return rval;
    }

    @Override
    public void clearCache() {
        cache.clear();
    }

    private ITypeBinding getBinding(Type type) {
        int key = type.hashCode();
        ITypeBinding value = cache.get(key);
        if (value == null) {
            value = resolveBinding(type);
            cache.put(key, value);
        }
        return value;
    }
}
