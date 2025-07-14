'use client';

import {useEffect, useRef} from "react";
import {useMutation} from "react-query";
import {useParams, usePathname} from "next/navigation";
import {AxiosResponse} from "axios";
import {useRecoilState} from "recoil";

import usePopup from "@/hooks/usePopup";
import useActionAndNavigate from "@/hooks/useActionAndNavigate";
import {EElementId, EModalMutationStatus, EPopup} from "@/types/enums/common-enum";
import axiosServer from "@/libs/axiosServer";
import {IAPIResponse} from "@/types/interfaces/common-interface";
import {IParam_DeletePost, IResult_DeletePost} from "@/types/interfaces/post-interface";
import {IModalMutation} from "@/types/interfaces/modal-interface";
import {modalMutationAtom} from "@/atoms/modalMutationAtom";

import styles from './BoardOptionPopup.module.scss';
import Overlay from "@/components/overlay/Overlay";

// 게시물 삭제 함수
async function serverAPI_DeletePost(param: IParam_DeletePost): Promise<AxiosResponse<IAPIResponse<IResult_DeletePost>>> {
    return await axiosServer.post('/private/post/boad/dlte', param);
}

const BoardOptionPopup = () => {
    const pathname = usePathname();
    const actionAndNavigate = useActionAndNavigate();
    const targetRef = useRef(null);
    const popupController = usePopup();
    const baseUrl = process.env.NEXT_PUBLIC_API_BASE_URL;
    const onClick = () => actionAndNavigate.actionAndNavigate(`${baseUrl}/${pathname}/edit`);
    const {slug} = useParams();
    const [, setRcModalMutation] = useRecoilState<IModalMutation>(modalMutationAtom);

    useEffect(() => {
        const updatePosition = () => {
            const targetElement = document.getElementById(EElementId.BoardOption);
            if (targetElement) {
                const rect = targetElement.getBoundingClientRect();

                if (targetRef.current) {
                    const element = targetRef.current as HTMLDivElement;
                    const thisRect = element.getBoundingClientRect();
                    popupController.openPopup(EPopup.BoardOption, {
                        position: {
                            top: rect.bottom - 10,
                            left: rect.left - thisRect.width + 30
                        }
                    });
                } else {
                    popupController.openPopup(EPopup.BoardOption);
                }
            }
        };

        if (popupController.isPopupOpen(EPopup.BoardOption)) {
            window.addEventListener('resize', updatePosition);
            updatePosition();
        }

        return () => {
            window.removeEventListener('resize', updatePosition);
        };
    }, [popupController.isPopupOpen(EPopup.BoardOption)]);

    useEffect(() => {
        const handleClickOutside = (event: MouseEvent) => {
            if (targetRef.current) {
                const element = targetRef.current as HTMLDivElement;
                if (!element.contains(event.target as Node)) {
                    popupController.closePopup(EPopup.BoardOption);
                }
            }
        };

        // document에 이벤트 리스너 추가
        document.addEventListener('mousedown', handleClickOutside);

        // 컴포넌트 언마운트 시 이벤트 리스너 제거
        return () => {
            document.removeEventListener('mousedown', handleClickOutside);
        };
    }, []);

    // 게시물 삭제 핸들러
    const handleDeletePost = () => {
        popupController.closeAll();
        setRcModalMutation(() => ({
            resultMutation: deletePost,
            message: "삭제",
            desc: "게시물을 삭제하시겠습니까?",
            modalMutationStatus: EModalMutationStatus.Confirm
        }));
    };

    // 게시물 삭제 Mutation
    const deletePost = useMutation(() => {
            return serverAPI_DeletePost({boadid: parseInt(slug as string, 10)});
        },
        {
            onSuccess: (data) => {
                if (data.data.isError) {
                    setRcModalMutation(() => ({
                        resultMutation: deletePost,
                        message: "삭제",
                        desc: "게시물 삭제에 실패했습니다. 담당자에게 연락해주세요.",
                        modalMutationStatus: EModalMutationStatus.Error
                    }));
                } else {
                    setRcModalMutation(() => ({
                        resultMutation: null,
                        message: "",
                        desc: "",
                        modalMutationStatus: EModalMutationStatus.Close
                    }));

                    actionAndNavigate.actionAndNavigate('/');
                }
            },
            onError: () => {
                setRcModalMutation(() => ({
                    resultMutation: deletePost,
                    message: "삭제",
                    desc: "전산 오류입니다. 담당자에게 연락해주세요.",
                    modalMutationStatus: EModalMutationStatus.Error
                }));
            }
        }
    );

    return (
        <>
            {
                popupController.isPopupOpen(EPopup.BoardOption) &&
                <Overlay>
                    <div ref={targetRef} className={styles.baseContainer} style={{
                        top: popupController.getPopupData(EPopup.BoardOption).position.top,
                        left: popupController.getPopupData(EPopup.BoardOption).position.left
                    }}>
                        <button className={styles.itemContainer} onClick={onClick}>
                            Edit Board
                        </button>
                        <div className={styles.line}/>
                        <button className={`${styles.itemContainer} ${styles.warn}`} onClick={handleDeletePost}>
                            Delete Board
                        </button>
                    </div>
                </Overlay>
            }
        </>
    )
}

export default BoardOptionPopup;