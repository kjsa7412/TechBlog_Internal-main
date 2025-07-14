'use client'

import {useEffect, useState} from "react";
import {useRecoilState} from "recoil";
import {AxiosResponse} from "axios";
import {useMutation, useQuery} from "react-query";

import {EBlank, EIcon, EPopup} from "@/types/enums/common-enum";
import {darkModeAtom} from "@/atoms/darkModeAtom";
import {
    IParam_CountList,
    IParam_UpdateLike,
    IResult_CountList,
    IResult_UpdateLike
} from "@/types/interfaces/post-interface";
import {IAPIResponse, ILogin} from "@/types/interfaces/common-interface";
import axiosServer from "@/libs/axiosServer";
import usePopup from "@/hooks/usePopup";
import {loginAtom} from "@/atoms/loginAtom";

import styles from './Like.module.scss';
import Icons from "@/components/Icons";
import Blank from "@/components/blank/Blank";

interface Props {
    slug : string;
}

async function serverAPI_CountList(param: IParam_CountList): Promise<AxiosResponse<IAPIResponse<IResult_CountList>>> {
    return await axiosServer.get('/public/get/boad/countList', { params: param });
}

async function serverAPI_UpdateLike(param: IParam_UpdateLike): Promise<AxiosResponse<IAPIResponse<IResult_UpdateLike>>> {
    return await axiosServer.post('/private/post/boad/like', param);
}

const Like = ({ slug }: Props) => {
    const [rcDarkMode, setRcDarkMode] = useRecoilState(darkModeAtom);
    const [rcLogin, setRcLogin] = useRecoilState<ILogin>(loginAtom);
    const popupController = usePopup();
    const [likeCount, setLikeCount] = useState('0');
    const [isLiked, setIsLiked] = useState(false);
    const [isAnimating, setIsAnimating] = useState(false);
    const [isHovered, setIsHovered] = useState(false); // Hover 상태 추가

    // slug가 바뀌면 동작하는 쿼리라서 routerPush 또는 새로고침일 경우에만 조회됨
    const result_CountList = useQuery(
        ["result_CountList", slug],
        () => serverAPI_CountList({ boadId: parseInt(slug) }),
        {
            enabled: false
        }
    );

    useEffect(() => {
        result_CountList.refetch();
    }, [slug]);

    useEffect(() => {
        const likeCnt = result_CountList.data?.data?.content?.likeCnt ?? '0';
        setLikeCount(likeCnt);
    }, [result_CountList.data]);

    const updateLike = useMutation(
        () => {
            return serverAPI_UpdateLike({ boadid: parseInt(slug) });
        },
        {
            onMutate: () => {
                setIsLiked(true);
                setIsAnimating(true);
            },
            onSuccess: (data) => {
                if (data.data.isError) {
                    popupController.openPopup(EPopup.Notify, {
                        contents: {
                            title: "게시물 좋아요 실패",
                            desc: "게시물 좋아요를 실패했습니다.",
                        },
                    });
                    setIsLiked(false);
                    setIsAnimating(false);
                    setIsHovered(false);
                } else {
                    setLikeCount(data.data.content.likeCnt);
                    
                    // 정상 응답의 경우 0.5초 뒤에 css 변경
                    setTimeout(() => {
                        setIsLiked(false);
                        setIsAnimating(false);
                    }, 500);
                }
            },
            onError: () => {
                popupController.openPopup(EPopup.Notify, {
                    contents: {
                        title: "게시물 좋아요 실패",
                        desc: "전산 오류입니다. 담당자에게 연락해주세요.",
                    },
                });
                setIsLiked(false);
                setIsAnimating(false);
                setIsHovered(false);
            },
        }
    );

    const handleClick = () => {
        if (!rcLogin.isLogin) {
            popupController.openPopup(EPopup.SignIn);
            return;
        }

        // 좋아요 요청을 보낸 상태이면 이벤트 중지
        if (isLiked) {
            return;
        }

        updateLike.mutate();
    };

    return (
        <div role="button"
             tabIndex={0}
             className={styles.baseContainer}
             onClick={handleClick}
             onMouseEnter={() => setIsHovered(true)} // Hover 시작
             onMouseLeave={() => setIsHovered(false)} // Hover 종료
        >
            <Icons
                iconType={isLiked ? EIcon.Like2 : EIcon.Like1}
                width={24}
                height={24}
                // 클릭 시 빨간색, 호버 시 노랑 또는 검정(다크모드 탐지), 기본 회색
                fill={isLiked ? '#D9381E' : isHovered ? rcDarkMode.isDark ? '#adff2f' : '#000000' : '#C0C0C0'}
                styleTag={`${isAnimating ? styles.popLike : ''} ${!isLiked ? styles.hoverEffect : ''}`}
            />
            <Blank type={EBlank.Row} size={10} />
            {likeCount}
        </div>
    );
}

export default Like;