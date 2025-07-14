import { NextRequest, NextResponse } from 'next/server';

import {getPostByTerm} from "@/utils/postUtil";

export async function GET(req: NextRequest) {
    const searchTerm = req.nextUrl.searchParams.get('q');

    if (!searchTerm || typeof searchTerm !== 'string') {
        return NextResponse.json({ error: 'Query parameter "q" is required and should be a string' }, { status: 400 });
    }

    return NextResponse.json(getPostByTerm(searchTerm), { status: 200 });
}
