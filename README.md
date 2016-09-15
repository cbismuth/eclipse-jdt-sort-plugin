# About JDT Sort

JDT Sort is an Eclipse JDT plugin which aims to improve Java project consistency by sorting order insensitive Java code elements.

@see Eclipse bug [#322494](https://bugs.eclipse.org/bugs/show_bug.cgi?id=322494)

## Available sorts

-   **Modifiers** on body declarations (JLS3 compliant)
-   List of **implemented interfaces** in type declarations (alphanumeric)
-   List of **thrown exceptions** in method declarations (alphanumeric)
-   **Catch clause blocks** in try statements (bottom-up sort based on hierarchy of caught exception type or alphanumeric when not related)
-   **Methods declarations** in type/enum declarations (alphanumeric & grouped by closest enclosing declaration type in hierarchy)
-   **Annotations declarations** (predefined order on annotation name and alphanumeric on member/value pairs)

## Additional cleanups

-   Remove the `public` modifier from method declarations within interface declarations
-   Remove the `final` modifier from parameter declarations within abstract method declarations
-   Remove comments (default option is **off**) to clear inconsistent documentation on legacy projects

## How to use it?

-   The *Sort Code* command can be triggered from the JDT *Source* context menu.

## Screenshots

### Context menu

![Context Menu](/doc/screenshots/menu.png)

### Preference page

![Preferences Page](/doc/screenshots/preferences.png)
