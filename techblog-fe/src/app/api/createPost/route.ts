import { NextRequest, NextResponse } from "next/server";

import {createPost} from "@/utils/postUtil";
import {IParam_CreatePost, IResult_CreatePost} from "@/types/interfaces/post-interface";

export async function POST(req: NextRequest) {
    const param: IParam_CreatePost = await req.json();

    const result: IResult_CreatePost = createPost(param);

    if (result.success) {
        return NextResponse.json(result, { status: 200 });
    } else {
        return NextResponse.json(result, { status: 400 });
    }
}