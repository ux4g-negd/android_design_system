'use client';

import React, { useState } from 'react';
import { Smartphone, Code, Sliders, Copy, Check, Eye } from 'lucide-react';

interface ComponentPreviewProps {
  title?: string;
  category?: string;
  code?: string;
  children: React.ReactNode;
}

export function ComponentPreview({
  title,
  category = 'Jetpack Compose Component',
  code,
  children,
}: ComponentPreviewProps) {
  const [activeTab, setActiveTab] = useState<'preview' | 'code'>('preview');
  const [copied, setCopied] = useState(false);

  const handleCopy = () => {
    if (!code) return;
    navigator.clipboard.writeText(code);
    setCopied(true);
    setTimeout(() => setCopied(false), 2000);
  };

  return (
    <div className="my-6 rounded-2xl border border-border bg-card overflow-hidden shadow-sm transition-all hover:border-indigo-500/40 not-prose">
      {/* Top Header Bar */}
      <div className="flex flex-col sm:flex-row sm:items-center justify-between gap-2.5 px-3.5 sm:px-4 py-2.5 sm:py-3 border-b border-border/80 bg-muted/40 backdrop-blur-sm">
        {/* Left: Title and Category Badge */}
        <div className="flex items-center flex-wrap gap-2 min-w-0">
          <span className="flex h-2.5 w-2.5 rounded-full bg-indigo-500 shrink-0" />
          <span className="text-xs sm:text-sm font-semibold text-foreground tracking-tight break-words">
            {title || 'Preview Canvas'}
          </span>
          {category && (
            <span className="text-[10px] uppercase font-bold tracking-wider px-2 py-0.5 rounded-md bg-indigo-500/10 text-indigo-600 dark:text-indigo-400 whitespace-nowrap shrink-0">
              {category}
            </span>
          )}
        </div>

        {/* Right: Tab Buttons & Copy Button */}
        <div className="flex items-center gap-1.5 shrink-0 self-start sm:self-auto">
          <div className="flex rounded-lg bg-background p-0.5 border border-border shrink-0">
            <button
              type="button"
              onClick={() => setActiveTab('preview')}
              className={`flex items-center gap-1 px-2.5 py-1 rounded-md text-xs font-medium transition-all whitespace-nowrap cursor-pointer ${
                activeTab === 'preview'
                  ? 'bg-indigo-600 text-white shadow-xs'
                  : 'text-muted-foreground hover:text-foreground'
              }`}
            >
              <Eye className="w-3.5 h-3.5 shrink-0" />
              <span>Canvas</span>
            </button>
            {code && (
              <button
                type="button"
                onClick={() => setActiveTab('code')}
                className={`flex items-center gap-1 px-2.5 py-1 rounded-md text-xs font-medium transition-all whitespace-nowrap cursor-pointer ${
                  activeTab === 'code'
                    ? 'bg-indigo-600 text-white shadow-xs'
                    : 'text-muted-foreground hover:text-foreground'
                }`}
              >
                <Code className="w-3.5 h-3.5 shrink-0" />
                <span>Compose Code</span>
              </button>
            )}
          </div>

          {code && (
            <button
              type="button"
              onClick={handleCopy}
              className="p-1.5 rounded-md border border-border bg-background hover:bg-accent text-muted-foreground hover:text-foreground text-xs transition-colors cursor-pointer shrink-0"
              title="Copy Kotlin Code"
            >
              {copied ? <Check className="w-3.5 h-3.5 text-emerald-500" /> : <Copy className="w-3.5 h-3.5" />}
            </button>
          )}
        </div>
      </div>

      {/* Main Content Area */}
      {activeTab === 'preview' ? (
        <div className="p-4 sm:p-6 md:p-8 flex flex-col items-center justify-center bg-[radial-gradient(#e5e7eb_1px,transparent_1px)] dark:bg-[radial-gradient(#1f2937_1px,transparent_1px)] bg-[size:16px_16px] min-h-[140px] overflow-x-auto max-w-full">
          <div className="w-full max-w-3xl flex flex-col items-center justify-center gap-4">
            {children}
          </div>
        </div>
      ) : (
        <div className="p-4 bg-slate-950 text-slate-100 font-mono text-xs overflow-x-auto max-w-full">
          <pre className="!m-0">{code}</pre>
        </div>
      )}
    </div>
  );
}
