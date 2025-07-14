'use client'

import {MouseEventHandler} from "react";
import {useRecoilState} from "recoil";

import {EIcon} from "@/types/enums/common-enum";
import {darkModeAtom} from "@/atoms/darkModeAtom";

import styles from "./CloseButton.module.scss";
import Icons from "@/components/Icons";


interface ICloseButton {
    onClick: MouseEventHandler<HTMLButtonElement>;
}

const CloseButton = (props : ICloseButton) => {
    const [rcDarkMode, setRcDarkMode] = useRecoilState(darkModeAtom);

    return (
        <button className={styles.baseContainer} onClick={props.onClick}>
            <Icons iconType={EIcon.Close} width={24} height={24} fill={rcDarkMode.isDark ? 'white' : 'black'}/>
        </button>
    )
}

export default CloseButton