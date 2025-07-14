/* eslint-disable */
/*
* 2024.09.28
* 기존 API 구조상 사용했던 코드
* 프로젝트 최종 배포 시 사용 유무에 따라 제거
*
* */
// import { AxiosResponse } from "axios";
// import { NextRequest, NextResponse } from "next/server";
//
// import axiosServer from "@/libs/axiosServer";
// import {IAPIResponse, IUser} from "@/types/interfaces/common-interface";
// import {IParam_UserJoin} from "@/types/interfaces/user-interface";
//
// async function fetchSignIn(param: IParam_UserJoin): Promise<AxiosResponse<IAPIResponse<IUser>>> {
//     return await axiosServer.post('/public/post/auth/signIn', param);
// }
//
// export async function POST(req: NextRequest) {
//     try {
//         const param: IParam_UserJoin = await req.json();
//         const response = await fetchSignIn(param);
//
//         // Set-Cookie 값 추출
//         const setCookieHeader = response?.headers['set-cookie'];
//
//         // NextResponse 객체 생성
//         const nextResponse = NextResponse.json(response.data, { status: 200 });
//
//         // Set-Cookie 할당
//         if (Array.isArray(setCookieHeader)) {
//             setCookieHeader.forEach((cookie) => {
//                 nextResponse.headers.append('Set-Cookie', cookie);
//             });
//         } else if (setCookieHeader) {
//             nextResponse.headers.set('Set-Cookie', setCookieHeader);
//         }
//
//         return nextResponse;
//     } catch (error) {
//         // 오류가 발생했을 경우
//         console.error('Error during the signup process:', error);
//
//         // 오류 응답 처리
//         return NextResponse.json(
//             { message: 'Unknown error occurred', error: 'Internal Server Error' },
//             { status: 500 }
//         );
//     }
// }
/* eslint-enable */

export {};