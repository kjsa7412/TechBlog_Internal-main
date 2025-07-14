'use client'

import {useRecoilState} from "recoil";
import {AxiosResponse} from "axios";
import {useMutation} from "react-query";

import {EBlank, EIcon, EModalMutationStatus} from "@/types/enums/common-enum";
import {IParam_DeleteCmmt, IResult_CmmtList, IResult_DeleteCmmt} from "@/types/interfaces/post-interface";
import {IMG} from "@/contants/common";
import {IAPIResponse, IUser} from "@/types/interfaces/common-interface";
import {userAtom} from "@/atoms/userAtom";
import axiosServer from "@/libs/axiosServer";
import {IModalMutation} from "@/types/interfaces/modal-interface";
import {modalMutationAtom} from "@/atoms/modalMutationAtom";

import styles from './ReadComment.module.scss';
import Icons from "@/components/Icons";
import Blank from "@/components/blank/Blank";
import Line from "@/components/line/Line";

interface Props {
    comment: IResult_CmmtList; // comment prop 추가
    resetCommentList: () => void;
}

async function serverAPI_DeleteComment(param: IParam_DeleteCmmt): Promise<AxiosResponse<IAPIResponse<IResult_DeleteCmmt>>> {
    return await axiosServer.post('/private/post/cmmt/dlte', param);
}

const ReadComment = ({ comment, resetCommentList }: Props) => {
    const [rcUser, setRcUser] = useRecoilState<IUser>(userAtom);
    const [, setRcModalMutation] = useRecoilState<IModalMutation>(modalMutationAtom);

    const resultMutation_deleteCmmt = useMutation(
        () => serverAPI_DeleteComment({cmtId: comment.cmtId,  boadId: comment.boadId}),
        {
            onSuccess: (data) => {
                if (data.data.isError) {
                    setRcModalMutation(() => ({
                        resultMutation: resultMutation_deleteCmmt,
                        message: "삭제",
                        desc: "전산 오류입니다. 담당자에게 연락해주세요.",
                        modalMutationStatus: EModalMutationStatus.Error
                    }));
                } else {
                    // 팝업 띄우려면 사용
                    // setRcModalMutation(() => ({
                    //     resultMutation: resultMutation_deleteCmmt,
                    //     message: "삭제",
                    //     desc: "댓글이 삭제되었습니다.",
                    //     modalMutationStatus: EModalMutationStatus.Success
                    // }));

                    setRcModalMutation(() => ({
                        resultMutation: null,
                        message: "",
                        desc: "",
                        modalMutationStatus: EModalMutationStatus.Close
                    }));

                    resetCommentList();
                }
            },
            onError: () => {
                setRcModalMutation(() => ({
                    resultMutation: resultMutation_deleteCmmt,
                    message: "삭제",
                    desc: "전산 오류입니다. 담당자에게 연락해주세요.",
                    modalMutationStatus: EModalMutationStatus.Error
                }));
            }
        }
    );

    const handleDeleteComment = () => {
        setRcModalMutation(() => ({
            resultMutation: resultMutation_deleteCmmt,
            message: "삭제",
            desc: "댓글을 삭제하시겠습니까?",
            modalMutationStatus: EModalMutationStatus.Confirm
        }));
    };

    return (
        <div className={styles.baseContainer}>
            <Line/>
            <div className={styles.bodyContainer}>
                <div className={styles.writerInfo}>
                    {comment.profilePicPath ? <Icons iconType={EIcon.Avatar} width={30} height={30} fill={IMG.DefaultPath + comment.profilePicPath} /> : <Icons iconType={EIcon.Avatar} width={30} height={30} fill={'#C0C0C0'} />}
                    <Blank type={EBlank.Row} size={10}/>
                    <p className={styles.writerInfoWriter}>
                        {comment.userName}
                    </p>
                    <Blank type={EBlank.Row} size={10}/>
                    <p className={styles.writerInfoDate}>
                        {comment.writeDate}
                    </p>
                    <Blank type={EBlank.Row} size={10}/>
                    {comment.userId === rcUser.userId &&
                        <button className={styles.delButton} onClick={handleDeleteComment}>
                            delete
                        </button>
                    }
                </div>
                <div className={styles.comment}>
                    {comment.cmt}
                </div>
            </div>
        </div>
    )
}

export default ReadComment;