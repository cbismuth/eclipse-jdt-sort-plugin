package org.jdtsort.visitor;

import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.jdt.core.dom.ITypeBinding;
import org.eclipse.jdt.core.dom.IVariableBinding;
import org.eclipse.jdt.core.dom.TypeDeclaration;
import org.jdtsort.util.AggregatedField;

import static java.util.Arrays.sort;
import static org.eclipse.jdt.core.dom.Modifier.PROTECTED;
import static org.eclipse.jdt.core.dom.Modifier.PUBLIC;

public class FieldAggregator extends ASTVisitor {
    private final Map<String, List<AggregatedField>> aggregatedFields;

    public FieldAggregator(Map<String, List<AggregatedField>> aggregatedFields) {
        this.aggregatedFields = aggregatedFields;
    }

    @Override
    public boolean visit(TypeDeclaration type) {
        String key = type.getName().toString();
        List<AggregatedField> fields = new LinkedList<AggregatedField>();
        aggregatedFields.put(key, fields);
        boolean base = true;
        ITypeBinding binding = type.resolveBinding();
        while (binding != null && !type.getAST().resolveWellKnownType("java.lang.Object").equals(binding)) {
            IVariableBinding[] declaredFields = binding.getDeclaredFields();
            sort(declaredFields, new Comparator<IVariableBinding>() {
                @Override
                public int compare(IVariableBinding o1, IVariableBinding o2) {
                    // http://www.eclipse.org/forums/index.php/mv/msg/243822/731877/#msg_731877
                    return o1.getVariableId() - o2.getVariableId();
                }
            });
            if (declaredFields.length > 0) {
                List<AggregatedField> _fields = new LinkedList<AggregatedField>();
                for (IVariableBinding field : declaredFields) {
                    boolean visibility;
                    if (base) {
                        visibility = true;
                    } else {
                        visibility = false;
                        visibility |= (field.getModifiers() & PUBLIC) != 0;
                        visibility |= (field.getModifiers() & PROTECTED) != 0;
                    }
                    if (visibility) {
                        _fields.add(new AggregatedField(field.getType().getName(), field.getName()));
                    }
                }
                fields.addAll(0, _fields);
            }
            binding = binding.getSuperclass();
            base = false;
        }
        return true;
    }
}
