'use client';

import { useState, useEffect, useRef } from 'react';

import styles from './Summary.module.scss';

interface Props {
    description: string;
}

const Summary = ({ description }: Props) => {
    const [isExpanded, setIsExpanded] = useState(false);
    const [showButton, setShowButton] = useState(false);
    const descriptionRef = useRef<HTMLDivElement>(null);

    const toggleDescription = () => {
        setIsExpanded(!isExpanded);
    };

    useEffect(() => {
        if (descriptionRef.current) {
            const contentHeight = descriptionRef.current.scrollHeight;
            if (contentHeight > 198) {
                setShowButton(true);
            } else {
                setShowButton(false);
            }
        }
    }, [description]);

    return (
        <div className={styles.baseContainer}>
            <div
                className={`${styles.description} ${isExpanded ? styles.expanded : styles.collapsed}`}
                ref={descriptionRef}
            >
                <div className={styles.header}>
                    <div className={`${styles.beta} ${styles.gradientText}`}>
                        {!!description ? "이 글을 AI가 요약했어요" : "이 글을 AI가 요약하고 있어요"}
                        <div className={`${styles.betaBox}`}>
                            BETA
                        </div>
                    </div>
                    {showButton && (
                        <button
                            className={styles.toggleButton}
                            onClick={toggleDescription}
                        >
                            {isExpanded ? '접기' : '더보기'}
                        </button>
                    )}
                </div>
                {description}
            </div>
        </div>
    );
};

export default Summary;
