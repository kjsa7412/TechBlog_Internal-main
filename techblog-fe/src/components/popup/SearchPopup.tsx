'use client'

import {useEffect, useRef, useState} from "react";

import usePopup from "@/hooks/usePopup";
import {EIcon, EPopup} from "@/types/enums/common-enum";
import useActionAndNavigate from "@/hooks/useActionAndNavigate";

import styles from './SearchPopup.module.scss';
import Icons from "@/components/Icons";

const SearchPopup = () => {
    const inputRef = useRef<any>(null);
    const popupController = usePopup();
    const [query, setQuery] = useState('');
    const actionAndNavigate = useActionAndNavigate();

    const closePopup = () => popupController.closePopup(EPopup.Search);

    const handleKeyPress = (event: React.KeyboardEvent<HTMLInputElement>) => {
        if (event.key === 'Enter') {
            if(query) {
                actionAndNavigate.actionAndNavigate(`/board/search/${query}`);
                setQuery('');
                closePopup();
            }
        }
    };

    useEffect(() => {
        if (inputRef.current instanceof HTMLInputElement) {
            inputRef.current.focus();
        }
        setQuery('');
    }, [popupController.isPopupOpen(EPopup.Search)]);

    return (
        <>
            {
                popupController.isPopupOpen(EPopup.Search) &&
                <div className={styles.baseContainer}>
                    <button className={styles.closeButton} onClick={closePopup}>
                        <Icons iconType={EIcon.Close3} fill={'white'} width={30} height={30}/>
                    </button>
                    <input className={styles.input}
                           ref={inputRef}
                           type="text"
                           value={query}
                           onChange={(e) => setQuery(e.target.value)}
                           onKeyPress={handleKeyPress}
                    />
                    <div className={styles.line}/>
                </div>
            }
        </>
    )
}

export default SearchPopup;