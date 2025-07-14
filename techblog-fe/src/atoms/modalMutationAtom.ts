import {atom} from 'recoil';

import {IModalMutation} from "@/types/interfaces/modal-interface";
import {EModalMutationStatus} from "@/types/enums/common-enum";

export const modalMutationAtom = atom<IModalMutation>({
    key: 'modalMutationAtom',
    default: {
        modalMutationStatus: EModalMutationStatus.Close,
        resultMutation: null,
        message: "",
        desc: ""
    },
});
