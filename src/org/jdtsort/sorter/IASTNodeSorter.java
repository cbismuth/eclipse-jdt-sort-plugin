package org.jdtsort.sorter;

import java.util.List;

import org.eclipse.jdt.core.dom.ASTNode;

public interface IASTNodeSorter<K extends ASTNode> {
    void sort(List<K> nodes);
}
