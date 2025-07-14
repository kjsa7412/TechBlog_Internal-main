export const dynamic = 'force-dynamic' // 실시간으로 데이터를 받아오기 위해 동적으로 설정

import {AxiosResponse} from "axios";
import {NextResponse} from "next/server";

import axiosServer from "@/libs/axiosServer";
import {IAPIResponse} from "@/types/interfaces/common-interface";
import {IPostData, IResult_PopPostList} from "@/types/interfaces/post-interface";
import {getAllPosts, getPostBySlug} from "@/utils/postUtil";

async function serverAPI_popPost(): Promise<AxiosResponse<IAPIResponse<IResult_PopPostList>>> {
    return await axiosServer.get('/public/get/boad/poplist');
}

export async function GET() {
    try {
        const response = await serverAPI_popPost();

        // slug 추출
        const slugs: string[] = response.data?.content.map((item: IResult_PopPostList) => String(item.boadId)) || [];

        // 게시물 추출
        const postDataArray: (IPostData | undefined)[] = slugs.map((slug: string) => getPostBySlug(slug));

        // NextResponse 객체 생성
        return NextResponse.json(postDataArray, {status: 200});

    } catch (error) {
        // 1. API 서버가 꺼졌거나 통신이 안된 경우
        // 2. API 서버에서 읽어온 인기 게시물 3개가 프론트 서버에 없는 경우(로컬 환경에서 발생할법한 오류)
        // 프론트 서버에서 가장 최신글 3개를 조회해서 리턴
        return NextResponse.json(getAllPosts().slice(0, 3), { status: 200 });
    }
}
