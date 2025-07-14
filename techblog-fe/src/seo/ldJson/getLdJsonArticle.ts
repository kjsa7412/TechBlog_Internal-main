import {ILdJsonArticle} from "@/types/interfaces/ldJson-interface";

export const getLdJsonArticle = (props: ILdJsonArticle) => {
    return {
        "@context": "https://schema.org",
        "@type": "NewsArticle",
        "@id": props.id,
        "headline": props.headline,
        "image": props.image,
        "datePublished": props.datePublished,
        "dateModified": props.dateModified,
        "author": props.author,
        "keywords": props.keywords,
        "inLanguage": "ko-KR"
    };
}

