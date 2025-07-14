import {atom} from "recoil";

import localStorageEffect from "@/atoms/localStorageEffect";

export const darkModeAtom = atom({
    key: 'darkModeAtom',
    default: {
        isDark: false
    },
    effects: [localStorageEffect('darkModeAtom')]
});