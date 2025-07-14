import {atom} from "recoil";

import {IUser} from "@/types/interfaces/common-interface";

import cookieStorageEffect from './cookieStorageEffect';

export const userAtom = atom<IUser>({
    key: 'userAtom',
    default: {
        userId: "",
        userName: "",
        profileCont: "",
        profilePic: "",
        profilePicPath: "",
        userAuth: ""
    },
    effects: [cookieStorageEffect('userAtom')],
});