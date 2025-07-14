import {EBlank} from "@/types/enums/common-enum";

import styles from './TagList.module.scss';
import Tag from "@/components/read/Tag";
import Blank from "@/components/blank/Blank";

interface Props {
    tags: string[]; // string 배열로 tags를 받음
}

const TagList = ({tags}: Props) => {
    return (
        <div className={styles.baseContainer}>
            {
                tags.map((tag) => (
                    <>
                        <Tag text={tag}/>
                        <Blank type={EBlank.Row} size={10}/>
                    </>
                ))
            }
        </div>
    );
};

export default TagList;
