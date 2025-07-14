'use client'

import React, {useEffect} from "react";
import {useRecoilState} from "recoil";

import {editTitleAtom} from "@/atoms/editTitleAtom";

import styles from './Title.module.scss';

interface TitleProps {
    title?: string;
}

// eslint-disable-next-line @typescript-eslint/ban-ts-comment
// @ts-ignore
const Title = (props: TitleProps) => {
    const [rcTitle, setRcTitle] = useRecoilState(editTitleAtom);

    useEffect(() => {
        if (props.title) {
            setRcTitle(props.title);
        }
    }, [props.title, setRcTitle]);

    const handleTitleKeyPress = (e: React.KeyboardEvent<HTMLTextAreaElement>) => {
        if (e.key === "Enter") {
            e.preventDefault();
            // plainTextRefs.current[0]?.focus(); // 첫 번째 PlainText로 포커스 이동
        }
    };

    const handleResizeHeight = (e: React.ChangeEvent<HTMLTextAreaElement>) => {
        // textarea의 높이를 scrollHeight에 맞춰 조정
        e.target.style.height = 'auto'; // 초기화
        e.target.style.height = `${e.target.scrollHeight}px`; // 스크롤 높이에 맞추기
        setRcTitle(e.target.value);
    };

    return (
        <div className={styles.baseContainer}>
            <textarea
                rows={1}
                className={styles.inputBox}
                placeholder="Title"
                defaultValue={props.title}
                onKeyPress={handleTitleKeyPress}
                onChange={handleResizeHeight} // 높이 자동 조정 호출
                style={{ resize: 'none', overflow: 'hidden' }} // 수동 크기 조정을 비활성화하고 overflow 숨김 처리
            />
        </div>
    );
}

export default Title;
