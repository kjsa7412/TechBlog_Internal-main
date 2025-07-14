import styles from "./RowContainer.module.scss";


type Props = {
    children: string | React.ReactNode;
}

const RowContainer = ({children}: Props) =>
    <div className={styles.baseContainer}>{children}</div>

export default RowContainer;
