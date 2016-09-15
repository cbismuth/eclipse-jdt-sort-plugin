package org.jdtsort.preferences;

import org.eclipse.core.runtime.preferences.AbstractPreferenceInitializer;
import org.eclipse.jface.preference.IPreferenceStore;
import org.jdtsort.Activator;

public class PreferenceInitializer extends AbstractPreferenceInitializer implements PreferenceConstants {
    public static boolean getBoolean(String name) {
        return Activator.getDefault().getPreferenceStore().getBoolean(name);
    }

    public static void setBoolean(String name, boolean value) {
        Activator.getDefault().getPreferenceStore().setValue(name, value);
    }

    @Override
    public void initializeDefaultPreferences() {
        IPreferenceStore store = Activator.getDefault().getPreferenceStore();
        store.setDefault(P_CLEANUP_INTERFACES, true);
        store.setDefault(P_SORT_ANNOTATIONS, true);
        store.setDefault(P_SORT_MODIFIERS, true);
        store.setDefault(P_SORT_IMPLEMENTED_INTERFACES, true);
        store.setDefault(P_SORT_THROWN_EXCEPTIONS, true);
        store.setDefault(P_SORT_CATCH_BLOCKS, true);
        store.setDefault(P_KIND_INDEX_CTOR, 0);
        store.setDefault(P_KIND_INDEX_GET, 1);
        store.setDefault(P_KIND_INDEX_SET, 2);
        store.setDefault(P_KIND_INDEX_OTHER, 3);
        store.setDefault(P_SORT_ACCESSORS_BY_FIELD_INDEX, true);
        store.setDefault(P_GROUP_METHODS_BY_HIERARCHY, true);
        store.setDefault(P_DEFAULT_SORT_METHODS_LEXICOGRAPHICALLY_ON_SAME_KIND, false);
        store.setDefault(P_DEFAULT_SORT_METHODS_BY_NUMBER_OF_ARGUMENTS_ON_SAME_KIND_AND_NAME, true);
        store.setDefault(P_REMOVE_COMMENTS, false);
        store.setDefault(P_FORMAT_SOURCE_CODE, true);
    }
}
