import os

docs_dir = r'c:\Users\Monu\Desktop\prac\openforge\ux4g-android-docs\content\docs\components'

components_data = {
    'accordion': {
        'title': 'Accordion',
        'desc': 'Expandable disclosure panels and grouped FAQ lists in Jetpack Compose',
        'component': 'Ux4gAccordion',
        'pkg': 'com.ux4g.designsystemv1.components.accordion.Ux4gAccordion',
        'code': '''Ux4gAccordion(
    title = "What documents are required for Income Certificate?",
    subtitle = "Click to view mandatory verification proofs",
    isExpanded = isExpanded,
    onToggle = { isExpanded = !isExpanded }
) {
    Text(
        text = "1. Aadhaar Card of the applicant\\n2. Salary slips for last 3 months or ITR\\n3. Proof of residence (Electricity bill / Ration card)",
        style = Ux4gTheme.typography.bodyMedium
    )
}''',
        'params': [
            ('title', 'String', 'Required', 'Header title of the expandable accordion panel'),
            ('subtitle', 'String?', 'null', 'Optional subtitle displayed below title'),
            ('isExpanded', 'Boolean', 'false', 'Controls expanded/collapsed state'),
            ('onToggle', '() -> Unit', 'Required', 'Callback triggered when clicking header'),
            ('content', '@Composable () -> Unit', 'Required', 'Child composable content inside panel')
        ]
    },
    'app-header': {
        'title': 'App Header',
        'desc': 'Top navigation app bar with National Emblem, back navigation, search, and action buttons',
        'component': 'Ux4gAppHeader',
        'pkg': 'com.ux4g.designsystemv1.components.navigation.Ux4gAppHeader',
        'code': '''Ux4gAppHeader(
    title = "National Scholarship Portal",
    subtitle = "Government of India",
    showEmblem = true,
    navigationIcon = {
        IconButton(onClick = { /* navigate back */ }) {
            Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
        }
    },
    actions = {
        IconButton(onClick = { /* search */ }) {
            Icon(Icons.Filled.Search, contentDescription = "Search")
        }
    }
)''',
        'params': [
            ('title', 'String', 'Required', 'Primary title displayed on app bar'),
            ('subtitle', 'String?', 'null', 'Secondary ministry or portal subtitle'),
            ('showEmblem', 'Boolean', 'true', 'Displays official Government Emblem'),
            ('navigationIcon', '(@Composable () -> Unit)?', 'null', 'Leading icon for back/drawer'),
            ('actions', '@Composable RowScope.() -> Unit', '{}', 'Trailing action icons')
        ]
    },
    'avatar': {
        'title': 'Avatar',
        'desc': 'User profile picture, initials fallback, and status badge indicator',
        'component': 'Ux4gAvatar',
        'pkg': 'com.ux4g.designsystemv1.components.avatar.Ux4gAvatar',
        'code': '''// Avatar with Image
Ux4gAvatar(
    imageUrl = "https://example.com/profile.jpg",
    name = "Aarav Sharma",
    size = Ux4gAvatarSize.Medium,
    status = Ux4gAvatarStatus.Online
)

// Avatar with Initials Fallback
Ux4gAvatar(
    name = "Priya Patel",
    size = Ux4gAvatarSize.Large,
    status = Ux4gAvatarStatus.Away
)''',
        'params': [
            ('imageUrl', 'String?', 'null', 'Optional remote image URL'),
            ('name', 'String', 'Required', 'User name used for initials fallback & accessibility'),
            ('size', 'Ux4gAvatarSize', 'Medium', 'Avatar size (Small, Medium, Large, ExtraLarge)'),
            ('status', 'Ux4gAvatarStatus?', 'null', 'Optional presence badge (Online, Offline, Away, Busy)')
        ]
    },
    'badge': {
        'title': 'Badge',
        'desc': 'Status labels, notification count badges, and verification indicators',
        'component': 'Ux4gBadge',
        'pkg': 'com.ux4g.designsystemv1.components.badges.Ux4gBadge',
        'code': '''// Status Badges
Ux4gBadge(text = "Verified", variant = Ux4gBadgeVariant.Success)
Ux4gBadge(text = "Pending Review", variant = Ux4gBadgeVariant.Warning)
Ux4gBadge(text = "Rejected", variant = Ux4gBadgeVariant.Error)
Ux4gBadge(text = "New", variant = Ux4gBadgeVariant.Primary)''',
        'params': [
            ('text', 'String', 'Required', 'Label text rendered inside badge'),
            ('variant', 'Ux4gBadgeVariant', 'Primary', 'Visual tone (Primary, Success, Warning, Error, Neutral)'),
            ('size', 'Ux4gBadgeSize', 'Medium', 'Badge size (Small, Medium)'),
            ('icon', 'ImageVector?', 'null', 'Optional leading status icon')
        ]
    },
    'biometric': {
        'title': 'Biometric Verification',
        'desc': 'Aadhaar Iris, Fingerprint, and Face Auth verification flow components',
        'component': 'Ux4gBiometricVerification',
        'pkg': 'com.ux4g.designsystemv1.components.biometric.Ux4gBiometricVerification',
        'code': '''Ux4gBiometricVerification(
    authType = Ux4gBiometricType.Fingerprint,
    title = "Aadhaar Authentication",
    subtitle = "Place your registered finger on the biometric scanner",
    onCapture = { biometricData -> /* Process RD Service auth */ },
    onCancel = { /* Handle cancel */ }
)''',
        'params': [
            ('authType', 'Ux4gBiometricType', 'Fingerprint', 'Biometric scanner mode (Fingerprint, Iris, Face)'),
            ('title', 'String', 'Required', 'Modal title for verification prompt'),
            ('subtitle', 'String', 'Required', 'Instruction message for citizen'),
            ('onCapture', '(BiometricResult) -> Unit', 'Required', 'Callback with biometric payload'),
            ('onCancel', '() -> Unit', 'Required', 'Callback when citizen cancels verification')
        ]
    },
    'card': {
        'title': 'Card',
        'desc': 'Surface containers for citizen services, scheme benefits, and interactive items',
        'component': 'Ux4gCard',
        'pkg': 'com.ux4g.designsystemv1.components.cards.Ux4gCard',
        'code': '''Ux4gCard(
    onClick = { /* Open Scheme Details */ },
    variant = Ux4gCardVariant.Elevated
) {
    Column(modifier = Modifier.padding(16.dp)) {
        Ux4gBadge(text = "Direct Benefit Transfer", variant = Ux4gBadgeVariant.Success)
        Spacer(modifier = Modifier.height(8.dp))
        Text(text = "PM Kisan Samman Nidhi", style = Ux4gTheme.typography.titleMedium)
        Text(text = "Financial benefit of Rs. 6,000 per year to eligible farmer families.", style = Ux4gTheme.typography.bodySmall)
    }
}''',
        'params': [
            ('onClick', '(() -> Unit)?', 'null', 'Optional click listener for interactive card'),
            ('variant', 'Ux4gCardVariant', 'Elevated', 'Card elevation style (Elevated, Outlined, Filled)'),
            ('content', '@Composable () -> Unit', 'Required', 'Composable slot inside card body')
        ]
    },
    'carousel': {
        'title': 'Carousel',
        'desc': 'Horizontal paging banner for announcements, flagship schemes, and visual highlights',
        'component': 'Ux4gCarousel',
        'pkg': 'com.ux4g.designsystemv1.components.carousel.Ux4gCarousel',
        'code': '''val banners = listOf(
    CarouselItem(title = "Digital India Week", imageRes = R.drawable.banner1),
    CarouselItem(title = "Viksit Bharat Sankalp Yatra", imageRes = R.drawable.banner2)
)

Ux4gCarousel(
    items = banners,
    autoScroll = true,
    intervalMs = 4000L,
    onItemClick = { item -> /* open banner detail */ }
)''',
        'params': [
            ('items', 'List<T>', 'Required', 'List of data models rendered in carousel'),
            ('autoScroll', 'Boolean', 'true', 'Enables automatic timed slider transition'),
            ('intervalMs', 'Long', '4000L', 'Duration in milliseconds per slide transition'),
            ('onItemClick', '(T) -> Unit', 'Required', 'Click handler when slide is pressed')
        ]
    },
    'checkbox': {
        'title': 'Checkbox',
        'desc': 'Multi-selection controls for terms agreements, filter criteria, and document checklists',
        'component': 'Ux4gCheckbox',
        'pkg': 'com.ux4g.designsystemv1.components.selection.Ux4gCheckbox',
        'code': '''Ux4gCheckbox(
    checked = isAgreed,
    onCheckedChange = { isAgreed = it },
    label = "I declare that the information provided above is true to my knowledge."
)''',
        'params': [
            ('checked', 'Boolean', 'Required', 'Current checked boolean state'),
            ('onCheckedChange', '(Boolean) -> Unit', 'Required', 'Callback triggered on toggle'),
            ('label', 'String?', 'null', 'Accompanying text label for citizen accessibility'),
            ('enabled', 'Boolean', 'true', 'Interactive state of checkbox')
        ]
    },
    'chips': {
        'title': 'Chips',
        'desc': 'Compact filter, selection, and action tags for categorical selection',
        'component': 'Ux4gChip',
        'pkg': 'com.ux4g.designsystemv1.components.chips.Ux4gChip',
        'code': '''// Filter Chips
Ux4gFilterChip(
    selected = isSelected,
    onClick = { isSelected = !isSelected },
    label = "Agriculture Schemes",
    leadingIcon = Icons.Filled.Check
)

// Action Chip
Ux4gActionChip(
    onClick = { /* download certificate */ },
    label = "Download PDF",
    leadingIcon = Icons.Filled.Download
)''',
        'params': [
            ('selected', 'Boolean', 'false', 'Selected state for filter/choice chip'),
            ('onClick', '() -> Unit', 'Required', 'Click listener on chip trigger'),
            ('label', 'String', 'Required', 'Text rendered inside chip'),
            ('leadingIcon', 'ImageVector?', 'null', 'Optional icon preceding chip label')
        ]
    },
    'date-picker': {
        'title': 'Date Picker',
        'desc': 'Calendar modal and input picker for DOB, application deadlines, and appointment slots',
        'component': 'Ux4gDatePicker',
        'pkg': 'com.ux4g.designsystemv1.components.datetime.Ux4gDatePicker',
        'code': '''Ux4gDatePicker(
    selectedDate = selectedDate,
    onDateSelected = { date -> selectedDate = date },
    label = "Date of Birth",
    placeholder = "DD/MM/YYYY",
    maxDate = LocalDate.now()
)''',
        'params': [
            ('selectedDate', 'LocalDate?', 'null', 'Selected calendar date object'),
            ('onDateSelected', '(LocalDate) -> Unit', 'Required', 'Callback with chosen date'),
            ('label', 'String', 'Required', 'Field title above input'),
            ('placeholder', 'String', 'DD/MM/YYYY', 'Placeholder hint text'),
            ('maxDate', 'LocalDate?', 'null', 'Maximum selectable upper bound')
        ]
    },
    'divider': {
        'title': 'Divider',
        'desc': 'Subtle visual separator lines for grouping related form sections and list rows',
        'component': 'Ux4gDivider',
        'pkg': 'com.ux4g.designsystemv1.components.layout.Ux4gDivider',
        'code': '''// Horizontal Divider
Ux4gDivider(thickness = 1.dp, color = Ux4gTheme.colors.neutralBorder)

// Divider with Section Label
Ux4gDividerWithLabel(label = "OR CONTINUE WITH")''',
        'params': [
            ('thickness', 'Dp', '1.dp', 'Line stroke height/thickness'),
            ('color', 'Color', 'Ux4gTheme.colors.neutralBorder', 'Border color token')
        ]
    },
    'dropdown': {
        'title': 'Dropdown',
        'desc': 'Searchable select menu for States, Districts, Talukas, and government categories',
        'component': 'Ux4gDropdown',
        'pkg': 'com.ux4g.designsystemv1.components.dropdown.Ux4gDropdown',
        'code': '''val states = listOf("Delhi", "Maharashtra", "Karnataka", "Tamil Nadu", "Uttar Pradesh")

Ux4gDropdown(
    label = "Select State / UT",
    items = states,
    selectedItem = selectedState,
    onItemSelected = { selectedState = it },
    isSearchable = true
)''',
        'params': [
            ('label', 'String', 'Required', 'Input header title'),
            ('items', 'List<T>', 'Required', 'Options list for citizen selection'),
            ('selectedItem', 'T?', 'null', 'Current selected item model'),
            ('onItemSelected', '(T) -> Unit', 'Required', 'Callback when item is selected'),
            ('isSearchable', 'Boolean', 'false', 'Enables type-ahead search filter')
        ]
    },
    'empty-state': {
        'title': 'Empty State',
        'desc': 'Informative fallback view for zero search results, empty inboxes, and fresh accounts',
        'component': 'Ux4gEmptyState',
        'pkg': 'com.ux4g.designsystemv1.components.feedback.Ux4gEmptyState',
        'code': '''Ux4gEmptyState(
    icon = Icons.Outlined.FolderOpen,
    title = "No Applications Found",
    description = "You have not submitted any scheme applications yet. Start a new application to track status here.",
    actionLabel = "Explore Schemes",
    onActionClick = { /* navigate to schemes */ }
)''',
        'params': [
            ('icon', 'ImageVector', 'Required', 'Hero visual icon'),
            ('title', 'String', 'Required', 'Bold summary headline'),
            ('description', 'String', 'Required', 'Explanation & guidance copy'),
            ('actionLabel', 'String?', 'null', 'Optional call-to-action button text'),
            ('onActionClick', '(() -> Unit)?', 'null', 'Action button callback')
        ]
    },
    'feedback-form': {
        'title': 'Feedback Form',
        'desc': 'Citizen rating stars, sentiment emojis, and comments submission widget',
        'component': 'Ux4gFeedbackForm',
        'pkg': 'com.ux4g.designsystemv1.components.feedback.Ux4gFeedbackForm',
        'code': '''Ux4gFeedbackForm(
    title = "How was your service experience?",
    onSubmit = { rating, comments ->
        /* Send citizen feedback telemetry */
    }
)''',
        'params': [
            ('title', 'String', 'Required', 'Header question for rating prompt'),
            ('onSubmit', '(Int, String) -> Unit', 'Required', 'Callback providing star rating (1-5) and comments')
        ]
    },
    'file-upload': {
        'title': 'File Upload',
        'desc': 'Document attachment drag-and-drop zone with size validation for PDF, JPG, PNG',
        'component': 'Ux4gFileUpload',
        'pkg': 'com.ux4g.designsystemv1.components.input.Ux4gFileUpload',
        'code': '''Ux4gFileUpload(
    label = "Upload Income Proof (PDF / JPG up to 5MB)",
    allowedExtensions = listOf("pdf", "jpg", "png"),
    maxSizeMb = 5,
    onFileSelected = { fileUri -> /* Upload to DigiLocker/Server */ }
)''',
        'params': [
            ('label', 'String', 'Required', 'Attachment prompt title'),
            ('allowedExtensions', 'List<String>', 'Required', 'Allowed file types (pdf, jpg, etc.)'),
            ('maxSizeMb', 'Int', '5', 'Max file size constraint'),
            ('onFileSelected', '(Uri) -> Unit', 'Required', 'File picker callback')
        ]
    },
    'input-field': {
        'title': 'Input Field',
        'desc': 'Text inputs with floating labels, helper text, error states, and prefix/suffix icons',
        'component': 'Ux4gTextField',
        'pkg': 'com.ux4g.designsystemv1.components.input.Ux4gTextField',
        'code': '''Ux4gTextField(
    value = fullName,
    onValueChange = { fullName = it },
    label = "Full Name (as per Aadhaar)",
    placeholder = "e.g. Ramesh Kumar",
    leadingIcon = Icons.Outlined.Person,
    isError = isInvalid,
    errorMessage = if (isInvalid) "Name is mandatory" else null
)''',
        'params': [
            ('value', 'String', 'Required', 'Current text input value'),
            ('onValueChange', '(String) -> Unit', 'Required', 'Input change handler'),
            ('label', 'String', 'Required', 'Floating header label'),
            ('leadingIcon', 'ImageVector?', 'null', 'Optional leading icon'),
            ('isError', 'Boolean', 'false', 'Visual red error validation state'),
            ('errorMessage', 'String?', 'null', 'Helper error message')
        ]
    },
    'otp-input': {
        'title': 'OTP Input',
        'desc': 'Auto-advancing 6-digit verification code input boxes with timer and resend action',
        'component': 'Ux4gOtpInput',
        'pkg': 'com.ux4g.designsystemv1.components.input.Ux4gOtpInput',
        'code': '''Ux4gOtpInput(
    length = 6,
    otpValue = otp,
    onOtpChange = { otp = it },
    onComplete = { completedOtp -> /* Trigger OTP verify API */ },
    onResendClick = { /* Send new SMS OTP */ }
)''',
        'params': [
            ('length', 'Int', '6', 'Number of OTP digit boxes (4 or 6)'),
            ('otpValue', 'String', 'Required', 'Current OTP string'),
            ('onOtpChange', '(String) -> Unit', 'Required', 'Change listener for digits'),
            ('onComplete', '(String) -> Unit', 'Required', 'Fires when all digits are entered'),
            ('onResendClick', '() -> Unit', 'Required', 'Resend OTP action callback')
        ]
    },
    'journey-timeline': {
        'title': 'Journey Timeline',
        'desc': 'Multi-stage application status tracker showing Submitted, Verified, Approved, Disbursed',
        'component': 'Ux4gJourneyTimeline',
        'pkg': 'com.ux4g.designsystemv1.components.timeline.Ux4gJourneyTimeline',
        'code': '''val stages = listOf(
    TimelineStep(title = "Application Submitted", date = "12 Aug 2024", state = StepState.Completed),
    TimelineStep(title = "Field Officer Verification", date = "15 Aug 2024", state = StepState.Completed),
    TimelineStep(title = "Sanction Order Issued", date = "In Progress", state = StepState.Current),
    TimelineStep(title = "DBT Bank Transfer", date = "Pending", state = StepState.Upcoming)
)

Ux4gJourneyTimeline(steps = stages)''',
        'params': [
            ('steps', 'List<TimelineStep>', 'Required', 'Ordered list of lifecycle application steps')
        ]
    },
    'link': {
        'title': 'Link',
        'desc': 'Interactive anchor hyperlinks for portal terms, privacy policy, and external portals',
        'component': 'Ux4gLink',
        'pkg': 'com.ux4g.designsystemv1.components.text.Ux4gLink',
        'code': '''Ux4gLink(
    text = "Read Terms & Privacy Policy",
    url = "https://india.gov.in",
    showExternalIcon = true,
    onClick = { url -> /* open browser */ }
)''',
        'params': [
            ('text', 'String', 'Required', 'Underlined anchor label'),
            ('url', 'String', 'Required', 'Destination Web URL'),
            ('showExternalIcon', 'Boolean', 'true', 'Shows external window arrow icon')
        ]
    },
    'loader': {
        'title': 'Loader & Spinner',
        'desc': 'Animated circular spinner and linear indeterminate progress bar for API loading states',
        'component': 'Ux4gSpinner',
        'pkg': 'com.ux4g.designsystemv1.components.feedback.Ux4gSpinner',
        'code': '''// Circular Spinner
Ux4gSpinner(size = Ux4gSpinnerSize.Medium, color = Ux4gTheme.colors.primary)

// Linear Indeterminate Progress Bar
Ux4gLinearLoader(modifier = Modifier.fillMaxWidth())''',
        'params': [
            ('size', 'Ux4gSpinnerSize', 'Medium', 'Spinner dimensions (Small, Medium, Large)'),
            ('color', 'Color', 'Primary', 'Accent tint color')
        ]
    },
    'modal': {
        'title': 'Modal Dialog',
        'desc': 'Focus-trapping popup dialog for confirming destructive actions or critical alerts',
        'component': 'Ux4gModal',
        'pkg': 'com.ux4g.designsystemv1.components.dialog.Ux4gModal',
        'code': '''Ux4gModal(
    show = showDialog,
    title = "Confirm Submission",
    message = "Are you sure you want to submit the application? Once submitted, details cannot be modified.",
    confirmText = "Yes, Submit",
    dismissText = "Review Again",
    onConfirm = { /* Submit API */ },
    onDismiss = { showDialog = false }
)''',
        'params': [
            ('show', 'Boolean', 'Required', 'Controls dialog visibility'),
            ('title', 'String', 'Required', 'Modal headline'),
            ('message', 'String', 'Required', 'Dialog description content'),
            ('confirmText', 'String', 'Confirm', 'Primary action button label'),
            ('onConfirm', '() -> Unit', 'Required', 'Confirm button action')
        ]
    },
    'bottom-sheet': {
        'title': 'Bottom Sheet',
        'desc': 'Slide-up drawer panel for mobile filter sheets, action menus, and contextual options',
        'component': 'Ux4gBottomSheet',
        'pkg': 'com.ux4g.designsystemv1.components.dialog.Ux4gBottomSheet',
        'code': '''Ux4gBottomSheet(
    show = showSheet,
    title = "Filter Applications",
    onDismiss = { showSheet = false }
) {
    // Filter controls inside sheet
    Column(modifier = Modifier.padding(16.dp)) {
        Ux4gRadioGroup(options = listOf("All", "Approved", "Pending"), selected = filter)
    }
}''',
        'params': [
            ('show', 'Boolean', 'Required', 'Sheet open/closed state'),
            ('title', 'String?', 'null', 'Optional header bar title'),
            ('onDismiss', '() -> Unit', 'Required', 'Dismiss handler'),
            ('content', '@Composable () -> Unit', 'Required', 'Sheet content slot')
        ]
    },
    'pagination': {
        'title': 'Pagination',
        'desc': 'Page number navigation and dot indicators for large tabular result sets',
        'component': 'Ux4gPagination',
        'pkg': 'com.ux4g.designsystemv1.components.navigation.Ux4gPagination',
        'code': '''Ux4gPagination(
    currentPage = page,
    totalPages = 12,
    onPageChange = { newPage -> page = newPage }
)''',
        'params': [
            ('currentPage', 'Int', 'Required', 'Active 1-indexed page number'),
            ('totalPages', 'Int', 'Required', 'Total number of pages'),
            ('onPageChange', '(Int) -> Unit', 'Required', 'Page change event callback')
        ]
    },
    'progress-indicator': {
        'title': 'Progress Indicator',
        'desc': 'Determinate percentage bar and circular gauges for form completion tracking',
        'component': 'Ux4gProgressIndicator',
        'pkg': 'com.ux4g.designsystemv1.components.feedback.Ux4gProgressIndicator',
        'code': '''Ux4gLinearProgress(
    progress = 0.75f, // 75% complete
    label = "Profile Completion: 75%"
)''',
        'params': [
            ('progress', 'Float', 'Required', 'Progress float value from 0.0f to 1.0f'),
            ('label', 'String?', 'null', 'Optional label above progress bar')
        ]
    },
    'radio-button': {
        'title': 'Radio Button',
        'desc': 'Single-selection radio group for gender selection, payment method, and category choices',
        'component': 'Ux4gRadioButton',
        'pkg': 'com.ux4g.designsystemv1.components.selection.Ux4gRadioButton',
        'code': '''val options = listOf("Male", "Female", "Transgender")

Ux4gRadioGroup(
    options = options,
    selectedOption = selectedGender,
    onOptionSelected = { selectedGender = it },
    label = "Gender Selection"
)''',
        'params': [
            ('options', 'List<String>', 'Required', 'List of selectable option strings'),
            ('selectedOption', 'String', 'Required', 'Current selected option string'),
            ('onOptionSelected', '(String) -> Unit', 'Required', 'Selection change callback')
        ]
    },
    'result-list': {
        'title': 'Result List',
        'desc': 'High-density search results and scheme listings with thumbnail, metadata, and badge',
        'component': 'Ux4gResultList',
        'pkg': 'com.ux4g.designsystemv1.components.list.Ux4gResultList',
        'code': '''Ux4gResultItem(
    title = "Pradhan Mantri Awas Yojana (Urban)",
    category = "Housing & Urban Affairs",
    badgeText = "Subsidy upto ₹2.67 Lakh",
    onClick = { /* navigate to scheme */ }
)''',
        'params': [
            ('title', 'String', 'Required', 'Main heading title'),
            ('category', 'String', 'Required', 'Subheading ministry category'),
            ('badgeText', 'String?', 'null', 'Highlight benefit tag')
        ]
    },
    'search-field': {
        'title': 'Search Field',
        'desc': 'Universal search bar with clear button, voice search trigger, and auto-complete',
        'component': 'Ux4gSearchField',
        'pkg': 'com.ux4g.designsystemv1.components.input.Ux4gSearchField',
        'code': '''Ux4gSearchField(
    query = searchQuery,
    onQueryChange = { searchQuery = it },
    placeholder = "Search schemes, certificates, or services...",
    onSearch = { query -> /* Fetch search API */ }
)''',
        'params': [
            ('query', 'String', 'Required', 'Current search query string'),
            ('onQueryChange', '(String) -> Unit', 'Required', 'Query text change handler'),
            ('placeholder', 'String', 'Required', 'Hint placeholder text'),
            ('onSearch', '(String) -> Unit', 'Required', 'Search keyboard action callback')
        ]
    },
    'slider': {
        'title': 'Slider',
        'desc': 'Continuous and discrete range sliders for loan amounts, tenure months, and thresholds',
        'component': 'Ux4gSlider',
        'pkg': 'com.ux4g.designsystemv1.components.input.Ux4gSlider',
        'code': '''Ux4gSlider(
    value = loanAmount,
    onValueChange = { loanAmount = it },
    valueRange = 10000f..500000f,
    steps = 49,
    label = "Loan Amount: ₹${loanAmount.toInt()}"
)''',
        'params': [
            ('value', 'Float', 'Required', 'Current slider float value'),
            ('onValueChange', '(Float) -> Unit', 'Required', 'Slider value change callback'),
            ('valueRange', 'ClosedFloatingPointRange<Float>', '0f..1f', 'Min and max bounds'),
            ('steps', 'Int', '0', 'Number of discrete snap steps')
        ]
    },
    'status-banner': {
        'title': 'Status Banner',
        'desc': 'Prominent announcement alert bar for draft state, KYC pending, or system maintenance',
        'component': 'Ux4gStatusBanner',
        'pkg': 'com.ux4g.designsystemv1.components.feedback.Ux4gStatusBanner',
        'code': '''Ux4gStatusBanner(
    title = "Application in Draft State",
    message = "Your application is saved as a draft. Please complete verification before 30 Sep.",
    variant = Ux4gBannerVariant.Warning,
    actionLabel = "Resume Form",
    onActionClick = { /* continue application */ }
)''',
        'params': [
            ('title', 'String', 'Required', 'Banner headline'),
            ('message', 'String', 'Required', 'Banner message body'),
            ('variant', 'Ux4gBannerVariant', 'Info', 'Tone (Info, Success, Warning, Error)')
        ]
    },
    'status-pipeline': {
        'title': 'Status Pipeline',
        'desc': 'Visual horizontal progress pipeline tracking approval checkpoints across departments',
        'component': 'Ux4gStatusPipeline',
        'pkg': 'com.ux4g.designsystemv1.components.pipeline.Ux4gStatusPipeline',
        'code': '''Ux4gStatusPipeline(
    stages = listOf("Submitted", "Tehsildar Review", "SDM Approval", "Certificate Issued"),
    currentStageIndex = 2
)''',
        'params': [
            ('stages', 'List<String>', 'Required', 'List of approval stage names'),
            ('currentStageIndex', 'Int', 'Required', 'Active 0-indexed stage pointer')
        ]
    },
    'stepper': {
        'title': 'Stepper',
        'desc': 'Multi-step form wizard navigation header for citizen application workflows',
        'component': 'Ux4gStepper',
        'pkg': 'com.ux4g.designsystemv1.components.navigation.Ux4gStepper',
        'code': '''val steps = listOf("Personal Details", "Address Proof", "Income Details", "Bank Account")

Ux4gStepper(
    steps = steps,
    currentStep = currentStepIndex,
    onStepClick = { index -> currentStepIndex = index }
)''',
        'params': [
            ('steps', 'List<String>', 'Required', 'Step title labels'),
            ('currentStep', 'Int', 'Required', 'Active step index (0-based)'),
            ('onStepClick', '((Int) -> Unit)?', 'null', 'Optional step header jump listener')
        ]
    },
    'tag': {
        'title': 'Tag',
        'desc': 'Small metadata pills for document categories, government schemes, and priority flags',
        'component': 'Ux4gTag',
        'pkg': 'com.ux4g.designsystemv1.components.badges.Ux4gTag',
        'code': '''Ux4gTag(text = "Central Sector Scheme", variant = Ux4gTagVariant.Primary)
Ux4gTag(text = "High Priority", variant = Ux4gTagVariant.Error)
Ux4gTag(text = "Offline Verification", variant = Ux4gTagVariant.Neutral)''',
        'params': [
            ('text', 'String', 'Required', 'Tag label text'),
            ('variant', 'Ux4gTagVariant', 'Primary', 'Color tone (Primary, Neutral, Success, Error)')
        ]
    },
    'text-area': {
        'title': 'Text Area',
        'desc': 'Multi-line expanded text box for grievance descriptions, remarks, and residential addresses',
        'component': 'Ux4gTextArea',
        'pkg': 'com.ux4g.designsystemv1.components.input.Ux4gTextArea',
        'code': '''Ux4gTextArea(
    value = grievanceDetails,
    onValueChange = { grievanceDetails = it },
    label = "Grievance Description",
    placeholder = "Please provide detailed description of the issue faced (min 50 characters)...",
    maxCharacters = 1000,
    minLines = 4
)''',
        'params': [
            ('value', 'String', 'Required', 'Multi-line text content'),
            ('onValueChange', '(String) -> Unit', 'Required', 'Text change listener'),
            ('label', 'String', 'Required', 'Header label'),
            ('maxCharacters', 'Int?', 'null', 'Character limit counter'),
            ('minLines', 'Int', '4', 'Minimum initial height lines')
        ]
    },
    'time-picker': {
        'title': 'Time Picker',
        'desc': 'Analog clock and digital hour/minute selection dialog for appointment scheduling',
        'component': 'Ux4gTimePicker',
        'pkg': 'com.ux4g.designsystemv1.components.datetime.Ux4gTimePicker',
        'code': '''Ux4gTimePicker(
    selectedTime = appointmentTime,
    onTimeSelected = { time -> appointmentTime = time },
    label = "Appointment Slot Time",
    is24Hour = false
)''',
        'params': [
            ('selectedTime', 'LocalTime?', 'null', 'Selected time value'),
            ('onTimeSelected', '(LocalTime) -> Unit', 'Required', 'Time selection callback'),
            ('label', 'String', 'Required', 'Field title text'),
            ('is24Hour', 'Boolean', 'false', 'Toggles 12-hour AM/PM vs 24-hour clock')
        ]
    },
    'time-slot': {
        'title': 'Time Slot',
        'desc': 'Available and booked appointment grid chips for RTO, Passport Seva, and Sub-Registrar slots',
        'component': 'Ux4gTimeSlotGrid',
        'pkg': 'com.ux4g.designsystemv1.components.datetime.Ux4gTimeSlotGrid',
        'code': '''val slots = listOf(
    TimeSlot(time = "09:30 AM", isAvailable = true),
    TimeSlot(time = "10:00 AM", isAvailable = false),
    TimeSlot(time = "10:30 AM", isAvailable = true)
)

Ux4gTimeSlotGrid(
    slots = slots,
    selectedSlot = selectedSlot,
    onSlotSelected = { slot -> selectedSlot = slot }
)''',
        'params': [
            ('slots', 'List<TimeSlot>', 'Required', 'List of available/booked appointment slots'),
            ('selectedSlot', 'TimeSlot?', 'null', 'Currently picked appointment slot'),
            ('onSlotSelected', '(TimeSlot) -> Unit', 'Required', 'Slot click listener')
        ]
    },
    'toast': {
        'title': 'Toast & Snackbars',
        'desc': 'Transient feedback messages with auto-dismiss and undo action for quick confirmations',
        'component': 'Ux4gToast',
        'pkg': 'com.ux4g.designsystemv1.components.feedback.Ux4gToast',
        'code': '''// Show Toast Notification
val toastState = rememberUx4gToastState()

toastState.show(
    message = "Application saved to DigiLocker successfully!",
    type = Ux4gToastType.Success,
    duration = Ux4gToastDuration.Short
)''',
        'params': [
            ('message', 'String', 'Required', 'Notification message text'),
            ('type', 'Ux4gToastType', 'Info', 'Toast type (Success, Error, Warning, Info)'),
            ('duration', 'Ux4gToastDuration', 'Short', 'Display time before auto-dismiss')
        ]
    },
    'toggle': {
        'title': 'Toggle Switch',
        'desc': 'Binary on/off switch for SMS alerts, biometric login, and theme preferences',
        'component': 'Ux4gSwitch',
        'pkg': 'com.ux4g.designsystemv1.components.selection.Ux4gSwitch',
        'code': '''Ux4gSwitch(
    checked = isSmsNotificationEnabled,
    onCheckedChange = { isSmsNotificationEnabled = it },
    label = "Receive SMS status updates on registered mobile"
)''',
        'params': [
            ('checked', 'Boolean', 'Required', 'Boolean toggle state'),
            ('onCheckedChange', '(Boolean) -> Unit', 'Required', 'Switch toggle event handler'),
            ('label', 'String?', 'null', 'Label accompanying the switch')
        ]
    },
    'tooltip': {
        'title': 'Tooltip',
        'desc': 'Contextual helper bubble explaining technical terms, mandatory requirements, and field tips',
        'component': 'Ux4gTooltip',
        'pkg': 'com.ux4g.designsystemv1.components.feedback.Ux4gTooltip',
        'code': '''Ux4gTooltip(
    tooltipText = "Enter the 12-digit number printed on your Aadhaar card or e-Aadhaar PDF."
) {
    Icon(
        imageVector = Icons.Outlined.Info,
        contentDescription = "Help information",
        tint = Ux4gTheme.colors.primary
    )
}''',
        'params': [
            ('tooltipText', 'String', 'Required', 'Helper explanation displayed inside bubble'),
            ('content', '@Composable () -> Unit', 'Required', 'Anchor composable that triggers tooltip on hover/click')
        ]
    }
}

for comp, data in components_data.items():
    mdx_path = os.path.join(docs_dir, f'{comp}.mdx')
    
    # Generate parameters table rows
    param_rows = '\n'.join([f'| `{p[0]}` | `{p[1]}` | {p[2]} | {p[3]} |' for p in data['params']])
    
    content = f'''---
title: {data['title']}
description: {data['desc']}
---

# {data['title']} (`{data['component']}`)

{data['desc']}.

Import from `{data['pkg']}`.

---

<h2><MIcon name="smartphone" /> Interactive Preview</h2>

<ComponentPreview
  title="{data['component']}"
  category="Jetpack Compose"
  code={{`{data['code']}`}}
>
  <div className="flex flex-col items-center justify-center py-2">
    <img
      src="/images/components/{comp}_preview.png"
      alt="UX4G {data['title']} Preview"
      className="max-w-full rounded-xl border border-border/60 shadow-sm"
    />
  </div>
</ComponentPreview>

---

<h2><MIcon name="code" /> Kotlin Usage</h2>

```kotlin title="Compose Example"
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import {data['pkg']}

@Composable
fun {data['component']}Example() {{
    {data['code']}
}}
```

---

<h2><MIcon name="settings" /> Parameters</h2>

| Parameter | Type | Default | Description |
| :--- | :--- | :--- | :--- |
{param_rows}
'''
    with open(mdx_path, 'w', encoding='utf-8') as fp:
        fp.write(content)
    print(f'Updated {comp}.mdx successfully!')

print('ALL 38 component MDX documentation files successfully upgraded!')
