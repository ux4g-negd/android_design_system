import React from 'react';
import Link from 'next/link';

export function OverviewPage() {
  return (
    <div className="w-full min-h-screen flutter-welcome-bg px-5 sm:px-8 md:px-12 py-8 md:py-16 transition-colors duration-200">
      <div className="w-full max-w-[1150px] mx-auto">
        {/* ── 1. HERO SECTION ──────────────────────────────────────────────── */}
        <div className="grid grid-cols-1 lg:grid-cols-12 gap-8 lg:gap-12 items-start">
          {/* Left Column */}
          <div className="lg:col-span-7 flex flex-col items-start">
            {/* UX4G Title Logo */}
            {/* eslint-disable-next-line @next/next/no-img-element */}
            <img
              src="/ux4g_logo.svg"
              alt="UX4G Logo"
              className="h-9 md:h-12 w-auto block"
            />

            {/* Main Title */}
            <h1 className="text-[32px] md:text-[44px] font-[700] tracking-tight text-slate-900 dark:text-white leading-[1.15] -mt-1 md:-mt-1.5 mb-0">
              Android Documentation | UX4G Design System
            </h1>

            {/* Version Tag */}
            <div className="inline-flex items-center px-3 py-1 rounded-full text-[13px] font-[700] bg-[#EDE9FE] dark:bg-[#312E81]/50 text-[#6D28D9] dark:text-[#C4B5FD] border border-[#DDD6FE] dark:border-[#6366F1]/30 mt-3">
              3.0
            </div>

            {/* Tagline */}
            <p className="text-[18px] md:text-[22px] font-[700] text-slate-900 dark:text-white leading-[1.35] mt-6 mb-0">
              Government-grade UI foundations for trusted public digital experiences.
            </p>

            {/* Description */}
            <p className="text-[15px] md:text-[16px] text-[#4B5563] dark:text-[#94A3B8] leading-[1.6] mt-4 mb-0">
              UX4G gives product, design, and engineering teams one coherent system for building accessible, consistent, and scalable citizen-facing services.
            </p>

            {/* Buttons */}
            <div className="flex flex-wrap gap-[14px] mt-8">
              <Link
                href="/docs/foundation/theme"
                className="inline-flex items-center justify-center px-6 py-[18px] rounded-[8px] text-[15px] font-[700] text-white bg-[#4F46E5] hover:bg-[#4338CA] active:bg-[#3730A3] transition-colors shadow-none no-underline leading-none"
              >
                Get Started
              </Link>
              <Link
                href="/docs/components/button"
                className="inline-flex items-center justify-center px-6 py-[18px] rounded-[8px] text-[15px] font-[700] text-[#334155] dark:text-white bg-white dark:bg-transparent border border-[#CBD5E1] dark:border-white/20 hover:bg-slate-50 dark:hover:bg-white/5 transition-colors shadow-none no-underline leading-none"
              >
                Component Library
              </Link>
            </div>
          </div>

          {/* Right Column: System Overview Card */}
          <div className="lg:col-span-5 w-full">
            <div className="rounded-[20px] p-7 bg-white dark:bg-[#1E293B] border border-[#E2E8F0] dark:border-white/10 shadow-[0_16px_36px_rgba(79,70,229,0.08)] dark:shadow-[0_16px_36px_rgba(0,0,0,0.3)] transition-all">
              <span className="text-[12px] font-[700] text-[#4A2BC2] dark:text-[#A5B4FC]">
                System overview
              </span>

              <h2 className="text-[22px] font-[800] text-slate-900 dark:text-white leading-[1.25] mt-4 mb-0">
                One platform for consistent public service journeys.
              </h2>

              <p className="text-[13.5px] text-[#4B5563] dark:text-[#94A3B8] leading-[1.5] mt-3 mb-0">
                Start from shared tokens, move into reusable components, and document delivery paths in one place.
              </p>

              {/* Stats Box */}
              <div className="mt-6 py-5 border-y border-[#F1F5F9] dark:border-white/10 grid grid-cols-4 items-center">
                {/* Stat 1 */}
                <div className="flex flex-col">
                  <span className="text-[22px] font-[800] text-slate-900 dark:text-white leading-tight">
                    45+
                  </span>
                  <span className="text-[11px] text-[#4B5563] dark:text-[#94A3B8] leading-[1.3] mt-1">
                    Reusable<br />components
                  </span>
                </div>

                {/* Stat 2 */}
                <div className="flex flex-col border-l border-[#E2E8F0] dark:border-white/10 pl-2 sm:pl-3">
                  <span className="text-[22px] font-[800] text-slate-900 dark:text-white leading-tight">
                    1K+
                  </span>
                  <span className="text-[11px] text-[#4B5563] dark:text-[#94A3B8] leading-[1.3] mt-1">
                    Design<br />tokens
                  </span>
                </div>

                {/* Stat 3 */}
                <div className="flex flex-col border-l border-[#E2E8F0] dark:border-white/10 pl-2 sm:pl-3">
                  <span className="text-[22px] font-[800] text-slate-900 dark:text-white leading-tight">
                    10+
                  </span>
                  <span className="text-[11px] text-[#4B5563] dark:text-[#94A3B8] leading-[1.3] mt-1">
                    Patterns
                  </span>
                </div>

                {/* Stat 4 */}
                <div className="flex flex-col border-l border-[#E2E8F0] dark:border-white/10 pl-2 sm:pl-3">
                  <span className="text-[22px] font-[800] text-slate-900 dark:text-white leading-tight">
                    AA
                  </span>
                  <span className="text-[11px] text-[#4B5563] dark:text-[#94A3B8] leading-[1.3] mt-1">
                    Accessibility<br />target
                  </span>
                </div>
              </div>

              {/* Table Details */}
              <div className="mt-5 space-y-3.5">
                <div className="flex items-center justify-between gap-4">
                  <span className="text-[13.5px] font-[700] text-slate-900 dark:text-white shrink-0">Tokens</span>
                  <span className="text-[13px] text-[#4B5563] dark:text-[#94A3B8] text-right">Spacing, Radius, Colors</span>
                </div>
                <div className="flex items-center justify-between gap-4">
                  <span className="text-[13.5px] font-[700] text-slate-900 dark:text-white shrink-0">Components</span>
                  <span className="text-[13px] text-[#4B5563] dark:text-[#94A3B8] text-right">Inputs, buttons, cards</span>
                </div>
                <div className="flex items-center justify-between gap-4">
                  <span className="text-[13.5px] font-[700] text-slate-900 dark:text-white shrink-0">Patterns</span>
                  <span className="text-[13px] text-[#4B5563] dark:text-[#94A3B8] text-right">Headers, footers, forms</span>
                </div>
              </div>
            </div>
          </div>
        </div>

        {/* ── 2. CORE STRENGTHS SECTION ────────────────────────────────────── */}
        <div className="mt-16 md:mt-24">
          <div className="mb-8">
            <span className="text-[13px] font-[700] text-[#4A2BC2] dark:text-[#A5B4FC] tracking-[0.5px]">
              Core strengths
            </span>
            <h2 className="text-[24px] md:text-[32px] font-[800] text-slate-900 dark:text-white tracking-[-0.5px] mt-2 mb-0 leading-[1.25]">
              Built for design systems that need<br className="hidden md:inline" /> to scale beyond one product team.
            </h2>
            <p className="text-[15.5px] text-[#4B5563] dark:text-[#94A3B8] leading-[1.55] mt-3 mb-0 max-w-3xl">
              UX4G combines visual consistency, engineering pragmatism, and accessibility expectations into a single documentation experience.
            </p>
          </div>

          <div className="grid grid-cols-1 md:grid-cols-2 gap-5">
            {/* Card 1 */}
            <div className="min-h-[184px] p-6 rounded-[16px] bg-white dark:bg-[#1E293B] border border-[#E2E8F0] dark:border-white/10 shadow-[0_4px_16px_rgba(0,0,0,0.03)] dark:shadow-none">
              <div className="inline-flex p-[10px] rounded-[8px] bg-[#F3F0FF] dark:bg-[#4F46E5]/20 text-[#6D28D9] dark:text-[#A5B4FC]">
                <span className="material-symbols-outlined text-[20px]">account_tree</span>
              </div>
              <h3 className="text-[16px] font-[700] text-slate-900 dark:text-white mt-4 mb-0">
                Scalable Architecture
              </h3>
              <p className="text-[13.5px] text-[#4B5563] dark:text-[#94A3B8] leading-[1.55] mt-2 mb-0">
                A structured system of foundations, patterns, and components that can scale across ministries, vendors, and service teams.
              </p>
            </div>

            {/* Card 2 */}
            <div className="min-h-[184px] p-6 rounded-[16px] bg-white dark:bg-[#1E293B] border border-[#E2E8F0] dark:border-white/10 shadow-[0_4px_16px_rgba(0,0,0,0.03)] dark:shadow-none">
              <div className="inline-flex p-[10px] rounded-[8px] bg-[#F3F0FF] dark:bg-[#4F46E5]/20 text-[#6D28D9] dark:text-[#A5B4FC]">
                <span className="material-symbols-outlined text-[20px]">accessibility_new</span>
              </div>
              <h3 className="text-[16px] font-[700] text-slate-900 dark:text-white mt-4 mb-0">
                Accessible Components
              </h3>
              <p className="text-[13.5px] text-[#4B5563] dark:text-[#94A3B8] leading-[1.55] mt-2 mb-0">
                Interaction patterns are designed for clarity, contrast, keyboard use, and dependable public-facing experiences.
              </p>
            </div>

            {/* Card 3 */}
            <div className="min-h-[184px] p-6 rounded-[16px] bg-white dark:bg-[#1E293B] border border-[#E2E8F0] dark:border-white/10 shadow-[0_4px_16px_rgba(0,0,0,0.03)] dark:shadow-none">
              <div className="inline-flex p-[10px] rounded-[8px] bg-[#F3F0FF] dark:bg-[#4F46E5]/20 text-[#6D28D9] dark:text-[#A5B4FC]">
                <span className="material-symbols-outlined text-[20px]">palette</span>
              </div>
              <h3 className="text-[16px] font-[700] text-slate-900 dark:text-white mt-4 mb-0">
                Token-Driven Design
              </h3>
              <p className="text-[13.5px] text-[#4B5563] dark:text-[#94A3B8] leading-[1.55] mt-2 mb-0">
                Color, typography, spacing, and elevation are governed through reusable tokens that keep every experience aligned.
              </p>
            </div>

            {/* Card 4 */}
            <div className="min-h-[184px] p-6 rounded-[16px] bg-white dark:bg-[#1E293B] border border-[#E2E8F0] dark:border-white/10 shadow-[0_4px_16px_rgba(0,0,0,0.03)] dark:shadow-none">
              <div className="inline-flex p-[10px] rounded-[8px] bg-[#F3F0FF] dark:bg-[#4F46E5]/20 text-[#6D28D9] dark:text-[#A5B4FC]">
                <span className="material-symbols-outlined text-[20px]">code</span>
              </div>
              <h3 className="text-[16px] font-[700] text-slate-900 dark:text-white mt-4 mb-0">
                Developer Friendly
              </h3>
              <p className="text-[13.5px] text-[#4B5563] dark:text-[#94A3B8] leading-[1.55] mt-2 mb-0">
                Composable utilities, production-ready Jetpack Compose components, and comprehensive documentation reduce friction from exploration to delivery.
              </p>
            </div>
          </div>
        </div>

        {/* ── 3. LIBRARY MAP SECTION ───────────────────────────────────────── */}
        <div className="mt-16 md:mt-24">
          <div className="mb-8">
            <span className="text-[13px] font-[700] text-[#4A2BC2] dark:text-[#A5B4FC] tracking-[0.5px]">
              Library map
            </span>
            <h2 className="text-[24px] md:text-[32px] font-[800] text-slate-900 dark:text-white tracking-[-0.5px] mt-2 mb-0 leading-[1.25]">
              Navigate the design system through the same<br className="hidden md:inline" /> categories shown in the sidebar.
            </h2>
            <p className="text-[15.5px] text-[#4B5563] dark:text-[#94A3B8] leading-[1.55] mt-3 mb-0 max-w-3xl">
              Components, tokens, and Patterns are organized to help teams move from exploration to implementation without losing context.
            </p>
          </div>

          <div className="grid grid-cols-1 md:grid-cols-2 gap-5">
            {/* Card 1: Components */}
            <div className="min-h-[255px] p-6 rounded-[16px] bg-white dark:bg-[#1E293B] border border-[#E2E8F0] dark:border-white/10 shadow-[0_4px_16px_rgba(0,0,0,0.03)] dark:shadow-none flex flex-col justify-between">
              <div>
                <div className="inline-flex p-[10px] rounded-[8px] bg-[#F3F0FF] dark:bg-[#4F46E5]/20 text-[#6D28D9] dark:text-[#A5B4FC]">
                  <span className="material-symbols-outlined text-[20px]">widgets</span>
                </div>
                <h3 className="text-[18px] font-[700] text-slate-900 dark:text-white mt-4 mb-0">
                  Components
                </h3>
                <p className="text-[13.5px] text-[#4B5563] dark:text-[#94A3B8] leading-[1.55] mt-2 mb-4">
                  Form controls, navigation, feedback, and layout primitives designed for real government workflows.
                </p>
                <ul className="space-y-2 text-[13.5px] text-slate-900 dark:text-white pl-0 list-none mb-0 leading-[1.4]">
                  <li className="flex items-start">
                    <span className="text-[#4F46E5] dark:text-[#A5B4FC] font-[800] mr-2">•</span>
                    <span>Production-ready Jetpack Compose building blocks</span>
                  </li>
                  <li className="flex items-start">
                    <span className="text-[#4F46E5] dark:text-[#A5B4FC] font-[800] mr-2">•</span>
                    <span>Interactive states documented with Kotlin code snippets</span>
                  </li>
                  <li className="flex items-start">
                    <span className="text-[#4F46E5] dark:text-[#A5B4FC] font-[800] mr-2">•</span>
                    <span>Consistent anatomy across Android surfaces</span>
                  </li>
                </ul>
              </div>
              <div className="pt-4 mt-4 border-t border-[#F1F5F9] dark:border-white/10">
                <Link
                  href="/docs/components/button"
                  className="text-[13.5px] font-semibold text-[#4F46E5] dark:text-[#A5B4FC] hover:underline inline-flex items-center"
                >
                  Browse Components →
                </Link>
              </div>
            </div>

            {/* Card 2: Tokens */}
            <div className="min-h-[255px] p-6 rounded-[16px] bg-white dark:bg-[#1E293B] border border-[#E2E8F0] dark:border-white/10 shadow-[0_4px_16px_rgba(0,0,0,0.03)] dark:shadow-none flex flex-col justify-between">
              <div>
                <div className="inline-flex p-[10px] rounded-[8px] bg-[#F3F0FF] dark:bg-[#4F46E5]/20 text-[#6D28D9] dark:text-[#A5B4FC]">
                  <span className="material-symbols-outlined text-[20px]">style</span>
                </div>
                <h3 className="text-[18px] font-[700] text-slate-900 dark:text-white mt-4 mb-0">
                  Tokens
                </h3>
                <p className="text-[13.5px] text-[#4B5563] dark:text-[#94A3B8] leading-[1.55] mt-2 mb-4">
                  Core design decisions captured as reusable color, typography, spacing, and semantic values.
                </p>
                <ul className="space-y-2 text-[13.5px] text-slate-900 dark:text-white pl-0 list-none mb-0 leading-[1.4]">
                  <li className="flex items-start">
                    <span className="text-[#4F46E5] dark:text-[#A5B4FC] font-[800] mr-2">•</span>
                    <span>Brand and neutral palettes</span>
                  </li>
                  <li className="flex items-start">
                    <span className="text-[#4F46E5] dark:text-[#A5B4FC] font-[800] mr-2">•</span>
                    <span>Semantic mappings for surfaces and states</span>
                  </li>
                  <li className="flex items-start">
                    <span className="text-[#4F46E5] dark:text-[#A5B4FC] font-[800] mr-2">•</span>
                    <span>A shared source of truth for scale</span>
                  </li>
                </ul>
              </div>
              <div className="pt-4 mt-4 border-t border-[#F1F5F9] dark:border-white/10">
                <Link
                  href="/docs/tokens/colors"
                  className="text-[13.5px] font-semibold text-[#4F46E5] dark:text-[#A5B4FC] hover:underline inline-flex items-center"
                >
                  Explore Tokens →
                </Link>
              </div>
            </div>

            {/* Card 3: Patterns */}
            <div className="min-h-[255px] p-6 rounded-[16px] bg-white dark:bg-[#1E293B] border border-[#E2E8F0] dark:border-white/10 shadow-[0_4px_16px_rgba(0,0,0,0.03)] dark:shadow-none flex flex-col justify-between">
              <div>
                <div className="inline-flex p-[10px] rounded-[8px] bg-[#F3F0FF] dark:bg-[#4F46E5]/20 text-[#6D28D9] dark:text-[#A5B4FC]">
                  <span className="material-symbols-outlined text-[20px]">dashboard_customize</span>
                </div>
                <h3 className="text-[18px] font-[700] text-slate-900 dark:text-white mt-4 mb-0">
                  Patterns
                </h3>
                <p className="text-[13.5px] text-[#4B5563] dark:text-[#94A3B8] leading-[1.55] mt-2 mb-4">
                  Reusable block-level compositions like headers, footers, and complex forms used across applications.
                </p>
                <ul className="space-y-2 text-[13.5px] text-slate-900 dark:text-white pl-0 list-none mb-0 leading-[1.4]">
                  <li className="flex items-start">
                    <span className="text-[#4F46E5] dark:text-[#A5B4FC] font-[800] mr-2">•</span>
                    <span>Pre-built structural blocks</span>
                  </li>
                  <li className="flex items-start">
                    <span className="text-[#4F46E5] dark:text-[#A5B4FC] font-[800] mr-2">•</span>
                    <span>Streamlined composition</span>
                  </li>
                  <li className="flex items-start">
                    <span className="text-[#4F46E5] dark:text-[#A5B4FC] font-[800] mr-2">•</span>
                    <span>Accelerated feature delivery</span>
                  </li>
                </ul>
              </div>
              <div className="pt-4 mt-4 border-t border-[#F1F5F9] dark:border-white/10">
                <Link
                  href="/docs/patterns/identity-and-access"
                  className="text-[13.5px] font-semibold text-[#4F46E5] dark:text-[#A5B4FC] hover:underline inline-flex items-center"
                >
                  View Patterns →
                </Link>
              </div>
            </div>
          </div>
        </div>

        {/* ── 4. FOOTER ────────────────────────────────────────────────────── */}
        <div className="mt-16 md:mt-20 pt-8 border-t border-[#E5E7EB] dark:border-white/10">
          <div className="flex flex-col sm:flex-row items-start sm:items-center justify-between gap-4">
            <div className="flex items-center space-x-4">
              {/* eslint-disable-next-line @next/next/no-img-element */}
              <img
                src="/ux4g_logo.svg"
                alt="UX4G Logo"
                className="h-[26px] w-auto"
              />
              <span className="text-[13px] text-[#4B5563] dark:text-[#94A3B8]">
                A shared platform for accessible, consistent, and reliable public digital products.
              </span>
            </div>

            <div className="flex items-center space-x-1.5 text-[14px] text-[#4B5563] dark:text-[#94A3B8] shrink-0">
              <span>Visit:</span>
              <a
                href="https://www.ux4g.gov.in"
                target="_blank"
                rel="noopener noreferrer"
                className="text-[14px] font-[600] text-[#4F46E5] dark:text-[#A5B4FC] hover:underline"
              >
                ux4g.gov.in
              </a>
            </div>
          </div>
        </div>
      </div>
    </div>
  );
}
