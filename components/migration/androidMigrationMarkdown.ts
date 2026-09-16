export const MIGRATION_MD_CONTENT = `# UX4G Android Design System — Complete Migration & Adoption Guide (MIGRATION.md)

This comprehensive guide provides everything Android engineering teams need to safely migrate from **Material 3 (M3) Compose** or **Legacy Material Components XML (MDC)** to the **UX4G Android Design System** without breaking existing business logic, app architecture, or state management.

---

## 1. Core Migration Philosophy & The 5 Golden Rules

\`\`\`
┌──────────────────────────────────────────────────────────────────────────────┐
│                    THE 5 GOLDEN RULES OF ANDROID MIGRATION                   │
├──────────────────────────────────────────────────────────────────────────────┤
│ 1. ZERO STATE DISRUPTION: Never alter ViewModel bindings or StateFlow.       │
│ 2. PRESERVE CONTRACTS: Keep identical public Composable parameter signatures.│
│ 3. TOKEN PARITY: Map custom colors to Ux4gTheme tokens before rewriting UI.  │
│ 4. SCREEN-BY-SCREEN: Migrate leaf components first, then full screens.       │
│ 5. ACCESSIBILITY LOCK: Ensure 48dp touch targets and WCAG 2.1 AA contrast.   │
└──────────────────────────────────────────────────────────────────────────────┘
\`\`\`

---

## 2. Dependency Setup (Gradle)

Ensure \`mavenCentral()\` is present in your \`settings.gradle.kts\`:

\`\`\`kotlin
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
    }
}
\`\`\`

Add the official UX4G Android Compose artifact to your \`app/build.gradle.kts\`:

\`\`\`kotlin
dependencies {
    // UX4G Android Design System
    implementation("io.github.ux4g-negd:designsystemv1:1.0.5")
    
    // Jetpack Compose BOM
    implementation(platform("androidx.compose:compose-bom:2024.09.00"))
    implementation("androidx.compose.ui:ui")
    implementation("androidx.compose.material3:material3")
}
\`\`\`

---

## 3. Theme Wrap

Wrap your application root with \`Ux4gTheme\`:

\`\`\`kotlin
import androidx.compose.runtime.Composable
import com.ux4g.designsystemv1.theme.Ux4gTheme

@Composable
fun MainApp() {
    Ux4gTheme(darkTheme = false) {
        AppNavigation()
    }
}
\`\`\`

---

## 4. Side-by-Side Migration Recipes

### 1. Button Migration
**Before (Material 3):**
\`\`\`kotlin
Button(
    onClick = { viewModel.submit() },
    enabled = !isLoading,
    modifier = Modifier.fillMaxWidth()
) {
    if (isLoading) {
        CircularProgressIndicator(modifier = Modifier.size(18.dp))
    } else {
        Text("Submit Application")
    }
}
\`\`\`

**After (UX4G Compose):**
\`\`\`kotlin
import com.ux4g.designsystemv1.components.Ux4gButton
import com.ux4g.designsystemv1.components.Ux4gButtonVariant

Ux4gButton(
    text = "Submit Application",
    onClick = { viewModel.submit() },
    variant = Ux4gButtonVariant.Primary,
    isLoading = isLoading,
    modifier = Modifier.fillMaxWidth()
)
\`\`\`

---

### 2. Input Field (TextField)
**Before (Material 3):**
\`\`\`kotlin
OutlinedTextField(
    value = text,
    onValueChange = { text = it },
    label = { Text("Full Name *") },
    isError = error != null,
    supportingText = { if (error != null) Text(error) },
    modifier = Modifier.fillMaxWidth()
)
\`\`\`

**After (UX4G Compose):**
\`\`\`kotlin
import com.ux4g.designsystemv1.components.Ux4gInputField

Ux4gInputField(
    value = text,
    onValueChange = { text = it },
    label = "Full Name",
    isRequired = true,
    errorMessage = error,
    modifier = Modifier.fillMaxWidth()
)
\`\`\`

---

### 3. Aadhaar Verification
**Before (Custom Hand-rolled):**
\`\`\`kotlin
// 100+ lines of custom formatting, input filtering, and Verhoeff checks
\`\`\`

**After (UX4G Compose):**
\`\`\`kotlin
import com.ux4g.designsystemv1.components.inputfield.Ux4gAadhaarInputField

Ux4gAadhaarInputField(
    value = aadhaar,
    onValueChange = { aadhaar = it },
    label = "Aadhaar Number",
    placeholder = "XXXX XXXX XXXX",
    caption = "Enter your 12-digit Aadhaar number",
    modifier = Modifier.fillMaxWidth()
)
\`\`\`

---

## 5. Components Handled Directly via Material 3
The following components are not bundled in UX4G and should continue to use official Jetpack Compose Material 3 implementations directly:
- **Bottom Navigation:** Use \`androidx.compose.material3.NavigationBar\` & \`NavigationBarItem\`
- **Bottom Sheet:** Use \`androidx.compose.material3.ModalBottomSheet\`
- **Navigation Drawer:** Use \`androidx.compose.material3.ModalNavigationDrawer\`
- **Floating Action Button (FAB):** Use \`androidx.compose.material3.FloatingActionButton\` / \`ExtendedFloatingActionButton\`

---

## 6. Post-Migration Verification Checklist
- [ ] All clickable targets meet 48dp x 48dp minimum bounds.
- [ ] ViewModel state flows and coroutine collectors function without regression.
- [ ] Multilingual Indian language text renders cleanly with proper baseline line heights.
- [ ] Dark theme contrast adheres to WCAG 2.1 AA ratios.
`;
