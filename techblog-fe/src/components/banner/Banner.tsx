'use client';

import {useRecoilState, useResetRecoilState} from "recoil";
import {useQuery} from "react-query";
import {useEffect, useState} from "react";

import {IBannerAtom, IUser} from "@/types/interfaces/common-interface";
import {userAtom} from "@/atoms/userAtom";
import {EBannerType, EIcon} from "@/types/enums/common-enum";
import {EQuerykey} from "@/types/enums/querykey-enum";
import axiosClient from "@/libs/axiosClient";
import {bannerAtom} from "@/atoms/bannerAtom";
import {writerAtom} from "@/atoms/writerAtom";
import {IMG} from "@/contants/common";

import styles from './Banner.module.scss';
import Icons from "@/components/Icons";
import BoardOptionButton from "@/components/button/BoardOptionButton";

interface IBanner {
    type: EBannerType,
    title: string,
    author?: string,
    dateModified?: string
}

const Banner = (props: IBanner) => {
    const [rcUser, setRcUser] = useRecoilState<IUser>(userAtom);
    const [rcBanner, setRcBanner] = useRecoilState<IBannerAtom>(bannerAtom);
    const [writerInfo, setWriterInfo] = useRecoilState(writerAtom);
    const [backgroundImage, setBackgroundImage] = useState('');

    const resUpdateImage = useQuery(
        [EQuerykey.UPDATE_IMAGE],
        () => axiosClient.get('/api/updateImage', {
            params: {topic: "office", perPage: 30}
        }),
        {
            onSuccess: (data) => {
                setRcBanner((prevState) => ({
                    ...prevState,
                    bannerUrl: data.data.imageUrl
                }));
            }
        }
    )

    useEffect(() => {
        if (rcBanner.bannerUrl) {
            setBackgroundImage(`url(${rcBanner.bannerUrl})`);
        }
    }, [rcBanner.bannerUrl]);

    return (
        <div className={styles.baseContainer}>
            <div className={styles.imageWrapper}>
                <div
                    className={styles.backgroundImage}
                    style={{backgroundImage}}
                />
            </div>
            <div className={styles.overlay}>
                <div className={styles.contentContainer}>
                    <div className={styles.content}>
                        {props.title}
                    </div>
                    {
                        (props.type === EBannerType.Read) &&
                        <div className={styles.info}>
                            <div className={styles.date}>
                                {props.dateModified}
                            </div>
                            {
                                rcUser.userId === props.author &&
                                <BoardOptionButton/>
                            }
                            {
                                writerInfo.profilePicPath ?
                                    <Icons iconType={EIcon.Avatar} width={32} height={32}
                                           fill={IMG.DefaultPath + writerInfo.profilePicPath}
                                           styleTag={styles.avatar}/> :
                                    <Icons iconType={EIcon.Avatar} width={32} height={32} fill={'#C0C0C0'}/>
                            }
                        </div>
                    }
                </div>
            </div>
        </div>
    )
};

export default Banner;