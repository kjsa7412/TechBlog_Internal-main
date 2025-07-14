'use client';

import {useState} from "react";

import useActionAndNavigate from "@/hooks/useActionAndNavigate";

import styles from "@/components/footer/FooterItem.module.scss";

export const FooterLogo = () => {
    const actionAndNavigate = useActionAndNavigate();

    const onClick = () => {
        actionAndNavigate.actionAndNavigate(`/`);
    }
    return (
        <button className={styles.logoContainer} onClick={onClick}>
            <p>TECH BLOG</p>
        </button>
    );
};

export const DarkMode = () => {
    const [isOn, setisOn] = useState(false);

    const onChange = (e: React.MouseEvent<HTMLDivElement>) => {
        isOn
            ? document.documentElement.setAttribute("data-theme", "dark")
            : document.documentElement.setAttribute("data-theme", "light");
    };
    return (
        <>
            {/* eslint-disable-next-line jsx-a11y/no-static-element-interactions */}
            <div
                className={`${styles.toggleContainer} ${isOn ? styles.toggleChecked : ''}`}
                onClick={(e) => {
                    setisOn(!isOn);
                }}
            >
                <div className={`${styles.toggleCircle} ${isOn ? styles.toggleChecked : ''}`}/>
            </div>
            <div className={styles.desc}>
                {isOn ? (
                    <div className={styles.ON}>FEJIGU Toggle Switch ON</div>
                ) : (
                    <div className={styles.OFF}>FEJIGU Toggle Switch OFF</div>
                )}
            </div>
        </>
    );
}
