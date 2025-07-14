'use client';

import {useEffect} from "react";
import {useQuery} from "react-query";
import {AxiosResponse} from "axios";
import {useRecoilState} from "recoil";

import {EBlank, EBreakPoint} from "@/types/enums/common-enum";
import axiosClient from "@/libs/axiosClient";
import {IPostData} from "@/types/interfaces/post-interface";
import useBreakPoint from "@/hooks/useBreakPoint";
import {apiAtom} from "@/atoms/apiAtom";
import {IApiState} from "@/types/interfaces/api-interface";

import styles from "@/components/search/SearchPost.module.scss";
import Label from "@/components/label/Label";
import RowPost from "@/components/post/RowPost";
import Blank from "@/components/blank/Blank";
import RowPostMd from "@/components/post/RowPostMd";

const searchAPI = (searchTerm: string):Promise<AxiosResponse<IPostData[]>> => {
    return axiosClient.get('/api/search', {
        params: {q: searchTerm}
    });
};

const SearchPost = ({keyword}: { keyword: string }) => {
    const breakPoint = useBreakPoint();
    const [apiState, setApiState] = useRecoilState<IApiState>(apiAtom);

    const result_searchAPI = useQuery(
        ["result_searchAPI"],
        () => searchAPI(keyword),
        {
            enabled: false,
            cacheTime: Infinity,
            staleTime: Infinity
        }
    )

    useEffect(() => {
        // routerPush 또는 새로고침일 경우에만 조회
        if (!apiState.result_searchAPI || keyword !== apiState.result_keyword) {
            result_searchAPI.refetch();
            setApiState((prevState) => ({
                ...prevState,
                result_keyword: keyword,
                result_searchAPI: true
            }));
        }
    }, []);

    return (
        <div className={styles.baseContainer}>
            <Label text={'검색 결과'}/>
            {
                result_searchAPI.status !== 'success' || result_searchAPI.isFetching === true ?
                    <div className={styles.loadingContainer}/> :
                    result_searchAPI.data?.data?.length === 0 ?
                        <div className={styles.notifyContainer}>
                            Make sure all words are spelled correctly. <br/>
                            Try different keywords. <br/>
                            Try more general keywords.
                        </div> :
                        result_searchAPI.data?.data?.map((value: IPostData) =>
                            <>
                                {
                                    breakPoint === EBreakPoint.LG ?
                                        <RowPost key={value.slug + value.author + value.datePublished}
                                                 postData={value}/> :
                                        <RowPostMd key={value.slug + value.author + value.datePublished}
                                                   postData={value}/>
                                }
                                <Blank type={EBlank.Column} size={60}/>
                            </>
                        )
            }
        </div>
    )
}

export default SearchPost;