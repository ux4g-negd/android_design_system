import type { BaseLayoutProps } from 'fumadocs-ui/layouts/shared';
import { gitConfig } from './shared';

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
    },
    links: [],
    githubUrl: `https://github.com/${gitConfig.user}/${gitConfig.repo}`,
  };
}
