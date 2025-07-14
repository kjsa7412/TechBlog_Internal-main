import {atom} from "recoil";

import {IBannerAtom} from "@/types/interfaces/common-interface";
import localStorageEffect from "@/atoms/localStorageEffect";

export const bannerAtom = atom<IBannerAtom>({
    key: 'bannerAtom',
    default: {
        bannerUrl : '/images/building02.jpg',
        updatedAt: ''
    },
    effects: [localStorageEffect('bannerAtom')]
});