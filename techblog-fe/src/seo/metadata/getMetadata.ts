import {Metadata} from "next";

import {IMetadata} from "@/types/interfaces/metadata-interface";
import {META} from "@/contants/metadata";

export const getMetadata = (props?: IMetadata): Metadata => {
    const metadata = props ? props : META;

    const result: Metadata = {
        metadataBase: new URL(metadata.baseUrl),
        alternates: {
            canonical: metadata.pageUrl || metadata.baseUrl,
        },
        title: metadata.title,
        description: metadata.description,
        keywords: [...metadata.keywords],
        openGraph: {
            type: 'article',
            locale: 'ko_KR',
            url: metadata.pageUrl || metadata.baseUrl,
            title: metadata.title,
            description: metadata.description,
            images: {
                url: metadata.ogImage,
            },
        },
        twitter: {
            card: 'summary',
            // eslint-disable-next-line @typescript-eslint/ban-ts-comment
            // @ts-ignore
            url: metadata.pageUrl || metadata.baseUrl,
            title: metadata.title,
            description: metadata.description,
            images: {
                url: metadata.ogImage,
            },
        },
    };

    return result;
};