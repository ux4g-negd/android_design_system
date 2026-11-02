export interface ChangelogItem {
  component: string;
  componentPath?: string | null;
  summary: string;
  details: string[];
}

export type ChangelogCategoryType = 'Added' | 'Fixed' | 'Enhanced' | 'Changed';

export interface ChangelogCategory {
  type: ChangelogCategoryType;
  label: string;
  items: ChangelogItem[];
}

export interface ChangelogRelease {
  version: string;
  date: string;
  relativeDate: string;
  tag: 'Latest' | 'Major' | 'Minor' | 'Maintenance' | 'Patch' | 'Initial';
  isLatest?: boolean;
  title: string;
  overview: string;
  highlights: string[];
  installCommand: string;
  groovyCommand?: string;
  categories: ChangelogCategory[];
}

export const ANDROID_CHANGELOG_DATA: ChangelogRelease[] = [
  {
    version: '1.0.8',
    date: 'October 15, 2026',
    relativeDate: 'Latest Release',
    tag: 'Latest',
    isLatest: true,
    title: 'Navigation Primitives, Physics Drawer & Modal Bottom Sheet Suite',
    overview:
      'Introduces 4 major mobile navigation and modal primitives to the UX4G Jetpack Compose design system: Ux4gBottomSheet with drag gestures and snap presets, Ux4gSideMenu & Ux4gDrawer with physics-based sliding animations and 6 presets, Ux4gFab with speed-dial menus, and Ux4gBottomNavigationBar with 6 variants.',
    highlights: [
      'Added Ux4gBottomSheet modal bottom sheet with velocity drag snapping (peek, half, expanded, full), fixed action footers, drag handle indicator, and backdrop dismiss.',
      'Implemented Ux4gSideMenu & Ux4gDrawer with physics-based left-to-right slide drawer animation, scrim fade, back gesture handling, and 6 presets.',
      'Implemented Ux4gFab Floating Action Button supporting 5 variants, 2 color themes (Primary, White), 3 sizes (Small 40dp, Medium 56dp, Large 96dp), and speed-dial submenus.',
      'Added Ux4gBottomNavigationBar supporting 6 visual variants (labelled, top rounded, icon only, centre action, floating pill, indicators), badge counters, and FAB notches.',
      'Published production artifact io.github.ux4g-negd:designsystemv1:1.0.8 with enhanced Compose compiler optimizations.',
    ],
    installCommand: 'implementation("io.github.ux4g-negd:designsystemv1:1.0.8")',
    groovyCommand: "implementation 'io.github.ux4g-negd:designsystemv1:1.0.8'",
    categories: [
      {
        type: 'Added',
        label: 'New Components & Navigation Suite',
        items: [
          {
            component: 'Ux4gBottomSheet',
            componentPath: '/components/bottom-sheet',
            summary: 'Modal Bottom Sheet with velocity snapping & fixed footer actions',
            details: [
              'Complete UX4G Modal Bottom Sheet implementation for Jetpack Compose with responsive snap presets: peek, half, expanded, and full screen.',
              'Smooth velocity drag gestures with snap physics, drag handle indicator, and configurable backdrop scrim.',
              'Anchored fixed CTA button footer actions that remain stationary while sheet content scrolls.',
              'Full light and dark theme adaptation with UX4G elevation and surface tokens.',
            ],
          },
          {
            component: 'Ux4gSideMenu',
            componentPath: '/components/side-menu',
            summary: 'Physics-based slide navigation drawer with 6 presets',
            details: [
              'Physics-based left-to-right slide drawer movement animation with scrim fade and hardware back gesture support.',
              'Supported 6 distinct government app presets: Standard, Officer Inbox, Citizen Profile, Action Required, Department Switcher, and Citizen Simple.',
              'Built-in support for header user profile card, collapsible sub-menus, notification badge counters, and footer logout actions.',
            ],
          },
          {
            component: 'Ux4gFab',
            componentPath: '/components/fab',
            summary: 'Floating Action Button with 5 variants & speed-dial submenus',
            details: [
              'Implemented Ux4gFab supporting Standard circular, Mini (40dp), Extended (with icon & text label), and Speed-Dial sub-action menus.',
              '2 color themes: Primary (Ux4gPrimary) and White surface with high-contrast icon styling.',
              'Smooth expand/collapse animations with staggered action item entrance and backdrop blur.',
            ],
          },
          {
            component: 'Ux4gBottomNavigationBar',
            componentPath: '/components/bottom-nav',
            summary: 'Configurable bottom navigation bar with 6 variants',
            details: [
              'Supports 6 visual variants: Labelled, Top Rounded, Icon Only, Centre Action FAB, Floating Pill, and Top Indicator bars.',
              'Integrated notification badges, active pill capsule highlight, and smooth selection animations.',
              'Responsive layout scaling down gracefully on compact mobile viewports (320dp–360dp) without label truncation.',
            ],
          },
        ],
      },
      {
        type: 'Enhanced',
        label: 'Accessibility & Touch Targets',
        items: [
          {
            component: 'Accessibility & Semantics',
            componentPath: null,
            summary: 'WCAG AA 48dp minimum touch target bounds',
            details: [
              'Enforced minimum 48dp interactive touch target sizing across all navigation tabs, drawer items, and FAB click areas.',
              'Integrated TalkBack accessibility traits and state description announcements for expanded/collapsed drawer and bottom sheet states.',
            ],
          },
        ],
      },
    ],
  },
  {
    version: '1.0.7',
    date: 'October 15, 2026',
    relativeDate: 'Maintenance Release',
    tag: 'Maintenance',
    title: 'Component Refinements & Jetpack Compose Parity',
    overview:
      'Prepares core foundations and dependencies for navigation components, updates internal token mappings, and improves Compose layout stability across varying screen sizes and dark theme modes.',
    highlights: [
      'Refined Compose layout stability and preview rendering in Android Studio.',
      'Enhanced dark theme token contrast across elevated cards and surface containers.',
      'Prepared internal architecture for modular bottom navigation and drawer components.',
    ],
    installCommand: 'implementation("io.github.ux4g-negd:designsystemv1:1.0.7")',
    groovyCommand: "implementation 'io.github.ux4g-negd:designsystemv1:1.0.7'",
    categories: [
      {
        type: 'Enhanced',
        label: 'Theme & Preview Stability',
        items: [
          {
            component: 'Ux4gTheme',
            componentPath: '/tokens/theme',
            summary: 'Elevation surface colors & contrast compliance',
            details: [
              'Enhanced dark theme surface contrast for elevated containers (Surface Container High, Highest).',
              'Added @Preview parameter providers for streamlined Android Studio multi-variant preview rendering.',
            ],
          },
        ],
      },
    ],
  },
  {
    version: '1.0.6',
    date: 'September 29, 2026',
    relativeDate: 'Accessibility Update',
    tag: 'Minor',
    title: 'Aadhaar Input Field Touch Targets & Mask Variants',
    overview:
      'Enhances Ux4gAadhaarInputField with minimumInteractiveComponentSize() enforcement for government accessibility standards, and refines Aadhaar number masking formatting.',
    highlights: [
      'Updated Ux4gAadhaarInputField mask variant to enforce 48dp minimum interactive touch target via minimumInteractiveComponentSize().',
      'Fixed Aadhaar input formatting with live 4-digit hyphen grouping (XXXX-XXXX-XXXX).',
      'Integrated Verhoeff checksum algorithm directly inside Aadhaar input validation for instant citizen feedback.',
    ],
    installCommand: 'implementation("io.github.ux4g-negd:designsystemv1:1.0.6")',
    groovyCommand: "implementation 'io.github.ux4g-negd:designsystemv1:1.0.6'",
    categories: [
      {
        type: 'Enhanced',
        label: 'Government Identity Modules',
        items: [
          {
            component: 'Ux4gAadhaarInputField',
            componentPath: '/components/aadhaar-input',
            summary: 'Touch target accessibility enforcement & Verhoeff validation',
            details: [
              'Added minimumInteractiveComponentSize() modifier to guarantee 48dp minimum touch target compliance for accessibility auditing.',
              'Refined Aadhaar masked display mode (showing only the last 4 digits `•••• •••• 1234` for sensitive citizen verification screens).',
              'Improved Verhoeff checksum validation error message localization support.',
            ],
          },
        ],
      },
      {
        type: 'Fixed',
        label: 'Input Field Bug Fixes',
        items: [
          {
            component: 'Ux4gAadhaarInputField',
            componentPath: '/components/aadhaar-input',
            summary: 'Caret positioning fix during rapid backspace',
            details: [
              'Resolved text cursor offset jumping when deleting digits across hyphen boundary separators.',
            ],
          },
        ],
      },
    ],
  },
  {
    version: '1.0.5',
    date: 'September 15, 2026',
    relativeDate: 'Gov Modules & Parity',
    tag: 'Minor',
    title: 'Biometric Face Auth, Result List & Time Slot Components',
    overview:
      'Introduces citizen-facing biometric face verification UI, standardized Result List for search/records, Time Slot booking grid for appointment booking, and automated PGP signing for Maven Central.',
    highlights: [
      'Added Ux4gBiometric biometric authentication component with camera face guide oval, liveness prompt animations, and fallback PIN option.',
      'Added Ux4gResultList component for displaying citizen search results, service directory items, and application tracking records.',
      'Added Ux4gTimeSlot component supporting morning, afternoon, and evening booking slots with available, booked, and selected states.',
      'Configured automated PGP in-memory key signing for Maven Central artifact deployment.',
    ],
    installCommand: 'implementation("io.github.ux4g-negd:designsystemv1:1.0.5")',
    groovyCommand: "implementation 'io.github.ux4g-negd:designsystemv1:1.0.5'",
    categories: [
      {
        type: 'Added',
        label: 'New Citizen Service Components',
        items: [
          {
            component: 'Ux4gBiometric',
            componentPath: '/components/biometric',
            summary: 'Face biometric verification & camera liveness guide',
            details: [
              'Camera guide oval overlay with live scanning pulse indicator.',
              'Liveness detection challenge prompts (Blink eyes, Turn head) with accessible audio-visual feedback.',
              'Secure PIN and OTP fallback action handlers.',
            ],
          },
          {
            component: 'Ux4gResultList',
            componentPath: '/components/result-list',
            summary: 'Standard citizen service result rows & record cards',
            details: [
              'Standardized layout for service listings, application records, and citizen search queries.',
              'Configurable metadata chips, status tags (Pending, Verified, Rejected), and primary action CTA.',
            ],
          },
          {
            component: 'Ux4gTimeSlot',
            componentPath: '/components/time-slot',
            summary: 'Appointment booking time slot grid',
            details: [
              'Interactive time slot selector grouped by session: Morning, Afternoon, Evening.',
              'Visual states: Available, Booked / Full, and Active Selected.',
            ],
          },
        ],
      },
      {
        type: 'Enhanced',
        label: 'Build & Release Engineering',
        items: [
          {
            component: 'Maven Central Publishing',
            componentPath: null,
            summary: 'In-memory PGP signing and automated checksum generation',
            details: [
              'Implemented in-memory PGP key signing in Gradle build script for reliable CI/CD deployment.',
              'Automated SHA-256 and MD5 checksum computation for library AAR and JAR artifacts.',
            ],
          },
        ],
      },
    ],
  },
  {
    version: '1.0.4',
    date: 'August 20, 2026',
    relativeDate: 'Form & Progress Suite',
    tag: 'Minor',
    title: 'Stepper Variants, TimePicker & Status Pipeline Suite',
    overview:
      'Expands form progression controls with multi-variant Stepper, modal TimePicker with clock dial and digital input, and Status Pipeline for multi-stage government application tracking.',
    highlights: [
      'Added Ux4gStepper supporting horizontal, vertical, numbered, and compact capsule variants.',
      'Added Ux4gTimePicker component with Material 3 clock face dial, AM/PM toggle, and keyboard text input mode.',
      'Added Ux4gStatusPipeline for visualizing sequential verification stages (Submitted → Verified → Approved → Dispatched).',
    ],
    installCommand: 'implementation("io.github.ux4g-negd:designsystemv1:1.0.4")',
    groovyCommand: "implementation 'io.github.ux4g-negd:designsystemv1:1.0.4'",
    categories: [
      {
        type: 'Added',
        label: 'Journey Progression & Time Controls',
        items: [
          {
            component: 'Ux4gStepper',
            componentPath: '/components/stepper',
            summary: 'Multi-variant step journey indicator',
            details: [
              'Horizontal and vertical stepper layouts for multi-step citizen applications.',
              'Numbered steps, completed checkmarks, current active highlight, and error alert indicators.',
              'Compact capsule stepper variant for header/top-bar space constraints.',
            ],
          },
          {
            component: 'Ux4gTimePicker',
            componentPath: '/components/time-picker',
            summary: 'Analog clock face & digital time selector dialog',
            details: [
              '12-hour and 24-hour time selector with circular clock dial.',
              'Seamless toggle between analog dial mode and digital text field mode.',
            ],
          },
          {
            component: 'Ux4gStatusPipeline',
            componentPath: '/components/status-pipeline',
            summary: 'Multi-stage government application pipeline tracker',
            details: [
              'Visual milestone progress for citizen status updates (e.g. Document Uploaded → Field Verification → Certificate Issued).',
              'Timestamps, officer notes, and status tags integrated per milestone.',
            ],
          },
        ],
      },
    ],
  },
  {
    version: '1.0.3',
    date: 'August 19, 2026',
    relativeDate: 'Form Controls Parity',
    tag: 'Patch',
    title: 'Toggle, Slider, Search Field & Radio Button Parity',
    overview:
      'Delivers core selection controls with UX4G tokens, including Ux4gToggle, Ux4gSlider with step increments and tooltips, Ux4gSearchField with live clear button, and accessible Ux4gRadioButton.',
    highlights: [
      'Added Ux4gToggle (Switch) with smooth thumb transition, icon indicators, and disabled states.',
      'Added Ux4gSlider supporting continuous and discrete step snapping with value indicator labels.',
      'Added Ux4gSearchField with prefix search icon, instant clear CTA, and debounced callback support.',
      'Added Ux4gRadioButton and Ux4gRadioGroup with full keyboard and screen reader accessibility.',
    ],
    installCommand: 'implementation("io.github.ux4g-negd:designsystemv1:1.0.3")',
    groovyCommand: "implementation 'io.github.ux4g-negd:designsystemv1:1.0.3'",
    categories: [
      {
        type: 'Added',
        label: 'Selection & Form Inputs',
        items: [
          {
            component: 'Ux4gToggle',
            componentPath: '/components/toggle',
            summary: 'Accessible toggle switch component',
            details: [
              'Custom thumb and track styling adhering to UX4G primary and surface tokens.',
              'Optional check/cross icons embedded within the slider thumb.',
            ],
          },
          {
            component: 'Ux4gSlider',
            componentPath: '/components/slider',
            summary: 'Continuous and discrete step slider',
            details: [
              'Custom thumb pill, active track fill, and discrete tick mark indicators.',
              'Value tooltip badge displaying current numeric selection on drag.',
            ],
          },
          {
            component: 'Ux4gSearchField',
            componentPath: '/components/search-field',
            summary: 'Citizen search input with auto-clear CTA',
            details: [
              'Leading search icon, trailing clear cross icon, and focus state outline.',
              'Debounced onValueChange callback support for high-performance query filtering.',
            ],
          },
          {
            component: 'Ux4gRadioButton',
            componentPath: '/components/radio-button',
            summary: 'Radio button and RadioGroup selection controls',
            details: [
              'Accessible radio button with inner circle dot and ripple touch feedback.',
              'Ux4gRadioGroup helper for managing mutually exclusive option groups.',
            ],
          },
        ],
      },
    ],
  },
  {
    version: '1.0.2',
    date: 'August 19, 2026',
    relativeDate: 'Progress & Gauges',
    tag: 'Patch',
    title: 'Circular & Half-Circle Progress Indicators',
    overview:
      'Adds circular progress indicators and gauge visualization widgets matching the UX4G government analytics specifications.',
    highlights: [
      'Added Ux4gCircularProgress with determinate percentage and indeterminate spinning modes.',
      'Added Ux4gHalfCircleProgress gauge indicator for citizen application completion scores and quota limits.',
    ],
    installCommand: 'implementation("io.github.ux4g-negd:designsystemv1:1.0.2")',
    groovyCommand: "implementation 'io.github.ux4g-negd:designsystemv1:1.0.2'",
    categories: [
      {
        type: 'Added',
        label: 'Progress Indicators',
        items: [
          {
            component: 'Ux4gCircularProgress',
            componentPath: '/components/progress-indicator',
            summary: 'Circular progress spinner and percentage loader',
            details: [
              'Determinate mode with animated progress stroke and center percentage label.',
              'Indeterminate continuous spinning mode with smooth acceleration easing.',
            ],
          },
          {
            component: 'Ux4gHalfCircleProgress',
            componentPath: '/components/progress-indicator',
            summary: 'Semi-circular gauge indicator',
            details: [
              'Half-circle arc visualization for quota limits, eligibility scores, and survey progress.',
            ],
          },
        ],
      },
    ],
  },
  {
    version: '1.0.1',
    date: 'August 19, 2026',
    relativeDate: 'Parity & Feedback',
    tag: 'Patch',
    title: 'Linear Progress Bar, Text Area & Citizen Feedback Suite',
    overview:
      'Achieves 1:1 parity with UX4G foundations for Linear Progress Bar with animated fill, Text Area with character count, Star/CSAT/NPS Feedback Forms, Tooltip, and Dotted Pagination.',
    highlights: [
      'Added Ux4gLinearProgressBar with animated gradient fill, step milestones, and percentage indicators.',
      'Added Ux4gTextArea with auto-expansion, character counter limits, and hint helper text.',
      'Added Ux4gFeedbackForm with Star ratings, CSAT smileys, and NPS scale options.',
      'Added Ux4gTooltip with auto-placement and arrow anchoring.',
      'Added Ux4gPaginationDotted with animated active dot pill expansion.',
    ],
    installCommand: 'implementation("io.github.ux4g-negd:designsystemv1:1.0.1")',
    groovyCommand: "implementation 'io.github.ux4g-negd:designsystemv1:1.0.1'",
    categories: [
      {
        type: 'Added',
        label: 'Form & Feedback Controls',
        items: [
          {
            component: 'Ux4gLinearProgressBar',
            componentPath: '/components/progress-indicator',
            summary: 'Animated linear progress bar with milestones',
            details: [
              'Smooth animation on progress updates with rounded ends.',
              'Support for status labels, percentage text, and multi-color gradient fills.',
            ],
          },
          {
            component: 'Ux4gTextArea',
            componentPath: '/components/text-area',
            summary: 'Multi-line text area with character counter',
            details: [
              'Auto-expanding multi-line text input adhering to UX4G typography tokens.',
              'Character count indicator with error highlighting when exceeding limit.',
            ],
          },
          {
            component: 'Ux4gFeedbackForm',
            componentPath: '/components/feedback-form',
            summary: 'Government citizen feedback collection forms',
            details: [
              'Star rating variant (1 to 5 stars) with customizable rating labels.',
              'CSAT emoji smiley rating variant (Very Dissatisfied to Very Satisfied).',
              'NPS numerical scale (0 to 10) for public service evaluation.',
            ],
          },
          {
            component: 'Ux4gTooltip',
            componentPath: '/components/tooltip',
            summary: 'Contextual help overlay tooltip',
            details: [
              'Lightweight popover tooltip with arrow pointer positioned relative to anchor.',
            ],
          },
          {
            component: 'Ux4gPaginationDotted',
            componentPath: '/components/pagination',
            summary: 'Carousel dot indicator with active pill expansion',
            details: [
              'Smooth animated expansion of active page dot into a rounded capsule pill.',
            ],
          },
        ],
      },
    ],
  },
  {
    version: '1.0.0',
    date: 'August 18, 2026',
    relativeDate: 'Initial Release',
    tag: 'Initial',
    title: 'Initial Release of UX4G Android Design System (Jetpack Compose)',
    overview:
      'First official public release of io.github.ux4g-negd:designsystemv1, providing government-grade Jetpack Compose UI foundations, accessible tokens, and 40+ native Android components.',
    highlights: [
      'Official UX4G Design System implementation for Android using 100% Jetpack Compose.',
      'Complete token foundations: Ux4gColor, Ux4gTypography, Ux4gSpacing, Ux4gElevation, Ux4gShape, Ux4gIcons.',
      'Gov-exclusive modules: Aadhaar input with Verhoeff validation, PAN input, OTP verification, Biometrics.',
      'Core UI components: Ux4gButton, Ux4gCard, Ux4gBadge, Ux4gModal, Ux4gToast, Ux4gDropdown, Ux4gCheckbox, Ux4gInputField, Ux4gAccordion, Ux4gDivider, and more.',
      'Light and Dark theme support with automatic system theme adaptation via Ux4gTheme.',
    ],
    installCommand: 'implementation("io.github.ux4g-negd:designsystemv1:1.0.0")',
    groovyCommand: "implementation 'io.github.ux4g-negd:designsystemv1:1.0.0'",
    categories: [
      {
        type: 'Added',
        label: 'Design System Foundations & Core Components',
        items: [
          {
            component: 'Ux4gTheme & Foundations',
            componentPath: '/tokens/theme',
            summary: 'Color, Typography, Spacing, Elevation, and Shape tokens',
            details: [
              'Full color system including Primary (#4A2BC2 / #5B3AE6), Secondary, Neutral, Success, Warning, Error, and Info palettes.',
              'Typography tokens for Display, Header, Title, Body, and Label scales.',
              'Spacing tokens (Ux4gSpace) from 4dp to 64dp.',
              'Shadow elevation tokens (Ux4gElevation) for card, modal, and drawer depths.',
            ],
          },
          {
            component: 'Government Identity Modules',
            componentPath: '/components/aadhaar-input',
            summary: 'Aadhaar, PAN, and OTP verification inputs',
            details: [
              'Ux4gAadhaarInputField with Verhoeff checksum algorithm and auto-formatting.',
              'Ux4gPanInputField with alphanumeric Regex formatting and instant validation.',
              'Ux4gOtpInput with 4 and 6-digit boxes, auto-focus jumping, and timer countdown.',
            ],
          },
          {
            component: 'Core Action & Structure Components',
            componentPath: '/components/button',
            summary: 'Buttons, Cards, Badges, Modals, Toasts, Dropdowns, Inputs',
            details: [
              'Ux4gButton with Primary, Secondary, Outline, Text, and Destructive variants.',
              'Ux4gCard with elevated, outlined, and filled styles.',
              'Ux4gBadge and Ux4gTag with status color presets.',
              'Ux4gModal dialog and Ux4gToast host with timed auto-dismiss.',
              'Ux4gSelectionDropdown and Ux4gActionDropdown.',
              'Ux4gCheckbox, Ux4gInputField, Ux4gAccordion, and Ux4gDivider.',
            ],
          },
        ],
      },
    ],
  },
];

