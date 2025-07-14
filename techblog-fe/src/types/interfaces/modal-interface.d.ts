import {EModalMutationStatus} from "@/types/enums/common-enum";

export interface IModalMutation {
    modalMutationStatus: EModalMutationStatus;
    resultMutation: any;
    navigatePath?: string;
    message: string;
    desc: string;
}