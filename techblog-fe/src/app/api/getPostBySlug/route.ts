import { NextRequest, NextResponse } from 'next/server';

import { getPostBySlug } from "@/utils/postUtil";

export async function GET(req: NextRequest) {
    const slug = req.nextUrl.searchParams.get('slug');

    if (!slug) {
        return NextResponse.json({ error: 'Query parameter "slug" is required and should be a string' }, { status: 400 });
    }

    try {
        const post = await getPostBySlug(slug);
        return NextResponse.json(post, { status: 200 });
    } catch (error) {
        return NextResponse.json({ error: 'Failed to fetch post' }, { status: 500 });
    }
}
