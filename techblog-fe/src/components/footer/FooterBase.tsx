'use client';

import {useEffect, useState} from "react";
import {usePathname} from "next/navigation";

import styles from "@/components/footer/FooterBase.module.scss";
import {FooterLogo} from "@/components/footer/FooterItem";
import Toggle from "@/components/toggle/Toggle";


const FooterBase = () => {
    const [isView, setIsView] = useState(false);

    const pathName = usePathname();

    useEffect(() => {
        setIsView(false);

        if (pathName === '/board/new' || /^\/board\/\d+\/edit$/.test(pathName)) {
            setIsView(false);
        } else {
            setIsView(true);
        }

    }, [pathName]);

    return (
        <>
            {
                isView &&
                <div className={styles.baseContainer}>
                    <div className={styles.content}>
                        <FooterLogo/>
                        <Toggle/>
                    </div>
                </div>
            }
        </>
    )
};

export default FooterBase;