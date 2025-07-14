import {EBannerType, EBlank} from "@/types/enums/common-enum";

import PageContainer from "@/components/containers/PageContainer";
import Blank from "@/components/blank/Blank";
import Banner from "@/components/banner/Banner";
import BodyContainer from "@/components/containers/BodyContainer";
import ContentsContainer from "@/components/containers/ContentsContainer";
import PopularPost from "@/components/home/PopularPost";
import AllPost from "@/components/home/AllPost";

const Home = () => {
    return (
        <PageContainer>
            <Blank type={EBlank.Header}/>
            <Banner type={EBannerType.Home} title={"Welcome"}/>
            <BodyContainer>
                <ContentsContainer>
                    <PopularPost/>
                    <Blank type={EBlank.Column} size={30}/>
                    <AllPost/>
                </ContentsContainer>
            </BodyContainer>
        </PageContainer>
    );
}

export default Home;