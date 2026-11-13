# Localization Standards

## Language
- The primary UI language for this application is **Russian**.
- All user-facing text must be written in Russian.

## Scope of Localization
- **Thymeleaf Templates (.html)**: Translate all user-facing text, including page titles, form labels, button texts, table headers, placeholder texts, and alt attributes.
- **README.md**: The documentation must be written entirely in Russian, explaining how to build and run the project.

## Strict Constraints (What NOT to change)
- **DO NOT** change any Java code logic.
- **DO NOT** change HTML tags, CSS classes, or variable names (e.g., Thymeleaf `th:field` or `th:text` expressions should remain untouched except for their literal string values).
- **DO NOT** translate database entity names, API endpoints, or backend keys. Only translate the literal text that the user sees in the browser.

## Technical Requirements
- Ensure all HTML templates have `<meta charset="UTF-8">` inside the `<head>` section to correctly support Cyrillic characters.