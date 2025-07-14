import {EPopup} from "@/types/enums/common-enum";

interface IPopupDataDetail {
    contents?: {
        title: string,
        desc: string,
        yesLabel?: string,
        noLabel?: string
    }
    position?: {
        top: number;
        left: number;
    };
}

export interface IPopupState {
    [key: EPopup]: boolean;
}

export interface IPopupData {
    [key: EPopup]: IPopupDataDetail | undefined; // 특정 타입을 원하면 any 대신 특정 타입으로 변경 가능
}
