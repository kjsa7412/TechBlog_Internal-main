'use client';

import React, {useEffect} from "react";
import {useRecoilState} from "recoil";

import {darkModeAtom} from "@/atoms/darkModeAtom";

import styles from "./MainContainer.module.scss";


type Props = {
    children: string | React.ReactNode;
}

const MainContainer = ({children}: Props) => {
    const [rcDarkMode, setRcDarkMode] = useRecoilState(darkModeAtom);

    useEffect(() => {
        document.documentElement.setAttribute("data-theme", rcDarkMode.isDark ? "dark" : "light");
    }, [rcDarkMode.isDark]);

    return <main className={styles.baseContainer}>{children}</main>;
}

export default MainContainer;
