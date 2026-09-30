'use client';

import React, { useEffect, useState } from 'react';
import { useTheme } from 'next-themes';
import { Moon, Sun } from 'lucide-react';

export function ThemeToggle({ className = '' }: { className?: string }) {
  const { setTheme, resolvedTheme } = useTheme();
  const [mounted, setMounted] = useState(false);
  const [isDark, setIsDark] = useState(false);

  useEffect(() => {
    setMounted(true);
    const checkDark = () => {
      const darkActive = document.documentElement.classList.contains('dark') || resolvedTheme === 'dark';
      setIsDark(darkActive);
    };
    checkDark();

    // Observe changes to html class attribute
    const observer = new MutationObserver(checkDark);
    observer.observe(document.documentElement, { attributes: true, attributeFilter: ['class'] });
    return () => observer.disconnect();
  }, [resolvedTheme]);

  const handleToggle = (e: React.MouseEvent) => {
    e.preventDefault();
    e.stopPropagation();

    const currentlyDark = document.documentElement.classList.contains('dark');
    const nextTheme = currentlyDark ? 'light' : 'dark';

    setTheme(nextTheme);

    if (nextTheme === 'dark') {
      document.documentElement.classList.add('dark');
      document.documentElement.classList.remove('light');
      document.documentElement.style.colorScheme = 'dark';
    } else {
      document.documentElement.classList.remove('dark');
      document.documentElement.classList.add('light');
      document.documentElement.style.colorScheme = 'light';
    }
    setIsDark(nextTheme === 'dark');
  };

  return (
    <button
      type="button"
      aria-label={isDark ? 'Switch to light theme' : 'Switch to dark theme'}
      title={isDark ? 'Switch to light theme' : 'Switch to dark theme'}
      onClick={handleToggle}
      className={`inline-flex items-center justify-center size-8 rounded-lg text-slate-600 hover:text-slate-900 dark:text-slate-300 dark:hover:text-white hover:bg-slate-100 dark:hover:bg-slate-800 active:scale-95 transition-all cursor-pointer ${className}`}
    >
      {mounted && isDark ? (
        <Sun className="size-[18px] text-amber-400 stroke-[2.2]" />
      ) : (
        <Moon className="size-[18px] text-slate-700 dark:text-slate-300 stroke-[2.2]" />
      )}
    </button>
  );
}
