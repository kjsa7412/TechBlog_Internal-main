import styles from './Label.module.scss';

const Label = ({text}: {text: string}) => {
    return(
        <div className={styles.baseContainer}>
            {text}
        </div>
    )
}

export default Label;