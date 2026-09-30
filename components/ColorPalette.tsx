'use client';

import React, { useState } from 'react';
import { Check, Copy } from 'lucide-react';

interface ColorItem {
  name: string;
  hex: string;
  composeRef: string;
  textColor?: string;
}

// Helper to determine if a color is dark or light
function isColorDark(hex: string): boolean {
  let cleanHex = hex.replace('#', '');
  if (cleanHex.length === 8) {
    cleanHex = cleanHex.substring(0, 6);
  }
  if (cleanHex.length === 3) {
    cleanHex = cleanHex.split('').map((c) => c + c).join('');
  }
  if (cleanHex.length !== 6) return false;

  const r = parseInt(cleanHex.substring(0, 2), 16);
  const g = parseInt(cleanHex.substring(2, 4), 16);
  const b = parseInt(cleanHex.substring(4, 6), 16);
  
  // HSP color model perceived brightness formula
  const hsp = Math.sqrt(0.299 * (r * r) + 0.587 * (g * g) + 0.114 * (b * b));
  return hsp < 140;
}

export function ColorPalette({ colors }: { colors: ColorItem[] }) {
  const [copiedKey, setCopiedKey] = useState<string | null>(null);

  const handleCopy = (text: string, key: string, e: React.MouseEvent) => {
    e.stopPropagation();
    navigator.clipboard.writeText(text);
    setCopiedKey(key);
    setTimeout(() => setCopiedKey(null), 1600);
  };

  return (
    <div className="grid grid-cols-1 sm:grid-cols-2 md:grid-cols-3 gap-4 my-6">
      {colors.map((c) => {
        const hexKey = `${c.name}-hex`;
        const refKey = `${c.name}-ref`;
        const isHexCopied = copiedKey === hexKey;
        const isRefCopied = copiedKey === refKey;
        const isDark = isColorDark(c.hex);

        return (
          <div
            key={c.name}
            className="group rounded-2xl border border-border/70 bg-card overflow-hidden shadow-xs hover:shadow-md hover:border-indigo-400/40 dark:hover:border-indigo-500/40 transition-all flex flex-col"
          >
            {/* Top Color Box with High-Contrast Copy Button */}
            <div className="relative h-28 w-full bg-[radial-gradient(#e5e7eb_1px,transparent_1px)] [background-size:8px_8px] dark:bg-[radial-gradient(#334155_1px,transparent_1px)]">
              <div
                className="absolute inset-0 p-3 flex items-start justify-end transition-opacity"
                style={{ backgroundColor: c.hex }}
              >
                <button
                  type="button"
                  onClick={(e) => handleCopy(c.hex, hexKey, e)}
                  title={`Copy hex ${c.hex}`}
                  className={`p-2 rounded-xl backdrop-blur-md transition-all shadow-sm flex items-center gap-1.5 cursor-pointer border ${
                    isDark
                      ? 'bg-white/25 hover:bg-white/40 text-white border-white/30'
                      : 'bg-black/10 hover:bg-black/20 text-black border-black/10 shadow-xs'
                  }`}
                >
                  {isHexCopied ? (
                    <>
                      <Check className="w-3.5 h-3.5 text-emerald-400" />
                      <span className="text-[10px] font-semibold text-emerald-400 leading-none">Copied</span>
                    </>
                  ) : (
                    <Copy className="w-3.5 h-3.5 opacity-90 hover:opacity-100" />
                  )}
                </button>
              </div>
            </div>

            {/* Bottom Content Area */}
            <div className="p-3.5 flex flex-col gap-2.5 bg-card min-w-0">
              {/* Row 1: Name and Hex */}
              <div className="flex items-center justify-between gap-2 min-w-0">
                <span className="text-sm font-bold text-slate-900 dark:text-slate-100 truncate min-w-0" title={c.name}>
                  {c.name}
                </span>
                <span className="text-xs font-mono font-semibold text-slate-800 dark:text-slate-200 bg-slate-100 dark:bg-slate-800/90 px-2 py-0.5 rounded-md border border-slate-300/80 dark:border-slate-700 shrink-0">
                  {c.hex}
                </span>
              </div>

              {/* Row 2: Compose Object Pill with Copy Button */}
              <div
                onClick={(e) => handleCopy(c.composeRef, refKey, e)}
                title={`Copy code: ${c.composeRef}`}
                className="flex items-center justify-between gap-2 bg-slate-50/90 hover:bg-slate-100/90 dark:bg-slate-800/60 dark:hover:bg-slate-800 border border-slate-200 dark:border-slate-700/80 rounded-xl px-2.5 py-2 cursor-pointer transition-all group/pill min-w-0"
              >
                <span
                  className="text-xs font-mono font-semibold text-indigo-700 dark:text-indigo-300 truncate min-w-0 flex-1"
                  title={c.composeRef}
                >
                  {c.composeRef}
                </span>
                <button
                  type="button"
                  className="shrink-0 p-1 text-slate-600 dark:text-slate-300 group-hover/pill:text-indigo-700 dark:group-hover/pill:text-indigo-300 transition-colors rounded-md"
                >
                  {isRefCopied ? (
                    <Check className="w-3.5 h-3.5 text-emerald-600 dark:text-emerald-400" />
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
