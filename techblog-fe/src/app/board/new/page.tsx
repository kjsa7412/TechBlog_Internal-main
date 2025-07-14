import { EBlank } from "@/types/enums/common-enum";

import PageContainer from "@/components/containers/PageContainer";
import Blank from "@/components/blank/Blank";
import BodyContainer from "@/components/containers/BodyContainer";
import ContentsContainer from "@/components/containers/ContentsContainer";
import Title from "@/components/edit/Title";
import EditorSection from "@/components/edit/EditorSection";

const New = () => {
    return (
        <PageContainer>
            <Blank type={EBlank.Header} />
            <BodyContainer editorPage={true}>
                <ContentsContainer>
                    <Blank type={EBlank.Column} size={60}/>
                    <Title/>
                    <EditorSection/>
                </ContentsContainer>
            </BodyContainer>
        </PageContainer>
    );
};

export default New;
