package org.jdtsort.visitor;

import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.jdt.core.dom.IExtendedModifier;
import org.eclipse.jdt.core.dom.MethodDeclaration;
import org.eclipse.jdt.core.dom.Modifier;
import org.eclipse.jdt.core.dom.SingleVariableDeclaration;
import org.jdtsort.util.BindingUtils;

public class InterfaceCleanupVisitor extends ASTVisitor {
    @Override
    public void endVisit(MethodDeclaration node) {
        if (node.getBody() == null) {
            Modifier publicModifier = null;
            for (Object modifier : node.modifiers()) {
                IExtendedModifier _modifier = (IExtendedModifier) modifier;
                if (_modifier.isModifier() && "public".equals(_modifier.toString())) {
                    if (BindingUtils.resolveBinding(node).getDeclaringClass().isInterface()) {
                        publicModifier = (Modifier) _modifier;
                    }
                }
            }
            if (publicModifier != null) {
                node.modifiers().remove(publicModifier);
            }
            node.accept(new ASTVisitor() {
                @Override
                public void endVisit(SingleVariableDeclaration innerNode) {
                    Modifier finalModifier = null;
                    for (Object modifier : innerNode.modifiers()) {
                        IExtendedModifier _modifier = (IExtendedModifier) modifier;
                        if (_modifier.isModifier() && "final".equals(_modifier.toString())) {
                            finalModifier = (Modifier) _modifier;
                        }
                    }
                    if (finalModifier != null) {
                        innerNode.modifiers().remove(finalModifier);
                    }
                }
            });
        }
    }
}
