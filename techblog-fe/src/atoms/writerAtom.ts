import {atom} from "recoil";

import {IResult_UserInfo} from "@/types/interfaces/user-interface";

export const writerAtom = atom<IResult_UserInfo>({
    key: 'writerAtom',
    default: {
        userId: "",
        userName: "",
        profilePicPath: "",
        profileCont: ""
    }
});