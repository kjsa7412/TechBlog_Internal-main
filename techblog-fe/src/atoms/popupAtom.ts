import { atom } from 'recoil';

import {IPopupData, IPopupState} from "@/types/interfaces/popup-interface";

export const popupState = atom<IPopupState>({
    key: 'popupState',
    default: {
        BoardOption: false,
        Confirm: false,
        EditProfile: false,
        Notify: false,
        ProfileOption: false,
        SignIn: false,
        SignUp: false,
        Search: false
    },
});

export const popupData = atom<IPopupData>({
    key: 'popupData',
    default: {
        BoardOption: undefined,
        Confirm: undefined,
        EditProfile: undefined,
        Notify: undefined,
        ProfileOption: undefined,
        SignIn: undefined,
        SignUp: undefined,
        Search: undefined
    },
});