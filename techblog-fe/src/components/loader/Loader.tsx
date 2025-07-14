import styles from './Loader.module.scss';

const Loader = ({size = 20, color = '#000'}: { size?: number, color?: string }) => {
    const style = {
        width: `${size}px`,
        height: `${size}px`,
        borderLeftColor: color,
        borderRightColor: color,
        borderBottomColor: color,
        borderWidth: `${size / 10}px`,
    };

    return (
        <div className={styles.loader} style={style}></div>
    );
};

export default Loader;