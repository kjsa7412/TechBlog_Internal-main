import {ITextButton} from "@/types/interfaces/button-interface";
import {EButtonShape, EButtonSize, EButtonType} from "@/types/enums/common-enum";

import styles from "./TextButton.module.scss"
import Loader from "@/components/loader/Loader";


const TextButton = (props: ITextButton) => {
    const isOpen = props.controller?.isOpen ?? true;
    return (
        <>
            {
                isOpen &&
                <button className={`${styles.baseContainer}
                                     ${styles[props.styles?.size || EButtonSize.Medium]}
                                     ${styles[props.styles?.shape || EButtonShape.Round]}
                                     ${styles[props.styles?.type || EButtonType.Red]}`}
                        onClick={props.controller?.isLoading ? undefined : props.controller?.onClick}
                        type={props.controller?.isSubmit ? "submit" : "button"}
                >
                    {
                        props.controller?.isLoading ?
                            <Loader color={
                                `${
                                    props.styles?.type === EButtonType.None ||
                                    props.styles?.type === EButtonType.Stroke ?
                                        "var(--color-text-1)" :
                                        "var(--color-button-text)"
                                }`
                            }/> :
                            !!props.controller?.label && props.controller.label
                    }
                </button>
            }
        </>
    )
}

export default TextButton;