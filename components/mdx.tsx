import defaultMdxComponents from 'fumadocs-ui/mdx';
import type { MDXComponents } from 'mdx/types';
import { ComponentPreview } from '@/components/ComponentPreview';
import { ColorPalette } from '@/components/ColorPalette';
import { MIcon } from '@/components/MIcon';
import { OverviewPage } from '@/components/OverviewPage';
import { MigrationPage } from '@/components/migration/MigrationPage';

import { TokenGrid } from '@/components/TokenGrid';
import { TypographyGrid } from '@/components/TypographyCard';
import { StepCard, PrerequisitesGrid, InfoCallout } from '@/components/QuickStartGuide';

export function getMDXComponents(components?: MDXComponents) {
  return {
    ...defaultMdxComponents,
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
    ...components,
  } satisfies MDXComponents;
}

export const useMDXComponents = getMDXComponents;

declare global {
  type MDXProvidedComponents = ReturnType<typeof getMDXComponents>;
}
