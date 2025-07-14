import { useState, useEffect } from "react";

import usePopup from "@/hooks/usePopup";
import { EPopup } from "@/types/enums/common-enum";

import styles from "./Overlay.module.scss";

type Props = {
    children: React.ReactNode;
}

const Overlay = ({ children }: Props) => {
    const [isColor, setIsColor] = useState(true);
    const popupController = usePopup();

    useEffect(() => {
        if (popupController.isPopupOpen(EPopup.BoardOption) ||
            popupController.isPopupOpen(EPopup.ProfileOption)) {
            setIsColor(false);
        } else {
            setIsColor(true);
        }
    }, [popupController]);

    return (
        <div className={`${styles.baseContainer} ${isColor ? styles.isColor : ''}`}>
            {children}
        </div>
    );
}

export default Overlay;
