package org.jdtsort.requestor;

import java.util.List;
import java.util.Map;

import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.dom.AbstractTypeDeclaration;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.text.edits.MalformedTreeException;
import org.jdtsort.util.AggregatedField;
import org.jdtsort.visitor.FieldAggregator;

import static org.jdtsort.Logger.log;

public class FieldAggregationRequestor extends AbstractASTSortRequestor {
    private final Map<String, List<AggregatedField>> aggregatedFields;

    public FieldAggregationRequestor(Map<String, List<AggregatedField>> aggregatedFields) {
        this.aggregatedFields = aggregatedFields;
    }

    @Override
    public void acceptAST(ICompilationUnit unit, CompilationUnit root) {
        preAcceptAST(unit);
        try {
            root.recordModifications();
            List types = root.types();
            if (!types.isEmpty()) {
                ((AbstractTypeDeclaration) types.get(0)).accept(new FieldAggregator(aggregatedFields));
            }
        } catch (IllegalArgumentException e) {
            log(e);
        } catch (MalformedTreeException e) {
            log(e);
        }
        postAcceptAST(unit, root);
    }

    @Override
    public String getTaskName() {
        return "Aggregate Fields";
    }
}
