'use client';

import {useRecoilState, useResetRecoilState} from "recoil";
import {FormProvider, useForm} from 'react-hook-form';
import {useEffect} from "react";
import {useMutation} from "react-query";
import {AxiosResponse} from "axios";

import {EBlank, EButtonShape, EButtonSize, EButtonType, EInputShape, EPopup} from "@/types/enums/common-enum";
import {IAPIResponse, ILogin, IUser} from "@/types/interfaces/common-interface";
import {loginAtom} from "@/atoms/loginAtom";
import {userAtom} from "@/atoms/userAtom";
import usePopup from "@/hooks/usePopup";
import {IParam_UserSignIn} from "@/types/interfaces/user-interface";
import {isValidEmail} from "@/utils/commonUtil";
import axiosServer from "@/libs/axiosServer";

import Input from "@/components/input/Input";
import Overlay from "@/components/overlay/Overlay";
import TextButton from "@/components/button/TextButton";
import CloseButton from "@/components/button/CloseButton";
import styles from './SignInPopup.module.scss';
import Blank from "@/components/blank/Blank";

async function serverAPI_signIn(param: IParam_UserSignIn): Promise<AxiosResponse<IAPIResponse<IUser>>> {
    return await axiosServer.post('/public/post/auth/signIn', param);
}

const SignInPopup = () => {
    const [rcLogin, setRcLogin] = useRecoilState<ILogin>(loginAtom);
    const [rcUser, setRcUser] = useRecoilState<IUser>(userAtom);
    const rcResetLogin = useResetRecoilState(loginAtom);
    const rcResetUser = useResetRecoilState(userAtom);

    const popupController = usePopup();

    const closePopup = () => popupController.closePopup(EPopup.SignIn);

    const openPopup = () => {
        popupController.closePopup(EPopup.SignIn);
        popupController.openPopup(EPopup.SignUp);
    }

    const methods = useForm({
        mode: 'onSubmit',
        defaultValues: {
            id: "",
            pw: ""
        },
    });

    const signIn = useMutation(
        () => {
            const data = methods.getValues();
            return serverAPI_signIn({userId: data.id, userPw: data.pw});
        },
        {
            onSuccess: (data) => {
                if (data?.data.isError) {
                    rcResetLogin();
                    rcResetUser();
                    popupController.openPopup(EPopup.Notify, {
                        contents: {
                            title: "로그인 실패",
                            desc: `${data.data.errorMsg}`
                        }
                    });
                } else {
                    const {...content} = data.data.content;
                    popupController.closeAll();
                    setRcLogin({isLogin: true});
                    setRcUser({...content});
                    //popupController.openPopup(EPopup.Notify, {contents: {title: "로그인 성공", desc: "로그인 되었습니다."}});
                }
            },
            onError: () => {
                rcResetLogin();
                rcResetUser();
                popupController.openPopup(EPopup.Notify, {contents: {title: "로그인 실패", desc: "전산 오류입니다. 담당자에게 연락해주세요."}});
            }
        }
    );

    // validation
    const handleFunction = () => {
        const data = methods.getValues();

        if (!data.id) {
            popupController.openPopup(EPopup.Notify, {contents: {title: "로그인 실패", desc: "이메일을 입력해주세요."}});
            return;
        }

        if (!data.pw) {
            popupController.openPopup(EPopup.Notify, {contents: {title: "로그인 실패", desc: "비밀번호를 입력해주세요."}});
            return;
        }

        if (!isValidEmail(data.id)) {
            popupController.openPopup(EPopup.Notify, {contents: {title: "로그인 실패", desc: "올바른 이메일 형식이 아닙니다."}});
            return;
        }

        signIn.mutate();
    };

    useEffect(() => {
        return () => {
            methods.reset();
        };
    }, [popupController.isPopupOpen(EPopup.SignIn)]);

    return (
        <>
            {
                popupController.isPopupOpen(EPopup.SignIn) &&
                <Overlay>
                    <div className={styles.baseContainer}>
                        <div className={styles.header}>
                            <CloseButton onClick={closePopup}/>
                        </div>
                        <FormProvider {...methods}>
                            <form className={styles.body} onSubmit={methods.handleSubmit(handleFunction)}
                                  autoComplete="off">
                                <div className={styles.body_title}>
                                    Welecome Back
                                </div>
                                <div className={styles.body_button}>
                                    <Input width="300" formDataName="id" placeholder="ID" shape={EInputShape.Round}/>
                                </div>
                                <div className={styles.body_button}>
                                    <Input width="300" formDataName="pw" placeholder="PW" type="password" shape={EInputShape.Round}/>
                                </div>
                                <div className={styles.body_button}>
                                    <TextButton
                                        controller={{
                                            isSubmit: true,
                                            label: "Sign In",
                                            isLoading: (signIn.status !== 'success' && signIn.status !== 'idle')
                                        }}
                                        styles={{
                                            size: EButtonSize.Large,
                                            shape: EButtonShape.Round,
                                            type: EButtonType.Black
                                        }}/>
                                </div>
                                <div className={styles.body_button}>
                                    <TextButton
                                        controller={{
                                            onClick: openPopup,
                                            label: "Sign Up"
                                        }}
                                        styles={{
                                            size: EButtonSize.Large,
                                            shape: EButtonShape.Round,
                                            type: EButtonType.None
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

export default SignInPopup;



