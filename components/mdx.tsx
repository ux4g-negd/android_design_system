import defaultMdxComponents from 'fumadocs-ui/mdx';
import type { MDXComponents } from 'mdx/types';
import { ComponentPreview } from '@/components/ComponentPreview';
import { ColorPalette } from '@/components/ColorPalette';
import { MIcon } from '@/components/MIcon';
import { OverviewPage } from '@/components/OverviewPage';
import { MigrationPage } from '@/components/migration/MigrationPage';
import { ChangelogPage } from '@/components/changelog/ChangelogPage';

import { TokenGrid } from '@/components/TokenGrid';
import { TypographyGrid } from '@/components/TypographyCard';
import { StepCard, PrerequisitesGrid, InfoCallout } from '@/components/QuickStartGuide';

import { Tab, Tabs } from 'fumadocs-ui/components/tabs';

function Table(props: React.ComponentProps<'table'>) {
  return (
    <div className="table-wrapper my-6 w-full overflow-x-auto rounded-xl border border-border/70 bg-card shadow-sm">
      <table className="w-full min-w-[640px] border-collapse text-left text-sm" {...props} />
    </div>
  );
}

export function getMDXComponents(components?: MDXComponents) {
  return {
    ...defaultMdxComponents,
    h1: () => null,
    table: Table,
    Tabs,
    Tab,
    ComponentPreview,
    ColorPalette,
    TokenGrid,
    TypographyGrid,
    StepCard,
    PrerequisitesGrid,
    InfoCallout,
    MIcon,
    OverviewPage,
    MigrationPage,
    ChangelogPage,
    ...components,
  } satisfies MDXComponents;
}

export const useMDXComponents = getMDXComponents;

declare global {
  type MDXProvidedComponents = ReturnType<typeof getMDXComponents>;
}
