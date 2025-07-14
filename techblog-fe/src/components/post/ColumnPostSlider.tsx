'use client';

import Image from 'next/image';
import React, { useRef, useState, MouseEvent, TouchEvent } from "react";

import { IPostData } from "@/types/interfaces/post-interface";
import { EBlank } from "@/types/enums/common-enum";
import useActionAndNavigate from "@/hooks/useActionAndNavigate";

import styles from './ColumnPostSlider.module.scss';
import Blank from "@/components/blank/Blank";

const SCROLL_LOCK_THRESHOLD = 10;
const SWIPE_THRESHOLD = 50;

const ColumnPostSlider = ({ posts = [] }: { posts: IPostData[] }) => {
    const actionAndNavigate = useActionAndNavigate();
    const [currentIndex, setCurrentIndex] = useState(0);
    const [isDragging, setIsDragging] = useState(false);
    const [isClick, setIsClick] = useState(false);
    const [startX, setStartX] = useState(0);
    const [translateX, setTranslateX] = useState(0);
    const containerRef = useRef(null);

    const startDragging = (clientX: number) => {
        setIsClick(false);
        setIsDragging(true);
        setStartX(clientX);
    };

    const onDragging = (clientX: number) => {
        if (!isDragging) return;
        const diff = clientX - startX;
        setTranslateX(diff);
        if (Math.abs(diff) > SCROLL_LOCK_THRESHOLD) {
            document.body.classList.add("stop-scroll");
        }
    };

    const endDragging = () => {
        if (Math.abs(translateX) === 0) {
            setIsClick(true);
        }
        setIsDragging(false);
        setTranslateX(0);
        document.body.classList.remove("stop-scroll");

        if (translateX > SWIPE_THRESHOLD && currentIndex > 0) {
            setCurrentIndex(currentIndex - 1);
        } else if (translateX < -SWIPE_THRESHOLD && currentIndex < posts.length - 1) {
            setCurrentIndex(currentIndex + 1);
        }
    };

    const handleMouseDown = (e: MouseEvent) => startDragging(e.clientX);
    const handleMouseMove = (e: MouseEvent) => onDragging(e.clientX);
    const handleTouchStart = (e: TouchEvent) => startDragging(e.touches[0].clientX);
    const handleTouchMove = (e: TouchEvent) => onDragging(e.touches[0].clientX);

    const handleClick = (postData: IPostData) => {
        if (isClick) {
            setIsClick(false);
            actionAndNavigate.actionAndNavigate(`/board/${postData.slug}`);
        }
    };

    return (
        <div className={styles.baseContainer}>
            <button
                className={styles.sliderContainer}
                style={{
                    transform: `translateX(calc(-${currentIndex * 100}% + ${translateX}px))`,
                    transition: !isDragging ? 'transform 0.5s ease' : 'none',
                }}
                onMouseDown={handleMouseDown}
                onMouseMove={handleMouseMove}
                onMouseUp={endDragging}
                onMouseLeave={endDragging}
                onTouchStart={handleTouchStart}
                onTouchMove={handleTouchMove}
                onTouchEnd={endDragging}
                ref={containerRef}
            >
                {posts.map((postData) => (
                    <button
                        key={`${postData.slug}-${postData.author}-${postData.datePublished}`}
                        className={styles.postContainer}
                        onClick={() => handleClick(postData)}
                    >
                        <div className={styles.thumbnail}>
                            <Image src={`${postData.thumbnail}`} alt='' fill style={{ objectFit: 'cover' }} />
                        </div>
                        <Blank type={EBlank.Column} size={20} />
                        <div className={styles.title}>
                            {postData.title}
                        </div>
                    </button>
                ))}
            </button>
            <div className={styles.dotContainer}>
                {posts.map((_, index) => (
                    <button
                        key={index}
                        className={`${styles.dot} ${index === currentIndex ? styles.active : ''}`}
                        onClick={() => setCurrentIndex(index)}
                    />
                ))}
            </div>
        </div>
    )
}

export default ColumnPostSlider;
