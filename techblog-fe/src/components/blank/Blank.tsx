import {EBlank} from "@/types/enums/common-enum";

import styles from './Blank.module.scss';

interface Props {
    type: EBlank,
    size?: number
}

const Blank = ({type, size = 10}: Props) => {
    const dynamicStyle = () => {
        if (type === EBlank.Row && size) {
            return {width: `${size}px`};
        } else if (type === EBlank.Column && size) {
            return {height: `${size}px`};
        }
        return {};
    };

    return (
        <div className={`${styles.baseContainer} ${type === EBlank.Header && styles.HeaderBlank}`}
             style={dynamicStyle()}
        />
    );
};

export default Blank;
