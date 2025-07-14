'use client';

import {useRecoilState} from "recoil";

import {EBlank, EButtonShape, EButtonSize, EButtonType, EModalMutationStatus, EPopup} from "@/types/enums/common-enum";
import {IModalMutation} from "@/types/interfaces/modal-interface";
import {modalMutationAtom} from "@/atoms/modalMutationAtom";
import useActionAndNavigate from "@/hooks/useActionAndNavigate";

import styles from './ModalMutation.module.scss';
import CloseButton from "@/components/button/CloseButton";
import TextButton from "@/components/button/TextButton";
import Blank from "@/components/blank/Blank";
import Overlay from "@/components/overlay/Overlay";


const ModalMutation = () => {
    const actionAndNavigate = useActionAndNavigate();
    const [rcModalMutation, setRcModalMutation] = useRecoilState<IModalMutation>(modalMutationAtom);

    const successFunction = () => {
        if (rcModalMutation?.navigatePath) {
            actionAndNavigate.actionAndNavigate(rcModalMutation?.navigatePath)
        }
    }

    const closeModal = () => {
        setRcModalMutation((prev) => ({
            ...prev,
            modalMutationStatus: EModalMutationStatus.Close
        }));
    }

    return (
        <>
            {
                rcModalMutation.modalMutationStatus !== EModalMutationStatus.Close &&
                <Overlay>
                    <div className={styles.baseContainer}>
                        <div className={styles.header}>
                            <CloseButton onClick={closeModal}/>
                        </div>
                        <div className={styles.body}>
                            {rcModalMutation.modalMutationStatus === EModalMutationStatus.Confirm &&
                                <>
                                    <div className={styles.body_mainText}>
                                        {rcModalMutation.message + "하시겠습니까?"}
                                    </div>
                                    <div className={styles.body_mainSubText}>
                                        {rcModalMutation.desc}
                                    </div>
                                    <Blank type={EBlank.Column} size={20}/>
                                    <div className={styles.body_buttonContainer}>
                                        <TextButton controller={{
                                            onClick: () => {
                                                setRcModalMutation((prev) => ({
                                                    ...prev,
                                                    modalMutationStatus: EModalMutationStatus.Progress
                                                }));
                                                rcModalMutation.resultMutation.mutate();
                                            }, label: "확인"
                                        }} styles={{
                                            size: EButtonSize.Medium,
                                            shape: EButtonShape.Round,
                                            type: EButtonType.Stroke
                                        }}
                                        />
                                        <Blank type={EBlank.Row}/>
                                        <TextButton controller={{onClick: closeModal, label: "취소"}}
                                                    styles={{
                                                        size: EButtonSize.Medium,
                                                        shape: EButtonShape.Round,
                                                        type: EButtonType.Black
                                                    }}
                                        />
                                    </div>
                                </>
                            }
                            {rcModalMutation.modalMutationStatus === EModalMutationStatus.Progress &&
                                <>
                                    <div className={styles.body_mainText}>
                                        {rcModalMutation.message + "중입니다."}
                                    </div>
                                    <div className={styles.body_mainSubText}>
                                        {rcModalMutation.desc}
                                    </div>
                                    <Blank type={EBlank.Column} size={20}/>
                                    <div className={styles.body_buttonContainer}>
                                        <TextButton controller={{isLoading: true}}
                                                    styles={{
                                                        size: EButtonSize.Large,
                                                        shape: EButtonShape.Round,
                                                        type: EButtonType.Stroke
                                                    }}
                                        />
                                    </div>
                                </>
                            }
                            {rcModalMutation.modalMutationStatus === EModalMutationStatus.Success &&
                                <>
                                    <div className={styles.body_mainText}>
                                        {rcModalMutation.message + "되었습니다."}
                                    </div>
                                    <div className={styles.body_mainSubText}>
                                        {rcModalMutation.desc}
                                    </div>
                                    <Blank type={EBlank.Column} size={20}/>
                                    <div className={styles.body_buttonContainer}>
                                        <TextButton controller={{
                                            onClick: () => {
                                                setRcModalMutation((prev) => ({
                                                    ...prev,
                                                    modalMutationStatus: EModalMutationStatus.Close
                                                }));
                                                rcModalMutation.resultMutation.reset();
                                                successFunction();
                                            }, label: "확인"
                                        }} styles={{
                                            size: EButtonSize.Medium,
                                            shape: EButtonShape.Round,
                                            type: EButtonType.Stroke
                                        }}
                                        />
                                    </div>
                                </>
                            }
                            {rcModalMutation.modalMutationStatus === EModalMutationStatus.Error &&
                                <>
                                    <div className={styles.body_mainText}>
                                        {"실패했습니다."}
                                    </div>
                                    <div className={styles.body_mainSubText}>
                                        {rcModalMutation.desc}
                                    </div>
                                    <Blank type={EBlank.Column} size={20}/>
                                    <div className={styles.body_buttonContainer}>
                                        <TextButton controller={{
                                            onClick: () => {
                                                setRcModalMutation((prev) => ({
                                                    ...prev,
                                                    modalMutationStatus: EModalMutationStatus.Close
                                                }));
                                                rcModalMutation.resultMutation.reset();
                                            }, label: "확인"
                                        }} styles={{
                                            size: EButtonSize.Medium,
                                            shape: EButtonShape.Round,
                                            type: EButtonType.Stroke
                                        }}
                                        />
                                    </div>
                                </>
                            }
                        </div>
                        <Blank type={EBlank.Column} size={40}/>
                    </div>
                </Overlay>
            }
        </>
    )
}

export default ModalMutation;



