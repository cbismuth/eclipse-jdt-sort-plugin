package org.jdtsort.comparator;

import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.eclipse.jdt.core.dom.Annotation;
import org.eclipse.jdt.core.dom.IExtendedModifier;

import static java.lang.Integer.MAX_VALUE;
import static java.lang.Integer.MIN_VALUE;

public class AnnotationComparator implements Comparator<IExtendedModifier> {
    private static final Map<String, Integer> ranks = new HashMap<String, Integer>();
    static {
        List<String> _ranks = new LinkedList<String>();
        // Table
        _ranks.add("Entity");
        _ranks.add("Table");
        _ranks.add("NamedQuery");
        // Column
        _ranks.add("Id");
        _ranks.add("Version");
        _ranks.add("GeneratedValue");
        _ranks.add("SequenceGenerator");
        _ranks.add("OneToMany");
        _ranks.add("ManyToOne");
        _ranks.add("ManyToMany");
        _ranks.add("JoinColumn");
        _ranks.add("Lob");
        _ranks.add("Temporal");
        _ranks.add("Column");
        // Java
        _ranks.add("Override");
        _ranks.add("Deprecated");
        _ranks.add("SuppressWarnings");
        for (String rank : _ranks) {
            ranks.put(rank, _ranks.indexOf(rank));
        }
    }

    @Override
    public int compare(IExtendedModifier o1, IExtendedModifier o2) {
        int rval;
        if (o1.isModifier() && o2.isModifier()) {
            rval = 0;
        } else if (o1.isModifier() && !o2.isModifier()) {
            rval = MAX_VALUE;
        } else if (!o1.isModifier() && o2.isModifier()) {
            rval = MIN_VALUE;
        } else if (!o1.isModifier() && !o2.isModifier()) {
            Integer r1 = ranks.get(((Annotation) o1).getTypeName().toString());
            Integer r2 = ranks.get(((Annotation) o2).getTypeName().toString());
            int _r1 = r1 == null ? MIN_VALUE : r1;
            int _r2 = r2 == null ? MIN_VALUE : r2;
            rval = _r1 - _r2;
        } else {
            rval = 0;
        }
        return rval;
    }
}
