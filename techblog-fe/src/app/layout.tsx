import "@/styles/globals.scss";
import React from "react";

import RecoilRootWrapper from "@/providers/RecoilWrapper";
import ReactQueryWrapper from "@/providers/ReactQueryWrapper";
import ScrollRestoreProvider from "@/providers/ScrollRestoreProvider";
import {getMetadata} from "@/seo/metadata/getMetadata";

import HeaderBase from "@/components/header/HeaderBase";
import {HeaderAction, HeaderLogo, HeaderProfile, HeaderSearch} from "@/components/header/HeaderItem";
import MainContainer from "@/components/containers/MainContainer";
import ProfileOptionPopup from "@/components/popup/ProfileOptionPopup";
import ConfirmPopup from "@/components/popup/ConfirmPopup";
import NotifyPopup from "@/components/popup/NotifyPopup";
import EditProfilePopup from "@/components/popup/EditProfilePopup";
import SignInPopup from "@/components/popup/SignInPopup";
import SignUpPopup from "@/components/popup/SignUpPopup";
import BoardOptionPopup from "@/components/popup/BoardOptionPopup";
import SearchPopup from "@/components/popup/SearchPopup";
import FooterBase from "@/components/footer/FooterBase";
import ModalMutation from "@/components/modal/ModalMutation";


interface Props {
    children: React.ReactNode;
}

export async function generateMetadata() {
    return getMetadata();
}

export default function RootLayout({children}: Props) {
    return (
        <html lang="en">
        <head>
            <link rel="preconnect" href="https://fonts.googleapis.com"/>
            <link rel="preconnect" href="https://fonts.gstatic.com" crossOrigin=""/>
            <link href="https://cdn.jsdelivr.net/gh/sunn-us/SUITE/fonts/static/woff2/SUITE.css" rel="stylesheet"/>
            <link
                href="https://fonts.googleapis.com/css2?family=Noto+Sans+KR:wght@100;400;700&family=Roboto:wght@100;400;700&display=swap"
                rel="stylesheet"/>
            <meta name="viewport" content="width=device-width, initial-scale=1, maximum-scale=1, user-scalable=no"/>
        </head>
        <body style={{backgroundColor: `var(--color-background-1)`}}>
        <ReactQueryWrapper>
            <RecoilRootWrapper>
                <ScrollRestoreProvider>
                    <MainContainer>
                        {/* eslint-disable-next-line react/jsx-key */}
                        <HeaderBase left={[<HeaderLogo/>, <HeaderSearch/>]} right={[<HeaderAction/>, <HeaderProfile/>]}/>
                        {children}
                        <FooterBase/>
                        <ModalMutation/>
                        <BoardOptionPopup/>
                        <ProfileOptionPopup/>
                        <EditProfilePopup/>
                        <SignInPopup/>
                        <SignUpPopup/>
                        <ConfirmPopup/>
                        <NotifyPopup/>
                        <SearchPopup/>
                    </MainContainer>
                </ScrollRestoreProvider>
            </RecoilRootWrapper>
        </ReactQueryWrapper>
        </body>
        </html>
    );
}
