export const ANDROID_TOKENS_DATA = {
  stats: {
    m3Parity: {
      pct: "95.2%",
      label: "Material 3 (M3) Compose Parity",
      text: "Comprehensive 1:1 mapping across UX4G components and Material Design 3 Jetpack Compose."
    },
    composeNative: {
      pct: "100%",
      label: "Jetpack Compose Native",
      text: "Zero legacy overhead. Built from the ground up for modern Kotlin Compose."
    },
    a11yCompliance: {
      pct: "100%",
      label: "WCAG 2.1 AA Compliance",
      text: "Government-grade 4.5:1 contrast ratios and 48dp minimum touch targets."
    },
    stateParity: {
      pct: "100%",
      label: "State & API Consistency",
      text: "Unidirectional data flow (UDF) state hoisting compatible with all ViewModels."
    }
  },
  colorTokens: [
    { token: "Ux4gColors.primary", m3: "MaterialTheme.colorScheme.primary", hex: "#4F46E5", role: "Primary action buttons, brand focus highlights & primary navigation" },
    { token: "Ux4gColors.secondary", m3: "MaterialTheme.colorScheme.secondary", hex: "#384860", role: "Supporting UI surfaces, secondary actions & chip filters" },
    { token: "Ux4gColors.success", m3: "MaterialTheme.colorScheme.tertiary", hex: "#155724", role: "Application approved, verified badge, payment completed" },
    { token: "Ux4gColors.error", m3: "MaterialTheme.colorScheme.error", hex: "#721C24", role: "Validation failure, error toast, rejection banner" },
    { token: "Ux4gColors.warning", m3: "Custom warning", hex: "#856404", role: "Action required, pending verification, SLA due soon" },
    { token: "Ux4gColors.info", m3: "MaterialTheme.colorScheme.primaryContainer", hex: "#0C5460", role: "Help tips, informative callouts & advisory banners" },
    { token: "Ux4gColors.neutral", m3: "MaterialTheme.colorScheme.outline", hex: "#6C757D", role: "Dividers, disabled states, unselected border strokes" }
  ],
  typeScale: [
    { level: "Display Large", fontSize: "36sp", lineHeight: "44sp", weight: "700 Bold", usage: "Main screen hero headers & splash titles" },
    { level: "Header Large", fontSize: "28sp", lineHeight: "36sp", weight: "700 Bold", usage: "Section headings & major category headers" },
    { level: "Title Medium", fontSize: "20sp", lineHeight: "26sp", weight: "600 SemiBold", usage: "Card titles, Modal headers & Dialog titles" },
    { level: "Body Large", fontSize: "16sp", lineHeight: "24sp", weight: "400 Regular", usage: "Primary paragraph content & article descriptions" },
    { level: "Body Medium", fontSize: "14sp", lineHeight: "20sp", weight: "400 Regular", usage: "Standard form labels, input values & list text" },
    { level: "Label Small", fontSize: "12sp", lineHeight: "16sp", weight: "500 Medium", usage: "Badges, helper text, timestamps & captions" }
  ],
  spacingScale: [
    { token: "Ux4gSpacing.xs", dp: "4dp", usage: "Tight icon-text gap and badge internal padding" },
    { token: "Ux4gSpacing.sm", dp: "8dp", usage: "Component internal gap & compact card margins" },
    { token: "Ux4gSpacing.md", dp: "16dp", usage: "Standard screen padding & input field vertical gap" },
    { token: "Ux4gSpacing.lg", dp: "24dp", usage: "Card margins and major section dividers" },
    { token: "Ux4gSpacing.xl", dp: "32dp", usage: "Screen top/bottom gutters and hero layout padding" }
  ]
};
