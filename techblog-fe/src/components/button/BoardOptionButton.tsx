import {useRef} from "react";

import usePopup from "@/hooks/usePopup";
import {EElementId, EIcon, EPopup} from "@/types/enums/common-enum";

import styles from "./BoardOptionButton.module.scss";
import Icons from "@/components/Icons";

const BoardOptionButton = () => {
    const targetRef = useRef(null);
    const popupController = usePopup();

    const togglePopup = () => {
        if (popupController.isPopupOpen(EPopup.BoardOption)) {
            popupController.closePopup(EPopup.BoardOption);
        } else {
            if(targetRef.current) {
                const element = targetRef.current as HTMLDivElement;
                const rect = element.getBoundingClientRect();
                popupController.openPopup(EPopup.BoardOption, {position: {top: rect.bottom, left: rect.left}})
            }
            else {
                popupController.openPopup(EPopup.BoardOption);
            }
        }
    };

    return (
        // eslint-disable-next-line jsx-a11y/no-static-element-interactions
        <button id={EElementId.BoardOption} ref={targetRef} className={styles.baseContainer} onClick={togglePopup}>
            <Icons iconType={EIcon.Option} width={32} height={32} fill={'#FFFFFF'}/>
        </button>
    )
}

export default BoardOptionButton;