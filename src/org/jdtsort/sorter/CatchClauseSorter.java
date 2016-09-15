package org.jdtsort.sorter;

import java.util.Collections;
import java.util.List;

import org.eclipse.jdt.core.dom.CatchClause;
import org.jdtsort.comparator.CatchClauseComparator;

public class CatchClauseSorter implements IASTNodeSorter<CatchClause> {
    @Override
    public void sort(List<CatchClause> nodes) {
        CatchClauseComparator cmp = new CatchClauseComparator();
        Collections.sort(nodes, cmp);
        cmp.clearCache();
    }
}
