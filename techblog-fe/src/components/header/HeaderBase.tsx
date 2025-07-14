'use client';

import styles from "./HeaderBase.module.scss";

interface Props {
    left?: Array<JSX.Element | JSX.Element[]>;
    right?: Array<JSX.Element | JSX.Element[]>;
}

const HeaderBase = ({left = [], right = []}: Props) => {
    return (
        <div className={styles.baseContainer}>
            <div className={styles.leftContainer}>
                {!!left && left.map((value, index) =>
                    value
                )}
            </div>
            <div className={styles.rightContainer}>
                {!!right && right.map((value, index) =>
                    value
                )}
            </div>
        </div>
    )
}

export default HeaderBase;