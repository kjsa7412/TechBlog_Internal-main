'use client';

import {useQuery} from "react-query";
import {useEffect, useState} from "react";
import {AxiosResponse} from "axios";
import {useRecoilState} from "recoil";

import {EBlank, EBreakPoint} from "@/types/enums/common-enum";
import axiosClient from "@/libs/axiosClient";
import {IPostData} from "@/types/interfaces/post-interface";
import useBreakPoint from "@/hooks/useBreakPoint";
import {IApiState} from "@/types/interfaces/api-interface";
import {apiAtom} from "@/atoms/apiAtom";

import Label from "@/components/label/Label";
import RowPost from "@/components/post/RowPost";
import Blank from "@/components/blank/Blank";
import RowPostMd from "@/components/post/RowPostMd";

const allPostAPI = ():Promise<AxiosResponse<IPostData[]>>  => {
    return axiosClient.get('/api/allPost');
};

const AllPost = () => {
    const breakPoint = useBreakPoint();
    const [apiState, setApiState] = useRecoilState<IApiState>(apiAtom);

    const result_allPostAPI = useQuery(
        ["result_allPostAPI"],
        () => allPostAPI(),
        {
            enabled: false,
            cacheTime: Infinity,
            staleTime: Infinity
        }
    )

    useEffect(() => {
        // routerPush 또는 새로고침일 경우에만 조회
        if (!apiState.result_allPostAPI) {
            result_allPostAPI.refetch();
            setApiState((prevState) => ({
                ...prevState,
                result_allPostAPI: true
            }));
        }
    }, []);

    return (
        <>
            <Label text={'전체 게시글'}/>
            {
                result_allPostAPI.status !== 'success' ||
                result_allPostAPI.isFetching === true ?
                    <div style={{width: '100%', height: '200px', borderRadius: '10px', background: 'lightgray'}}/> :
                    result_allPostAPI.data?.data?.map((value: IPostData) =>
                        <>
                            {
                                breakPoint === EBreakPoint.LG ?
                                    <RowPost key={value.slug + value.author + value.datePublished} postData={value}/> :
                                    <RowPostMd key={value.slug + value.author + value.datePublished} postData={value}/>
                            }
                            <Blank type={EBlank.Column} size={60}/>
                        </>
                    )
            }
        </>
    )
}

export default AllPost;