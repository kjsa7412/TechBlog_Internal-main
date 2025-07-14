'use client';

import {FormProvider, useForm} from 'react-hook-form';
import {useEffect, useState, useRef} from "react";
import {useMutation} from "react-query";
import {useRecoilState} from "recoil";
import {AxiosResponse} from "axios";

import {EBlank, EButtonShape, EButtonSize, EButtonType, EIcon, EInputShape, EPopup} from "@/types/enums/common-enum";
import usePopup from "@/hooks/usePopup";
import {IParam_UserEdit} from "@/types/interfaces/user-interface";
import {IAPIResponse, IUser} from "@/types/interfaces/common-interface";
import {userAtom} from "@/atoms/userAtom";
import axiosServer from "@/libs/axiosServer";
import {IMG} from "@/contants/common";

import styles from './EditProfilePopup.module.scss';
import CloseButton from "@/components/button/CloseButton";
import TextButton from "@/components/button/TextButton";
import Overlay from "@/components/overlay/Overlay";
import Icons from "@/components/Icons";
import Input from "@/components/input/Input";
import Blank from "@/components/blank/Blank";

async function serverAPI_signEdit(formData: FormData): Promise<AxiosResponse<IAPIResponse<IUser>>> {
    return await axiosServer.post('/private/post/user/signEdit', formData, {
        headers: {
            'Content-Type': 'multipart/form-data'
        }
    });
}

const EditProfilePopup = () => {
    const popupController = usePopup();
    const closePopup = () => popupController.closePopup(EPopup.EditProfile);
    const [rcUser, setRcUser] = useRecoilState<IUser>(userAtom);

    const methods = useForm({
        mode: 'onSubmit',
        defaultValues: {
            name: rcUser?.userName || "",
            desc: rcUser?.profileCont || ""
        }
    });

    useEffect(() => {
        if (rcUser) {
            methods.reset({
                name: rcUser.userName || "",
                desc: rcUser.profileCont || ""
            });
        }
    }, [rcUser, methods]);

    // 사진 업로드 변수
    const fileInputRef = useRef<HTMLInputElement | null>(null);
    const [imageSrc, setImageSrc] = useState<string | null>(null); // 이미지 소스를 문자열로 관리

    const handleAvatarClick = () => {
        if (fileInputRef.current) {
            fileInputRef.current.click(); // 클릭 시 파일 입력 필드 열기
        }
    };

    const handleFileChange = (event: React.ChangeEvent<HTMLInputElement>) => {
        const file = event.target.files?.[0]; // 선택된 파일 가져오기
        if (file) {
            const reader = new FileReader();
            reader.onload = (e) => {
                setImageSrc(e.target?.result as string); // 미리보기 이미지 소스 설정
            };
            reader.readAsDataURL(file); // 이미지 파일을 Data URL로 읽기
        }
    };

    const handleDelete = () => {
        setImageSrc("del");
        if (fileInputRef.current) {
            fileInputRef.current.value = ""; // Clear the file input
        }
    }

    const signEdit = useMutation(
        () => {
            const data = methods.getValues();
            const formData = new FormData();

            // 사용자 정보 수정 파라미터
            const param: IParam_UserEdit = {
                userName: data.name,
                profilePic: rcUser.profilePic,
                profileCont: data.desc,
                // 이미지 삭제 요청 확인 변수
                delState: imageSrc === "del" ? "del" : ""
            }
            formData.append("param", new Blob([JSON.stringify(param)], {
                type: "application/json"
            }));

            // 이미지 파라미터
            if (imageSrc && imageSrc !== "del") {
                const fileInput = fileInputRef.current;
                if (fileInput?.files) {
                    const file = fileInput.files[0];
                    formData.append('image', file);
                }
            }

            return serverAPI_signEdit(formData);
        },
        {
            onSuccess: (data) => {
                if (data.data.isError) {
                    popupController.openPopup(EPopup.Notify, {
                        contents: {
                            title: "회원정보 변경 실패",
                            desc: `${data.data.errorMsg}`
                        }
                    });
                    signEdit.reset();
                } else {
                    closePopup();
                    const {...content} = data.data.content;
                    setRcUser({...content});
                    popupController.openPopup(EPopup.Notify, {contents: {title: "회원정보 변경 성공", desc: "회원정보를 변경 하였습니다."}});
                }
            },
            onError: () => {
                signEdit.reset();
                popupController.openPopup(EPopup.Notify, {contents: {title: "회원정보 변경 실패", desc: "전산 오류입니다. 담당자에게 연락해주세요."}});
            }
        }
    );

    const handleFunction = () => {
        const data = methods.getValues();

        if (!data.name) {
            popupController.openPopup(EPopup.Notify, {contents: {title: "회원정보 수정 실패", desc: "사용자 이름을 입력해주세요."}});
            return;
        }

        signEdit.mutate();
    };

    useEffect( () => {
        return () => {
            setImageSrc(null);
            methods.reset();
        };
    },[popupController.isPopupOpen(EPopup.EditProfile)]);

    return (
        <>
            {
                popupController.isPopupOpen(EPopup.EditProfile) &&
                <Overlay>
                    <div className={styles.baseContainer}>
                        <div className={styles.header}>
                            <CloseButton onClick={closePopup}/>
                        </div>
                        <div className={styles.title}>
                            Profile
                        </div>
                        <FormProvider {...methods}>
                            <form className={styles.body} onSubmit={methods.handleSubmit(handleFunction)}
                                  autoComplete="off">
                                <div className={styles.label} style={{marginTop: '0px'}}>
                                    Photo
                                </div>
                                <div className={styles.profileContainer}>
                                    <div role="button" tabIndex={0} className={styles.avatarContainer}
                                         onClick={handleAvatarClick}>
                                        {rcUser.profilePicPath && imageSrc !== "del" && imageSrc === null ? (
                                            <Icons iconType={EIcon.Avatar} width={80} height={80}
                                                   fill={IMG.DefaultPath + rcUser.profilePicPath}/>
                                        ) : imageSrc && imageSrc !== "del" ? (
                                            <img className={styles.avatarImage} src={imageSrc}/>// 미리보기 이미지 사용
                                        ) : (
                                            <Icons iconType={EIcon.Avatar} width={80} height={80} fill={'#C0C0C0'}/>
                                        )}
                                    </div>
                                    <div className={styles.infoContainer}>
                                        <div role="button" tabIndex={0} className={styles.delete}
                                             style={{marginBottom: '8px'}} onClick={handleDelete}>Delete
                                        </div>
                                        <p className={styles.desc}>
                                            Recommended: Square JPG, PNG, or GIF, at least 1,000 pixels per side.
                                        </p>
                                    </div>
                                </div>
                                <div className={styles.label}>
                                    Name *
                                </div>
                                <Input width="100%" formDataName="name" placeholder="Name" shape={EInputShape.Square}
                                       textAlign="left" maxLength={10}/>
                                <div className={styles.label}>
                                    Description
                                </div>
                                <Input width="100%" formDataName="desc" placeholder="Description"
                                       shape={EInputShape.Square} textAlign="left" maxLength={250}/>
                                <div className={styles.button}>
                                    <TextButton
                                        controller={{
                                            isSubmit: true,
                                            label: "Save",
                                            isLoading: (signEdit.status !== 'success' && signEdit.status !== 'idle')
                                        }}
                                        styles={{
                                            size: EButtonSize.Medium,
                                            shape: EButtonShape.Round,
                                            type: EButtonType.Black
                                        }}/>
                                </div>
                            </form>
                        </FormProvider>
                        <Blank type={EBlank.Column} size={40}/>
                    </div>
                    <input
                        type="file"
                        ref={fileInputRef}
                        style={{display: 'none'}}
                        multiple
                        accept="image/*"
                        onChange={handleFileChange}
                    />
                </Overlay>
            }
        </>
    )
}

export default EditProfilePopup;



