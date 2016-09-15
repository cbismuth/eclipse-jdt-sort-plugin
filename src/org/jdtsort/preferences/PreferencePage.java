package org.jdtsort.preferences;

import org.eclipse.jface.preference.BooleanFieldEditor;
import org.eclipse.jface.preference.FieldEditorPreferencePage;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPreferencePage;
import org.jdtsort.Activator;

public class PreferencePage extends FieldEditorPreferencePage implements IWorkbenchPreferencePage, PreferenceConstants {
    public PreferencePage() {
        super(FieldEditorPreferencePage.GRID);
        setPreferenceStore(Activator.getDefault().getPreferenceStore());
        setDescription("General settings for Java element sort.\n\nDefault sort is:\n\t1. constructor,\n\t2. getter,\n\t3. setter,\n\t4. others.\n\n");
    }

    @Override
    public void init(IWorkbench workbench) {}

    @Override
    public void createFieldEditors() {
        addField(new BooleanFieldEditor(P_CLEANUP_INTERFACES, "&Cleanup interfaces", getFieldEditorParent()));
        addField(new BooleanFieldEditor(P_SORT_ANNOTATIONS, "&Sort annotations", getFieldEditorParent()));
        addField(new BooleanFieldEditor(P_SORT_MODIFIERS, "&Sort modifiers", getFieldEditorParent()));
        addField(new BooleanFieldEditor(P_SORT_IMPLEMENTED_INTERFACES, "&Sort implemented interfaces", getFieldEditorParent()));
        addField(new BooleanFieldEditor(P_SORT_THROWN_EXCEPTIONS, "&Sort thrown exceptions", getFieldEditorParent()));
        addField(new BooleanFieldEditor(P_SORT_CATCH_BLOCKS, "&Sort catch blocks", getFieldEditorParent()));
        addField(new BooleanFieldEditor(P_SORT_ACCESSORS_BY_FIELD_INDEX, "&Sort accessors by field index", getFieldEditorParent()));
        addField(new BooleanFieldEditor(P_GROUP_METHODS_BY_HIERARCHY, "&Group methods by hierarchy", getFieldEditorParent()));
        addField(new BooleanFieldEditor(P_DEFAULT_SORT_METHODS_LEXICOGRAPHICALLY_ON_SAME_KIND, "&Default sort methods lexicographically on same kind (exclude interfaces)", getFieldEditorParent()));
        addField(new BooleanFieldEditor(P_DEFAULT_SORT_METHODS_BY_NUMBER_OF_ARGUMENTS_ON_SAME_KIND_AND_NAME, "&Default sort methods by number of arguments on same kind and name", getFieldEditorParent()));
        addField(new BooleanFieldEditor(P_REMOVE_COMMENTS, "&Remove comments", getFieldEditorParent()));
        addField(new BooleanFieldEditor(P_FORMAT_SOURCE_CODE, "&Format source code", getFieldEditorParent()));
    }
}
