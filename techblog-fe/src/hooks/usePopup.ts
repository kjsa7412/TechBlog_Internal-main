import {useRecoilState, useResetRecoilState} from "recoil";

import {IPopupData, IPopupDataDetail, IPopupState} from "@/types/interfaces/popup-interface";
import {popupData, popupState} from "@/atoms/popupAtom";
import {EPopup} from "@/types/enums/common-enum";

const usePopup = () => {
    const [state, setState] = useRecoilState(popupState);
    const [data, setData] = useRecoilState(popupData);
    const resetPopupState = useResetRecoilState(popupState);
    const resetPopupData = useResetRecoilState(popupData);

    const openPopup = (popupName:EPopup, popupValue?:IPopupDataDetail) => {
        document.body.classList.add("stop-scroll");
        setState((prev) => ({ ...prev, [popupName]: true }));
        setData((prev) => ({ ...prev, [popupName]: popupValue }));
    };

    const closePopup = (popupName:EPopup) => {
        document.body.classList.remove("stop-scroll");
        setState((prev) => ({ ...prev, [popupName]: false }));
        setData((prev) => ({ ...prev, [popupName]: undefined }));
    };

    const isPopupOpen = (popupName: EPopup) => state[popupName as keyof IPopupState];

    const getPopupData = (popupName: EPopup): IPopupDataDetail | any => data[popupName as keyof IPopupData];

    const closeAll = () => {
        document.body.classList.remove("stop-scroll");
        resetPopupState();
        resetPopupData();
    }

    return {
        openPopup,
        closePopup,
        isPopupOpen,
        getPopupData,
        closeAll
    };
}

export default usePopup;