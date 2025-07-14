import {NextRequest, NextResponse} from "next/server";

import {deletePost} from "@/utils/postUtil";
import {IParam_DropPost, IResult_DropPost} from "@/types/interfaces/post-interface";

export async function POST(req: NextRequest) {
    const param: IParam_DropPost = await req.json();

    const result: IResult_DropPost = deletePost(param);

    if (result.success) {
        return NextResponse.json(result, { status: 200 });
    } else {
        return NextResponse.json(result, { status: 400 });
    }
}