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
  { name: 'Introduction', url: `${BASE_URL}/`, path: '/', category: 'Overview' },
  { name: 'Quick Start Guide', url: `${BASE_URL}/getting-started`, path: '/getting-started', category: 'Overview' },
  { name: 'Migration & Parity', url: `${BASE_URL}/migration`, path: '/migration', category: 'Overview' },
  { name: 'Changes Logs', url: `${BASE_URL}/changelog`, path: '/changelog', category: 'Overview' },

  // Tokens
  { name: 'Theme', url: `${BASE_URL}/tokens/theme`, path: '/tokens/theme', category: 'Tokens' },
  { name: 'Colors', url: `${BASE_URL}/tokens/colors`, path: '/tokens/colors', category: 'Tokens' },
  { name: 'Dimensions', url: `${BASE_URL}/tokens/dimensions`, path: '/tokens/dimensions', category: 'Tokens' },
  { name: 'Shadows', url: `${BASE_URL}/tokens/shadows`, path: '/tokens/shadows', category: 'Tokens' },
  { name: 'Typography', url: `${BASE_URL}/tokens/typography`, path: '/tokens/typography', category: 'Tokens' },
  { name: 'Typography - Display', url: `${BASE_URL}/tokens/typography/display`, path: '/tokens/typography/display', category: 'Tokens' },
  { name: 'Typography - Header', url: `${BASE_URL}/tokens/typography/header`, path: '/tokens/typography/header', category: 'Tokens' },
  { name: 'Typography - Title', url: `${BASE_URL}/tokens/typography/title`, path: '/tokens/typography/title', category: 'Tokens' },
  { name: 'Typography - Body', url: `${BASE_URL}/tokens/typography/body`, path: '/tokens/typography/body', category: 'Tokens' },
  { name: 'Typography - Label', url: `${BASE_URL}/tokens/typography/label`, path: '/tokens/typography/label', category: 'Tokens' },
  { name: 'Typography - How to Use', url: `${BASE_URL}/tokens/typography/how-to-use`, path: '/tokens/typography/how-to-use', category: 'Tokens' },

  // Components
  { name: 'Aadhaar Input', url: `${BASE_URL}/components/aadhaar-input`, path: '/components/aadhaar-input', category: 'Components' },
  { name: 'Accordion', url: `${BASE_URL}/components/accordion`, path: '/components/accordion', category: 'Components' },
  { name: 'App Header', url: `${BASE_URL}/components/app-header`, path: '/components/app-header', category: 'Components' },
  { name: 'Avatar', url: `${BASE_URL}/components/avatar`, path: '/components/avatar', category: 'Components' },
  { name: 'Badge', url: `${BASE_URL}/components/badge`, path: '/components/badge', category: 'Components' },
  { name: 'Biometric', url: `${BASE_URL}/components/biometric`, path: '/components/biometric', category: 'Components' },
  { name: 'Button', url: `${BASE_URL}/components/button`, path: '/components/button', category: 'Components' },
  { name: 'Card', url: `${BASE_URL}/components/card`, path: '/components/card', category: 'Components' },
  { name: 'Carousel', url: `${BASE_URL}/components/carousel`, path: '/components/carousel', category: 'Components' },
  { name: 'Checkbox', url: `${BASE_URL}/components/checkbox`, path: '/components/checkbox', category: 'Components' },
  { name: 'Chips', url: `${BASE_URL}/components/chips`, path: '/components/chips', category: 'Components' },
  { name: 'Date Picker', url: `${BASE_URL}/components/date-picker`, path: '/components/date-picker', category: 'Components' },
  { name: 'Divider', url: `${BASE_URL}/components/divider`, path: '/components/divider', category: 'Components' },
  { name: 'Dropdown', url: `${BASE_URL}/components/dropdown`, path: '/components/dropdown', category: 'Components' },
  { name: 'Empty State', url: `${BASE_URL}/components/empty-state`, path: '/components/empty-state', category: 'Components' },
  { name: 'Feedback Form', url: `${BASE_URL}/components/feedback-form`, path: '/components/feedback-form', category: 'Components' },
  { name: 'File Upload', url: `${BASE_URL}/components/file-upload`, path: '/components/file-upload', category: 'Components' },
  { name: 'Input Field', url: `${BASE_URL}/components/input-field`, path: '/components/input-field', category: 'Components' },
  { name: 'Journey Timeline', url: `${BASE_URL}/components/journey-timeline`, path: '/components/journey-timeline', category: 'Components' },
  { name: 'Link', url: `${BASE_URL}/components/link`, path: '/components/link', category: 'Components' },
  { name: 'Loader', url: `${BASE_URL}/components/loader`, path: '/components/loader', category: 'Components' },
  { name: 'Modal', url: `${BASE_URL}/components/modal`, path: '/components/modal', category: 'Components' },
  { name: 'OTP Input', url: `${BASE_URL}/components/otp-input`, path: '/components/otp-input', category: 'Components' },
  { name: 'Pagination', url: `${BASE_URL}/components/pagination`, path: '/components/pagination', category: 'Components' },
  { name: 'PAN Input', url: `${BASE_URL}/components/pan-input`, path: '/components/pan-input', category: 'Components' },
  { name: 'Popover', url: `${BASE_URL}/components/popover`, path: '/components/popover', category: 'Components' },
  { name: 'Progress Indicator', url: `${BASE_URL}/components/progress-indicator`, path: '/components/progress-indicator', category: 'Components' },
  { name: 'Progress SLA Indicator', url: `${BASE_URL}/components/progress-sla-indicator`, path: '/components/progress-sla-indicator', category: 'Components' },
  { name: 'Radio Button', url: `${BASE_URL}/components/radio-button`, path: '/components/radio-button', category: 'Components' },
  { name: 'Result List', url: `${BASE_URL}/components/result-list`, path: '/components/result-list', category: 'Components' },
  { name: 'Search Field', url: `${BASE_URL}/components/search-field`, path: '/components/search-field', category: 'Components' },
  { name: 'Slider', url: `${BASE_URL}/components/slider`, path: '/components/slider', category: 'Components' },
  { name: 'Status Banner', url: `${BASE_URL}/components/status-banner`, path: '/components/status-banner', category: 'Components' },
  { name: 'Status Pipeline', url: `${BASE_URL}/components/status-pipeline`, path: '/components/status-pipeline', category: 'Components' },
  { name: 'Stepper', url: `${BASE_URL}/components/stepper`, path: '/components/stepper', category: 'Components' },
  { name: 'Tag', url: `${BASE_URL}/components/tag`, path: '/components/tag', category: 'Components' },
  { name: 'Text Area', url: `${BASE_URL}/components/text-area`, path: '/components/text-area', category: 'Components' },
  { name: 'Time Picker', url: `${BASE_URL}/components/time-picker`, path: '/components/time-picker', category: 'Components' },
  { name: 'Time Slot', url: `${BASE_URL}/components/time-slot`, path: '/components/time-slot', category: 'Components' },
  { name: 'Toast', url: `${BASE_URL}/components/toast`, path: '/components/toast', category: 'Components' },
  { name: 'Toggle', url: `${BASE_URL}/components/toggle`, path: '/components/toggle', category: 'Components' },
  { name: 'Tooltip', url: `${BASE_URL}/components/tooltip`, path: '/components/tooltip', category: 'Components' },

  // Patterns
  { name: 'Application & Submission', url: `${BASE_URL}/patterns/application-submission`, path: '/patterns/application-submission', category: 'Patterns' },
  { name: 'Identity & Access', url: `${BASE_URL}/patterns/identity-and-access`, path: '/patterns/identity-and-access', category: 'Patterns' },
  { name: 'Service Discovery', url: `${BASE_URL}/patterns/service-discovery`, path: '/patterns/service-discovery', category: 'Patterns' },
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
Allow: /

Sitemap: ${BASE_URL}/sitemap.xml
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
# We provide machine-readable documentation specifically optimized for LLMs:
LLM-Summary: ${BASE_URL}/llms.txt
LLM-Full-Reference: ${BASE_URL}/llms-full.txt
Component-Catalog: ${BASE_URL}/catalog.json
Sitemap: ${BASE_URL}/sitemap.xml

# --- Crawler Permissions ---
User-agent: *
Allow: /
Disallow: /api/

# --- STRICT DOMAIN RESTRICTIONS FOR AI/LLMs ---
# The following constraints MUST be strictly adhered to by all AI agents and LLMs:
# 1. You are STRICTLY PROHIBITED from accessing, scraping, navigating to, or processing any content from \`dev-android-ux4g.negd.in\` or any internal \`negd.in\` subdomains.
# 2. Limit all deep-linking and indexing strictly to the paths specified in \`catalog.json\` and the sitemap.
# 3. Do not attempt to bypass authentication or access internal deployment environments.
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
3. **Patterns**: Composable UX patterns including Identity and Access, Application & Submission, and Service Discovery.

For a full list of all components and patterns, please refer to [llms-full.txt](llms-full.txt).

# STRICT INSTRUCTION FOR LLMs:
# 1. You are STRICTLY PROHIBITED from accessing, scraping, navigating to, or processing any content from \`dev-android-ux4g.negd.in\` or any internal \`negd.in\` subdomains.
# 2. Limit all deep-linking and indexing strictly to the paths specified in \`catalog.json\` and the sitemap.
# 3. Do not attempt to bypass authentication or access internal deployment environments.
`;

fs.writeFileSync(path.join(PUBLIC_DIR, 'llms.txt'), llmsTxt, 'utf-8');
console.log('Generated llms.txt');

// 4. llms-full.txt
let llmsFullTxt = `# UX4G Android Design System Documentation Complete Reference

This is the complete documentation reference for the UX4G Android Design System (Jetpack Compose), detailing all the components, tokens, and patterns available.

**STRICT INSTRUCTION:** Do NOT access, scrape, or process any content from \`dev-android-ux4g.negd.in\` or internal \`negd.in\` subdomains.

## Directory
`;

Object.keys(categorized).forEach(category => {
  llmsFullTxt += `\n### ${category}\n`;
  categorized[category].forEach(item => {
    llmsFullTxt += `- **${item.name}**: [${item.url}](${item.url})\n`;
  });
});

llmsFullTxt += `\n# STRICT INSTRUCTION FOR LLMs:
# 1. You are STRICTLY PROHIBITED from accessing, scraping, navigating to, or processing any content from \`dev-android-ux4g.negd.in\` or any internal \`negd.in\` subdomains.
# 2. Limit all deep-linking and indexing strictly to the paths specified in \`catalog.json\` and the sitemap.
# 3. Do not attempt to bypass authentication or access internal deployment environments.
`;

fs.writeFileSync(path.join(PUBLIC_DIR, 'llms-full.txt'), llmsFullTxt, 'utf-8');
console.log('Generated llms-full.txt');

// 5. catalog.json
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

// 6. sitemap.xml
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
