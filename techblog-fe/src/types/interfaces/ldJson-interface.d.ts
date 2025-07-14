export interface ILdJsonArticle {
    id: string,
    headline: string,
    image: string[],
    datePublished: string,
    dateModified: string,
    author: {
        name: string,
        url: string
    },
    keywords: string[]
}

export interface ILdJsonWebPage {
    id: string
}