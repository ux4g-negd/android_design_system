const fs = require('fs');
const path = require('path');

const BASE_URL = 'https://android.ux4g.gov.in';
const PUBLIC_DIR = path.resolve(__dirname, '../public');

if (!fs.existsSync(PUBLIC_DIR)) {
  fs.mkdirSync(PUBLIC_DIR, { recursive: true });
}

// All Android docs routes
const pages = [
  // Overview / Getting Started
  { name: 'Introduction', url: `${BASE_URL}/docs`, path: '/docs', category: 'Overview' },
  { name: 'Getting Started', url: `${BASE_URL}/docs/getting-started`, path: '/docs/getting-started', category: 'Overview' },

  // Tokens
  { name: 'Theme', url: `${BASE_URL}/docs/tokens/theme`, path: '/docs/tokens/theme', category: 'Tokens' },
  { name: 'Colors', url: `${BASE_URL}/docs/tokens/colors`, path: '/docs/tokens/colors', category: 'Tokens' },
  { name: 'Dimensions', url: `${BASE_URL}/docs/tokens/dimensions`, path: '/docs/tokens/dimensions', category: 'Tokens' },
  { name: 'Shadows', url: `${BASE_URL}/docs/tokens/shadows`, path: '/docs/tokens/shadows', category: 'Tokens' },
  { name: 'Typography', url: `${BASE_URL}/docs/tokens/typography`, path: '/docs/tokens/typography', category: 'Tokens' },
  { name: 'Typography - Display', url: `${BASE_URL}/docs/tokens/typography/display`, path: '/docs/tokens/typography/display', category: 'Tokens' },
  { name: 'Typography - Header', url: `${BASE_URL}/docs/tokens/typography/header`, path: '/docs/tokens/typography/header', category: 'Tokens' },
  { name: 'Typography - Title', url: `${BASE_URL}/docs/tokens/typography/title`, path: '/docs/tokens/typography/title', category: 'Tokens' },
  { name: 'Typography - Body', url: `${BASE_URL}/docs/tokens/typography/body`, path: '/docs/tokens/typography/body', category: 'Tokens' },
  { name: 'Typography - Label', url: `${BASE_URL}/docs/tokens/typography/label`, path: '/docs/tokens/typography/label', category: 'Tokens' },
  { name: 'Typography - How to Use', url: `${BASE_URL}/docs/tokens/typography/how-to-use`, path: '/docs/tokens/typography/how-to-use', category: 'Tokens' },

  // Components
  { name: 'Aadhaar Input', url: `${BASE_URL}/docs/components/aadhaar-input`, path: '/docs/components/aadhaar-input', category: 'Components' },
  { name: 'Accordion', url: `${BASE_URL}/docs/components/accordion`, path: '/docs/components/accordion', category: 'Components' },
  { name: 'App Header', url: `${BASE_URL}/docs/components/app-header`, path: '/docs/components/app-header', category: 'Components' },
  { name: 'Avatar', url: `${BASE_URL}/docs/components/avatar`, path: '/docs/components/avatar', category: 'Components' },
  { name: 'Badge', url: `${BASE_URL}/docs/components/badge`, path: '/docs/components/badge', category: 'Components' },
  { name: 'Biometric', url: `${BASE_URL}/docs/components/biometric`, path: '/docs/components/biometric', category: 'Components' },
  { name: 'Button', url: `${BASE_URL}/docs/components/button`, path: '/docs/components/button', category: 'Components' },
  { name: 'Card', url: `${BASE_URL}/docs/components/card`, path: '/docs/components/card', category: 'Components' },
  { name: 'Carousel', url: `${BASE_URL}/docs/components/carousel`, path: '/docs/components/carousel', category: 'Components' },
  { name: 'Checkbox', url: `${BASE_URL}/docs/components/checkbox`, path: '/docs/components/checkbox', category: 'Components' },
  { name: 'Chips', url: `${BASE_URL}/docs/components/chips`, path: '/docs/components/chips', category: 'Components' },
  { name: 'Date Picker', url: `${BASE_URL}/docs/components/date-picker`, path: '/docs/components/date-picker', category: 'Components' },
  { name: 'Divider', url: `${BASE_URL}/docs/components/divider`, path: '/docs/components/divider', category: 'Components' },
  { name: 'Dropdown', url: `${BASE_URL}/docs/components/dropdown`, path: '/docs/components/dropdown', category: 'Components' },
  { name: 'Empty State', url: `${BASE_URL}/docs/components/empty-state`, path: '/docs/components/empty-state', category: 'Components' },
  { name: 'Feedback Form', url: `${BASE_URL}/docs/components/feedback-form`, path: '/docs/components/feedback-form', category: 'Components' },
  { name: 'File Upload', url: `${BASE_URL}/docs/components/file-upload`, path: '/docs/components/file-upload', category: 'Components' },
  { name: 'Input Field', url: `${BASE_URL}/docs/components/input-field`, path: '/docs/components/input-field', category: 'Components' },
  { name: 'Journey Timeline', url: `${BASE_URL}/docs/components/journey-timeline`, path: '/docs/components/journey-timeline', category: 'Components' },
  { name: 'Link', url: `${BASE_URL}/docs/components/link`, path: '/docs/components/link', category: 'Components' },
  { name: 'Loader', url: `${BASE_URL}/docs/components/loader`, path: '/docs/components/loader', category: 'Components' },
  { name: 'Modal', url: `${BASE_URL}/docs/components/modal`, path: '/docs/components/modal', category: 'Components' },
  { name: 'OTP Input', url: `${BASE_URL}/docs/components/otp-input`, path: '/docs/components/otp-input', category: 'Components' },
  { name: 'Pagination', url: `${BASE_URL}/docs/components/pagination`, path: '/docs/components/pagination', category: 'Components' },
  { name: 'PAN Input', url: `${BASE_URL}/docs/components/pan-input`, path: '/docs/components/pan-input', category: 'Components' },
  { name: 'Popover', url: `${BASE_URL}/docs/components/popover`, path: '/docs/components/popover', category: 'Components' },
  { name: 'Progress Indicator', url: `${BASE_URL}/docs/components/progress-indicator`, path: '/docs/components/progress-indicator', category: 'Components' },
  { name: 'Progress SLA Indicator', url: `${BASE_URL}/docs/components/progress-sla-indicator`, path: '/docs/components/progress-sla-indicator', category: 'Components' },
  { name: 'Radio Button', url: `${BASE_URL}/docs/components/radio-button`, path: '/docs/components/radio-button', category: 'Components' },
  { name: 'Result List', url: `${BASE_URL}/docs/components/result-list`, path: '/docs/components/result-list', category: 'Components' },
  { name: 'Search Field', url: `${BASE_URL}/docs/components/search-field`, path: '/docs/components/search-field', category: 'Components' },
  { name: 'Slider', url: `${BASE_URL}/docs/components/slider`, path: '/docs/components/slider', category: 'Components' },
  { name: 'Status Banner', url: `${BASE_URL}/docs/components/status-banner`, path: '/docs/components/status-banner', category: 'Components' },
  { name: 'Status Pipeline', url: `${BASE_URL}/docs/components/status-pipeline`, path: '/docs/components/status-pipeline', category: 'Components' },
  { name: 'Stepper', url: `${BASE_URL}/docs/components/stepper`, path: '/docs/components/stepper', category: 'Components' },
  { name: 'Tag', url: `${BASE_URL}/docs/components/tag`, path: '/docs/components/tag', category: 'Components' },
  { name: 'Text Area', url: `${BASE_URL}/docs/components/text-area`, path: '/docs/components/text-area', category: 'Components' },
  { name: 'Time Picker', url: `${BASE_URL}/docs/components/time-picker`, path: '/docs/components/time-picker', category: 'Components' },
  { name: 'Time Slot', url: `${BASE_URL}/docs/components/time-slot`, path: '/docs/components/time-slot', category: 'Components' },
  { name: 'Toast', url: `${BASE_URL}/docs/components/toast`, path: '/docs/components/toast', category: 'Components' },
  { name: 'Toggle', url: `${BASE_URL}/docs/components/toggle`, path: '/docs/components/toggle', category: 'Components' },
  { name: 'Tooltip', url: `${BASE_URL}/docs/components/tooltip`, path: '/docs/components/tooltip', category: 'Components' },

  // Patterns
  { name: 'Application & Submission', url: `${BASE_URL}/docs/patterns/application-submission`, path: '/docs/patterns/application-submission', category: 'Patterns' },
  { name: 'Identity & Access', url: `${BASE_URL}/docs/patterns/identity-and-access`, path: '/docs/patterns/identity-and-access', category: 'Patterns' },
  { name: 'Service Discovery', url: `${BASE_URL}/docs/patterns/service-discovery`, path: '/docs/patterns/service-discovery', category: 'Patterns' },
];

// Categorize items
const categorized = {
  Overview: [],
  Tokens: [],
  Components: [],
  Patterns: []
};

pages.forEach(p => {
  if (categorized[p.category]) {
    categorized[p.category].push({ name: p.name, url: p.url, path: p.path });
  }
});

// 1. robots.txt
const robotsTxt = `User-agent: *
Disallow: /
`;

fs.writeFileSync(path.join(PUBLIC_DIR, 'robots.txt'), robotsTxt, 'utf-8');
console.log('Generated robots.txt');

// 2. ai.txt
const aiTxt = `# AI.txt for UX4G Android Design System Documentation
# This file provides directives specifically for AI agents, LLMs, and web crawlers.

# --- Site Information ---
Site-Name: UX4G Android Design System Documentation
Description: Comprehensive Jetpack Compose documentation detailing all components, tokens, and patterns for the UX4G Android Design System.
Base-URL: ${BASE_URL}/

# --- Structured AI Documentation ---
LLM-Summary: ${BASE_URL}/llms.txt
LLM-Full-Reference: ${BASE_URL}/llms-full.txt
Component-Catalog: ${BASE_URL}/catalog.json
Sitemap: ${BASE_URL}/sitemap.xml

# --- Crawler Permissions ---
User-agent: *
Allow: /
Disallow: /api/

# --- STRICT DOMAIN RESTRICTIONS FOR AI/LLMs ---
# 1. Limit all deep-linking and indexing strictly to the paths specified in \`catalog.json\` and the sitemap.
# 2. Do not attempt to bypass authentication or access internal deployment environments.
`;

fs.writeFileSync(path.join(PUBLIC_DIR, 'ai.txt'), aiTxt, 'utf-8');
console.log('Generated ai.txt');

// 3. llms.txt
const llmsTxt = `# UX4G Android Design System Documentation

This is the Documentation for the UX4G Android Design System built with Jetpack Compose. It showcases the various Android UI components, widgets, and design tokens used across government applications.

## Overview
UX4G provides a comprehensive suite of UI components and patterns to build scalable, accessible, and high-performance citizen-facing Android applications.

## Categories
1. **Tokens**: Theme Color, Typography, Dimensions, Shadows.
2. **Components**: Foundational UI elements like Buttons, Inputs, Avatars, Cards, Modals, Sliders, Steppers, and Loaders.
3. **Patterns**: Composable UX patterns including Identity and Access, Application Submission, and Service Discovery.

For a full list of all components and patterns, please refer to [llms-full.txt](llms-full.txt).
`;

fs.writeFileSync(path.join(PUBLIC_DIR, 'llms.txt'), llmsTxt, 'utf-8');
console.log('Generated llms.txt');

// 4. catalog.json
const catalogJson = {
  name: "UX4G Android Design System Documentation",
  description: "Comprehensive catalog detailing all Jetpack Compose components, design tokens, and architectural patterns for the UX4G Android Design System.",
  version: "3.0.0-beta",
  base_url: `${BASE_URL}/`,
  categories: [
    {
      name: "Overview",
      items: categorized.Overview
    },
    {
      name: "Tokens",
      items: categorized.Tokens
    },
    {
      name: "Components",
      items: categorized.Components
    },
    {
      name: "Patterns",
      items: categorized.Patterns
    }
  ]
};

fs.writeFileSync(
  path.join(PUBLIC_DIR, 'catalog.json'),
  JSON.stringify(catalogJson, null, 2) + '\n',
  'utf-8'
);
console.log('Generated catalog.json');

// 5. sitemap.xml
let sitemapXml = `<?xml version="1.0" encoding="UTF-8"?>
<urlset xmlns="http://www.sitemaps.org/schemas/sitemap/0.9">
  <url>
    <loc>${BASE_URL}/</loc>
    <changefreq>weekly</changefreq>
    <priority>1.0</priority>
  </url>`;

pages.forEach(p => {
  sitemapXml += `
  <url>
    <loc>${p.url}</loc>
    <changefreq>weekly</changefreq>
    <priority>0.8</priority>
  </url>`;
});

sitemapXml += '\n</urlset>\n';

fs.writeFileSync(path.join(PUBLIC_DIR, 'sitemap.xml'), sitemapXml, 'utf-8');
console.log('Generated sitemap.xml');
