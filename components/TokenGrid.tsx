'use client';

import React, { useState } from 'react';
import { Check, Copy, Ruler, Circle, Minus, Layers } from 'lucide-react';

export interface TokenItem {
  name: string;
  value: string;
  composeRef: string;
  type?: 'space' | 'radius' | 'border' | 'shadow' | 'font' | 'icon';
  description?: string;
  iconName?: string;
  numericVal?: number;
}

function getTypeInfo(type?: string): { label: string; colorClass: string; Icon: React.ElementType } {
  switch (type) {
    case 'space':
      return { label: 'Spacing', colorClass: 'bg-violet-50 text-violet-700 border-violet-200/60 dark:bg-violet-950/40 dark:text-violet-300 dark:border-violet-800/40', Icon: Ruler };
    case 'radius':
      return { label: 'Radius', colorClass: 'bg-sky-50 text-sky-700 border-sky-200/60 dark:bg-sky-950/40 dark:text-sky-300 dark:border-sky-800/40', Icon: Circle };
    case 'border':
      return { label: 'Border', colorClass: 'bg-amber-50 text-amber-700 border-amber-200/60 dark:bg-amber-950/40 dark:text-amber-300 dark:border-amber-800/40', Icon: Minus };
    case 'shadow':
      return { label: 'Elevation', colorClass: 'bg-slate-100 text-slate-700 border-slate-200/60 dark:bg-slate-800/60 dark:text-slate-300 dark:border-slate-700/40', Icon: Layers };
    default:
      return { label: 'Token', colorClass: 'bg-indigo-50 text-indigo-700 border-indigo-200/60 dark:bg-indigo-950/40 dark:text-indigo-300 dark:border-indigo-800/40', Icon: Layers };
  }
}

export function TokenGrid({ tokens }: { tokens: TokenItem[] }) {
  const [copiedKey, setCopiedKey] = useState<string | null>(null);

  const handleCopy = (text: string, key: string, e: React.MouseEvent) => {
    e.stopPropagation();
    navigator.clipboard.writeText(text);
    setCopiedKey(key);
    setTimeout(() => setCopiedKey(null), 1600);
  };

  return (
    <div className="flex flex-col gap-3.5 my-6">
      {tokens.map((t) => {
        const valKey = `${t.name}-val`;
        const refKey = `${t.name}-ref`;
        const isValCopied = copiedKey === valKey;
        const isRefCopied = copiedKey === refKey;
        const typeInfo = getTypeInfo(t.type);
        const TypeIcon = typeInfo.Icon;

        return (
          <div
            key={t.name}
            className="group rounded-2xl border border-border/60 bg-card overflow-hidden shadow-xs hover:shadow-md hover:border-indigo-400/30 dark:hover:border-indigo-500/30 transition-all duration-200"
          >
            {/* ─── Top: Visual Preview ─── */}
            <div className="relative px-5 pt-5 pb-4 bg-gradient-to-br from-slate-50/80 via-white to-indigo-50/30 dark:from-slate-900/60 dark:via-slate-900/40 dark:to-indigo-950/20 border-b border-border/40 flex items-center justify-center min-h-[88px]">
              {/* Copy value button */}
              <button
                type="button"
                onClick={(e) => handleCopy(t.value, valKey, e)}
                title={`Copy value: ${t.value}`}
                className="absolute top-3 right-3 p-1.5 rounded-lg bg-white/90 hover:bg-white dark:bg-slate-800/90 dark:hover:bg-slate-700 text-muted-foreground hover:text-foreground border border-border/50 shadow-xs backdrop-blur-md transition-all flex items-center gap-1 z-10 cursor-pointer"
              >
                {isValCopied ? (
                  <>
                    <Check className="w-3.5 h-3.5 text-emerald-500" />
                    <span className="text-[10px] font-semibold text-emerald-500 leading-none">Copied</span>
                  </>
                ) : (
                  <Copy className="w-3.5 h-3.5" />
                )}
              </button>

              {/* ─── Preview Visualizations ─── */}
              {t.type === 'space' && (
                <div className="flex flex-col items-center gap-2 w-full max-w-[280px]">
                  <div className="w-full flex items-center gap-2">
                    <div className="flex-1 h-px bg-indigo-300/60 dark:bg-indigo-600/40" />
                    <div
                      className="bg-indigo-500/20 dark:bg-indigo-400/30 border-2 border-dashed border-indigo-500/50 dark:border-indigo-400/40 rounded-md transition-all"
                      style={{
                        width: `${Math.min(Math.max((t.numericVal ?? 8) * 2.5, 8), 160)}px`,
                        height: `${Math.min(Math.max((t.numericVal ?? 8) * 1.2, 8), 48)}px`,
                      }}
                    />
                    <div className="flex-1 h-px bg-indigo-300/60 dark:bg-indigo-600/40" />
                  </div>
                  <span className="text-xs font-mono font-bold text-indigo-600 dark:text-indigo-400">{t.value}</span>
                </div>
              )}

              {t.type === 'radius' && (
                <div className="flex flex-col items-center gap-2">
                  <div
                    className="w-20 h-20 border-2 border-sky-500 dark:border-sky-400 bg-sky-50/80 dark:bg-sky-950/30 flex items-center justify-center transition-all"
                    style={{
                      borderRadius: t.numericVal === 999 ? '9999px' : `${Math.min((t.numericVal ?? 4) * 1.5, 32)}px`,
                    }}
                  >
                    <span className="text-xs font-mono font-bold text-sky-600 dark:text-sky-300">
                      {t.value}
                    </span>
                  </div>
                </div>
              )}

              {t.type === 'border' && (
                <div className="flex flex-col items-center gap-3 w-full max-w-[200px]">
                  <div
                    className="w-full bg-amber-500 dark:bg-amber-400 rounded-full transition-all"
                    style={{ height: `${Math.max((t.numericVal ?? 1) * 3, 2)}px` }}
                  />
                  <span className="text-xs font-mono font-bold text-amber-600 dark:text-amber-300">{t.value}</span>
                </div>
              )}

              {t.type === 'shadow' && (
                <div className="flex flex-col items-center gap-2">
                  <div
                    className="w-24 h-16 bg-white dark:bg-slate-800 rounded-xl flex items-center justify-center text-xs font-bold text-foreground border border-border/30 transition-all"
                    style={{
                      boxShadow:
                        t.numericVal === 0
                          ? 'none'
                          : t.numericVal === 1
                          ? '0 1px 3px rgba(0,0,0,0.08), 0 1px 2px rgba(0,0,0,0.06)'
                          : t.numericVal === 2
                          ? '0 4px 6px rgba(0,0,0,0.07), 0 2px 4px rgba(0,0,0,0.06)'
                          : t.numericVal === 3
                          ? '0 10px 15px rgba(0,0,0,0.1), 0 4px 6px rgba(0,0,0,0.05)'
                          : '0 20px 25px rgba(0,0,0,0.1), 0 8px 10px rgba(0,0,0,0.04)',
                    }}
                  >
                    Lv {t.numericVal}
                  </div>
                </div>
              )}

              {t.type === 'font' && (
                <div className="flex flex-col items-center justify-center">
                  <span
                    className="font-bold text-foreground leading-none"
                    style={{ fontSize: `${Math.min(Math.max((t.numericVal ?? 16) * 0.9, 14), 38)}px` }}
                  >
                    Aa
                  </span>
                </div>
              )}

              {!t.type && (
                <div className="text-center font-bold text-foreground text-sm">{t.name}</div>
              )}
            </div>

            {/* ─── Bottom: Meta + Code ─── */}
            <div className="px-5 py-4 flex flex-col gap-3">
              {/* Row 1: Name + type badge */}
              <div className="flex flex-wrap items-center gap-2">
                <span className="text-sm font-bold text-foreground">
                  {t.name}
                </span>
                <span className={`inline-flex items-center gap-1 px-2 py-0.5 rounded-md text-[10px] font-bold uppercase tracking-wider border ${typeInfo.colorClass}`}>
                  <TypeIcon className="w-2.5 h-2.5" />
                  {typeInfo.label}
                </span>
              </div>

              {/* Row 2: Value pill + description */}
              <div className="flex flex-wrap items-center gap-2">
                <span className="inline-flex items-center px-2.5 py-1 rounded-lg text-[11px] font-mono font-bold bg-slate-100/80 text-slate-700 dark:bg-slate-800/60 dark:text-slate-300 border border-slate-200/50 dark:border-slate-700/40">
                  {t.value}
                </span>
                {t.description && (
                  <span className="text-[11px] text-muted-foreground">
                    — {t.description}
                  </span>
                )}
              </div>

              {/* Row 3: Compose ref copy pill */}
              <div
                onClick={(e) => handleCopy(t.composeRef, refKey, e)}
                title={`Copy: ${t.composeRef}`}
                className="flex items-center justify-between gap-2 bg-slate-50/80 hover:bg-indigo-50/60 dark:bg-slate-800/50 dark:hover:bg-indigo-950/40 border border-border/50 rounded-xl px-3.5 py-2.5 cursor-pointer transition-all group/code"
              >
                <code className="text-xs sm:text-[13px] font-mono font-medium text-indigo-600 dark:text-indigo-400 truncate">
                  {t.composeRef}
                </code>
                <button
                  type="button"
                  className="shrink-0 p-1.5 rounded-lg text-muted-foreground group-hover/code:text-indigo-600 dark:group-hover/code:text-indigo-400 transition-colors flex items-center gap-1.5 hover:bg-indigo-100/50 dark:hover:bg-indigo-900/30"
                >
                  {isRefCopied ? (
                    <>
                      <Check className="w-3.5 h-3.5 text-emerald-500" />
                      <span className="text-[11px] font-semibold text-emerald-500 hidden sm:inline">Copied!</span>
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
