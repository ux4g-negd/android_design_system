export const MIGRATION_PROMPT_MD_CONTENT = `# UX4G Android Design System — Non-Destructive AI Migration Prompt

You are an expert Android Jetpack Compose engineer tasked with migrating an existing Android application to the **UX4G Android Design System** (\`io.github.ux4g-negd:designsystemv1:1.0.5\`).

---

## 🎯 Primary Objective
Migrate Android composables and XML layouts from Material 3 (M3) or Material Components Android (MDC) to UX4G Android Compose while strictly preserving all existing state controllers, business logic, ViewModel flows, and UI contracts.

---

## 📜 Zero-Disruption Migration Contract (Critical Rules)

1. **State Preservation:**
   - Do NOT alter any \`MutableState\`, \`StateFlow\`, \`LiveData\`, or ViewModel bindings.
   - Do NOT rename event handler callbacks or change coroutine dispatcher contexts.

2. **Component Replacement Mapping:**
   - \`Button\` / \`MaterialButton\` ➔ \`Ux4gButton\`
   - \`IconButton\` ➔ \`Ux4gIconButton\`
   - \`OutlinedTextField\` / \`TextInputEditText\` ➔ \`Ux4gInputField\`
   - \`AlertDialog\` / \`MaterialAlertDialog\` ➔ \`Ux4gModal\`
   - \`Card\` / \`MaterialCardView\` ➔ \`Ux4gCard\`
   - \`Checkbox\` / \`MaterialCheckBox\` ➔ \`Ux4gCheckbox\`
   - \`RadioButton\` / \`MaterialRadioButton\` ➔ \`Ux4gRadioButton\`
   - \`Switch\` / \`MaterialSwitch\` ➔ \`Ux4gSwitch\`
   - \`LinearProgressIndicator\` ➔ \`Ux4gLinearProgressBar\`
   - \`CircularProgressIndicator\` ➔ \`Ux4gCircularProgress\`
   - Custom Arc Gauge ➔ \`Ux4gHalfCircleProgress\`
   - Custom 12-digit Aadhaar logic ➔ \`Ux4gAadhaarInputField\`
   - Custom PAN text watchers ➔ \`Ux4gPanInputField\`
   - Custom OTP digit boxes ➔ \`Ux4gOtpInput\`
   - Feedback rating/forms ➔ \`Ux4gFeedbackFormStar\`, \`Ux4gFeedbackFormCsat\`, \`Ux4gFeedbackFormNps\`
   - Tooltip / Popover ➔ \`Ux4gTooltip\`

3. **Components Retaining Standard Material 3 (Not in UX4G):**
   - Bottom Navigation ➔ Keep Material 3 \`NavigationBar\`
   - Bottom Sheet ➔ Keep Material 3 \`ModalBottomSheet\`
   - Navigation Drawer ➔ Keep Material 3 \`ModalNavigationDrawer\`
   - Floating Action Button (FAB) ➔ Keep Material 3 \`FloatingActionButton\`

4. **Theme & Tokens:**
   - Ensure the app root is wrapped inside \`Ux4gTheme { ... }\`.
   - Use \`Ux4gTheme.colors\`, \`Ux4gTheme.typography\`, and \`Ux4gTheme.spacing\` for all design tokens.

---

## 🚀 Execution Process
1. Inspect the target Android project structure and identify all UI screens and components.
2. Outline a component-by-component migration plan before making modifications.
3. Replace components incrementally from leaf nodes to screen compositions.
4. Verify compile safety, preview rendering, and touch target accessibility.
`;
