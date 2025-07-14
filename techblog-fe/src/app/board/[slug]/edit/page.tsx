'use client';

import {AxiosResponse} from "axios";
import {useEffect, useState} from "react";
import {useQuery} from "react-query";
import {useRecoilState} from "recoil";

import {EBlank} from "@/types/enums/common-enum";
import {IPostData} from "@/types/interfaces/post-interface";
import axiosClient from "@/libs/axiosClient";
import {IUser} from "@/types/interfaces/common-interface";
import {userAtom} from "@/atoms/userAtom";
import useActionAndNavigate from "@/hooks/useActionAndNavigate";

import PageContainer from "@/components/containers/PageContainer";
import Blank from "@/components/blank/Blank";
import BodyContainer from "@/components/containers/BodyContainer";
import ContentsContainer from "@/components/containers/ContentsContainer";
import Title from "@/components/edit/Title";
import EditorSection from "@/components/edit/EditorSection";


interface Props {
    params: {
        slug: string
    }
}

const getPostBySlugAPI = (slug: string): Promise<AxiosResponse<IPostData>> => {
    return axiosClient.get('/api/getPostBySlug', {
        params: {slug: slug}
    });
};

const Edit = (props: Props) => {
    const [rcUser, setRcUser] = useRecoilState<IUser>(userAtom);
    const actionAndNavigate = useActionAndNavigate();
    const [post, setPost] = useState<IPostData | undefined>();

    const result_getPostBySlugAPI = useQuery(
        ["result_getPostBySlugAPI"],
        () => getPostBySlugAPI(props.params.slug),
        {
            onSuccess: (data) => {
                setPost(data.data);
            },
            onError: () => {
                actionAndNavigate.actionAndNavigate('/');
            }
        }
    );

    useEffect(() => {
        result_getPostBySlugAPI.refetch();
    }, [])

    useEffect(()=> {
        if (!!post && (post.author != rcUser.userId)) {
            actionAndNavigate.actionAndNavigate('/');
        }
    }, [post]);

    return (
        <PageContainer>
            <Blank type={EBlank.Header} />
            {
                !!post &&
                <BodyContainer editorPage={true}>
                    <ContentsContainer>
                        <Blank type={EBlank.Column} size={60}/>
                        <Title title={post.title}/>
                        <EditorSection post={post}/>
                    </ContentsContainer>
                </BodyContainer>
            }
        </PageContainer>
    );
};

export default Edit;
