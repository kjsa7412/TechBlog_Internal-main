'use client';

import {useEffect, useState} from "react";
import {useRecoilState, useResetRecoilState} from "recoil";

import {darkModeAtom} from "@/atoms/darkModeAtom";

import styles from './Toggle.module.scss';

const Toggle = () => {
    const [isOn, setisOn] = useState(false);
    const [rcDarkMode, setRcDarkMode] = useRecoilState(darkModeAtom);

    const onChange = () => {
        document.documentElement.setAttribute("data-theme", !rcDarkMode.isDark ? "dark" : "light");
        setRcDarkMode((prev) => ({...prev, isDark: !prev.isDark}));
    }

    useEffect(() => {
        document.documentElement.setAttribute("data-theme", rcDarkMode.isDark ? "dark" : "light");
        setisOn(rcDarkMode.isDark);
    }, [rcDarkMode.isDark]);

    return (
        // eslint-disable-next-line jsx-a11y/no-static-element-interactions
        <div className={styles.baseContainer} onClick={onChange}>
            <div className={styles.label} style={{marginRight: '5px'}}>LIGHT</div>
            <div className={`${styles.toggleContainer} ${isOn && styles.checked}`}>
                <div className={`${styles.toggle}`}/>
            </div>
            <div className={styles.label} style={{marginLeft: '5px'}}>DARK</div>
        </div>
    )
}

export default Toggle;