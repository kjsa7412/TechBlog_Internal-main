'use client';

import {useRecoilState, useResetRecoilState} from "recoil";
import {useEffect, useRef} from "react";
import {AxiosResponse} from "axios";
import {useMutation} from "react-query";

import {EElementId, EPopup} from "@/types/enums/common-enum";
import {userAtom} from "@/atoms/userAtom";
import {loginAtom} from "@/atoms/loginAtom";
import usePopup from "@/hooks/usePopup";
import {IAPIResponse, IUser} from "@/types/interfaces/common-interface";
import useActionAndNavigate from "@/hooks/useActionAndNavigate";
import axiosServer from "@/libs/axiosServer";

import styles from "./ProfileOptionPopup.module.scss";
import Overlay from "@/components/overlay/Overlay";

async function serverAPI_signOut(): Promise<AxiosResponse<IAPIResponse<IUser>>> {
    return await axiosServer.post('/private/post/auth/signOut');
}

const ProfileOptionPopup = () => {
    const actionAndNavigate = useActionAndNavigate();
    const targetRef = useRef(null);
    const popupController = usePopup();
    const [rcUser, setRcUser] = useRecoilState<IUser>(userAtom);
    const resetRcUserAtom = useResetRecoilState(userAtom);
    const resetRcLoginAtom = useResetRecoilState(loginAtom);

    const signOut = useMutation(
        () => {
            return serverAPI_signOut();
        },
        {
            onSettled: () => {
                actionAndNavigate.actionAndNavigate(`/`);
                resetRcLoginAtom();
                resetRcUserAtom();
            }
        }
    );

    const handleSignOut = () => {
        //todo
        // 로그아웃 시 yse no 팝업을 사용하려는 경우, yes no 팝업 전역 기능 구현 뒤 사용
        // if (true) {
        //     popupController.openPopup(EPopup.Confirm, {contents: {title: "로그아웃", desc: "로그아웃 하시겠습니까?", yesLabel: "YES", noLabel: "NO"}});
        //     return;
        // }

        signOut.mutate();
    };

    const editProfile = () => {
        popupController.closePopup(EPopup.ProfileOption);
        popupController.openPopup(EPopup.EditProfile);
    }

    const onClick = () => {
        actionAndNavigate.actionAndNavigate(`/board/search/${rcUser.userId}`);
    }

    useEffect(() => {
        const updatePosition = () => {
            const targetElement = document.getElementById(EElementId.HeaderProfile);

            if (targetElement && targetRef.current) {
                const element = targetRef.current as HTMLDivElement;
                const thisRect = element.getBoundingClientRect();
                const targetRect = targetElement.getBoundingClientRect();

                popupController.openPopup(EPopup.ProfileOption, {
                    position: {
                        top: targetRect.bottom - 10,
                        left: targetRect.left - thisRect.width + 30,
                    },
                });
            } else {
                popupController.openPopup(EPopup.ProfileOption);
            }
        };

        if (popupController.isPopupOpen(EPopup.ProfileOption)) {
            window.addEventListener('resize', updatePosition);
            updatePosition();
        }

        return () => {
            window.removeEventListener('resize', updatePosition);
        };
    }, [popupController.isPopupOpen(EPopup.ProfileOption)]);

    useEffect(() => {
        const handleClickOutside = (event: MouseEvent) => {
            if (targetRef.current) {
                const element = targetRef.current as HTMLDivElement;
                if (!element.contains(event.target as Node)) {
                    popupController.closePopup(EPopup.ProfileOption);
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

    return (
        <>
            {
                popupController.isPopupOpen(EPopup.ProfileOption) &&
                <Overlay>
                    <div ref={targetRef} className={styles.baseContainer} style={{
                        top: popupController.getPopupData(EPopup.ProfileOption).position.top,
                        left: popupController.getPopupData(EPopup.ProfileOption).position.left
                    }}>
                        <button className={styles.itemContainer} onClick={editProfile}>
                            Profile
                        </button>
                        <button className={styles.itemContainer} onClick={onClick}>
                            Posts
                        </button>
                        <div className={styles.line}/>
                        <button className={styles.itemContainer} onClick={handleSignOut}>
                            Sign Out
                        </button>
                    </div>
                </Overlay>
            }
        </>
    )
}

export default ProfileOptionPopup;