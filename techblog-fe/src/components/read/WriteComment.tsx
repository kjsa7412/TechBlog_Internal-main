'use client'

import {AxiosResponse} from "axios";
import {useMutation} from "react-query";
import React, {useCallback, useState} from "react";
import {useRecoilState} from "recoil";

import {EButtonShape, EButtonSize, EButtonType, EPopup} from "@/types/enums/common-enum";
import {IAPIResponse, ILogin} from "@/types/interfaces/common-interface";
import axiosServer from "@/libs/axiosServer";
import {IParam_InsertCmmt, IResult_InsertCmmt} from "@/types/interfaces/post-interface";
import usePopup from "@/hooks/usePopup";
import {loginAtom} from "@/atoms/loginAtom";

import styles from './WriteComment.module.scss';
import TextButton from "@/components/button/TextButton";

interface Props {
    slug: string;
    resetCommentList: () => void;
}

async function serverAPI_InsertComment(param: IParam_InsertCmmt): Promise<AxiosResponse<IAPIResponse<IResult_InsertCmmt>>> {
    return await axiosServer.post('/private/post/cmmt/inst', param);
}

const WriteComment = ({slug, resetCommentList}: Props) => {
    const [cmmtValue, setCmmtValue] = useState('');
    const popupController = usePopup();
    const [rcLogin, setRcLogin] = useRecoilState<ILogin>(loginAtom);
    const handleChange = useCallback((e: React.ChangeEvent<HTMLTextAreaElement>) => {
        setCmmtValue(e.target.value);
    }, []);

    const resultMutation_insertCmmt = useMutation(
        () => serverAPI_InsertComment({boadId: parseInt(slug), cmt: cmmtValue}),
        {
            onSuccess: (data) => {
                if (data.data.isError) {
                    popupController.openPopup(EPopup.Notify, {contents: {title: "댓글 작성 실패", desc: "댓글 작성을 실패하였습니다."}});
                } else {
                    // 리턴받은 댓글 목록 이용하여 mutation 초기화
                    resetCommentList();
                    setCmmtValue('');
                }
            },
            onError: () => {
                popupController.openPopup(EPopup.Notify, {
                    contents: {
                        title: "댓글 작성 실패",
                        desc: "전산 오류입니다. 담당자에게 연락해주세요."
                    }
                });
            }
        }
    );

    const validation = () => {
        if (!cmmtValue) {
            popupController.openPopup(EPopup.Notify, {contents: {title: "댓글 작성", desc: "댓글을 작성해주세요."}});
            return;
        }

        resultMutation_insertCmmt.mutate();
    };

    return (
        <div className={styles.baseContainer}>
             <textarea
                 disabled={!rcLogin.isLogin}
                 className={styles.textBox}
                 placeholder={rcLogin.isLogin ? '댓글을 입력해주세요.' : '로그인이 필요한 서비스입니다.'}
                 value={cmmtValue}
                 onChange={handleChange}
             />
            <div className={styles.buttonContainer}>
                <TextButton
                    controller={{
                        onClick: validation,
                        isOpen: rcLogin.isLogin,
                        label: "등록",
                        isLoading: resultMutation_insertCmmt.isLoading
                    }}
                    styles={{
                        size: EButtonSize.Small,
                        shape: EButtonShape.Round,
                        type: EButtonType.Black
                    }}/>
            </div>
        </div>
    )
}

export default WriteComment;