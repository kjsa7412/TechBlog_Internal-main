import styles from './Tag.module.scss';

interface Props {
    text: string;
}

const Tag = ({ text }: Props) => {
    return (
        <div className={styles.tag}>
            {text}
        </div>
    );
};

export default Tag;
