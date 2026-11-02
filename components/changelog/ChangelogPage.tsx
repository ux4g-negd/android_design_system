'use client';

import React, { useState, useMemo } from 'react';
import Link from 'next/link';
import {
  ANDROID_CHANGELOG_DATA,
  type ChangelogRelease,
  type ChangelogCategory,
  type ChangelogCategoryType,
} from './changelogData';

export function ChangelogPage() {
  const [selectedVersion, setSelectedVersion] = useState<string>('all');
  const [selectedCategory, setSelectedCategory] = useState<string>('all');
  const [searchQuery, setSearchQuery] = useState<string>('');
  const [syntaxMode, setSyntaxMode] = useState<'kotlin' | 'groovy'>('kotlin');
  const [copiedKey, setCopiedKey] = useState<string | null>(null);

  const handleCopy = (text: string, key: string) => {
    navigator.clipboard.writeText(text);
    setCopiedKey(key);
    setTimeout(() => {
      setCopiedKey((curr) => (curr === key ? null : curr));
    }, 2200);
  };

  const resetFilters = () => {
    setSelectedVersion('all');
    setSelectedCategory('all');
    setSearchQuery('');
  };

  const latestRelease = ANDROID_CHANGELOG_DATA.find((r) => r.isLatest) || ANDROID_CHANGELOG_DATA[0];

  // Filtering logic
  const filteredReleases = useMemo(() => {
    let list = ANDROID_CHANGELOG_DATA;

    // Filter by version
    if (selectedVersion !== 'all') {
      list = list.filter((r) => r.version === selectedVersion);
    }

    const query = searchQuery.trim().toLowerCase();

    // Filter by search query
    if (query.length > 0) {
      list = list
        .map((release) => {
          const versionMatches = release.version.toLowerCase().includes(query);
          const titleMatches = release.title.toLowerCase().includes(query);
          const overviewMatches = release.overview.toLowerCase().includes(query);
          const highlightMatches = release.highlights.some((h) => h.toLowerCase().includes(query));

          const filteredCategories = release.categories
            .map((cat) => {
              const catTypeMatches = cat.type.toLowerCase().includes(query);
              const catLabelMatches = cat.label.toLowerCase().includes(query);

              const filteredItems = cat.items.filter((item) => {
                const compMatches = item.component.toLowerCase().includes(query);
                const sumMatches = item.summary.toLowerCase().includes(query);
                const detailMatches = item.details.some((d) => d.toLowerCase().includes(query));
                return compMatches || sumMatches || detailMatches;
              });

              if (filteredItems.length > 0 || catTypeMatches || catLabelMatches) {
                return {
                  ...cat,
                  items: filteredItems.length > 0 ? filteredItems : cat.items,
                };
              }
              return null;
            })
            .filter((cat): cat is ChangelogCategory => cat !== null);

          if (versionMatches || titleMatches || overviewMatches || highlightMatches || filteredCategories.length > 0) {
            return {
              ...release,
              categories: filteredCategories.length > 0 ? filteredCategories : release.categories,
            };
          }
          return null;
        })
        .filter((r): r is ChangelogRelease => r !== null);
    }

    // Filter by category
    if (selectedCategory !== 'all') {
      list = list
        .map((release) => {
          const matchingCategories = release.categories.filter((cat) => {
            const catType = cat.type.toLowerCase();
            const sel = selectedCategory.toLowerCase();
            if (sel === 'fixed') return catType === 'fixed' || cat.label.toLowerCase().includes('fix');
            if (sel === 'added') return catType === 'added' || cat.label.toLowerCase().includes('add');
            if (sel === 'enhanced') return catType === 'enhanced' || cat.label.toLowerCase().includes('enhanc');
            if (sel === 'changed') return catType === 'changed' || cat.label.toLowerCase().includes('change');
            return catType === sel;
          });

          if (matchingCategories.length > 0) {
            return {
              ...release,
              categories: matchingCategories,
            };
          }
          return null;
        })
        .filter((r): r is ChangelogRelease => r !== null);
    }

    return list;
  }, [selectedVersion, selectedCategory, searchQuery]);

  // Compute total items count in filtered releases
  const totalItemsCount = useMemo(() => {
    return filteredReleases.reduce((acc, r) => {
      return acc + r.categories.reduce((cAcc, cat) => cAcc + cat.items.length, 0);
    }, 0);
  }, [filteredReleases]);

  const showSpotlight =
    (selectedVersion === 'all' || selectedVersion === latestRelease.version) &&
    searchQuery === '' &&
    selectedCategory === 'all';

  const isFiltered = selectedVersion !== 'all' || selectedCategory !== 'all' || searchQuery.trim() !== '';

  const getTagBadgeClass = (tag: string) => {
    switch (tag.toLowerCase()) {
      case 'latest':
        return 'bg-emerald-50 dark:bg-emerald-950/40 text-emerald-700 dark:text-emerald-400 border-emerald-200 dark:border-emerald-800/50';
      case 'major':
        return 'bg-[#EDE9FE] dark:bg-[#312E81]/50 text-[#6D28D9] dark:text-[#C4B5FD] border-[#DDD6FE] dark:border-[#6366F1]/30';
      case 'minor':
      case 'maintenance':
      case 'patch':
        return 'bg-blue-50 dark:bg-blue-950/40 text-blue-700 dark:text-blue-400 border-blue-200 dark:border-blue-800/50';
      case 'initial':
        return 'bg-slate-100 dark:bg-slate-800 text-slate-700 dark:text-slate-300 border-slate-200 dark:border-slate-700';
      default:
        return 'bg-slate-100 dark:bg-slate-800 text-slate-600 dark:text-slate-400 border-slate-200 dark:border-slate-700';
    }
  };

  const getCategoryChipClass = (catKey: string, activeColor: 'primary' | 'emerald' | 'amber' | 'blue') => {
    const isSelected = selectedCategory.toLowerCase() === catKey.toLowerCase();
    if (!isSelected) {
      return 'border-[#E2E8F0] dark:border-[#334155] text-slate-600 dark:text-slate-400 hover:text-slate-900 dark:hover:text-white hover:bg-slate-100 dark:hover:bg-slate-800 bg-transparent';
    }

    switch (activeColor) {
      case 'primary':
        return 'bg-[#EDE9FE] dark:bg-[#312E81]/60 text-[#4F46E5] dark:text-indigo-300 border-[#C7D2FE] dark:border-indigo-500/50 font-bold';
      case 'emerald':
        return 'bg-emerald-50 dark:bg-emerald-950/50 text-emerald-700 dark:text-emerald-300 border-emerald-300 dark:border-emerald-700/60 font-bold';
      case 'amber':
        return 'bg-amber-50 dark:bg-amber-950/50 text-amber-700 dark:text-amber-300 border-amber-300 dark:border-amber-700/60 font-bold';
      case 'blue':
        return 'bg-blue-50 dark:bg-blue-950/50 text-blue-700 dark:text-blue-300 border-blue-300 dark:border-blue-700/60 font-bold';
    }
  };

  const getCategoryBlockHeaderStyle = (type: ChangelogCategoryType) => {
    switch (type.toLowerCase()) {
      case 'fixed':
        return {
          badge: 'bg-emerald-50 dark:bg-emerald-950/40 text-emerald-700 dark:text-emerald-400 border-emerald-200 dark:border-emerald-800/50',
          dot: 'bg-emerald-500',
        };
      case 'added':
        return {
          badge: 'bg-[#EDE9FE] dark:bg-[#312E81]/50 text-[#6D28D9] dark:text-[#C4B5FD] border-[#DDD6FE] dark:border-[#6366F1]/30',
          dot: 'bg-[#4F46E5]',
        };
      case 'enhanced':
        return {
          badge: 'bg-amber-50 dark:bg-amber-950/40 text-amber-700 dark:text-amber-400 border-amber-200 dark:border-amber-800/50',
          dot: 'bg-amber-500',
        };
      default:
        return {
          badge: 'bg-blue-50 dark:bg-blue-950/40 text-blue-700 dark:text-blue-400 border-blue-200 dark:border-blue-800/50',
          dot: 'bg-blue-500',
        };
    }
  };

  return (
    <div className="w-full max-w-6xl mx-auto px-4 sm:px-6 lg:px-8 py-8 md:py-12 text-slate-900 dark:text-white space-y-8 font-sans">
      {/* ── 1. Header ────────────────────────────────────────────── */}
      <div className="space-y-4">
        {/* Top Badges Row */}
        <div className="flex flex-wrap items-center gap-2.5">
          {/* UX4G Design System badge with live pulse dot */}
          <div className="inline-flex items-center gap-2 px-3 py-1 rounded-full text-xs font-semibold bg-[#F8FAFC] dark:bg-[#1E293B] border border-[#E2E8F0] dark:border-[#334155] text-slate-700 dark:text-slate-300 shadow-xs">
            <span className="relative flex h-2 w-2">
              <span className="animate-ping absolute inline-flex h-full w-full rounded-full bg-emerald-400 opacity-75"></span>
              <span className="relative inline-flex rounded-full h-2 w-2 bg-emerald-500"></span>
            </span>
            UX4G Design System
          </div>

          {/* Latest Version Pill */}
          <div className="inline-flex items-center gap-1.5 px-3 py-1 rounded-full text-xs font-bold bg-emerald-50 dark:bg-emerald-950/40 text-emerald-700 dark:text-emerald-400 border border-emerald-200 dark:border-emerald-800/50 shadow-xs">
            <svg
              className="w-3.5 h-3.5 text-emerald-600 dark:text-emerald-400"
              viewBox="0 0 20 20"
              fill="currentColor"
            >
              <path
                fillRule="evenodd"
                d="M10 18a8 8 0 100-16 8 8 0 000 16zm3.857-9.809a.75.75 0 00-1.214-.882l-3.483 4.79-1.88-1.88a.75.75 0 10-1.06 1.061l2.5 2.5a.75.75 0 001.137-.089l4-5.5z"
                clipRule="evenodd"
              />
            </svg>
            Latest v{latestRelease.version}
          </div>

          {/* Android Jetpack Compose tag */}
          <div className="inline-flex items-center gap-1.5 px-3 py-1 rounded-full text-xs font-semibold bg-[#EDE9FE] dark:bg-[#312E81]/50 text-[#6D28D9] dark:text-[#C4B5FD] border border-[#DDD6FE] dark:border-[#6366F1]/30 shadow-xs">
            <svg className="w-3.5 h-3.5" viewBox="0 0 24 24" fill="currentColor">
              <path d="M17.523 15.3414c-.5511 0-.9993-.4486-.9993-1.0001s.4482-1.0001.9993-1.0001c.5516 0 .9997.4486.9997 1.0001s-.4481 1.0001-.9997 1.0001m-11.046 0c-.5511 0-.9993-.4486-.9993-1.0001s.4482-1.0001.9993-1.0001c.5516 0 .9997.4486.9997 1.0001s-.4481 1.0001-.9997 1.0001m11.4045-6.02l1.9973-3.4592a.416.416 0 00-.1523-.5676.416.416 0 00-.5676.1523l-2.0223 3.503C15.5902 8.4116 13.8533 8.125 12 8.125c-1.8533 0-3.5902.2866-5.1366.8249L4.8411 5.4469a.416.416 0 00-.5676-.1523.416.416 0 00-.1523.5676l1.9973 3.4592C2.6889 11.1867.3432 14.6589 0 18.75h24c-.3432-4.0911-2.6889-7.5633-6.1185-9.4286" />
            </svg>
            Android Jetpack Compose
          </div>
        </div>

        {/* Title */}
        <h1 className="text-3xl sm:text-4xl lg:text-5xl font-extrabold tracking-tight text-slate-900 dark:text-white">
          Changes Logs
        </h1>

        {/* Subtitle */}
        <p className="text-base sm:text-lg text-slate-600 dark:text-slate-300 max-w-3xl leading-relaxed">
          Keep track of recent updates, new components, bug fixes, enhancements, and breaking changes across every release of the UX4G Android (Jetpack Compose) component library.
        </p>

        {/* Dependency Command Snippet */}
        <div className="pt-2 flex flex-wrap items-center gap-3">
          <div className="inline-flex items-center rounded-lg border border-[#E2E8F0] dark:border-[#334155] bg-white dark:bg-[#1E293B] p-1 shadow-xs">
            <button
              onClick={() => setSyntaxMode('kotlin')}
              className={`px-2.5 py-1 text-xs font-semibold rounded-md transition-colors cursor-pointer ${
                syntaxMode === 'kotlin'
                  ? 'bg-[#4F46E5] text-white shadow-xs'
                  : 'text-slate-600 dark:text-slate-400 hover:text-slate-900 dark:hover:text-white'
              }`}
            >
              Kotlin DSL
            </button>
            <button
              onClick={() => setSyntaxMode('groovy')}
              className={`px-2.5 py-1 text-xs font-semibold rounded-md transition-colors cursor-pointer ${
                syntaxMode === 'groovy'
                  ? 'bg-[#4F46E5] text-white shadow-xs'
                  : 'text-slate-600 dark:text-slate-400 hover:text-slate-900 dark:hover:text-white'
              }`}
            >
              Groovy
            </button>
          </div>

          <div className="inline-flex items-center gap-2 px-3 py-1.5 rounded-lg border border-[#E2E8F0] dark:border-[#334155] bg-[#F8FAFC] dark:bg-[#0F172A] shadow-xs text-xs sm:text-sm font-mono text-slate-800 dark:text-slate-200">
            <span>
              {syntaxMode === 'kotlin' ? latestRelease.installCommand : latestRelease.groovyCommand}
            </span>
            <span className="h-4 w-px bg-[#E2E8F0] dark:bg-[#334155]" />
            <button
              onClick={() =>
                handleCopy(
                  syntaxMode === 'kotlin' ? latestRelease.installCommand : latestRelease.groovyCommand || '',
                  'header-cmd'
                )
              }
              className="inline-flex items-center gap-1 font-sans text-xs font-semibold text-slate-600 dark:text-slate-400 hover:text-[#4F46E5] dark:hover:text-indigo-400 transition-colors cursor-pointer"
              title="Copy to clipboard"
            >
              {copiedKey === 'header-cmd' ? (
                <>
                  <svg className="w-3.5 h-3.5 text-emerald-600" viewBox="0 0 20 20" fill="currentColor">
                    <path
                      fillRule="evenodd"
                      d="M16.707 5.293a1 1 0 010 1.414l-8 8a1 1 0 01-1.414 0l-4-4a1 1 0 011.414-1.414L8 12.586l7.293-7.293a1 1 0 011.414 0z"
                      clipRule="evenodd"
                    />
                  </svg>
                  <span className="text-emerald-600 font-bold">Copied!</span>
                </>
              ) : (
                <>
                  <svg className="w-3.5 h-3.5" viewBox="0 0 24 24" fill="none" stroke="currentColor" strokeWidth="2">
                    <rect x="9" y="9" width="13" height="13" rx="2" ry="2" />
                    <path d="M5 15H4a2 2 0 0 1-2-2V4a2 2 0 0 1 2-2h9a2 2 0 0 1 2 2v1" />
                  </svg>
                  <span>Copy</span>
                </>
              )}
            </button>
          </div>
        </div>
      </div>

      {/* ── 2. Spotlight Card (Latest Version Highlights) ─────────── */}
      {showSpotlight && (
        <div className="relative overflow-hidden rounded-2xl border border-[#E2E8F0] dark:border-[#334155] bg-white dark:bg-[#1E293B] p-6 sm:p-8 shadow-sm space-y-6">
          <div className="flex flex-wrap items-center justify-between gap-3">
            <div className="inline-flex items-center gap-1.5 px-3 py-1 rounded-full text-xs font-bold bg-[#EDE9FE] dark:bg-[#312E81]/50 text-[#6D28D9] dark:text-[#C4B5FD] border border-[#DDD6FE] dark:border-[#6366F1]/30">
              <svg className="w-3.5 h-3.5 fill-current" viewBox="0 0 20 20">
                <path d="M9.049 2.927c.3-.921 1.603-.921 1.902 0l1.07 3.292a1 1 0 00.95.69h3.462c.969 0 1.371 1.24.588 1.81l-2.8 2.034a1 1 0 00-.364 1.118l1.07 3.292c.3.921-.755 1.688-1.54 1.118l-2.8-2.034a1 1 0 00-1.175 0l-2.8 2.034c-.784.57-1.838-.197-1.539-1.118l1.07-3.292a1 1 0 00-.364-1.118L2.98 8.72c-.783-.57-.38-1.81.588-1.81h3.461a1 1 0 00.951-.69l1.07-3.292z" />
              </svg>
              Latest Version Highlights • v{latestRelease.version}
            </div>
            <div className="text-xs sm:text-sm font-medium text-slate-500 dark:text-slate-400">
              Released {latestRelease.relativeDate} ({latestRelease.date})
            </div>
          </div>

          <div className="space-y-2">
            <h2 className="text-xl sm:text-2xl font-bold tracking-tight text-slate-900 dark:text-white">
              {latestRelease.title}
            </h2>
            <p className="text-sm sm:text-base text-slate-600 dark:text-slate-300 leading-relaxed">
              {latestRelease.overview}
            </p>
          </div>

          {/* Highlights grid */}
          {latestRelease.highlights.length > 0 && (
            <div className="grid grid-cols-1 md:grid-cols-2 gap-3 pt-2">
              {latestRelease.highlights.map((highlight, idx) => (
                <div key={idx} className="flex items-start gap-2.5 text-xs sm:text-sm text-slate-700 dark:text-slate-200">
                  <span className="flex-shrink-0 mt-0.5 rounded-full p-0.5 bg-emerald-50 dark:bg-emerald-950/50 text-emerald-600 dark:text-emerald-400">
                    <svg className="w-3.5 h-3.5" viewBox="0 0 20 20" fill="currentColor">
                      <path
                        fillRule="evenodd"
                        d="M16.707 5.293a1 1 0 010 1.414l-8 8a1 1 0 01-1.414 0l-4-4a1 1 0 011.414-1.414L8 12.586l7.293-7.293a1 1 0 011.414 0z"
                        clipRule="evenodd"
                      />
                    </svg>
                  </span>
                  <span className="leading-snug">{highlight}</span>
                </div>
              ))}
            </div>
          )}
        </div>
      )}

      {/* ── 3. Controls & Filters Card ────────────────────────────── */}
      <div className="rounded-2xl border border-[#E2E8F0] dark:border-[#334155] bg-white dark:bg-[#1E293B] p-5 sm:p-6 shadow-sm space-y-4">
        {/* Row 1: Search Box + Version Dropdown */}
        <div className="flex flex-col sm:flex-row items-stretch sm:items-center gap-3">
          {/* Search Field */}
          <div className="flex-1 relative">
            <div className="absolute inset-y-0 left-0 pl-3.5 flex items-center pointer-events-none text-slate-400">
              <svg className="w-4 h-4" viewBox="0 0 24 24" fill="none" stroke="currentColor" strokeWidth="2">
                <circle cx="11" cy="11" r="8" />
                <line x1="21" y1="21" x2="16.65" y2="16.65" />
              </svg>
            </div>
            <input
              type="text"
              value={searchQuery}
              onChange={(e) => setSearchQuery(e.target.value)}
              placeholder="Search updates, components, or bug fixes..."
              className="w-full pl-9 pr-9 py-2 rounded-xl border border-[#E2E8F0] dark:border-[#334155] bg-[#F8FAFC] dark:bg-[#0F172A] text-slate-900 dark:text-white text-sm focus:outline-none focus:ring-2 focus:ring-[#4F46E5]/30 focus:border-[#4F46E5] transition-all placeholder:text-slate-400 dark:placeholder:text-slate-500"
            />
            {searchQuery && (
              <button
                onClick={() => setSearchQuery('')}
                className="absolute inset-y-0 right-0 pr-3 flex items-center text-slate-400 hover:text-slate-600 dark:hover:text-slate-200 cursor-pointer"
                title="Clear search"
              >
                <svg className="w-4 h-4" viewBox="0 0 20 20" fill="currentColor">
                  <path
                    fillRule="evenodd"
                    d="M10 18a8 8 0 100-16 8 8 0 000 16zM8.28 7.22a.75.75 0 00-1.06 1.06L8.94 10l-1.72 1.72a.75.75 0 101.06 1.06L10 11.06l1.72 1.72a.75.75 0 101.06-1.06L11.06 10l1.72-1.72a.75.75 0 00-1.06-1.06L10 8.94 8.28 7.22z"
                    clipRule="evenodd"
                  />
                </svg>
              </button>
            )}
          </div>

          {/* Version Dropdown */}
          <div className="sm:w-60">
            <select
              value={selectedVersion}
              onChange={(e) => setSelectedVersion(e.target.value)}
              className="w-full py-2 px-3 rounded-xl border border-[#E2E8F0] dark:border-[#334155] bg-[#F8FAFC] dark:bg-[#0F172A] text-slate-900 dark:text-white text-sm focus:outline-none focus:ring-2 focus:ring-[#4F46E5]/30 focus:border-[#4F46E5] transition-all cursor-pointer font-medium"
            >
              <option value="all">All Releases ({ANDROID_CHANGELOG_DATA.length})</option>
              {ANDROID_CHANGELOG_DATA.map((rel) => (
                <option key={rel.version} value={rel.version}>
                  v{rel.version} {rel.isLatest ? '(Latest)' : ''}
                </option>
              ))}
            </select>
          </div>
        </div>

        {/* Row 2: Version Pills Scroll Row (Clean scroll without native thick scrollbar) */}
        <div className="flex items-center gap-2 overflow-x-auto pb-1 pt-0.5 [scrollbar-width:none] [-ms-overflow-style:none] [&::-webkit-scrollbar]:hidden">
          {/* All Releases Pill */}
          <button
            onClick={() => setSelectedVersion('all')}
            className={`inline-flex items-center gap-1.5 px-3 py-1.5 rounded-full text-xs font-semibold whitespace-nowrap border transition-all cursor-pointer ${
              selectedVersion === 'all'
                ? 'bg-[#4F46E5] text-white border-[#4F46E5] shadow-xs'
                : 'bg-slate-100 dark:bg-slate-800 border-slate-200 dark:border-slate-700 text-slate-700 dark:text-slate-300 hover:bg-slate-200/70 dark:hover:bg-slate-700'
            }`}
          >
            All Releases
          </button>

          {/* Specific Version Pills */}
          {ANDROID_CHANGELOG_DATA.map((rel) => {
            const isSelected = selectedVersion === rel.version;
            return (
              <button
                key={rel.version}
                onClick={() => setSelectedVersion(rel.version)}
                className={`inline-flex items-center gap-1.5 px-3 py-1.5 rounded-full text-xs font-semibold whitespace-nowrap border transition-all cursor-pointer ${
                  isSelected
                    ? 'bg-[#4F46E5] text-white border-[#4F46E5] shadow-xs font-bold'
                    : 'bg-slate-100 dark:bg-slate-800 border-slate-200 dark:border-slate-700 text-slate-700 dark:text-slate-300 hover:bg-slate-200/70 dark:hover:bg-slate-700'
                }`}
              >
                <span>v{rel.version}</span>
                {rel.isLatest && (
                  <span
                    className={`px-1.5 py-0.2 rounded-full text-[10px] font-bold ${
                      isSelected
                        ? 'bg-white/25 text-white'
                        : 'bg-emerald-50 dark:bg-emerald-950/50 text-emerald-600 dark:text-emerald-400'
                    }`}
                  >
                    Latest
                  </span>
                )}
              </button>
            );
          })}
        </div>

        {/* Divider */}
        <div className="border-t border-[#E2E8F0] dark:border-[#334155] pt-3">
          {/* Row 3: Category Filter Chips & Status Summary */}
          <div className="flex flex-col sm:flex-row sm:items-center sm:justify-between gap-3">
            {/* Category Chips (Flex-wrap layout without ANY horizontal scrollbar) */}
            <div className="flex flex-wrap items-center gap-2">
              <span className="text-xs font-semibold text-slate-500 dark:text-slate-400 mr-1">
                Filter Category:
              </span>
              <button
                onClick={() => setSelectedCategory('all')}
                className={`px-3 py-1 rounded-lg text-xs border transition-all cursor-pointer ${getCategoryChipClass(
                  'all',
                  'primary'
                )}`}
              >
                All Updates
              </button>
              <button
                onClick={() => setSelectedCategory('Added')}
                className={`px-3 py-1 rounded-lg text-xs border transition-all cursor-pointer ${getCategoryChipClass(
                  'Added',
                  'primary'
                )}`}
              >
                Added
              </button>
              <button
                onClick={() => setSelectedCategory('Fixed')}
                className={`px-3 py-1 rounded-lg text-xs border transition-all cursor-pointer ${getCategoryChipClass(
                  'Fixed',
                  'emerald'
                )}`}
              >
                Fixed
              </button>
              <button
                onClick={() => setSelectedCategory('Enhanced')}
                className={`px-3 py-1 rounded-lg text-xs border transition-all cursor-pointer ${getCategoryChipClass(
                  'Enhanced',
                  'amber'
                )}`}
              >
                Enhanced
              </button>
              <button
                onClick={() => setSelectedCategory('Changed')}
                className={`px-3 py-1 rounded-lg text-xs border transition-all cursor-pointer ${getCategoryChipClass(
                  'Changed',
                  'blue'
                )}`}
              >
                Changed
              </button>
            </div>

            {/* Status summary & reset button */}
            <div className="flex items-center gap-3 text-xs text-slate-500 dark:text-slate-400 sm:ml-auto">
              <span>
                Showing <strong className="text-slate-900 dark:text-white">{filteredReleases.length}</strong> releases (
                <strong className="text-slate-900 dark:text-white">{totalItemsCount}</strong> items)
              </span>

              {isFiltered && (
                <button
                  onClick={resetFilters}
                  className="inline-flex items-center gap-1 text-[#4F46E5] dark:text-indigo-400 hover:underline font-semibold cursor-pointer"
                >
                  <svg className="w-3.5 h-3.5" viewBox="0 0 20 20" fill="currentColor">
                    <path
                      fillRule="evenodd"
                      d="M4.293 4.293a1 1 0 011.414 0L10 8.586l4.293-4.293a1 1 0 111.414 1.414L11.414 10l4.293 4.293a1 1 0 01-1.414 1.414L10 11.414l-4.293 4.293a1 1 0 01-1.414-1.414L8.586 10 4.293 5.707a1 1 0 010-1.414z"
                      clipRule="evenodd"
                    />
                  </svg>
                  Reset
                </button>
              )}
            </div>
          </div>
        </div>
      </div>

      {/* ── 4. Releases List / Timeline ───────────────────────────── */}
      {filteredReleases.length === 0 ? (
        /* Empty State */
        <div className="rounded-2xl border border-dashed border-[#E2E8F0] dark:border-[#334155] bg-white dark:bg-[#1E293B] p-12 text-center space-y-4">
          <div className="w-14 h-14 mx-auto rounded-full bg-slate-100 dark:bg-slate-800 flex items-center justify-center text-slate-400">
            <svg className="w-7 h-7" viewBox="0 0 24 24" fill="none" stroke="currentColor" strokeWidth="2">
              <circle cx="11" cy="11" r="8" />
              <line x1="21" y1="21" x2="16.65" y2="16.65" />
            </svg>
          </div>
          <div className="space-y-1">
            <h3 className="text-lg font-bold text-slate-900 dark:text-white">No matching changelog entries found</h3>
            <p className="text-sm text-slate-600 dark:text-slate-400 max-w-md mx-auto">
              We couldn&apos;t find any releases or component updates matching your current filters. Try searching for a different keyword or component name.
            </p>
          </div>
          <button
            onClick={resetFilters}
            className="inline-flex items-center gap-1.5 px-4 py-2 rounded-lg bg-[#4F46E5] text-white text-sm font-semibold hover:bg-[#4338CA] transition-colors shadow-xs cursor-pointer"
          >
            Reset Filters
          </button>
        </div>
      ) : (
        <div className="space-y-8">
          {filteredReleases.map((release) => {
            const copyKey = `card-dep-${release.version}`;
            const isCopied = copiedKey === copyKey;
            const depCode = syntaxMode === 'kotlin' ? release.installCommand : release.groovyCommand;

            return (
              <article
                key={release.version}
                id={`v${release.version}`}
                className={`rounded-2xl border bg-white dark:bg-[#1E293B] shadow-sm hover:shadow-md transition-shadow overflow-hidden ${
                  release.isLatest ? 'border-[#4F46E5]/40 dark:border-indigo-500/40 ring-1 ring-[#4F46E5]/20' : 'border-[#E2E8F0] dark:border-[#334155]'
                }`}
              >
                {/* Release Card Header Bar */}
                <div className="bg-[#F8FAFC] dark:bg-[#0F172A]/70 px-5 sm:px-7 py-4 border-b border-[#E2E8F0] dark:border-[#334155] flex flex-wrap items-center justify-between gap-3">
                  {/* Left: Version + Tag + Date */}
                  <div className="flex flex-wrap items-center gap-2.5 not-prose my-0">
                    <span className="text-xl sm:text-2xl font-black text-slate-900 dark:text-white tracking-tight leading-none flex items-center">
                      v{release.version}
                    </span>
                    <span
                      className={`inline-flex items-center justify-center px-2.5 py-1 rounded-full text-xs font-bold border leading-none shrink-0 ${getTagBadgeClass(
                        release.tag
                      )}`}
                    >
                      {release.tag}
                    </span>
                    <span className="text-xs sm:text-sm font-medium text-slate-500 dark:text-slate-400 inline-flex items-center gap-1.5 leading-none">
                      <span>•</span>
                      <span>Released {release.date} ({release.relativeDate})</span>
                    </span>
                  </div>

                  {/* Right: Quick Copy Dependency */}
                  <button
                    onClick={() => handleCopy(depCode || '', copyKey)}
                    className="inline-flex items-center gap-1.5 px-3 py-1 rounded-lg border border-[#E2E8F0] dark:border-[#334155] bg-white dark:bg-[#1E293B] hover:bg-slate-50 dark:hover:bg-slate-800 text-xs font-semibold text-slate-700 dark:text-slate-300 transition-colors shadow-2xs cursor-pointer"
                    title={`Copy ${syntaxMode === 'kotlin' ? 'Kotlin DSL' : 'Groovy'} dependency`}
                  >
                    {isCopied ? (
                      <>
                        <svg className="w-3.5 h-3.5 text-emerald-600" viewBox="0 0 20 20" fill="currentColor">
                          <path
                            fillRule="evenodd"
                            d="M16.707 5.293a1 1 0 010 1.414l-8 8a1 1 0 01-1.414 0l-4-4a1 1 0 011.414-1.414L8 12.586l7.293-7.293a1 1 0 011.414 0z"
                            clipRule="evenodd"
                          />
                        </svg>
                        <span className="text-emerald-600 font-bold">Copied!</span>
                      </>
                    ) : (
                      <>
                        <svg className="w-3.5 h-3.5 text-slate-400" viewBox="0 0 24 24" fill="none" stroke="currentColor" strokeWidth="2">
                          <rect x="9" y="9" width="13" height="13" rx="2" ry="2" />
                          <path d="M5 15H4a2 2 0 0 1-2-2V4a2 2 0 0 1 2-2h9a2 2 0 0 1 2 2v1" />
                        </svg>
                        <span>artifacts @{release.version}</span>
                      </>
                    )}
                  </button>
                </div>

                {/* Release Card Body */}
                <div className="p-5 sm:p-7 space-y-6">
                  {/* Title & Overview */}
                  <div className="space-y-1.5">
                    <h3 className="text-lg sm:text-xl font-bold text-slate-900 dark:text-white tracking-tight">
                      {release.title}
                    </h3>
                    <p className="text-sm sm:text-base text-slate-600 dark:text-slate-300 leading-relaxed">
                      {release.overview}
                    </p>
                  </div>

                  {/* Categories Breakdown */}
                  <div className="space-y-6">
                    {release.categories.map((category, catIdx) => {
                      const headerStyle = getCategoryBlockHeaderStyle(category.type);
                      return (
                        <div key={catIdx} className="space-y-4">
                          {/* Category Header */}
                          <div className="flex items-center gap-3.5 not-prose my-0 pb-1.5">
                            <span className={`inline-flex items-center justify-center gap-1.5 px-2.5 py-1 rounded-md text-xs font-bold border leading-none shrink-0 ${headerStyle.badge}`}>
                              <span className={`w-1.5 h-1.5 rounded-full shrink-0 ${headerStyle.dot}`} />
                              <span>{category.type}</span>
                            </span>
                            <h4 className="text-sm font-bold text-slate-900 dark:text-white !m-0 !p-0 leading-none flex items-center">
                              {category.label}
                            </h4>
                          </div>

                          {/* Items List - Note: Component links removed as requested */}
                          <div className="space-y-5 pl-1 sm:pl-2 pt-1">
                            {category.items.map((item, itemIdx) => (
                              <div key={itemIdx} className="space-y-2">
                                <div className="flex flex-wrap items-center gap-3 not-prose my-0">
                                  {/* Pure static badge without any redirection link */}
                                  <span className="inline-flex items-center justify-center px-2.5 py-1 rounded-md text-xs font-mono font-bold bg-slate-100 dark:bg-slate-800 text-slate-800 dark:text-slate-200 border border-slate-200 dark:border-slate-700 leading-none select-all shrink-0">
                                    {item.component}
                                  </span>
                                  <span className="text-sm font-semibold text-slate-900 dark:text-white leading-none flex items-center !m-0 !p-0">
                                    {item.summary}
                                  </span>
                                </div>

                                {/* Details bullet points */}
                                <ul className="list-disc list-outside pl-5 space-y-1 text-xs sm:text-sm text-slate-600 dark:text-slate-300 leading-relaxed">
                                  {item.details.map((detail, dIdx) => (
                                    <li key={dIdx}>
                                      <span>{detail}</span>
                                    </li>
                                  ))}
                                </ul>
                              </div>
                            ))}
                          </div>
                        </div>
                      );
                    })}
                  </div>
                </div>
              </article>
            );
          })}
        </div>
      )}

      {/* ── 5. Footer Info ────────────────────────────────────────── */}
      <div className="rounded-xl border border-[#E2E8F0] dark:border-[#334155] bg-[#F8FAFC] dark:bg-[#1E293B] p-5 flex flex-col sm:flex-row items-center justify-between gap-4 text-xs sm:text-sm text-slate-600 dark:text-slate-400">
        <div>
          <span>Looking to install or migrate from XML / Material 3? Check the </span>
          <Link href="/getting-started" className="text-[#4F46E5] dark:text-indigo-400 font-semibold hover:underline">
            Quick Start Guide
          </Link>
          <span> or </span>
          <Link href="/migration" className="text-[#4F46E5] dark:text-indigo-400 font-semibold hover:underline">
            Migration &amp; Parity Matrix
          </Link>
          .
        </div>
        <div className="text-xs text-slate-500 dark:text-slate-400 font-medium">
          UX4G Android • Official Design System
        </div>
      </div>
    </div>
  );
}
