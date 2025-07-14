import styles from "./ContentsContainer.module.scss";

type Props = {
    children: string | React.ReactNode;
}

const ContentsContainer = ({children}: Props) =>
    <div className={styles.baseContainer}>{children}</div>

export default ContentsContainer;
