import {atom} from "recoil";

import {IBreakPoint} from "@/types/interfaces/common-interface";
import localStorageEffect from "@/atoms/localStorageEffect";
import {EBreakPoint} from "@/types/enums/common-enum";

export const breakPointAtom = atom<IBreakPoint>({
    key: 'breakPointAtom',
    default: {
        breakPoint: EBreakPoint.LG
    },
    effects: [localStorageEffect('breakPointAtom')],
});