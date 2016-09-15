package org.jdtsort.comparator;

public interface ICachedComparator {
    /**
     * Clears internal class cache.
     * 
     * @see http://www.eclipse.org/forums/index.php/mv/msg/203385/650109/#msg_650109
     */
    void clearCache();
}
