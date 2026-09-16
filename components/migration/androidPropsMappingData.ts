export interface AndroidPropMappingItem {
  component: string;
  propGroup: string;
  ux4gProp: string;
  m3Prop: string;
  description: string;
}

export const ANDROID_PROPS_MAPPING_DATA: AndroidPropMappingItem[] = [
  // 1. Accordion
  {
    component: "Accordion",
    propGroup: "Items List",
    ux4gProp: "items: List<Ux4gAccordionItem>",
    m3Prop: "Manual Column + AnimatedVisibility",
    description: "Ordered list of collapsible disclosure cards with titles, icons, and content slots."
  },
  {
    component: "Accordion",
    propGroup: "Expansion State",
    ux4gProp: "expandedIndex: Int?, onExpandedIndexChange: (Int?) -> Unit",
    m3Prop: "var expanded by remember { mutableStateOf(false) }",
    description: "Two-way state binding controlling which accordion section is currently active and open."
  },
  {
    component: "Accordion",
    propGroup: "Item Configuration",
    ux4gProp: "title: String, enabled: Boolean = true",
    m3Prop: "Header Row with Text & Icon composables",
    description: "Card header title string and interactive enabled/disabled state."
  },

  // 2. AppHeader
  {
    component: "AppHeader",
    propGroup: "Title & Style",
    ux4gProp: "title: String, variant: Ux4gAppHeaderVariant (Light, Brand, Dark)",
    m3Prop: "title = { Text(...) }, colors = TopAppBarDefaults.topAppBarColors()",
    description: "Government portal title and high-contrast color scheme styling."
  },
  {
    component: "AppHeader",
    propGroup: "Navigation",
    ux4gProp: "showBackButton: Boolean = false, onBackClick: () -> Unit",
    m3Prop: "navigationIcon = { IconButton(onClick = ...) { Icon(Icons.Default.ArrowBack) } }",
    description: "Automated back navigation arrow with standardized touch targets and click callback."
  },
  {
    component: "AppHeader",
    propGroup: "Actions & Avatar",
    ux4gProp: "actions: List<Ux4gAppHeaderAction>, showAvatar: Boolean, avatarInitials: String?",
    m3Prop: "actions = { IconButton(...) }, Custom Avatar Box",
    description: "Trailing utility icons (search, language selector, settings) and citizen profile avatar."
  },

  // 3. Avatar
  {
    component: "Avatar",
    propGroup: "Content Hierarchy",
    ux4gProp: "imageUrl: String?, initials: String?, icon: ImageVector?",
    m3Prop: "Box + AsyncImage / Text / Icon manual fallback",
    description: "Triple-tier profile fallback hierarchy: Remote Image -> Initials -> Vector Icon."
  },
  {
    component: "Avatar",
    propGroup: "Size Scale",
    ux4gProp: "size: Ux4gAvatarSize (XS, S, M, L, XL)",
    m3Prop: "Modifier.size(24.dp .. 64.dp)",
    description: "Standardized dimensions from 24dp (extra small) to 64dp (extra large)."
  },
  {
    component: "Avatar",
    propGroup: "Verification Badge",
    ux4gProp: "badge: Ux4gAvatarBadge?, status: Ux4gAvatarStatus?",
    m3Prop: "BadgedBox / Overlay icon Box",
    description: "Anchored government verification checkmark or citizen presence status dot."
  },

  // 4. AvatarGroup
  {
    component: "AvatarGroup",
    propGroup: "Stack Cluster",
    ux4gProp: "avatars: List<Ux4gAvatarData>, maxVisible: Int = 4",
    m3Prop: "Row { avatars.take(4).forEachIndexed { ... } }",
    description: "Stacked overlapping citizen avatars with automated '+N' overflow pill badge."
  },
  {
    component: "AvatarGroup",
    propGroup: "Layout Spacing",
    ux4gProp: "overlapOffset: Dp = 8.dp",
    m3Prop: "Modifier.offset(x = (-idx * 8).dp)",
    description: "Configurable horizontal overlapping negative margin between clustered avatars."
  },

  // 5. Badge
  {
    component: "Badge",
    propGroup: "Badge Variant",
    ux4gProp: "type: Ux4gBadgeType (Count, Label, Dot, ReadyToUse)",
    m3Prop: "Badge(content = { Text(...) })",
    description: "Numeric counter with max double-digit limit, string pill label, or presence dot."
  },
  {
    component: "Badge",
    propGroup: "Color Tokens",
    ux4gProp: "containerColor: Color, contentColor: Color",
    m3Prop: "containerColor: Color, contentColor: Color",
    description: "Background surface tint and high-contrast typography color tokens."
  },

  // 6. Biometric
  {
    component: "Biometric",
    propGroup: "Verification Payload",
    ux4gProp: "onVerify: suspend (ByteArray) -> BiometricVerificationResult",
    m3Prop: "BiometricPrompt.authenticate(CryptoObject)",
    description: "Suspends Aadhaar e-KYC face liveness / fingerprint verification API payload."
  },
  {
    component: "Biometric",
    propGroup: "Sensor Checks",
    ux4gProp: "enableBlinkCheck: Boolean, enableLightingCheck: Boolean, enableLiveness: Boolean",
    m3Prop: "Manual CameraX frame analyzer pipeline",
    description: "Enforces automated blink detection, facial liveness validation, and ambient lux checks."
  },
  {
    component: "Biometric",
    propGroup: "Lifecycle Handlers",
    ux4gProp: "onSuccess: (BiometricVerificationResult) -> Unit, onFailure: (String, String) -> Unit",
    m3Prop: "AuthenticationCallback.onAuthenticationSucceeded / Failed",
    description: "Authentication success and retry failure callbacks."
  },

  // 7. Button
  {
    component: "Button",
    propGroup: "Trigger Action",
    ux4gProp: "onClick: () -> Unit",
    m3Prop: "onClick: () -> Unit",
    description: "Primary click lambda invoked when the button is tapped."
  },
  {
    component: "Button",
    propGroup: "Visual Variant",
    ux4gProp: "variant: Ux4gButtonVariant (Primary, Secondary, Outline, Ghost, Danger)",
    m3Prop: "Button, FilledTonalButton, OutlinedButton, TextButton",
    description: "Selects the visual emphasis hierarchy, elevation, and background token."
  },
  {
    component: "Button",
    propGroup: "Label Content",
    ux4gProp: "text: String",
    m3Prop: "content: @Composable RowScope.() -> Unit",
    description: "Text label displayed inside the button with standardized typography scale."
  },
  {
    component: "Button",
    propGroup: "Loading State",
    ux4gProp: "isLoading: Boolean = false",
    m3Prop: "CircularProgressIndicator(modifier = Modifier.size(18.dp)) in content slot",
    description: "Replaces button label with an animated loader and blocks duplicate clicks."
  },
  {
    component: "Button",
    propGroup: "Icons",
    ux4gProp: "leadingIcon: ImageVector?, trailingIcon: ImageVector?",
    m3Prop: "Icon(imageVector = ...) inside content Row",
    description: "Prepends or appends an icon with standardized 8dp margin."
  },
  {
    component: "Button",
    propGroup: "Interaction State",
    ux4gProp: "enabled: Boolean = true",
    m3Prop: "enabled: Boolean = true",
    description: "Controls touch availability and applies disabled token styling."
  },

  // 8. Card
  {
    component: "Card",
    propGroup: "Structured Content",
    ux4gProp: "title: String?, subtitle: String?, body: String?",
    m3Prop: "Card { Column { Text(title); Text(subtitle); Text(body) } }",
    description: "Standardized title, subtitle, and body copy layout hierarchy."
  },
  {
    component: "Card",
    propGroup: "Action Footers",
    ux4gProp: "footerType: Ux4gCardFooterType, primaryButtonText: String?, secondaryButtonText: String?",
    m3Prop: "Row(horizontalArrangement = Arrangement.End) { Button(...); Button(...) }",
    description: "Integrated primary and secondary action button footers."
  },
  {
    component: "Card",
    propGroup: "Interactivity",
    ux4gProp: "isClickable: Boolean = false, onClick: (() -> Unit)? = null",
    m3Prop: "Card(onClick = ...)",
    description: "Adds ripple animation and keyboard focus ring for navigable cards."
  },

  // 9. Carousel
  {
    component: "Carousel",
    propGroup: "Slides List",
    ux4gProp: "items: List<@Composable () -> Unit>",
    m3Prop: "HorizontalPager(state = pagerState) { page -> items[page]() }",
    description: "Composable slide elements rendered inside a horizontal paging container."
  },
  {
    component: "Carousel",
    propGroup: "Auto-Play",
    ux4gProp: "autoPlay: Boolean = true, autoPlayIntervalMs: Long = 3500L",
    m3Prop: "LaunchedEffect(pagerState) { while(true) { delay(...); pagerState.animateScrollToPage(...) } }",
    description: "Infinite automated looping banner carousel with configurable interval."
  },
  {
    component: "Carousel",
    propGroup: "Pagination Controls",
    ux4gProp: "showPagination: Boolean = true, showPaginationArrows: Boolean = true",
    m3Prop: "Row of indicator dots + prev/next IconButton chevrons",
    description: "Integrated active capsule indicators and prev/next navigation arrows."
  },

  // 10. Checkbox
  {
    component: "Checkbox",
    propGroup: "State Binding",
    ux4gProp: "checked: Boolean, onCheckedChange: (Boolean) -> Unit",
    m3Prop: "checked: Boolean, onCheckedChange: ((Boolean) -> Unit)?",
    description: "Two-way boolean state representing checked or unchecked."
  },
  {
    component: "Checkbox",
    propGroup: "Accessible Label",
    ux4gProp: "label: String?, helperText: String?",
    m3Prop: "Row { Checkbox(...); Column { Text(label); Text(helper) } }",
    description: "Built-in typography label ensuring minimum 48dp touchable hit-box."
  },
  {
    component: "Checkbox",
    propGroup: "Validation",
    ux4gProp: "isError: Boolean = false, enabled: Boolean = true",
    m3Prop: "colors = CheckboxDefaults.colors(errorCheckmarkColor = ...)",
    description: "Applies error border color tokens or disabled state styling."
  },

  // 11. Chips
  {
    component: "Chips",
    propGroup: "Selection State",
    ux4gProp: "selected: Boolean, onClick: () -> Unit",
    m3Prop: "FilterChip(selected = ..., onClick = ...)",
    description: "Active toggle state with solid fill or tonal background styling."
  },
  {
    component: "Chips",
    propGroup: "Affixes",
    ux4gProp: "leadingContent: (@Composable () -> Unit)?, onDismiss: (() -> Unit)?",
    m3Prop: "leadingIcon = { ... }, trailingIcon = { Icon(Icons.Default.Close) }",
    description: "Leading avatar/icon slot and trailing dismiss close action button."
  },
  {
    component: "Chips",
    propGroup: "Size Scale",
    ux4gProp: "size: Ux4gChipSize (XS, S, M)",
    m3Prop: "AssistChip / FilterChip height Modifier",
    description: "Three standardized touch-target height presets for form tags and filters."
  },

  // 12. CircularProgress
  {
    component: "CircularProgress",
    propGroup: "Progress Value",
    ux4gProp: "value: Float, size: Ux4gCircularProgressSize (S, M, L, XL, XXL)",
    m3Prop: "CircularProgressIndicator(progress = { value }, modifier = Modifier.size(dp))",
    description: "Determinate progress ratio (0.0f to 1.0f) and 5 pre-calibrated size scales."
  },
  {
    component: "CircularProgress",
    propGroup: "Center Content",
    ux4gProp: "centerValueText: String?, centerDescription: String?",
    m3Prop: "Box { CircularProgressIndicator(); Column { Text(...); Text(...) } }",
    description: "Centered bold deadline/percentage numbers (e.g. '8 days left') and labels."
  },
  {
    component: "CircularProgress",
    propGroup: "Cap & Color",
    ux4gProp: "strokeCap: StrokeCap = StrokeCap.Round, progressColor: Color",
    m3Prop: "strokeCap = StrokeCap.Round, color = Color",
    description: "Rounded arc ends and customized semantic tier color tokens."
  },

  // 13. DatePicker
  {
    component: "DatePicker",
    propGroup: "Date State",
    ux4gProp: "selectedDate: LocalDate?, onDateSelected: (LocalDate?) -> Unit",
    m3Prop: "DatePickerState.selectedDateMillis",
    description: "Type-safe Java LocalDate selection binding."
  },
  {
    component: "DatePicker",
    propGroup: "Field Header",
    ux4gProp: "label: String, isRequired: Boolean = false, placeholder: String",
    m3Prop: "OutlinedTextField(label = { Text(...) }, placeholder = { Text(...) })",
    description: "Input trigger field with required asterisk (*) indicator."
  },
  {
    component: "DatePicker",
    propGroup: "Boundary Constraints",
    ux4gProp: "minDate: LocalDate?, maxDate: LocalDate?",
    m3Prop: "SelectableDates interface in DatePickerDefaults",
    description: "Restricts selectable dates to valid birthdates or fiscal year bounds."
  },

  // 14. Divider
  {
    component: "Divider",
    propGroup: "Stroke Style",
    ux4gProp: "style: Ux4gDividerStyle (Solid, Dashed, Dotted)",
    m3Prop: "HorizontalDivider (Solid stroke only without PathEffect)",
    description: "Renders solid, dashed, or dotted separator partition lines."
  },
  {
    component: "Divider",
    propGroup: "Dimensions & Inset",
    ux4gProp: "thickness: Dp = 1.dp, startIndent: Dp = 0.dp, endIndent: Dp = 0.dp",
    m3Prop: "thickness: Dp, modifier = Modifier.padding(start = dp, end = dp)",
    description: "Configures boundary stroke width and edge padding margins."
  },

  // 15. DraftStatusBanner
  {
    component: "DraftStatusBanner",
    propGroup: "Auto-Save State",
    ux4gProp: "variant: Ux4gBannerVariant (SavingLight, SuccessLight, WarningLight, ErrorLight)",
    m3Prop: "Card { Row { Icon(...); Text(...) } }",
    description: "Visual state for real-time draft saving, saved confirmation, or expiration warning."
  },
  {
    component: "DraftStatusBanner",
    propGroup: "Application Details",
    ux4gProp: "title: String, subtitle: String?, badge: (@Composable () -> Unit)?",
    m3Prop: "Row { Column { Text(title); Text(subtitle) }; Badge(...) }",
    description: "Form title, last saved timestamp, and current workflow stage badge."
  },
  {
    component: "DraftStatusBanner",
    propGroup: "Quick CTA",
    ux4gProp: "actionButtonText: String?, onActionPressed: (() -> Unit)?",
    m3Prop: "TextButton(onClick = ...) { Text(action) }",
    description: "Quick action callback e.g. 'Resume Application' or 'Discard'."
  },

  // 16. Dropdown
  {
    component: "Dropdown",
    propGroup: "Options Data",
    ux4gProp: "options: List<Ux4gDropdownOption>, selectedOptionId: String?",
    m3Prop: "ExposedDropdownMenuBox + DropdownMenuItem list",
    description: "List of dropdown menu items with unique IDs, labels, and selection callbacks."
  },
  {
    component: "Dropdown",
    propGroup: "Search Filter",
    ux4gProp: "isSearchable: Boolean = true, searchPlaceholder: String",
    m3Prop: "OutlinedTextField inside ExposedDropdownMenu",
    description: "Integrated real-time type-ahead search filter box."
  },
  {
    component: "Dropdown",
    propGroup: "Multi-Select",
    ux4gProp: "isMultiSelect: Boolean, selectedIds: Set<String>",
    m3Prop: "DropdownMenuItem with Checkbox composable",
    description: "Multiple choice selection with integrated dismissible chips in the field trigger."
  },

  // 17. EmptyState
  {
    component: "EmptyState",
    propGroup: "Preset Illustration",
    ux4gProp: "variant: Ux4gEmptyStateVariant (NoResults, NoData, ComingSoon, Error)",
    m3Prop: "Column { Image(...); Text(...); Button(...) }",
    description: "Pre-configured semantic illustrations and typography hierarchy."
  },
  {
    component: "EmptyState",
    propGroup: "Action Trigger",
    ux4gProp: "buttonText: String?, onButtonPressed: (() -> Unit)?",
    m3Prop: "Button(onClick = ...) { Text(buttonText) }",
    description: "Actionable CTA e.g. 'Clear all filters' or 'Refresh Inbox'."
  },

  // 18. FeedbackForm
  {
    component: "FeedbackForm",
    propGroup: "Star Rating",
    ux4gProp: "onSubmit: (rating: Int, chips: List<String>, comment: String) -> Unit",
    m3Prop: "Custom rating bar form composable",
    description: "5-star citizen satisfaction rating with improvement category tags and comment box."
  },
  {
    component: "FeedbackForm",
    propGroup: "CSAT & NPS",
    ux4gProp: "Ux4gFeedbackFormCsat (1-5 face scale), Ux4gFeedbackFormNps (0-10 scale)",
    m3Prop: "Manual sentiment icon row / score radio buttons",
    description: "Official CSAT sentiment face rating scale and Net Promoter Score recommendation forms."
  },

  // 19. FileUpload
  {
    component: "FileUpload",
    propGroup: "File Constraints",
    ux4gProp: "allowedExtensions: List<String>, maxSizeMb: Int",
    m3Prop: "ActivityResultContracts.GetContent() + manual extension checking",
    description: "Enforces supported document extensions (PDF, JPG, PNG) and max upload size in MB."
  },
  {
    component: "FileUpload",
    propGroup: "Upload Handler",
    ux4gProp: "onFileSelected: (Uri) -> Unit, label: String",
    m3Prop: "launcher.launch('*/*')",
    description: "Invoked with the chosen Android content URI upon user selection."
  },

  // 20. HalfCircleProgress
  {
    component: "HalfCircleProgress",
    propGroup: "Speedometer Value",
    ux4gProp: "value: Float, valueText: String?, description: String?",
    m3Prop: "Canvas { drawArc(startAngle = 180f, sweepAngle = value * 180f) }",
    description: "180° semicircle speedometer gauge with bold center value and animated caption."
  },
  {
    component: "HalfCircleProgress",
    propGroup: "Graduation Scale",
    ux4gProp: "showScale: Boolean = true, size: Ux4gHalfCircleProgressSize",
    m3Prop: "Manual scale text positioning calculations",
    description: "Renders 0% and 100% boundary graduation scale marks."
  },

  // 21. IconButton
  {
    component: "IconButton",
    propGroup: "Icon & Action",
    ux4gProp: "icon: ImageVector, onClick: () -> Unit, contentDescription: String?",
    m3Prop: "IconButton(onClick = ...) { Icon(imageVector = ..., contentDescription = ...) }",
    description: "Single-tap icon trigger with accessible content description."
  },
  {
    component: "IconButton",
    propGroup: "Style & Shape",
    ux4gProp: "variant: Ux4gIconButtonVariant (Filled, Outline, Text, Tonal), isCircular: Boolean",
    m3Prop: "IconButton, FilledIconButton, OutlinedIconButton",
    description: "Circular or rounded-square icon action buttons with 48dp touch bounds."
  },

  // 22. InputField
  {
    component: "InputField",
    propGroup: "State Value",
    ux4gProp: "value: String, onValueChange: (String) -> Unit",
    m3Prop: "value: String, onValueChange: (String) -> Unit",
    description: "Two-way state binding for the current text value."
  },
  {
    component: "InputField",
    propGroup: "Header Label",
    ux4gProp: "label: String, isRequired: Boolean = false",
    m3Prop: "label = { Text(...) }",
    description: "Field title with automatic red asterisk (*) indicator for mandatory fields."
  },
  {
    component: "InputField",
    propGroup: "Validation Status",
    ux4gProp: "status: Ux4gInputFieldStatus (Default, Success, Warning, Error), caption: String?",
    m3Prop: "isError: Boolean, supportingText = { Text(...) }",
    description: "Displays accessible validation status borders, icons, and helper text."
  },
  {
    component: "InputField",
    propGroup: "Keyboard & Mask",
    ux4gProp: "type: Ux4gInputFieldType (Text, Password, Email, Phone, Number)",
    m3Prop: "KeyboardOptions(keyboardType = ...) + PasswordVisualTransformation()",
    description: "Selects keyboard input layout and password peek toggle."
  },

  // 23. AadhaarInputField
  {
    component: "AadhaarInputField",
    propGroup: "12-Digit Formatting",
    ux4gProp: "value: String, onValueChange: (String) -> Unit",
    m3Prop: "Manual text chunking in OutlinedTextField",
    description: "Auto-formats 12 digits into 4-digit spaced chunks (XXXX XXXX XXXX)."
  },
  {
    component: "AadhaarInputField",
    propGroup: "Verhoeff Checksum",
    ux4gProp: "onValidationResult: (isValid: Boolean) -> Unit",
    m3Prop: "Manual Verhoeff algorithm calculation",
    description: "Real-time Verhoeff checksum algorithm calculating genuine UIDAI format."
  },
  {
    component: "AadhaarInputField",
    propGroup: "Masking & Peek",
    ux4gProp: "isMasked: Boolean = true, allowPeek: Boolean = true",
    m3Prop: "PasswordVisualTransformation()",
    description: "Masks initial 8 digits (•••• •••• 1234) with eye icon toggle."
  },

  // 24. PanInputField
  {
    component: "PanInputField",
    propGroup: "Capitalization",
    ux4gProp: "value: String, onValueChange: (String) -> Unit",
    m3Prop: "value.uppercase() in onValueChange",
    description: "Auto-converts 10-character alphanumeric characters into uppercase."
  },
  {
    component: "PanInputField",
    propGroup: "Income Tax Regex",
    ux4gProp: "onValidationResult: (isValid: Boolean) -> Unit",
    m3Prop: "Regex('^[A-Z]{5}[0-9]{4}[A-Z]{1}$').matches(value)",
    description: "Validates official PAN pattern (5 letters, 4 digits, 1 letter)."
  },

  // 25. OtpInput
  {
    component: "OtpInput",
    propGroup: "Digit Boxes",
    ux4gProp: "length: Int = 6, otpValue: String, onOtpChange: (String) -> Unit",
    m3Prop: "BasicTextField + Row of 6 individual Box composables",
    description: "4 or 6 discrete secure PIN boxes with auto-advancing focus."
  },
  {
    component: "OtpInput",
    propGroup: "Auto-Submit & Resend",
    ux4gProp: "onComplete: (String) -> Unit, onResendClick: (() -> Unit)?",
    m3Prop: "if (value.length == 6) verifyOtp(value)",
    description: "Triggers verification API immediately upon entering the final digit."
  },

  // 26. JourneyTimeline
  {
    component: "JourneyTimeline",
    propGroup: "Steps Progression",
    ux4gProp: "steps: List<Ux4gJourneyStep>, orientation: Ux4gTimelineOrientation",
    m3Prop: "Column / Row with step Canvas indicators",
    description: "Multi-stage milestone steps in Vertical or Horizontal layout."
  },
  {
    component: "JourneyTimeline",
    propGroup: "Step State",
    ux4gProp: "step.state: Ux4gJourneyStepState (Completed, Current, Upcoming, Error)",
    m3Prop: "Manual green check / ring / grey dot icons",
    description: "Milestone node indicator style with connecting path lines."
  },
  {
    component: "JourneyTimeline",
    propGroup: "Metadata",
    ux4gProp: "helpingText: String?, tag: String?, date: String?",
    m3Prop: "Text composables for timestamps and status tags",
    description: "Actor timestamps, grievance status pills, and guidance copy."
  },

  // 27. LinearProgressBar
  {
    component: "LinearProgressBar",
    propGroup: "Value & Height",
    ux4gProp: "value: Float, height: Dp = 8.dp",
    m3Prop: "LinearProgressIndicator(progress = { value }, modifier = Modifier.height(8.dp))",
    description: "Determinate percentage ratio (0.0f to 1.0f) and track height."
  },
  {
    component: "LinearProgressBar",
    propGroup: "Percentage Label",
    ux4gProp: "showPercentage: Boolean = true, labelPosition: Ux4gProgressLabelPosition",
    m3Prop: "Row { LinearProgressIndicator(...); Text('60%') }",
    description: "Renders percentage value inside or outside the progress track."
  },
  {
    component: "LinearProgressBar",
    propGroup: "Header Meta",
    ux4gProp: "icon: ImageVector?, label: String?, hint: String?",
    m3Prop: "Row { Icon(...); Text(label); Spacer(); Text(hint) }",
    description: "Header icon, milestone title, and '3 of 4 completed' hint text."
  },

  // 28. Link
  {
    component: "Link",
    propGroup: "Text & Action",
    ux4gProp: "text: String, onClick: () -> Unit, url: String?",
    m3Prop: "Text(text, modifier = Modifier.clickable { ... })",
    description: "Accessible hyperlinked text with focus ring."
  },
  {
    component: "Link",
    propGroup: "External Icon",
    ux4gProp: "showExternalIcon: Boolean = true, underline: Boolean = true",
    m3Prop: "Row { Text(...); Icon(Icons.Default.OpenInNew) }",
    description: "Trailing external link icon and underline typography decoration."
  },

  // 29. Modal
  {
    component: "Modal",
    propGroup: "Dialog Visibility",
    ux4gProp: "headerTitle: String, showCloseButton: Boolean, onDismiss: () -> Unit",
    m3Prop: "AlertDialog(onDismissRequest = ..., title = { Text(...) })",
    description: "Dialog title, top close icon, and backdrop dismiss callback."
  },
  {
    component: "Modal",
    propGroup: "Body Copy",
    ux4gProp: "showSubtitle: Boolean, subtitleText: String?, bodyText: String?",
    m3Prop: "text = { Column { Text(...); Text(...) } }",
    description: "Structured description copy and rich custom body slots."
  },
  {
    component: "Modal",
    propGroup: "Footer Actions",
    ux4gProp: "confirmButtonText: String?, dismissButtonText: String?",
    m3Prop: "confirmButton = { Button(...) }, dismissButton = { OutlinedButton(...) }",
    description: "Primary confirmation and secondary dismissal action buttons."
  },

  // 30. Pagination
  {
    component: "Pagination",
    propGroup: "Page State",
    ux4gProp: "totalPageCount: Int, currentPageIndex: Int, onPageChange: (Int) -> Unit",
    m3Prop: "Manual page tracking in PagerState",
    description: "Zero-indexed active page state binding and total page count."
  },
  {
    component: "Pagination",
    propGroup: "Arrow Controls",
    ux4gProp: "showArrows: Boolean = true, arrowsOnRight: Boolean = false",
    m3Prop: "IconButton navigation chevrons",
    description: "Bilateral prev/next navigation arrows flanking the dot progression row."
  },

  // 31. Popover
  {
    component: "Popover",
    propGroup: "Custom Content",
    ux4gProp: "customContent: @Composable () -> Unit",
    m3Prop: "Popup / DropdownMenu content slot",
    description: "Composable slot for custom card bodies, multi-line text, and action links."
  },
  {
    component: "Popover",
    propGroup: "Placement & State",
    ux4gProp: "placement: Ux4gTooltipPlacement, state: TooltipState(isPersistent = true)",
    m3Prop: "PopupPositionProvider + custom popup state",
    description: "Directional pointer arrow pointing to anchor and persistent open state."
  },

  // 32. RadioButton
  {
    component: "RadioButton",
    propGroup: "Selection State",
    ux4gProp: "selected: Boolean, onClick: (() -> Unit)?",
    m3Prop: "selected: Boolean, onClick: (() -> Unit)?",
    description: "Boolean state for single-option choice within a group."
  },
  {
    component: "RadioButton",
    propGroup: "Generic Overload",
    ux4gProp: "value: T, groupValue: T, onValueChange: (T) -> Unit",
    m3Prop: "Column { RadioButton(selected = groupValue == value, ...) }",
    description: "Type-safe generic selection overload."
  },
  {
    component: "RadioButton",
    propGroup: "Label & Status",
    ux4gProp: "label: String, status: Ux4gRadioButtonStatus, size: Ux4gRadioButtonSize",
    m3Prop: "Row { RadioButton(...); Text(...) }",
    description: "Option title, validation ring status color, and size hierarchy."
  },

  // 33. ResultList
  {
    component: "ResultList",
    propGroup: "Card Header",
    ux4gProp: "title: String, statusTag: String?, details: List<Ux4gResultDetail>",
    m3Prop: "ListItem with headlineContent and supportingContent",
    description: "Expandable search result card with 2-column details metadata grid."
  },
  {
    component: "ResultList",
    propGroup: "Action CTA",
    ux4gProp: "actionButtonText: String?, onActionPressed: (() -> Unit)?",
    m3Prop: "trailingContent = { OutlinedButton(...) }",
    description: "Built-in action button e.g. 'View Details' or 'Track Status'."
  },

  // 34. SearchField
  {
    component: "SearchField",
    propGroup: "Query Binding",
    ux4gProp: "value: String, onValueChange: (String) -> Unit, placeholder: String",
    m3Prop: "SearchBar(query = ..., onQueryChange = ..., placeholder = ...)",
    description: "Two-way search input query string binding."
  },
  {
    component: "SearchField",
    propGroup: "Voice & Clear",
    ux4gProp: "showVoiceIcon: Boolean, onVoiceClick: () -> Unit, showClearIcon: Boolean",
    m3Prop: "trailingIcon = { IconButton(Voice); IconButton(Clear) }",
    description: "Built-in microphone voice search trigger and one-tap clear button."
  },
  {
    component: "SearchField",
    propGroup: "Autocomplete",
    ux4gProp: "suggestions: List<String>, onSuggestionSelected: (String) -> Unit",
    m3Prop: "SearchBar content lambda with LazyColumn",
    description: "Real-time autocomplete suggestion list with instant keyword highlight."
  },

  // 35. Slider
  {
    component: "Slider",
    propGroup: "Value Range",
    ux4gProp: "value: Float, onValueChange: (Float) -> Unit, valueRange: ClosedFloatingPointRange<Float>",
    m3Prop: "Slider(value = ..., onValueChange = ..., valueRange = ...)",
    description: "Single-thumb continuous or discrete slider selection."
  },
  {
    component: "Slider",
    propGroup: "Range Slider",
    ux4gProp: "values: ClosedFloatingPointRange<Float>, onValueChange: (ClosedFloatingPointRange<Float>) -> Unit",
    m3Prop: "RangeSlider(value = ..., onValueChange = ...)",
    description: "Dual-thumb range selection for income or subsidy criteria."
  },
  {
    component: "Slider",
    propGroup: "Graduation Ticks",
    ux4gProp: "showMarksAndValues: Boolean = true, steps: Int = 9",
    m3Prop: "steps: Int (tick labels require manual Canvas)",
    description: "Automatic graduation tick marks and numeric scale labels."
  },

  // 36. SpinnerLoader
  {
    component: "SpinnerLoader",
    propGroup: "Dimensions",
    ux4gProp: "size: Dp = 40.dp, strokeWidth: Dp = 4.dp",
    m3Prop: "CircularProgressIndicator(modifier = Modifier.size(dp), strokeWidth = dp)",
    description: "Outer diameter and arc stroke thickness."
  },
  {
    component: "SpinnerLoader",
    propGroup: "Sweep Animation",
    ux4gProp: "percentage: Float = 100f, rotationDuration: Int = 1200",
    m3Prop: "InfiniteTransition spinning animation",
    description: "Sweep gradient arc length and rotational speed in milliseconds."
  },

  // 37. StatusPipeline
  {
    component: "StatusPipeline",
    propGroup: "Steps Progress",
    ux4gProp: "steps: List<Ux4gPipelineStep>, currentStep: Int",
    m3Prop: "Row / Column with custom Canvas step nodes",
    description: "Sequential approval stages with automated completed/current state resolution."
  },
  {
    component: "StatusPipeline",
    propGroup: "Orientation & Size",
    ux4gProp: "orientation: Ux4gPipelineOrientation, size: Ux4gPipelineSize (S, M, L)",
    m3Prop: "Row / Column layout modifiers",
    description: "Horizontal header banner or vertical detailed timeline."
  },

  // 38. Stepper
  {
    component: "Stepper",
    propGroup: "Wizard Navigation",
    ux4gProp: "totalSteps: Int, currentStep: Int, steps: List<Ux4gStepItem>",
    m3Prop: "Custom step indicator layout",
    description: "Multi-step form progression with numbered circles and stage titles."
  },
  {
    component: "Stepper",
    propGroup: "Mobile Capsule",
    ux4gProp: "Ux4gCompactStepper (pill indicators with 5 layout arrangements)",
    m3Prop: "Row of LinearProgressIndicator segments",
    description: "Space-efficient mobile stepper with previous/next controls."
  },

  // 39. Switch
  {
    component: "Switch",
    propGroup: "Toggle State",
    ux4gProp: "checked: Boolean, onCheckedChange: (Boolean) -> Unit",
    m3Prop: "checked: Boolean, onCheckedChange: ((Boolean) -> Unit)?",
    description: "Two-way binary state for user preferences and feature flags."
  },
  {
    component: "Switch",
    propGroup: "Label Placement",
    ux4gProp: "label: String?, labelPosition: Ux4gSwitchLabelPosition (Right, Left, NoLabel)",
    m3Prop: "Row { Switch(...); Text(...) }",
    description: "Accessible label text and placement positioning options."
  },
  {
    component: "Switch",
    propGroup: "Size Hierarchy",
    ux4gProp: "size: Ux4gSwitchSize (S: 32dp, M: 40dp, L: 48dp)",
    m3Prop: "Switch scale modifier",
    description: "Standardized size tokens with smooth animated thumb transitions."
  },

  // 40. Tag
  {
    component: "Tag",
    propGroup: "Color Themes",
    ux4gProp: "colorScheme: Ux4gTagColor (Neutral, Brand, Success, Warning, Error, Info)",
    m3Prop: "SuggestionChip(colors = SuggestionChipDefaults...)",
    description: "6 semantic government scheme badge color themes."
  },
  {
    component: "Tag",
    propGroup: "Visual Styles",
    ux4gProp: "style: Ux4gTagStyle (Tonal, Filled, Outline, Text)",
    m3Prop: "SuggestionChip / FilterChip container styles",
    description: "Background depth, border outline, or plain text formatting."
  },
  {
    component: "Tag",
    propGroup: "Multi-Segment Pill",
    ux4gProp: "Ux4gUnifiedPillTag(segments: List<Ux4gPillSegment>)",
    m3Prop: "Row with dividers and multiple chips",
    description: "Cohesive multi-part status indicator (e.g. '2 days remaining | Pending')."
  },

  // 41. TextArea
  {
    component: "TextArea",
    propGroup: "Multiline Value",
    ux4gProp: "value: String, onValueChange: (String) -> Unit, minLines: Int = 4",
    m3Prop: "OutlinedTextField(value = ..., minLines = 4, singleLine = false)",
    description: "Multiline text entry for grievance descriptions and residential addresses."
  },
  {
    component: "TextArea",
    propGroup: "Character Limit",
    ux4gProp: "maxCharacters: Int = 1000, label: String",
    m3Prop: "supportingText = { Text('${value.length} / 1000') }",
    description: "Built-in live character countdown counter and max length validator."
  },

  // 42. TimePicker
  {
    component: "TimePicker",
    propGroup: "Time State",
    ux4gProp: "selectedTime: Ux4gTimeOfDay?, onTimeSelected: (Ux4gTimeOfDay?) -> Unit",
    m3Prop: "TimePickerState(initialHour = ..., initialMinute = ...)",
    description: "Time value state binding in 12-hour AM/PM notation."
  },
  {
    component: "TimePicker",
    propGroup: "Slot Intervals",
    ux4gProp: "minuteInterval: Int = 5, label: String, isRequired: Boolean",
    m3Prop: "OutlinedTextField trigger + TimePicker dialog",
    description: "Configurable minute step increments (5, 15, 30 min) and trigger label."
  },

  // 43. TimeSlot
  {
    component: "TimeSlot",
    propGroup: "Month Grid Data",
    ux4gProp: "timeslotData: Ux4gTimeslotData, onDateSelected: (LocalDate) -> Unit",
    m3Prop: "LazyVerticalGrid of date chip buttons",
    description: "Full calendar month slot grid showing date availability states."
  },
  {
    component: "TimeSlot",
    propGroup: "Dialog View Modes",
    ux4gProp: "viewMode: SlotPickerViewMode (Compact, Expanded)",
    m3Prop: "ModalBottomSheet with slot list",
    description: "2-column compact capacity grid or vertical expanded card list."
  },

  // 44. Toast
  {
    component: "Toast",
    propGroup: "Category Icon",
    ux4gProp: "category: Ux4gToastCategory (Info, Success, Warning, Error, Slot)",
    m3Prop: "Snackbar(visuals = customSnackbarVisuals)",
    description: "Semantic category defining the left status icon and accent border tint."
  },
  {
    component: "Toast",
    propGroup: "Host State",
    ux4gProp: "toastHostState.showToast(title, subtitle, category)",
    m3Prop: "snackbarHostState.showSnackbar(message)",
    description: "Coroutine-safe trigger presenting top or bottom animated toast overlay."
  },

  // 45. Tooltip
  {
    component: "Tooltip",
    propGroup: "Guidance Text",
    ux4gProp: "tooltipText: String, placement: Ux4gTooltipPlacement",
    m3Prop: "TooltipBox(tooltip = { PlainTooltip { Text(...) } })",
    description: "Contextual helper bubble explaining technical terms and field guidance."
  },
  {
    component: "Tooltip",
    propGroup: "Anchor Trigger",
    ux4gProp: "content: @Composable () -> Unit",
    m3Prop: "TooltipBox content anchor slot",
    description: "Icon or text composable that triggers the tooltip on hover or long-press."
  }
];
