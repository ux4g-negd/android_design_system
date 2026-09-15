import { source } from '@/lib/source';

export const dynamic = 'force-static';

export function GET() {
  const indexes = source.getPages().map((page) => ({
    title: page.data.title,
    description: page.data.description,
    url: page.url,
    id: page.url,
    structuredData: page.data.structuredData,
  }));
  return Response.json(indexes);
}
