package org.jdtsort.comparator;

import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jdt.core.dom.IMethodBinding;
import org.eclipse.jdt.core.dom.MethodDeclaration;
import org.eclipse.jdt.core.dom.SingleVariableDeclaration;
import org.jdtsort.Activator;
import org.jdtsort.preferences.PreferenceConstants;
import org.jdtsort.util.AggregatedField;
import org.jdtsort.util.BindingUtils;

import static org.eclipse.jdt.core.dom.PrimitiveType.VOID;
import static org.jdtsort.preferences.PreferenceInitializer.getBoolean;
import static org.jdtsort.util.BindingUtils.resolveBinding;

public class MethodDeclarationComparator implements Comparator<MethodDeclaration>, ICachedComparator, PreferenceConstants {
    private final Map<String, List<AggregatedField>> aggregatedFields;
    private final Map<Integer, IMethodBinding> cacheOfMethodBinding = new HashMap<Integer, IMethodBinding>();
    private final Map<Integer, IType> cacheOfClosestEnclosingTypeDeclaration = new HashMap<Integer, IType>();

    public MethodDeclarationComparator(Map<String, List<AggregatedField>> aggregatedFields) {
        this.aggregatedFields = aggregatedFields;
    }

    @Override
    public int compare(MethodDeclaration o1, MethodDeclaration o2) {
        int rval;
        if (isSameMethodKind(o1, o2)) {
            if (o1.isConstructor()) {
                if (getBoolean(P_DEFAULT_SORT_METHODS_BY_NUMBER_OF_ARGUMENTS_ON_SAME_KIND_AND_NAME)) {
                    rval = o1.parameters().size() - o2.parameters().size();
                } else {
                    rval = 0;
                }
            } else if (isGetter(o1) || isSetter(o1)) {
                if (getBoolean(P_SORT_ACCESSORS_BY_FIELD_INDEX)) {
                    rval = getAccessorIndex(o1) - getAccessorIndex(o2);
                } else if (getBoolean(P_DEFAULT_SORT_METHODS_LEXICOGRAPHICALLY_ON_SAME_KIND)) {
                    rval = o1.getName().toString().compareTo(o2.getName().toString());
                } else {
                    rval = 0;
                }
            } else {
                rval = innerCompare(o1, o2);
            }
        } else if (P_KIND_INDEX_OTHER.equals(getMethodKindKey(o1)) && P_KIND_INDEX_OTHER.equals(getMethodKindKey(o2))) {
            rval = innerCompare(o1, o2);
        } else {
            rval = getMethodKindIndex(getMethodKindKey(o1)) - getMethodKindIndex(getMethodKindKey(o2));
        }
        return rval;
    }

    @Override
    public void clearCache() {
        cacheOfMethodBinding.clear();
        cacheOfClosestEnclosingTypeDeclaration.clear();
    }

    private int getAccessorIndex(MethodDeclaration declaration) {
        String declarationName = declaration.getName().toString();
        String declaringClassName = resolveMethodBinding(declaration).getDeclaringClass().getName();
        String expectedFieldName = declarationName.substring(3, 4).toLowerCase() + declarationName.substring(4);
        return aggregatedFields.get(declaringClassName).indexOf(new AggregatedField(declaration.getReturnType2().resolveBinding().getName(), expectedFieldName));
    }

    private int getMethodKindIndex(String kind) {
        return Activator.getDefault().getPreferenceStore().getInt(kind);
    }

    private int innerCompare(MethodDeclaration o1, MethodDeclaration o2) {
        IType type1 = getClosestEnclosingTypeDeclaration(o1);
        IType type2 = getClosestEnclosingTypeDeclaration(o2);
        if (isInterface(type1) && isInterface(type2)) {
            return 0;
        }
        // --
        int rval;
        String sortKey1 = type1 != null ? type1.getElementName() : "";
        String sortKey2 = type2 != null ? type2.getElementName() : "";
        if (getBoolean(P_GROUP_METHODS_BY_HIERARCHY) && !sortKey1.equals(sortKey2)) {
            rval = sortKey1.compareTo(sortKey2);
        } else {
            String name1 = o1.getName().toString();
            String name2 = o2.getName().toString();
            if (getBoolean(P_DEFAULT_SORT_METHODS_LEXICOGRAPHICALLY_ON_SAME_KIND) && !name1.equals(name2)) {
                rval = name1.compareTo(name2);
            } else if (getBoolean(P_DEFAULT_SORT_METHODS_BY_NUMBER_OF_ARGUMENTS_ON_SAME_KIND_AND_NAME) && name1.equals(name2)) {
                rval = o1.parameters().size() - o2.parameters().size();
            } else {
                rval = 0;
            }
        }
        return rval;
    }

    private boolean isInterface(IType type) {
        try {
            return type.isInterface();
        } catch (JavaModelException ignored) {
            return false;
        }
    }

    private IType getClosestEnclosingTypeDeclaration(MethodDeclaration declaration) {
        int key = declaration.hashCode();
        IType value = cacheOfClosestEnclosingTypeDeclaration.get(key);
        if (value == null) {
            value = BindingUtils.getClosestEnclosingTypeDeclaration(declaration, true);
            cacheOfClosestEnclosingTypeDeclaration.put(key, value);
        }
        return value;
    }

    private boolean isSameMethodKind(MethodDeclaration o1, MethodDeclaration o2) {
        return getMethodKindKey(o1).equals(getMethodKindKey(o2));
    }

    private String getMethodKindKey(MethodDeclaration o) {
        String key;
        if (o.isConstructor()) {
            key = P_KIND_INDEX_CTOR;
        } else if (isGetter(o)) {
            key = P_KIND_INDEX_GET;
        } else if (isSetter(o)) {
            key = P_KIND_INDEX_SET;
        } else {
            key = P_KIND_INDEX_OTHER;
        }
        return key;
    }

    private boolean isGetter(MethodDeclaration declaration) {
        boolean isGetter = false;
        if (declaration.parameters().isEmpty() && !declaration.getReturnType2().equals(VOID)) {
            boolean prefix = false;
            prefix |= declaration.getName().toString().startsWith("get");
            prefix |= declaration.getName().toString().startsWith("is");
            prefix |= declaration.getName().toString().startsWith("has");
            if (prefix) {
                String declarationName = declaration.getName().toString();
                String declaringClassName = resolveMethodBinding(declaration).getDeclaringClass().getName();
                if (aggregatedFields.containsKey(declaringClassName)) {
                    String expectedFieldName = declarationName.substring(3, 4).toLowerCase() + declarationName.substring(4);
                    isGetter = aggregatedFields.get(declaringClassName).contains(new AggregatedField(declaration.getReturnType2().resolveBinding().getName(), expectedFieldName));
                }
            }
        }
        return isGetter;
    }

    private boolean isSetter(MethodDeclaration declaration) {
        boolean isSetter = false;
        if (declaration.parameters().size() == 1 && "void".equals(declaration.getReturnType2().resolveBinding().getName())) {
            boolean prefix = false;
            prefix |= declaration.getName().toString().startsWith("set");
            prefix |= declaration.getName().toString().startsWith("is");
            prefix |= declaration.getName().toString().startsWith("has");
            if (prefix) {
                String declarationName = declaration.getName().toString();
                String declaringClassName = resolveMethodBinding(declaration).getDeclaringClass().getName();
                if (aggregatedFields.containsKey(declaringClassName)) {
                    String expectedFieldName = declarationName.substring(3, 4).toLowerCase() + declarationName.substring(4);
                    isSetter = aggregatedFields.get(declaringClassName).contains(new AggregatedField(((SingleVariableDeclaration) declaration.parameters().get(0)).resolveBinding().getType().getName(), expectedFieldName));
                }
            }
        }
        return isSetter;
    }

    private IMethodBinding resolveMethodBinding(MethodDeclaration declaration) {
        Integer key = declaration.hashCode();
        IMethodBinding value;
        if (cacheOfMethodBinding.containsKey(key)) {
            value = cacheOfMethodBinding.get(key);
        } else {
            value = resolveBinding(declaration);
            cacheOfMethodBinding.put(key, value);
        }
        return value;
    }
}
