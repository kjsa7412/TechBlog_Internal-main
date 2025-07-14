import styles from "./BodyContainer.module.scss";

type Props = {
    children: string | React.ReactNode;
    editorPage?: boolean;
}

const BodyContainer = ({children, editorPage = false}: Props) =>
    <div className={editorPage ? styles.baseEditorContainer : styles.baseContainer}>{children}</div>

export default BodyContainer;
