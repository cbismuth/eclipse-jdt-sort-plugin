package org.jdtsort.util;

import java.util.LinkedList;
import java.util.List;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.core.IMethod;
import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.core.dom.IAnnotationBinding;
import org.eclipse.jdt.core.dom.IMethodBinding;
import org.eclipse.jdt.core.dom.ITypeBinding;
import org.eclipse.jdt.core.dom.MethodDeclaration;
import org.eclipse.jdt.core.dom.Type;
import org.eclipse.jdt.core.search.IJavaSearchConstants;
import org.eclipse.jdt.core.search.IJavaSearchScope;
import org.eclipse.jdt.core.search.SearchEngine;
import org.eclipse.jdt.core.search.SearchMatch;
import org.eclipse.jdt.core.search.SearchParticipant;
import org.eclipse.jdt.core.search.SearchPattern;
import org.eclipse.jdt.core.search.SearchRequestor;
import org.jdtsort.comparator.SuperInterfaceComparator;

import static java.lang.String.format;
import static java.util.Collections.sort;
import static org.eclipse.jdt.core.search.SearchEngine.createHierarchyScope;
import static org.eclipse.jdt.core.search.SearchEngine.getDefaultSearchParticipant;
import static org.eclipse.jdt.core.search.SearchPattern.R_CASE_SENSITIVE;
import static org.eclipse.jdt.core.search.SearchPattern.R_ERASURE_MATCH;
import static org.eclipse.jdt.core.search.SearchPattern.R_EXACT_MATCH;
import static org.eclipse.jdt.core.search.SearchPattern.createPattern;
import static org.jdtsort.Logger.log;
import static org.jdtsort.Logger.logError;

public final class BindingUtils implements IJavaSearchConstants {
    public static IType getClosestEnclosingTypeDeclaration(MethodDeclaration declaration, boolean withOverrideAnnotationOnly) {
        IType closestEnclosingTypeDeclaration = null;
        IMethodBinding binding = resolveBinding(declaration);
        boolean override = false;
        if (withOverrideAnnotationOnly) {
            IAnnotationBinding[] annotations = binding.getAnnotations();
            if (annotations.length > 0) {
                for (IAnnotationBinding annotation : annotations) {
                    if ("Override".equals(annotation.getName())) {
                        override = true;
                    }
                }
            }
        } else {
            override = true;
        }
        if (override) {
            // http://www.eclipse.org/forums/index.php/mv/msg/203289/649682/#msg_649682
            IJavaElement element = binding.getJavaElement();
            IType declaringClassJavaElement = (IType) binding.getDeclaringClass().getJavaElement();
            List<IType> hierarchy = getBottomUpHierarchy(element, declaringClassJavaElement);
            if (!hierarchy.isEmpty()) {
                int i = 0;
                while (i < hierarchy.size() && hierarchy.get(i).getElementName().equals(declaringClassJavaElement.getElementName())) {
                    i++;
                }
                closestEnclosingTypeDeclaration = hierarchy.get(i);
            }
        }
        return closestEnclosingTypeDeclaration;
    }

    public static IMethodBinding resolveBinding(MethodDeclaration declaration) {
        IMethodBinding binding = declaration.resolveBinding();
        if (binding == null) {
            logError(format("Binding of method %s can't be resolved.", declaration.getName().toString()));
        }
        return binding;
    }

    private static List<IType> getBottomUpHierarchy(IJavaElement element, IType declaringClassJavaElement) {
        final List<IType> hierarchy = new LinkedList<IType>();
        int limitTo = DECLARATIONS | IGNORE_DECLARING_TYPE;
        int matchRule = R_EXACT_MATCH | R_CASE_SENSITIVE | R_ERASURE_MATCH;
        SearchPattern pattern = createPattern(element, limitTo, matchRule);
        SearchParticipant[] participants = new SearchParticipant[] {
            getDefaultSearchParticipant()
        };
        try {
            // http://www.eclipse.org/forums/index.php/mv/msg/203056/648940/#msg_648940
            IJavaSearchScope scope = createHierarchyScope(declaringClassJavaElement);
            new SearchEngine().search(pattern, participants, scope, new SearchRequestor() {
                @Override
                public void acceptSearchMatch(SearchMatch match) {
                    IMethod method = (IMethod) match.getElement();
                    IType type = method.getDeclaringType();
                    if (type != null) {
                        hierarchy.add(type);
                    }
                }
            }, null);
            sort(hierarchy, new SuperInterfaceComparator());
        } catch (CoreException e) {
            log(e);
        }
        return hierarchy;
    }

    public static boolean isSubType(ITypeBinding type1, ITypeBinding type2) {
        boolean isSubType = false;
        ITypeBinding superClass = type1.getSuperclass();
        while (superClass != null && !isSubType) {
            if (superClass.equals(type2)) {
                isSubType = true;
            } else {
                superClass = superClass.getSuperclass();
            }
        }
        return isSubType;
    }

    public static ITypeBinding resolveBinding(Type declaration) {
        ITypeBinding binding = declaration.resolveBinding();
        if (binding == null) {
            logError(format("Binding of type %s can't be resolved.", declaration.toString()));
        }
        return binding;
    }

    private BindingUtils() {}
}
