'use client';

import {useRecoilState, useRecoilValue} from "recoil";
import {useCallback, useEffect, useRef, useState} from "react";
import {usePathname, useParams} from "next/navigation";
import {AxiosResponse} from "axios";
import {useMutation} from "react-query";

import {EBreakPoint, EButtonType, EElementId, EIcon, EPopup} from "@/types/enums/common-enum";
import {IAPIResponse, ILogin, IUser} from "@/types/interfaces/common-interface";
import {loginAtom} from "@/atoms/loginAtom";
import usePopup from "@/hooks/usePopup";
import useActionAndNavigate from "@/hooks/useActionAndNavigate";
import useBreakPoint from "@/hooks/useBreakPoint";
import {userAtom} from "@/atoms/userAtom";
import {IMG} from "@/contants/common";
import {editorAtom} from "@/atoms/editorAtom";
import axiosServer from "@/libs/axiosServer";
import {editTitleAtom} from "@/atoms/editTitleAtom";
import {
    IParam_InsertPost,
    IParam_UpdatePost,
    IResult_InsertPost,
    IResult_UpdatePost
} from "@/types/interfaces/post-interface";

import Icons from "@/components/Icons";
import styles from "./HeaderItem.module.scss";
import SearchBar from "@/components/search/SearchBar";
import SearchIcon from "@/components/search/SearchIcon";
import Loader from "@/components/loader/Loader";

// 게시물 작성 함수
async function serverAPI_InsertPost(param: IParam_InsertPost): Promise<AxiosResponse<IAPIResponse<IResult_InsertPost>>> {
    return await axiosServer.post('/private/post/boad/inst', param);
}

// 게시물 수정 함수
async function serverAPI_UpdatePost(param: IParam_UpdatePost): Promise<AxiosResponse<IAPIResponse<IResult_UpdatePost>>> {
    return await axiosServer.post('/private/post/boad/updt', param);
}

export const HeaderLogo = () => {
    const actionAndNavigate = useActionAndNavigate();

    const onClick = () => {
        actionAndNavigate.actionAndNavigate(`/`);
    }
    return (
        <button className={styles.logoContainer} onClick={onClick}>
            <p>TECH BLOG</p>
        </button>
    );
};

export const HeaderProfile = () => {
    const [rcLogin, setRcLogin] = useRecoilState<ILogin>(loginAtom);
    const [rcUser, setRcUser] = useRecoilState<IUser>(userAtom);
    const targetRef = useRef(null);
    const popupController = usePopup();
    const [isMounted, setIsMounted] = useState(false); // 마운트 여부 관리

    useEffect(() => {
        setIsMounted(true); // 컴포넌트가 마운트되면 true로 설정
    }, []);

    const togglePopup = () => {
        if (popupController.isPopupOpen(EPopup.ProfileOption)) {
            popupController.closePopup(EPopup.ProfileOption);
        } else {
            if (targetRef.current) {
                const element = targetRef.current as HTMLDivElement;
                const rect = element.getBoundingClientRect();
                popupController.openPopup(EPopup.ProfileOption, {position: {top: rect.bottom, left: rect.left}})
            } else {
                popupController.openPopup(EPopup.ProfileOption);
            }
        }
    };

    const onClick = () => {
        if (rcLogin.isLogin) {
            return togglePopup();
        } else {
            return popupController.openPopup(EPopup.SignIn);
        }
    };

    return (
        <button id={EElementId.HeaderProfile} ref={targetRef} className={styles.profileContainer} onClick={onClick}>
            {isMounted && (rcUser.profilePicPath ?
                <Icons iconType={EIcon.Avatar} width={32} height={32} fill={IMG.DefaultPath + rcUser.profilePicPath}/> :
                <Icons iconType={EIcon.Avatar} width={32} height={32} fill={'#C0C0C0'}/>)}
        </button>
    );
}

export const HeaderAction = () => {
    const [action, setAction] = useState('')
    const [rcLogin] = useRecoilState<ILogin>(loginAtom);
    const actionAndNavigate = useActionAndNavigate();
    const popupController = usePopup();
    const pathName = usePathname();
    const editorRef = useRecoilValue(editorAtom);
    const editTitle = useRecoilValue(editTitleAtom);
    const { slug } = useParams();

    useEffect(() => {
        if (pathName === '/board/new' && !rcLogin.isLogin) {
            actionAndNavigate.actionAndNavigate('/')
            return;
        }
    }, []);

    // 경로 변경을 감지해서 버튼값 설정
    useEffect(() => {
        if (pathName === '/board/new') {
            setAction('Publish');
        } else if (/^\/board\/\d+\/edit$/.test(pathName)) {
            setAction('Publish');
        } else {
            setAction(rcLogin.isLogin ? 'Write' : '');
        }
    }, [rcLogin, pathName]);

    // 게시물 작성 Mutation
    const insertPost = useMutation(
        (formattedContent: string) => {
            return serverAPI_InsertPost({title: editTitle, boadConts: formattedContent});
        },
        {
            onSuccess: (data) => {
                if (data.data.isError) {
                    popupController.openPopup(EPopup.Notify, {
                        contents: {
                            title: "게시물 작성 실패",
                            desc: "게시물 작성에 실패했습니다. 담당자에게 연락해주세요."
                        }
                    });
                } else {
                    actionAndNavigate.actionAndNavigate(`/board/${data.data.content.boadId}`);
                }
            },
            onError: () => {
                popupController.openPopup(EPopup.Notify, {
                    contents: {
                        title: "게시물 작성 실패",
                        desc: "전산 오류입니다. 담당자에게 연락해주세요."
                    }
                });
            }
        }
    );

    // 게시물 수정 Mutation
    const updatePost = useMutation(
        (formattedContent: string) => {
            return serverAPI_UpdatePost({boadid: parseInt(slug as string, 10), title: editTitle, boadConts: formattedContent});
        },
        {
            onSuccess: (data) => {
                if (data.data.isError) {
                    popupController.openPopup(EPopup.Notify, {
                        contents: {
                            title: "게시물 수정 실패",
                            desc: "게시물 수정에 실패했습니다. 담당자에게 연락해주세요."
                        }
                    });
                } else {
                    actionAndNavigate.actionAndNavigate(`/board/${data.data.content.boadId}`);
                }
            },
            onError: () => {
                popupController.openPopup(EPopup.Notify, {
                    contents: {
                        title: "게시물 수정 실패",
                        desc: "전산 오류입니다. 담당자에게 연락해주세요."
                    }
                });
            }
        }
    );

    const onClick = useCallback(() => {
        // 게시물 작성 url 이동
        if (action === 'Write') {
            actionAndNavigate.actionAndNavigate('/board/new');
        } else if (action === 'Publish') {
            if (!editTitle) {
                popupController.openPopup(EPopup.Notify, {contents: {title: `게시물 ${slug ? '수정' : '작성'} 실패`, desc: "제목을 입력해주세요."}});
                return;
            }

            if (!editorRef?.getMarkdown()) {
                popupController.openPopup(EPopup.Notify, {contents: {title: `게시물 ${slug ? '수정' : '작성'} 실패`, desc: "내용을 입력해주세요."}});
                return;
            }

            // md 파일을 html로 출력하기 위한 전처리
            // \n\n이 두번 발생한 경우 &nbsp; 추가
            const lines: string[] = editorRef.getMarkdown().split('\n');
            let formattedContent = '';
            let lineBreakCount = 0;

            for (let i = 0; i < lines.length; i++) {
                formattedContent += lines[i]; // 현재 줄 추가

                if (lines[i] === '') {
                    lineBreakCount++;
                } else {
                    lineBreakCount = 0; // 줄바꿈 카운트 초기화
                }

                // 줄바꿈이 두 번 연속되면 `&nbsp;` 추가
                if (lineBreakCount === 2) {
                    formattedContent += '&nbsp;';
                    lineBreakCount = 0; // 카운트 초기화
                }
                formattedContent += '\n'; // 각 줄 끝에 줄바꿈 추가
            }

            // 글 작성 프로세스
            if (pathName === '/board/new') {
                insertPost.mutate(formattedContent);
            }
            // 글 수정 프로세스
            else if (/^\/board\/\d+\/edit$/.test(pathName)) {
                updatePost.mutate(formattedContent);
            }
        }
    }, [action, actionAndNavigate, editorRef]);

    return (
        <>
            {
                insertPost.isLoading || updatePost.isLoading ?
                    <div className={styles.actionContainer}>
                        <Loader color={`${"var(--color-text-1)"}`}/>
                    </div>
                    :
                    <button className={styles.actionContainer} onClick={onClick}>
                        {action}
                    </button>
            }
        </>
    )
}

export const HeaderSearch = () => {
    const breakPoint = useBreakPoint();
    return (
        <>
            {
                breakPoint === EBreakPoint.LG ?
                    <SearchBar/> : <SearchIcon/>
            }
        </>
    )
}

