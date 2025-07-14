/* eslint-disable */
/*
* 2024.09.28
* 기존 API 구조상 사용했던 코드
* 프로젝트 최종 배포 시 사용 유무에 따라 제거
*
* */
//
// import { AxiosResponse } from "axios";
// import { NextRequest, NextResponse } from "next/server";
//
// import axiosServer from "@/libs/axiosServer";
// import { EJWT } from "@/types/enums/common-enum";
// import { IAPIResponse } from "@/types/interfaces/common-interface";
// import {IParam_UserEdit, IResult_UserJoin} from "@/types/interfaces/user-interface";
//
// async function fetchSignEdit(param: IParam_UserEdit, authToken: string, renewToken: string): Promise<AxiosResponse<IAPIResponse<IResult_UserJoin>>> {
//     return await axiosServer.post('/private/post/user/signEdit', param, {
//         headers: {
//             Authorization: "Bearer " + authToken, // Access 토큰 전달
//             Cookie: EJWT.RefreshToken + "=" + renewToken // Refresh 토큰 전달
//         }
//     });
// }
//
// export async function POST(req: NextRequest) {
//     // 쿠키에서 토큰 추출
//     const authToken = req.cookies.get(EJWT.AccessToken)?.value || '';
//     const renewToken = req.cookies.get(EJWT.RefreshToken)?.value || '';
//
//     // 요청 바디에서 파라미터 추출
//     const param: IParam_UserEdit = await req.json();
//
//     // Axios로부터 응답을 받아 그대로 반환
//     const response = await fetchSignEdit(param, authToken, renewToken);
//
//     // 응답 데이터와 상태 코드를 그대로 반환
//     return NextResponse.json(response.data, { status: response.status });
// }
/* eslint-enable */

export {};