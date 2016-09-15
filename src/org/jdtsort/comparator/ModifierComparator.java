package org.jdtsort.comparator;

import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.eclipse.jdt.core.dom.IExtendedModifier;
import org.eclipse.jdt.core.dom.Modifier;

import static java.lang.Integer.MAX_VALUE;
import static java.lang.Integer.MIN_VALUE;

public class ModifierComparator implements Comparator<IExtendedModifier> {
    private static final Map<String, Integer> ranks = new HashMap<String, Integer>();
    static {
        // http://java.sun.com/docs/books/jls/third_edition/html/classes.html#8.1.1
        // http://java.sun.com/docs/books/jls/third_edition/html/classes.html#8.3.1
        // http://java.sun.com/docs/books/jls/third_edition/html/classes.html#8.4.3
        List<String> _ranks = new LinkedList<String>();
        _ranks.add("public");
        _ranks.add("protected");
        _ranks.add("private");
        _ranks.add("abstract");
        _ranks.add("static");
        _ranks.add("final");
        _ranks.add("transient");
        _ranks.add("volatile");
        _ranks.add("synchronized");
        _ranks.add("native");
        _ranks.add("strictfp");
        for (String rank : _ranks) {
            ranks.put(rank, _ranks.indexOf(rank));
        }
    }

    @Override
    public int compare(IExtendedModifier o1, IExtendedModifier o2) {
        int rval;
        if (!o1.isModifier() && !o2.isModifier()) {
            rval = 0;
        } else if (o1.isModifier() && !o2.isModifier()) {
            rval = MAX_VALUE;
        } else if (!o1.isModifier() && o2.isModifier()) {
            rval = MIN_VALUE;
        } else if (o1.isModifier() && o2.isModifier()) {
            Integer r1 = ranks.get(((Modifier) o1).getKeyword().toString());
            Integer r2 = ranks.get(((Modifier) o2).getKeyword().toString());
            int _r1 = r1 == null ? MIN_VALUE : r1;
            int _r2 = r2 == null ? MIN_VALUE : r2;
            rval = _r1 - _r2;
        } else {
            rval = 0;
        }
        return rval;
    }
}
