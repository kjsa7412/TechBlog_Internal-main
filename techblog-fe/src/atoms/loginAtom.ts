import {atom} from "recoil";

import {ILogin} from "@/types/interfaces/common-interface";
import cookieStorageEffect from "@/atoms/cookieStorageEffect";

export const loginAtom = atom<ILogin>({
    key: 'loginAtom',
    default: {
        isLogin: false
    },
    effects: [cookieStorageEffect('loginAtom')]
});