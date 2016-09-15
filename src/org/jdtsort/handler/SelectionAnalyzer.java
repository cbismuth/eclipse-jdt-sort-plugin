package org.jdtsort.handler;

import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.IPackageFragment;
import org.eclipse.jdt.core.IPackageFragmentRoot;
import org.eclipse.jdt.core.ITypeRoot;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jdt.ui.JavaUI;
import org.eclipse.jface.text.ITextSelection;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.ui.IEditorPart;

import static java.util.Arrays.asList;
import static org.eclipse.jdt.core.IJavaElement.COMPILATION_UNIT;
import static org.eclipse.jdt.core.IJavaElement.PACKAGE_FRAGMENT;
import static org.eclipse.jdt.core.IPackageFragmentRoot.K_SOURCE;
import static org.jdtsort.Logger.log;

final class SelectionAnalyzer {
    public static Map<IJavaProject, Collection<ICompilationUnit>> getCompilationUnitsFromSelection(IEditorPart editor, ISelection selection) {
        Map<IJavaProject, Collection<ICompilationUnit>> map = new HashMap<IJavaProject, Collection<ICompilationUnit>>();
        if (selection instanceof IStructuredSelection) {
            for (Object selected : ((IStructuredSelection) selection).toList()) {
                if (selected instanceof IJavaElement) {
                    IJavaElement element = (IJavaElement) selected;
                    try {
                        if (element.exists() && element.isStructureKnown()) {
                            if (element instanceof IJavaProject) {
                                IJavaProject project = (IJavaProject) element;
                                Collection<ICompilationUnit> units = map.get(project);
                                if (units == null) {
                                    units = new LinkedList<ICompilationUnit>();
                                    map.put(project, units);
                                }
                                units.addAll(getCompilationUnitsFromJavaProject((IJavaProject) element));
                            } else if (element instanceof IPackageFragmentRoot) {
                                IPackageFragmentRoot root = (IPackageFragmentRoot) element;
                                IJavaProject project = root.getJavaProject();
                                Collection<ICompilationUnit> units = map.get(project);
                                if (units == null) {
                                    units = new LinkedList<ICompilationUnit>();
                                    map.put(project, units);
                                }
                                units.addAll(getCompilationUnitsFromPackageFragmentRoot((IPackageFragmentRoot) element));
                            } else if (element instanceof IPackageFragment) {
                                IPackageFragment pack = (IPackageFragment) element;
                                IJavaProject project = pack.getJavaProject();
                                Collection<ICompilationUnit> units = map.get(project);
                                if (units == null) {
                                    units = new LinkedList<ICompilationUnit>();
                                    map.put(project, units);
                                }
                                units.addAll(getCompilationUnitsFromPackageFragment((IPackageFragment) element));
                            } else if (element instanceof ICompilationUnit) {
                                ICompilationUnit unit = (ICompilationUnit) element;
                                IJavaProject project = unit.getJavaProject();
                                Collection<ICompilationUnit> units = map.get(project);
                                if (units == null) {
                                    units = new LinkedList<ICompilationUnit>();
                                    map.put(project, units);
                                }
                                units.add(unit);
                            }
                        }
                    } catch (JavaModelException e) {
                        log(e);
                    }
                }
            }
        } else {
            if (editor != null && selection instanceof ITextSelection) {
                ITypeRoot root = JavaUI.getEditorInputTypeRoot(editor.getEditorInput());
                if (root != null && root.getElementType() == COMPILATION_UNIT) {
                    ICompilationUnit unit = (ICompilationUnit) root;
                    IJavaProject project = unit.getJavaProject();
                    if (project != null) {
                        Collection<ICompilationUnit> units = new LinkedList<ICompilationUnit>();
                        map.put(project, units);
                        units.add(unit);
                    }
                }
            }
        }
        return map;
    }

    private static Collection<ICompilationUnit> getCompilationUnitsFromJavaProject(IJavaProject project) throws JavaModelException {
        Collection<ICompilationUnit> units = new LinkedList<ICompilationUnit>();
        IPackageFragmentRoot[] roots = project.getPackageFragmentRoots();
        for (IPackageFragmentRoot root : roots) {
            units.addAll(getCompilationUnitsFromPackageFragmentRoot(root));
        }
        return units;
    }

    private static Collection<ICompilationUnit> getCompilationUnitsFromPackageFragmentRoot(IPackageFragmentRoot root) throws JavaModelException {
        Collection<ICompilationUnit> units = new LinkedList<ICompilationUnit>();
        if (root.getKind() == K_SOURCE) {
            IJavaElement[] children = root.getChildren();
            for (IJavaElement child : children) {
                if (child.getElementType() == PACKAGE_FRAGMENT) {
                    units.addAll(getCompilationUnitsFromPackageFragment((IPackageFragment) child));
                }
            }
        }
        return units;
    }

    private static Collection<ICompilationUnit> getCompilationUnitsFromPackageFragment(IPackageFragment pack) throws JavaModelException {
        return asList(pack.getCompilationUnits());
    }

    private SelectionAnalyzer() {}
}
