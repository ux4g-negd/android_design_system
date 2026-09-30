import type { BaseLayoutProps } from 'fumadocs-ui/layouts/shared';
import { ThemeToggle } from '@/components/ThemeToggle';

export function baseOptions(): BaseLayoutProps {
  return {
    nav: {
      title: (
        <div className="flex items-center gap-2">
          {/* eslint-disable-next-line @next/next/no-img-element */}
          <img
            src="/ux4g_logo.svg"
            alt="UX4G Logo"
            className="h-8 w-auto object-contain"
          />
        </div>
      ),
      transparentMode: 'top',
      children: (
        <div className="flex items-center justify-end gap-1.5 ml-auto">
          <ThemeToggle />
        </div>
      ),
    },
    themeSwitch: {
      enabled: false,
    },
    links: [],
  };
}

