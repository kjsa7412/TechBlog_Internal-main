export const dynamic = 'force-dynamic' // 실시간으로 데이터를 받아오기 위해 동적으로 설정

import {NextResponse} from "next/server";

import {getAllPosts} from "@/utils/postUtil";

export async function GET() {
    return NextResponse.json(getAllPosts(), { status: 200 });
}