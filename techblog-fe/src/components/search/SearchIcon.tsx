'use client'

import {EIcon, EPopup} from "@/types/enums/common-enum";
import usePopup from "@/hooks/usePopup";

import styles from "@/components/search/SearchIcon.module.scss";
import Icons from "@/components/Icons";

const SearchIcon = () => {
    const popupController = usePopup();

    const onClick = () => {
        return popupController.openPopup(EPopup.Search);
    };
    
    return (
    <button className={styles.baseContainer} onClick={onClick}>
        <Icons iconType={EIcon.Search} fill={'#929292'} width={20} height={20}/>
    </button>
    )
}

export default SearchIcon;