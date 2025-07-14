import { atom } from 'recoil';

import {IRouterState} from "@/types/interfaces/common-interface";

export const routerAtom = atom<IRouterState>({
    key: 'routerAtom',
    default: {
        routerState: false
    },
});