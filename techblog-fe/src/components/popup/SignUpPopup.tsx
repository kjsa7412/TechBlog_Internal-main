'use client';

import {FormProvider, useForm} from 'react-hook-form';
import {useEffect} from "react";
import {useMutation} from "react-query";
import {AxiosResponse} from "axios";

import {EBlank, EButtonShape, EButtonSize, EButtonType, EInputShape, EPopup} from "@/types/enums/common-enum";
import usePopup from "@/hooks/usePopup";
import {IParam_UserSignUp, IResult_UserSignUp} from "@/types/interfaces/user-interface";
import {isValidEmail, isValidPassword} from "@/utils/commonUtil";
import {IAPIResponse} from "@/types/interfaces/common-interface";
import axiosServer from "@/libs/axiosServer";

import styles from './SignUpPopup.module.scss';
import CloseButton from "@/components/button/CloseButton";
import TextButton from "@/components/button/TextButton";
import Overlay from "@/components/overlay/Overlay";
import Input from "@/components/input/Input";
import Blank from "@/components/blank/Blank";

async function serverAPI_signUp(param: IParam_UserSignUp): Promise<AxiosResponse<IAPIResponse<IResult_UserSignUp>>> {
    return await axiosServer.post('/public/post/user/signUp', param);
}

const SignUpPopup = () => {
    const popupController = usePopup();

    const closePopup = () => popupController.closePopup(EPopup.SignUp);

    const openPopup = () => {
        popupController.closePopup(EPopup.SignUp);
        popupController.openPopup(EPopup.SignIn);
    }

    const methods = useForm({
        mode: 'onSubmit',
        defaultValues: {
            id: "",
            pw: ""
        },
    });

    const signUp = useMutation(
        () => {
            const data = methods.getValues();
            return serverAPI_signUp({userId: data.id, userPw: data.pw});
        },
        {
            onSuccess: (data) => {
                if (data.data.isError) {
                    popupController.openPopup(EPopup.Notify, {
                        contents: {
                            title: "회원가입 실패",
                            desc: `${data.data.errorMsg}`
                        }
                    });
                } else {
                    closePopup();
                    popupController.openPopup(EPopup.Notify, {contents: {title: "회원가입 완료", desc: "회원가입을 성공했습니다."}});
                }
            },
            onError: () => {
                popupController.openPopup(EPopup.Notify, {contents: {title: "회원가입 실패", desc: "전산 오류입니다. 담당자에게 연락해주세요."}});
            }
        }
    );

    // validation
    const handleFunction = () => {
        const data = methods.getValues();

        if (!data.id) {
            popupController.openPopup(EPopup.Notify, {contents: {title: "회원가입 실패", desc: "이메일을 입력해주세요."}});
            return;
        }

        if (!data.pw) {
            popupController.openPopup(EPopup.Notify, {contents: {title: "회원가입 실패", desc: "비밀번호를 입력해주세요."}});
            return;
        }

        if(!isValidEmail(data.id)) {
            popupController.openPopup(EPopup.Notify, {contents: {title: "회원가입 실패", desc: "올바른 이메일 형식이 아닙니다."}});
            return;
        }

        if(!isValidPassword(data.pw)) {
            popupController.openPopup(EPopup.Notify, {contents: {title: "회원가입 실패", desc: "비밀번호는 영어, 숫자, 특수문자로 이루어져야합니다."}});
            return;
        }

        signUp.mutate();
    };

    useEffect(() => {
        return () => {
            methods.reset();
        };
    }, [popupController.isPopupOpen(EPopup.SignUp)]);

    return (
        <>
            {
                popupController.isPopupOpen(EPopup.SignUp) &&
                <Overlay>
                    <div className={styles.baseContainer}>
                        <div className={styles.header}>
                            <CloseButton onClick={closePopup}/>
                        </div>
                        <FormProvider {...methods}>
                            <form className={styles.body} onSubmit={methods.handleSubmit(handleFunction)}
                                  autoComplete="off">
                                <div className={styles.body_title}>
                                    Join Us
                                </div>
                                <div className={styles.body_button}>
                                    <Input width="300" formDataName="id" placeholder="email" shape={EInputShape.Square}/>
                                </div>
                                <div className={styles.body_button}>
                                    <Input width="300" formDataName="pw" placeholder="password" type="password" shape={EInputShape.Square}/>
                                </div>
                                <div className={styles.body_button}>
                                    <TextButton
                                        controller={{
                                            isSubmit: true,
                                            label: "Sign Up",
                                            isLoading: (signUp.status !== 'success' && signUp.status !== 'idle')
                                        }}
                                        styles={{
                                            size: EButtonSize.Large,
                                            shape: EButtonShape.Square,
                                            type: EButtonType.Black
                                        }}/>
                                </div>
                                <div className={styles.body_button}>
                                    <TextButton
                                        controller={{
                                            onClick: openPopup,
                                            label: "Sign In"
                                        }}
                                        styles={{
                                            size: EButtonSize.Large,
                                            shape: EButtonShape.Square,
                                            type: EButtonType.None,
                                        }}/>
                                </div>
                            </form>
                        </FormProvider>
                        <Blank type={EBlank.Column} size={40}/>
                    </div>
                </Overlay>
            }
        </>
    )
}

export default SignUpPopup;



