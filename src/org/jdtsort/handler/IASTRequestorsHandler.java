package org.jdtsort.handler;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.jdtsort.requestor.AbstractASTRequestor;
import org.jdtsort.util.AggregatedField;

public interface IASTRequestorsHandler {
    Collection<AbstractASTRequestor> getASTRequestors(Map<String, List<AggregatedField>> aggregatedFields);

    String getTaskName();

    int getTotalCompilationUnits();

    int getTotalWork();
}
