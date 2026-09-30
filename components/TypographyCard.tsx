'use client';

import React, { useState } from 'react';
import { Check, Copy, Type } from 'lucide-react';

export interface TypographyItem {
  tokenName: string;
  label: string;
  fontSize: number;
  lineHeight: number;
  fontWeight: string; // e.g. "600 (SemiBold)", "700 (Bold)", "400 (Regular)", "500 (Medium)"
  previewText?: string;
}

function parseCssWeight(fw: string): string {
  if (fw.includes('700') || fw.toLowerCase().includes('bold')) return '700';
  if (fw.includes('600') || fw.toLowerCase().includes('semibold')) return '600';
  if (fw.includes('500') || fw.toLowerCase().includes('medium')) return '500';
  return '400';
}

function getWeightTag(fw: string): { label: string; colorClass: string } {
  const lower = fw.toLowerCase();
  // Check SemiBold BEFORE Bold — 'semibold' contains 'bold' as a substring
  if (fw.includes('600') || lower.includes('semibold'))
    return { label: 'SemiBold', colorClass: 'bg-indigo-100/90 text-indigo-950 border-indigo-300 dark:bg-indigo-950/60 dark:text-indigo-200 dark:border-indigo-700/60' };
  if (fw.includes('700') || lower.includes('bold'))
    return { label: 'Bold', colorClass: 'bg-amber-100/90 text-amber-950 border-amber-300 dark:bg-amber-950/60 dark:text-amber-200 dark:border-amber-700/60' };
  if (fw.includes('500') || lower.includes('medium'))
    return { label: 'Medium', colorClass: 'bg-sky-100/90 text-sky-950 border-sky-300 dark:bg-sky-950/60 dark:text-sky-200 dark:border-sky-700/60' };
  return { label: 'Regular', colorClass: 'bg-slate-200/90 text-slate-900 border-slate-300 dark:bg-slate-800 dark:text-slate-100 dark:border-slate-600' };
}

export function TypographyGrid({ items }: { items: TypographyItem[] }) {
  const [copiedKey, setCopiedKey] = useState<string | null>(null);

  const handleCopy = (text: string, key: string, e: React.MouseEvent) => {
    e.stopPropagation();
    navigator.clipboard.writeText(text);
    setCopiedKey(key);
    setTimeout(() => setCopiedKey(null), 1600);
  };

  return (
    <div className="flex flex-col gap-3.5 my-6">
      {items.map((item) => {
        const codeRef = `Ux4gTheme.typography.${item.tokenName}`;
        const isCodeCopied = copiedKey === `${item.tokenName}-code`;
        const cssWeight = parseCssWeight(item.fontWeight);
        const weightTag = getWeightTag(item.fontWeight);
        const previewFontSize = Math.min(Math.max(item.fontSize, 12), 36);
        const previewLineHeight = Math.min(Math.max(item.lineHeight, 16), 42);
        const previewStr = item.previewText || 'The quick brown fox jumps over the lazy dog';

        return (
          <div
            key={item.tokenName}
            className="group rounded-2xl border border-border/60 bg-card overflow-hidden shadow-xs hover:shadow-md hover:border-indigo-400/30 dark:hover:border-indigo-500/30 transition-all duration-200"
          >
            {/* ─── Top: Preview Strip ─── */}
            <div className="relative px-5 pt-5 pb-4 bg-gradient-to-br from-slate-50/80 via-white to-indigo-50/30 dark:from-slate-900/60 dark:via-slate-900/40 dark:to-indigo-950/20 border-b border-border/40">
              {/* Type icon */}
              <div className="absolute top-3.5 right-3.5 p-1.5 rounded-lg bg-indigo-100/80 dark:bg-indigo-900/40 text-indigo-700 dark:text-indigo-300">
                <Type className="w-3.5 h-3.5" />
              </div>

              <p
                className="text-slate-900 dark:text-slate-50 tracking-tight pr-8 break-words"
                style={{
                  fontSize: `${previewFontSize}px`,
                  fontWeight: cssWeight,
                  lineHeight: `${previewLineHeight}px`,
                }}
              >
                {previewStr}
              </p>
            </div>

            {/* ─── Bottom: Meta + Code ─── */}
            <div className="px-5 py-4 flex flex-col gap-3">
              {/* Row 1: Token name + weight tag */}
              <div className="flex flex-wrap items-center gap-2">
                <span className="text-sm font-bold text-slate-900 dark:text-slate-100">
                  {item.label}
                </span>
                <span className={`inline-flex items-center px-2 py-0.5 rounded-md text-[10px] font-bold uppercase tracking-wider border ${weightTag.colorClass}`}>
                  {weightTag.label}
                </span>
              </div>

              {/* Row 2: Specs pills */}
              <div className="flex flex-wrap items-center gap-1.5">
                <span className="inline-flex items-center gap-1 px-2.5 py-1 rounded-lg text-[11px] font-semibold bg-slate-100 text-slate-900 dark:bg-slate-800 dark:text-slate-100 border border-slate-300 dark:border-slate-700">
                  <span className="text-slate-600 dark:text-slate-300 font-bold">Size</span>
                  {item.fontSize}sp
                </span>
                <span className="inline-flex items-center gap-1 px-2.5 py-1 rounded-lg text-[11px] font-semibold bg-slate-100 text-slate-900 dark:bg-slate-800 dark:text-slate-100 border border-slate-300 dark:border-slate-700">
                  <span className="text-slate-600 dark:text-slate-300 font-bold">Line</span>
                  {item.lineHeight}sp
                </span>
                <span className="inline-flex items-center gap-1 px-2.5 py-1 rounded-lg text-[11px] font-semibold bg-slate-100 text-slate-900 dark:bg-slate-800 dark:text-slate-100 border border-slate-300 dark:border-slate-700">
                  <span className="text-slate-600 dark:text-slate-300 font-bold">Weight</span>
                  {item.fontWeight}
                </span>
              </div>

              {/* Row 3: Code copy pill */}
              <div
                onClick={(e) => handleCopy(codeRef, `${item.tokenName}-code`, e)}
                title={`Copy: ${codeRef}`}
                className="flex items-center justify-between gap-2 bg-slate-50/90 hover:bg-slate-100/90 dark:bg-slate-800/60 dark:hover:bg-slate-800 border border-slate-200 dark:border-slate-700/80 rounded-xl px-3.5 py-2.5 cursor-pointer transition-all group/code"
              >
                <code className="text-xs sm:text-[13px] font-mono font-semibold text-indigo-700 dark:text-indigo-300 truncate">
                  {codeRef}
                </code>
                <button
                  type="button"
                  className="shrink-0 p-1.5 rounded-lg text-slate-600 dark:text-slate-300 group-hover/code:text-indigo-700 dark:group-hover/code:text-indigo-300 transition-colors flex items-center gap-1.5 hover:bg-indigo-100/50 dark:hover:bg-indigo-900/30"
                >
                  {isCodeCopied ? (
                    <>
                      <Check className="w-3.5 h-3.5 text-emerald-600 dark:text-emerald-400" />
                      <span className="text-[11px] font-semibold text-emerald-600 dark:text-emerald-400 hidden sm:inline">Copied!</span>
                    </>
                  ) : (
                    <Copy className="w-3.5 h-3.5" />
                  )}
                </button>
              </div>
            </div>
          </div>
        );
      })}
    </div>
  );
}
