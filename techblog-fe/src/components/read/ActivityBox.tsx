'use client'

import {AxiosResponse} from "axios";
import {useEffect} from "react";
import {useQuery, useQueryClient} from "react-query";

import {IAPIResponse} from "@/types/interfaces/common-interface";
import axiosServer from "@/libs/axiosServer";
import {IParam_CmmtList, IResult_CmmtList} from "@/types/interfaces/post-interface";

import Reaction from "@/components/read/Reaction";
import WriteComment from "@/components/read/WriteComment";
import ReadComment from "@/components/read/ReadComment";
import styles from "@/components/read/ActivityBox.module.scss";

interface Props {
    slug: string;
}

async function serverAPI_CmmtList(param: IParam_CmmtList): Promise<AxiosResponse<IAPIResponse<IResult_CmmtList[]>>> {
    return await axiosServer.get('/public/get/cmmt/list', {params: param});
}

const ActivityBox = ({slug}: Props) => {

    // slug가 바뀌면 동작하는 쿼리라서 routerPush 또는 새로고침일 경우에만 조회됨
    const result_CmmtList = useQuery(
        ["serverAPI_CmmtList", slug],
        () => serverAPI_CmmtList({boadId: parseInt(slug)})
    )

    const handleAddComment = () => {
        result_CmmtList.refetch();
    };

    const comments = result_CmmtList?.data?.data?.content ?? [];

    return (
        <>
            <Reaction slug={slug} commentCount={comments.length.toString()} />
            <WriteComment slug={slug} resetCommentList={handleAddComment} />

            {result_CmmtList.isError ? (
                <div className={styles.baseContainer}>
                    댓글 조회에 실패하였습니다.
                </div>
            ) : comments.length === 0 ? (
                <div className={styles.baseContainer}>
                    작성된 댓글이 존재하지 않습니다.
                </div>
            ) : (
                comments.map((comment: IResult_CmmtList) => (
                    <ReadComment key={comment.cmtId} comment={comment} resetCommentList={handleAddComment} />
                ))
            )}
        </>
    )
}

export default ActivityBox;