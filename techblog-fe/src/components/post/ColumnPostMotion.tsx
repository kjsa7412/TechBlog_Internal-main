'use client';

import React, {useState} from 'react';
import Image from 'next/image';

import {IPostData} from "@/types/interfaces/post-interface";
import useActionAndNavigate from "@/hooks/useActionAndNavigate";

import styles from './ColumnPostMotion.module.scss';
import RowContainer from "@/components/containers/RowContainer";

const ColumnPostMotion = ({posts = []}: { posts: IPostData[] }) => {
    const actionAndNavigate = useActionAndNavigate();
    const [activeIndex, setActiveIndex] = useState<number | null>(null)

    return (
        <RowContainer>
            {
                posts.map((postData, index, array) =>
                    <button
                        key={postData.slug + postData.author + postData.datePublished}
                        className={styles.postContainer}
                        onMouseEnter={() => setActiveIndex(index)}
                        onMouseLeave={() => setActiveIndex(null)}
                        onClick={() => actionAndNavigate.actionAndNavigate(`/board/${postData.slug}`)}
                    >
                        <Image src={`${postData.thumbnail ? postData.thumbnail : "/images/banner.jpg"}`} alt='' fill
                               style={{objectFit: 'cover', borderRadius: '10px'}}/>
                        <div className={styles.overlay}/>
                        <div className={styles.infoContainer}>
                            <p className={styles.title}>{postData.title}</p>
                            <p>{`${postData.dateModified} | ${postData.author}`}</p>
                            {
                                activeIndex === index &&
                                <p className={styles.excerpt}>
                                    {postData.description}
                                </p>
                            }
                        </div>
                    </button>
                )
            }
        </RowContainer>
    );
};

export default ColumnPostMotion;
