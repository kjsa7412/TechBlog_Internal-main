import {MouseEventHandler} from "react";

import {EButtonShape, EButtonSize, EButtonType} from "@/types/enums/common-enum";

export interface ITextButton {
    controller?: {
        isOpen?: boolean,
        onClick?: MouseEventHandler<HTMLButtonElement>,
        label?: string,
        isSubmit?: boolean
        isLoading?: boolean
    },
    styles?: {
        size?: EButtonSize,
        shape?: EButtonShape,
        type?: EButtonType
    }
}