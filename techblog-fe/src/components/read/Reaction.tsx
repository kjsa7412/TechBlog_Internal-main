import {EBlank} from "@/types/enums/common-enum";

import styles from './Reaction.module.scss';
import Like from "@/components/read/Like";
import Blank from "@/components/blank/Blank";
import CommentCount from "@/components/read/CommentCount";

interface Props {
    slug : string;
    commentCount: string;
}

const Reaction = ({ slug, commentCount }: Props) => {
    return(
        <div className={styles.baseContainer}>
            <Like slug={slug}/>
            <Blank type={EBlank.Row} size={20}/>
            <CommentCount commentCount={commentCount}/>
        </div>
    )
}

export default Reaction;