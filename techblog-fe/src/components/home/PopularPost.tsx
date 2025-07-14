'use client';

import {AxiosResponse} from "axios";
import {useEffect} from "react";
import {useQuery} from "react-query";
import {useRecoilState} from "recoil";

import {IPostData} from "@/types/interfaces/post-interface";
import axiosClient from "@/libs/axiosClient";
import useBreakPoint from "@/hooks/useBreakPoint";
import {EBreakPoint} from "@/types/enums/common-enum";
import {IApiState} from "@/types/interfaces/api-interface";
import {apiAtom} from "@/atoms/apiAtom";

import Label from "@/components/label/Label";
import ColumnPostMotion from "@/components/post/ColumnPostMotion";
import ColumnPostSlider from "@/components/post/ColumnPostSlider";

const popPost = (): Promise<AxiosResponse<IPostData[]>> => {
    return axiosClient.get('/api/popPost');
};

const PopularPost = () => {
    const breakPoint = useBreakPoint();
    const [apiState, setApiState] = useRecoilState<IApiState>(apiAtom);

    const result_popPostAPI = useQuery(
        ["result_popPostAPI"],
        () => popPost(),
        {
            enabled: false,
            cacheTime: Infinity,
            staleTime: Infinity
        }
    );

    useEffect(() => {
        // routerPush 또는 새로고침일 경우에만 조회
        if (!apiState.result_popPostAPI) {
            result_popPostAPI.refetch();
            setApiState((prevState) => ({
                ...prevState,
                result_popPostAPI: true
            }));
        }
    }, []);

    return (
        <>
            <Label text={'인기 있는 글'}/>
            {
                result_popPostAPI.status !== 'success' ||
                result_popPostAPI.isFetching ?
                    <div style={{width: '100%', height: '388px', borderRadius: '10px', background: 'lightgray'}}/> :
                    (
                        breakPoint === EBreakPoint.LG ?
                            <ColumnPostMotion posts={result_popPostAPI.data?.data}/> :
                            <ColumnPostSlider posts={result_popPostAPI.data?.data}/>
                    )
            }
        </>
    )
}

export default PopularPost;