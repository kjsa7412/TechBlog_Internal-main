'use client';

import Image from 'next/image';

import {EBlank} from "@/types/enums/common-enum";
import {IPostData} from "@/types/interfaces/post-interface";
import useActionAndNavigate from "@/hooks/useActionAndNavigate";

import Blank from "@/components/blank/Blank";
import styles from './ColumnPost.module.scss';

const ColumnPost = (props: IPostData) => {
    const actionAndNavigate = useActionAndNavigate();
    const onClick = () => actionAndNavigate.actionAndNavigate(`/board/${props.slug}`)

    return (
        <button className={styles.baseContainer} onClick={onClick}>
            <div className={styles.thumbnail}>
                <Image src={`${!!props.thumbnail ? props.thumbnail : "/images/banner.jpg"}`} alt='' fill
                       style={{objectFit: 'cover'}}/>
            </div>
            <Blank type={EBlank.Column} size={20}/>
            <div className={styles.title}>
                {props.title}
            </div>
            <div className={styles.info}>
                {`${props.dateModified} | ${props.author}`}
            </div>
        </button>
    )
}


export default ColumnPost;