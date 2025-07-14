'use client';

import {useState} from "react";

import {EIcon} from "@/types/enums/common-enum";
import useActionAndNavigate from "@/hooks/useActionAndNavigate";

import styles from './SearchBar.module.scss';
import Icons from "@/components/Icons";

const SearchBar = () => {
    const [query, setQuery] = useState('');
    const actionAndNavigate = useActionAndNavigate();

    const handleSearch = () => {
        if(query) {
            actionAndNavigate.actionAndNavigate(`/board/search/${query}`);
            setQuery('');
        }
    };

    const handleKeyPress = (event: React.KeyboardEvent<HTMLInputElement>) => {
        if (event.key === 'Enter') {
            handleSearch();
        }
    };

    return (
            <div className={styles.baseContainer}>
                <div className={styles.inputContainer}>
                    <button className={styles.buttonContainer} onClick={handleSearch}>
                        <Icons iconType={EIcon.Search} fill={'#929292'} width={20} height={20}/>
                    </button>
                    <input className={styles.input}
                           type="text"
                           value={query}
                           onChange={(e) => setQuery(e.target.value)}
                           placeholder={"Search"}
                           onKeyPress={handleKeyPress}
                    />
                </div>
            </div>
    )
}

export default SearchBar;