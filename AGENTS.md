# TypeScript Project Rules

## External File Loading

CRITICAL: When you encounter a file reference (e.g., @.agents/rules/general.md), use your Read tool to load it on a need-to-know basis. They're relevant to the SPECIFIC task at hand.

Instructions:

- Do NOT preemptively load all references - use lazy loading based on actual need
- When loaded, treat content as mandatory instructions that override defaults
- Follow references recursively when needed

## Development Guidelines

For UI related code style and best practices: @.agents/rules/ui.md

## Domain: Currency
- All monetary values in the application are in Russian rubles (RUB).
- Rounding follows standard RUB conventions (2 decimal places).

## Shadleaf (Thymeleaf UI components)

- The UI uses Shadleaf: UI components as Thymeleaf tags with the `sl:` prefix
  (`xmlns:sl="https://shadleaf.dev/sl"`), rendered on the server. Not React: no JSX, no Radix, no `asChild`.
- Docs as Markdown: https://shadleaf.dev/1.0.2/llms.txt. Read the component's page
  (`.../components/<name>.md`) before using a component; it ends with the attribute table.
- An attribute that is not a prop passes through to the HTML silently, so a misspelt prop does nothing.
  Use only the props in the attribute table; `th:<prop>="${...}"` sets one from an expression.
- Change a component's look with its props (`variant`, `size`, `border`, ...), not with utility classes:
  the library's stylesheet defines none. `class` is appended to the component's own class.
- A link that looks like a button is `<sl:button as="a" th:href="@{/x}">`. `<sl:button>` renders
  `type="button"`: write `type="submit"` on the one that submits a form.
- Forms: `th:object` on the `<form>`, `th:field` on `<sl:field>` (its label, control and errors share it),
  `<sl:form-errors/>` before the fields.
- Dialogs and sheets need an `id` and open from any button with `commandfor="<id>" command="show-modal"`
  (`command="close"` closes). There are no trigger or close parts. Menus and popovers open with
  `<sl:dropdown-menu-trigger>` / `<sl:popover-trigger>` or `popovertarget`.
- Icons: `<sl:icon name="...">` with Lucide names. An icon-only button (`size="icon"`, `icon-sm`, ...)
  needs an `aria-label`; rendering fails without one.
- Charts: `<sl:chart th:data="${rows}" label="...">` with parts (`sl:chart-x-axis key="month"`,
  `sl:chart-bar key="desktop" th:label="#{...}"`). No `ChartConfig` and no formatter functions: labels and
  colours go on the series parts, number and date formats are names (`format="month-short"`).
- Rendering fails with a message on a wrong prop value, a missing required prop or a missing accessible
  name: render every page you change (run the tests or load the page) and fix what it reports.
- With `hx-boost` on `<body>`, every boosted page load sends `HX-Request`: return a fragment only when the
  request targets one (see the "Application shell" guide, "With htmx").