'use client';

import React from 'react';
import { Sparkles, Box, Palette, Bell, Terminal, Settings, Layers } from 'lucide-react';

/* ─── Prerequisite pill ─── */
function PrereqPill({ icon: Icon, label, value }: { icon: React.ElementType; label: string; value: string }) {
  return (
    <div className="flex items-center gap-3 px-4 py-3 rounded-xl bg-slate-50/80 dark:bg-slate-800/50 border border-border/40 hover:border-indigo-300/40 dark:hover:border-indigo-600/30 transition-all">
      <div className="p-2 rounded-lg bg-indigo-100/60 dark:bg-indigo-900/30 text-indigo-600 dark:text-indigo-400 shrink-0">
        <Icon className="w-4 h-4" />
      </div>
      <div className="flex flex-col min-w-0">
        <span className="text-[11px] font-semibold text-muted-foreground uppercase tracking-wider">{label}</span>
        <span className="text-sm font-bold text-foreground truncate">{value}</span>
      </div>
    </div>
  );
}

/* ─── Step header card (wraps children from MDX) ─── */
export function StepCard({
  step,
  title,
  description,
  iconName,
  children,
}: {
  step: number;
  title: string;
  description?: string;
  iconName?: string;
  children?: React.ReactNode;
}) {
  const iconMap: Record<string, React.ElementType> = {
    Settings, Box, Layers, Palette, Bell, Terminal, Sparkles,
  };
  const Icon = iconMap[iconName || 'Settings'] || Settings;

  return (
    <div className="relative rounded-2xl border border-border/60 bg-card overflow-hidden shadow-xs hover:shadow-md hover:border-indigo-400/30 dark:hover:border-indigo-500/30 transition-all duration-200 my-4">
      {/* Header strip */}
      <div className="px-5 py-4 bg-gradient-to-r from-slate-50/80 via-white to-indigo-50/30 dark:from-slate-900/60 dark:via-slate-900/40 dark:to-indigo-950/20 border-b border-border/40 flex items-center gap-3.5">
        <div className="flex items-center justify-center w-9 h-9 rounded-xl bg-indigo-600 dark:bg-indigo-500 text-white font-bold text-sm shadow-sm shrink-0">
          {step}
        </div>
        <div className="flex-1 min-w-0">
          <div className="flex items-center gap-2">
            <Icon className="w-4 h-4 text-indigo-600 dark:text-indigo-400 shrink-0" />
            <h3 className="text-base font-bold text-foreground m-0">{title}</h3>
          </div>
          {description && (
            <p className="text-xs text-muted-foreground mt-0.5 m-0">{description}</p>
          )}
        </div>
      </div>
      {/* Body */}
      <div className="px-5 py-4">
        {children}
      </div>
    </div>
  );
}

/* ─── Prerequisites grid ─── */
export function PrerequisitesGrid() {
  return (
    <div className="grid grid-cols-1 sm:grid-cols-2 lg:grid-cols-3 gap-3">
      <PrereqPill icon={Terminal} label="Android Studio" value="Ladybug / Meerkat+" />
      <PrereqPill icon={Box} label="Min SDK" value="26 (Android 8.0 Oreo)" />
      <PrereqPill icon={Layers} label="Target / Compile SDK" value="35" />
      <PrereqPill icon={Sparkles} label="Kotlin" value="2.0.0+" />
      <PrereqPill icon={Palette} label="Jetpack Compose" value="Compose Compiler" />
    </div>
  );
}

/* ─── Info callout ─── */
export function InfoCallout({ children }: { children: React.ReactNode }) {
  return (
    <div className="flex items-start gap-2 px-3 py-2.5 rounded-xl bg-indigo-50/60 dark:bg-indigo-950/30 border border-indigo-200/40 dark:border-indigo-800/30 my-3">
      <Sparkles className="w-4 h-4 text-indigo-500 dark:text-indigo-400 mt-0.5 shrink-0" />
      <p className="text-xs text-indigo-700 dark:text-indigo-300 m-0">
        {children}
      </p>
    </div>
  );
}
