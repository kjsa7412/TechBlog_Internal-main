import styles from "./ColumnContainer.module.scss";


type Props = {
    children: string | React.ReactNode;
}

const ColumnContainer = ({children}: Props) =>
    <div className={styles.baseContainer}>{children}</div>

export default ColumnContainer;
