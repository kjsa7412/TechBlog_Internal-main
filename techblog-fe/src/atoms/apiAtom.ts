import { atom } from 'recoil';

import {IApiState} from "@/types/interfaces/api-interface";

export const apiAtom = atom<IApiState>({
    key: 'apiAtom',
    default: {
        result_popPostAPI: false,
        result_allPostAPI: false,
        result_slug: "",
        result_getPostBySlugAPI: false,
        result_UserInfo: false,
        result_CountList: false,
        result_CmmtList: false,
        result_keyword: "",
        result_searchAPI: false
    },
});