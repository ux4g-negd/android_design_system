'use client';

import React, { useState, useMemo, useEffect } from 'react';
import Link from 'next/link';
import { ANDROID_COMPONENTS_DATA, type AndroidComponentItem } from './androidComponentsData';
import { ANDROID_PROPS_MAPPING_DATA } from './androidPropsMappingData';
import { ANDROID_TOKENS_DATA } from './androidTokensData';
import { MIGRATION_MD_CONTENT } from './androidMigrationMarkdown';
import { MIGRATION_PROMPT_MD_CONTENT } from './androidMigrationPromptMarkdown';
import './MigrationDoc.css';

function MaterialDesignIcon({ className = 'mg-framework-icon' }: { className?: string }) {
  return (
    <svg viewBox="0 0 100 100" fill="none" xmlns="http://www.w3.org/2000/svg" className={className}>
      <circle cx="50" cy="50" r="43" stroke="currentColor" strokeWidth="8" />
      <path
        d="M26 26H74M26 74H74M26 26V74M74 26V74M26 26L50 62L74 26"
        stroke="currentColor"
        strokeWidth="8"
        strokeLinecap="round"
        strokeLinejoin="round"
      />
    </svg>
  );
}

type TabId = 'components' | 'props-mapping' | 'tokens' | 'gov-exclusive';

interface TabDef {
  id: TabId;
  label: string;
  badge?: string;
}

const TABS: TabDef[] = [
  { id: 'components', label: 'Components', badge: `${ANDROID_COMPONENTS_DATA.length}` },
  { id: 'props-mapping', label: 'Props & API Mapping', badge: `${ANDROID_PROPS_MAPPING_DATA.length} APIs` },
  { id: 'tokens', label: 'Tokens & Theming', badge: 'Parity' },
  { id: 'gov-exclusive', label: 'Gov-Exclusive Modules', badge: '8+' },
];

export function MigrationPage() {
  const [activeTab, setActiveTab] = useState<TabId>('components');
  const [scrolled, setScrolled] = useState(false);

  // Tab 1: Components state
  const [compSearch, setCompSearch] = useState('');
  const [compFilter, setCompFilter] = useState('all');

  // Tab 2: Props Mapping state
  const [propSearch, setPropSearch] = useState('');
  const [selectedCompFilter, setSelectedCompFilter] = useState('all');

  useEffect(() => {
    const handleScroll = () => {
      setScrolled(window.scrollY > 20);
    };
    window.addEventListener('scroll', handleScroll, { passive: true });
    return () => window.removeEventListener('scroll', handleScroll);
  }, []);

  // Filtered Components
  const filteredComponents = useMemo(() => {
    const s = compSearch.toLowerCase().trim();
    return ANDROID_COMPONENTS_DATA.filter((item: AndroidComponentItem) => {
      const matchText =
        `${item.name} ${item.ux4gText} ${item.m3Text} ${item.notes} ${item.category}`.toLowerCase();
      const textMatches = !s || matchText.includes(s);
      if (!textMatches) return false;

      if (compFilter === 'all') return true;
      if (compFilter === 'both') return item.match === 'both';
      if (compFilter === 'has-ux4g') return item.ux4gText.includes('Yes');
      if (compFilter === 'ux4g-only') return item.match === 'ux4g-only';
      if (compFilter === 'has-m3') return item.m3Text.includes('Yes');
      if (compFilter === 'only-m3') {
        return item.m3Text.includes('Yes') && !item.ux4gText.includes('Yes');
      }
      if (compFilter === 'partial') return item.match === 'partial';
      return true;
    });
  }, [compSearch, compFilter]);

  // Filtered Props
  const filteredProps = useMemo(() => {
    const s = propSearch.toLowerCase().trim();
    return ANDROID_PROPS_MAPPING_DATA.filter((item) => {
      if (selectedCompFilter !== 'all' && item.component.toLowerCase() !== selectedCompFilter.toLowerCase()) {
        return false;
      }
      if (!s) return true;
      const combined =
        `${item.component} ${item.propGroup} ${item.ux4gProp} ${item.m3Prop} ${item.description}`.toLowerCase();
      return combined.includes(s);
    });
  }, [selectedCompFilter, propSearch]);

  const uniquePropComponents = useMemo(() => {
    return Array.from(new Set(ANDROID_PROPS_MAPPING_DATA.map((p) => p.component))).sort((a, b) =>
      a.localeCompare(b)
    );
  }, []);

  const [downloadSuccess, setDownloadSuccess] = useState(false);
  const [downloadPromptSuccess, setDownloadPromptSuccess] = useState(false);

  const handleDownloadMigrationDoc = () => {
    try {
      const blob = new Blob([MIGRATION_MD_CONTENT], { type: 'text/markdown;charset=utf-8' });
      const url = URL.createObjectURL(blob);
      const link = document.createElement('a');
      link.href = url;
      link.download = 'MIGRATION.md';
      document.body.appendChild(link);
      link.click();
      document.body.removeChild(link);
      URL.revokeObjectURL(url);
      setDownloadSuccess(true);
      setTimeout(() => setDownloadSuccess(false), 3000);
    } catch (err) {
      console.error('Failed to download MIGRATION.md:', err);
    }
  };

  const handleDownloadPromptDoc = () => {
    try {
      const blob = new Blob([MIGRATION_PROMPT_MD_CONTENT], { type: 'text/markdown;charset=utf-8' });
      const url = URL.createObjectURL(blob);
      const link = document.createElement('a');
      link.href = url;
      link.download = 'migration-prompt.md';
      document.body.appendChild(link);
      link.click();
      document.body.removeChild(link);
      URL.revokeObjectURL(url);
      setDownloadPromptSuccess(true);
      setTimeout(() => setDownloadPromptSuccess(false), 3000);
    } catch (err) {
      console.error('Failed to download migration-prompt.md:', err);
    }
  };

  return (
    <div className="mg-page">
      {/* Blocker to cover top header gap on scroll */}
      {scrolled && <div className="mg-scroll-blocker" />}

      {/* Hero Header */}
      <section className="mg-hero">
        <div className="mg-ambient-glow-1" />
        <div className="mg-ambient-glow-2" />

        <div className="mg-hero-content">
          {/* Top Eyebrow Badge */}
          <div className="mg-eyebrow-badge">
            <svg width="14" height="14" viewBox="0 0 24 24" fill="none" stroke="currentColor" strokeWidth="2.5" strokeLinecap="round" strokeLinejoin="round">
              <path d="M12 2l3.09 6.26L22 9.27l-5 4.87 1.18 6.88L12 17.77l-6.18 3.25L7 14.14 2 9.27l6.91-1.01L12 2z" />
            </svg>
            <span>Jetpack Compose Ecosystem Parity & Migration Report</span>
          </div>

          {/* Title with Framework logos */}
          <div className="mg-hero-logos">
            {/* UX4G Android Logo Box */}
            <span className="mg-framework-logo-box" title="UX4G Android Design System (Compose)">
              {/* eslint-disable-next-line @next/next/no-img-element */}
              <img
                src="/ux4g_logo.svg"
                alt="UX4G"
                className="mg-framework-img"
              />
            </span>

            <span className="mg-framework-arrow">↔</span>

            {/* Material Design 3 Logo */}
            <span className="mg-framework-logo-box" title="Material Design 3 (M3 Compose)">
              <MaterialDesignIcon className="mg-framework-icon" />
            </span>
          </div>

          <p className="mg-hero-desc">
            Detailed cross-framework component mapping for 45+ UX4G Android Jetpack Compose components, Material 3 (M3 Compose)
            equivalents, composable parameter parity, and non-destructive migration guidelines.
          </p>

          {/* Download Action Buttons */}
          <div className="mg-hero-actions" style={{ marginTop: '1.25rem', display: 'flex', gap: '0.75rem', flexWrap: 'wrap' }}>
            <button
              type="button"
              onClick={handleDownloadMigrationDoc}
              className={`mg-download-btn ${downloadSuccess ? 'is-success' : ''}`}
              title="Download MIGRATION.md guide"
            >
              <span className="material-symbols-outlined mg-download-icon">
                {downloadSuccess ? 'check' : 'download'}
              </span>
              <span>migration.md</span>
            </button>
            <button
              type="button"
              onClick={handleDownloadPromptDoc}
              className={`mg-download-btn ${downloadPromptSuccess ? 'is-success' : ''}`}
              title="Download migration-prompt.md for AI coding assistants"
            >
              <span className="material-symbols-outlined mg-download-icon">
                {downloadPromptSuccess ? 'check' : 'terminal'}
              </span>
              <span>migration-prompt.md</span>
            </button>
          </div>
        </div>
      </section>

      {/* Pill-Segmented Developer Tab Bar */}
      <section className="mg-sticky-tab-bar">
        <div className="mg-tab-container">
          <div className="mg-tablist" role="tablist">
            {TABS.map((tab) => {
              const isActive = activeTab === tab.id;
              return (
                <button
                  key={tab.id}
                  type="button"
                  role="tab"
                  onClick={() => setActiveTab(tab.id)}
                  className={`mg-tab ${isActive ? 'is-active' : ''}`}
                >
                  <span>{tab.label}</span>
                  {tab.badge && <span className="mg-tab-badge">{tab.badge}</span>}
                </button>
              );
            })}
          </div>
        </div>
      </section>

      {/* Main Content Area */}
      <main className="mg-body-container">
        {/* TAB 1: COMPONENTS */}
        {activeTab === 'components' && (
          <section>
            <div className="mg-section-header">
              <div style={{ flex: 1, minWidth: 260 }}>
                <div className="mg-section-title-row">
                  <h2 className="mg-section-heading">Jetpack Compose Component Mapping</h2>
                  <span className="mg-count-pill">
                    {filteredComponents.length} of {ANDROID_COMPONENTS_DATA.length} components
                  </span>
                </div>
                <p className="mg-section-desc">
                  Side-by-side component availability across UX4G Android Compose and standard Material 3 (M3) Jetpack Compose.
                </p>
              </div>
            </div>

            {/* Filter Bar */}
            <div className="mg-filter-bar">
              <div className="mg-search-wrapper">
                <span className="material-symbols-outlined mg-search-icon">search</span>
                <input
                  type="text"
                  placeholder="Search component name or composable..."
                  value={compSearch}
                  onChange={(e) => setCompSearch(e.target.value)}
                  className="mg-search-input"
                />
              </div>

              <select
                value={compFilter}
                onChange={(e) => setCompFilter(e.target.value)}
                className="mg-select"
              >
                <option value="all">All Components</option>
                <option value="both">In Both Libraries</option>
                <option value="has-ux4g">Has UX4G</option>
                <option value="ux4g-only">UX4G Exclusive</option>
                <option value="has-m3">Has Material 3 (M3)</option>
                <option value="only-m3">Only Material 3 (M3)</option>
                <option value="partial">Partial / Alternative</option>
              </select>
            </div>

            {/* Table */}
            <div className="mg-table-wrapper">
              <table className="mg-table">
                <thead>
                  <tr>
                    <th style={{ width: '6%' }}>#</th>
                    <th style={{ width: '28%' }}>Component Name</th>
                    <th className="mg-th-ux4g" style={{ width: '33%' }}>UX4G Android (Compose)</th>
                    <th className="mg-th-bs" style={{ width: '33%' }}>Material 3 (M3 Compose)</th>
                  </tr>
                </thead>
                <tbody>
                  {filteredComponents.map((item) => (
                    <tr key={item.id}>
                      <td>{item.id}</td>
                      <td>
                        <div style={{ fontWeight: 600 }}>{item.name}</div>
                        <div style={{ fontSize: 11, color: '#64748b' }}>
                          {item.category}
                        </div>
                      </td>
                      <td dangerouslySetInnerHTML={{ __html: item.ux4gHtml }} />
                      <td dangerouslySetInnerHTML={{ __html: item.m3Html }} />
                    </tr>
                  ))}
                </tbody>
              </table>
            </div>
          </section>
        )}

        {/* TAB 2: PROPS & API MAPPING */}
        {activeTab === 'props-mapping' && (
          <section>
            <div className="mg-section-header">
              <div>
                <div className="mg-section-title-row">
                  <h2 className="mg-section-heading">Props & Composable Parameter Mapping</h2>
                  <span className="mg-count-pill">
                    {filteredProps.length} of {ANDROID_PROPS_MAPPING_DATA.length} Parameters ({uniquePropComponents.length} Components)
                  </span>
                </div>
                <p className="mg-section-desc">
                  Direct composable parameter translation reference between Material 3 (M3 Compose) and UX4G Android components.
                </p>
              </div>
            </div>

            {/* Component Filter Pills */}
            <div className="mg-pill-toolbar">
              <button
                type="button"
                onClick={() => setSelectedCompFilter('all')}
                className={`mg-pill-btn ${selectedCompFilter === 'all' ? 'is-active' : ''}`}
              >
                All Components ({ANDROID_PROPS_MAPPING_DATA.length})
              </button>
              {uniquePropComponents.map((comp) => {
                const count = ANDROID_PROPS_MAPPING_DATA.filter((p) => p.component === comp).length;
                return (
                  <button
                    key={comp}
                    type="button"
                    onClick={() => setSelectedCompFilter(comp)}
                    className={`mg-pill-btn ${selectedCompFilter === comp ? 'is-active' : ''}`}
                  >
                    {comp} ({count})
                  </button>
                );
              })}
            </div>

            {/* Filter Bar */}
            <div className="mg-filter-bar">
              <div className="mg-search-wrapper">
                <span className="material-symbols-outlined mg-search-icon">search</span>
                <input
                  type="text"
                  placeholder="Search parameter, component, or description..."
                  value={propSearch}
                  onChange={(e) => setPropSearch(e.target.value)}
                  className="mg-search-input"
                />
              </div>
            </div>

            {/* Table */}
            <div className="mg-table-wrapper">
              <table className="mg-table">
                <thead>
                  <tr>
                    <th style={{ width: '15%' }}>Component</th>
                    <th style={{ width: '15%' }}>Prop Area</th>
                    <th className="mg-th-ux4g" style={{ width: '26%' }}>UX4G Android Parameter</th>
                    <th className="mg-th-bs" style={{ width: '24%' }}>M3 Compose Equivalent</th>
                    <th style={{ width: '20%' }}>Description & Migration</th>
                  </tr>
                </thead>
                <tbody>
                  {filteredProps.map((item, idx) => (
                    <tr key={idx}>
                      <td style={{ fontWeight: 600 }}>{item.component}</td>
                      <td>
                        <span className="mg-count-pill" style={{ fontSize: '11px' }}>
                          {item.propGroup}
                        </span>
                      </td>
                      <td>
                        <code className="mg-css-code">{item.ux4gProp}</code>
                      </td>
                      <td>
                        <code className="mg-css-code" style={{ color: '#7952b3' }}>
                          {item.m3Prop}
                        </code>
                      </td>
                      <td style={{ fontSize: 13 }}>
                        {item.description}
                      </td>
                    </tr>
                  ))}
                </tbody>
              </table>
            </div>
          </section>
        )}

        {/* TAB 3: TOKENS & THEMING */}
        {activeTab === 'tokens' && (
          <section>
            <div className="mg-section-header">
              <div>
                <h2 className="mg-section-heading">Tokens & Theming Parity</h2>
                <p className="mg-section-desc">
                  Complete design tokens calibration for Android Jetpack Compose: Colors, Typography Scale, Dimensions/Spacing, and WCAG AA contrast.
                </p>
              </div>
            </div>

            {/* Metric Cards Dashboard */}
            <div className="mg-stats-grid">
              <div className="mg-stat-card">
                <div className="mg-stat-card__top">
                  <span className="mg-stat-card__title">M3 Compose Parity</span>
                  <div className="mg-stat-card__icon">
                    <span className="material-symbols-outlined">layers</span>
                  </div>
                </div>
                <div className="mg-stat-card__pct">{ANDROID_TOKENS_DATA.stats.m3Parity.pct}</div>
                <div className="mg-stat-card__label">{ANDROID_TOKENS_DATA.stats.m3Parity.label}</div>
                <p className="mg-stat-card__desc">{ANDROID_TOKENS_DATA.stats.m3Parity.text}</p>
                <div className="mg-progress-track">
                  <div className="mg-progress-fill" style={{ width: ANDROID_TOKENS_DATA.stats.m3Parity.pct }} />
                </div>
              </div>

              <div className="mg-stat-card">
                <div className="mg-stat-card__top">
                  <span className="mg-stat-card__title">Jetpack Compose Native</span>
                  <div className="mg-stat-card__icon">
                    <span className="material-symbols-outlined">android</span>
                  </div>
                </div>
                <div className="mg-stat-card__pct">{ANDROID_TOKENS_DATA.stats.composeNative.pct}</div>
                <div className="mg-stat-card__label">{ANDROID_TOKENS_DATA.stats.composeNative.label}</div>
                <p className="mg-stat-card__desc">{ANDROID_TOKENS_DATA.stats.composeNative.text}</p>
                <div className="mg-progress-track">
                  <div className="mg-progress-fill" style={{ width: '100%' }} />
                </div>
              </div>

              <div className="mg-stat-card">
                <div className="mg-stat-card__top">
                  <span className="mg-stat-card__title">Government Accessibility</span>
                  <div className="mg-stat-card__icon">
                    <span className="material-symbols-outlined">verified</span>
                  </div>
                </div>
                <div className="mg-stat-card__pct">{ANDROID_TOKENS_DATA.stats.a11yCompliance.pct}</div>
                <div className="mg-stat-card__label">{ANDROID_TOKENS_DATA.stats.a11yCompliance.label}</div>
                <p className="mg-stat-card__desc">{ANDROID_TOKENS_DATA.stats.a11yCompliance.text}</p>
                <div className="mg-progress-track">
                  <div className="mg-progress-fill" style={{ width: '100%' }} />
                </div>
              </div>

              <div className="mg-stat-card">
                <div className="mg-stat-card__top">
                  <span className="mg-stat-card__title">State & API Consistency</span>
                  <div className="mg-stat-card__icon">
                    <span className="material-symbols-outlined">sync</span>
                  </div>
                </div>
                <div className="mg-stat-card__pct">{ANDROID_TOKENS_DATA.stats.stateParity.pct}</div>
                <div className="mg-stat-card__label">{ANDROID_TOKENS_DATA.stats.stateParity.label}</div>
                <p className="mg-stat-card__desc">{ANDROID_TOKENS_DATA.stats.stateParity.text}</p>
                <div className="mg-progress-track">
                  <div className="mg-progress-fill" style={{ width: '100%' }} />
                </div>
              </div>
            </div>

            {/* Colors Table */}
            <div style={{ marginTop: '2.5rem' }}>
              <div className="mg-matrix-card" style={{ marginBottom: '2rem' }}>
                <div className="mg-matrix-header">
                  <div style={{ display: 'flex', alignItems: 'center', gap: '8px' }}>
                    <div style={{ width: '8px', height: '8px', borderRadius: '9999px', backgroundColor: '#5b3ae6' }} />
                    <h3 style={{ fontSize: '14px', fontWeight: 700, margin: 0 }}>
                      1. Color Tokens & Theme Mapping (WCAG 2.1 AA)
                    </h3>
                  </div>
                  <span className="mg-count-pill" style={{ fontSize: '11px' }}>Theme Colors</span>
                </div>
                <div className="mg-table-wrapper">
                  <table className="mg-table">
                    <thead>
                      <tr>
                        <th>UX4G Compose Token</th>
                        <th>M3 Compose Equivalent</th>
                        <th>Color Preview & Hex</th>
                        <th>Functional Role</th>
                      </tr>
                    </thead>
                    <tbody>
                      {ANDROID_TOKENS_DATA.colorTokens.map((c, idx) => (
                        <tr key={idx}>
                          <td><code>{c.token}</code></td>
                          <td><code>{c.m3}</code></td>
                          <td>
                            <div style={{ display: 'flex', alignItems: 'center', gap: '8px' }}>
                              <div style={{ width: 16, height: 16, borderRadius: 4, backgroundColor: c.hex, border: '1px solid rgba(0,0,0,0.1)' }} />
                              <code>{c.hex}</code>
                            </div>
                          </td>
                          <td style={{ fontSize: 13 }}>{c.role}</td>
                        </tr>
                      ))}
                    </tbody>
                  </table>
                </div>
              </div>

              {/* Typography Scale */}
              <div className="mg-matrix-card" style={{ marginBottom: '2rem' }}>
                <div className="mg-matrix-header">
                  <div style={{ display: 'flex', alignItems: 'center', gap: '8px' }}>
                    <div style={{ width: '8px', height: '8px', borderRadius: '9999px', backgroundColor: '#5b3ae6' }} />
                    <h3 style={{ fontSize: '14px', fontWeight: 700, margin: 0 }}>
                      2. Typography Scale (Multilingual Indian Script Optimized)
                    </h3>
                  </div>
                  <span className="mg-count-pill" style={{ fontSize: '11px' }}>Typography</span>
                </div>
                <div className="mg-table-wrapper">
                  <table className="mg-table">
                    <thead>
                      <tr>
                        <th>Style Level</th>
                        <th>Font Size</th>
                        <th>Line Height</th>
                        <th>Font Weight</th>
                        <th>Usage</th>
                      </tr>
                    </thead>
                    <tbody>
                      {ANDROID_TOKENS_DATA.typeScale.map((t, idx) => (
                        <tr key={idx}>
                          <td style={{ fontWeight: 600 }}>{t.level}</td>
                          <td><code>{t.fontSize}</code></td>
                          <td><code>{t.lineHeight}</code></td>
                          <td>{t.weight}</td>
                          <td style={{ fontSize: 13 }}>{t.usage}</td>
                        </tr>
                      ))}
                    </tbody>
                  </table>
                </div>
              </div>

              {/* Spacing Scale */}
              <div className="mg-matrix-card">
                <div className="mg-matrix-header">
                  <div style={{ display: 'flex', alignItems: 'center', gap: '8px' }}>
                    <div style={{ width: '8px', height: '8px', borderRadius: '9999px', backgroundColor: '#5b3ae6' }} />
                    <h3 style={{ fontSize: '14px', fontWeight: 700, margin: 0 }}>
                      3. Spacing Scale & Dimensions (4dp Grid)
                    </h3>
                  </div>
                  <span className="mg-count-pill" style={{ fontSize: '11px' }}>Spacing Scale</span>
                </div>
                <div className="mg-table-wrapper">
                  <table className="mg-table">
                    <thead>
                      <tr>
                        <th>Token</th>
                        <th>DP Value</th>
                        <th>Application Usage</th>
                      </tr>
                    </thead>
                    <tbody>
                      {ANDROID_TOKENS_DATA.spacingScale.map((s, idx) => (
                        <tr key={idx}>
                          <td><code>{s.token}</code></td>
                          <td><code>{s.dp}</code></td>
                          <td style={{ fontSize: 13 }}>{s.usage}</td>
                        </tr>
                      ))}
                    </tbody>
                  </table>
                </div>
              </div>
            </div>
          </section>
        )}

        {/* TAB 4: GOV-EXCLUSIVE MODULES */}
        {activeTab === 'gov-exclusive' && (
          <section>
            <div className="mg-section-header">
              <div>
                <div className="mg-section-title-row">
                  <h2 className="mg-section-heading">Government-Exclusive Native Modules</h2>
                  <span className="mg-count-pill">8 Exclusive Components</span>
                </div>
                <p className="mg-section-desc">
                  High-value UI composables built specifically for digital governance, citizen verification, and public service apps on Android.
                </p>
              </div>
            </div>

            <div style={{ display: 'grid', gridTemplateColumns: 'repeat(auto-fit, minmax(300px, 1fr))', gap: '1.25rem' }}>
              <div className="mg-stat-card">
                <div className="mg-stat-card__top">
                  <span className="mg-stat-card__title">Aadhaar Input Field</span>
                  <div className="mg-stat-card__icon"><span className="material-symbols-outlined">fingerprint</span></div>
                </div>
                <p className="mg-stat-card__desc" style={{ marginTop: '0.75rem' }}>
                  Auto-formatting 12-digit input with built-in Verhoeff checksum algorithm and masked peek toggle.
                </p>
                <Link href="/components/aadhaar-input" className="mg-page-btn" style={{ marginTop: '1rem', width: 'max-content' }}>
                  Explore Aadhaar Input &rarr;
                </Link>
              </div>

              <div className="mg-stat-card">
                <div className="mg-stat-card__top">
                  <span className="mg-stat-card__title">PAN Card Input Field</span>
                  <div className="mg-stat-card__icon"><span className="material-symbols-outlined">credit_card</span></div>
                </div>
                <p className="mg-stat-card__desc" style={{ marginTop: '0.75rem' }}>
                  10-character alphanumeric PAN validator with uppercase formatting and structural syntax checks.
                </p>
                <Link href="/components/pan-input" className="mg-page-btn" style={{ marginTop: '1rem', width: 'max-content' }}>
                  Explore PAN Input &rarr;
                </Link>
              </div>

              <div className="mg-stat-card">
                <div className="mg-stat-card__top">
                  <span className="mg-stat-card__title">Secure OTP Box</span>
                  <div className="mg-stat-card__icon"><span className="material-symbols-outlined">password</span></div>
                </div>
                <p className="mg-stat-card__desc" style={{ marginTop: '0.75rem' }}>
                  4/6-digit secure PIN inputs with auto-focus, SMS User Consent auto-fill, and resend timer.
                </p>
                <Link href="/components/otp-input" className="mg-page-btn" style={{ marginTop: '1rem', width: 'max-content' }}>
                  Explore OTP Input &rarr;
                </Link>
              </div>

              <div className="mg-stat-card">
                <div className="mg-stat-card__top">
                  <span className="mg-stat-card__title">Journey Timeline</span>
                  <div className="mg-stat-card__icon"><span className="material-symbols-outlined">timeline</span></div>
                </div>
                <p className="mg-stat-card__desc" style={{ marginTop: '0.75rem' }}>
                  Multi-stage citizen application tracker (Submitted &rarr; In Review &rarr; Approved) with timestamps.
                </p>
                <Link href="/components/journey-timeline" className="mg-page-btn" style={{ marginTop: '1rem', width: 'max-content' }}>
                  Explore Journey Timeline &rarr;
                </Link>
              </div>

              <div className="mg-stat-card">
                <div className="mg-stat-card__top">
                  <span className="mg-stat-card__title">Status Pipeline</span>
                  <div className="mg-stat-card__icon"><span className="material-symbols-outlined">alt_route</span></div>
                </div>
                <p className="mg-stat-card__desc" style={{ marginTop: '0.75rem' }}>
                  Linear workflow stage pipeline showing Department Verification and Payment Disbursal.
                </p>
                <Link href="/components/status-pipeline" className="mg-page-btn" style={{ marginTop: '1rem', width: 'max-content' }}>
                  Explore Status Pipeline &rarr;
                </Link>
              </div>

              <div className="mg-stat-card">
                <div className="mg-stat-card__top">
                  <span className="mg-stat-card__title">Progress SLA Indicator</span>
                  <div className="mg-stat-card__icon"><span className="material-symbols-outlined">timer</span></div>
                </div>
                <p className="mg-stat-card__desc" style={{ marginTop: '0.75rem' }}>
                  SLA timeline dial displaying on-track, due-soon, and breached service SLA status.
                </p>
                <Link href="/components/progress-sla-indicator" className="mg-page-btn" style={{ marginTop: '1rem', width: 'max-content' }}>
                  Explore SLA Indicator &rarr;
                </Link>
              </div>

              <div className="mg-stat-card">
                <div className="mg-stat-card__top">
                  <span className="mg-stat-card__title">Biometric Authentication</span>
                  <div className="mg-stat-card__icon"><span className="material-symbols-outlined">lock</span></div>
                </div>
                <p className="mg-stat-card__desc" style={{ marginTop: '0.75rem' }}>
                  Ready-to-use BiometricPrompt dialog with fingerprint & face unlock sensor integration.
                </p>
                <Link href="/components/biometric" className="mg-page-btn" style={{ marginTop: '1rem', width: 'max-content' }}>
                  Explore Biometric Auth &rarr;
                </Link>
              </div>

              <div className="mg-stat-card">
                <div className="mg-stat-card__top">
                  <span className="mg-stat-card__title">Time Slot Picker</span>
                  <div className="mg-stat-card__icon"><span className="material-symbols-outlined">calendar_month</span></div>
                </div>
                <p className="mg-stat-card__desc" style={{ marginTop: '0.75rem' }}>
                  Citizen appointment slot booking grid with Available, Fast-filling, and Booked states.
                </p>
                <Link href="/components/time-slot" className="mg-page-btn" style={{ marginTop: '1rem', width: 'max-content' }}>
                  Explore Time Slot &rarr;
                </Link>
              </div>
            </div>
          </section>
        )}
      </main>
    </div>
  );
}
