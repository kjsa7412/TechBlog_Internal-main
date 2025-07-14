export interface IPost {
    url: string,
    title: string,
    desc?: string,
    date: string,
    writer: string,
    slug: string
}

export interface IPostData {
    slug: string;
    title: string;
    description: string;
    thumbnail: string;
    keywords: string[];
    author: string
    datePublished: string;
    dateModified: string;
    content: string;
}

// 인기 게시물 조회
export interface IResult_PopPostList {
    map(arg0: (item: IResult_PopPostList) => string): string[];
    boadId: number
    title: string
    summary: string
    useYN: string
    openStatus: string
    hashtag: string
    thumbnailid: number
    thumbnailpath: string
    views: number
    likes: number
    lastMd: number
    conts: string
    userId: string
    userName: string
}

// 게시물 작성 (브라우저 to API)
export interface IParam_InsertPost {
    title: string;
    boadConts: string;
}

export interface IResult_InsertPost {
    boadId: string;
}

// 게시물 작성 (API to NODE)
export interface IParam_CreatePost {
    slug: string;
    title: string;
    description: string;
    thumbnail: string;
    keywords: string[];
    author: string
    datePublished: string;
    dateModified: string;
    content: string;
}

export interface IResult_CreatePost {
    success: boolean;
    message: string;
    filePath?: string; // 성공 시에만 포함
}

// 게시물 수정 (브라우저 to API)
export interface IParam_UpdatePost {
    boadid: number;
    title: string;
    boadConts: string;
}

export interface IResult_UpdatePost {
    boadId: string;
}

// 게시물 삭제 (브라우저 to API)
export interface IParam_DeletePost {
    boadid: number;
}

export interface IResult_DeletePost {
    boadId: string;
}

// 게시물 삭제 (API to NODE)
export interface IParam_DropPost {
    slug: string;
}

export interface IResult_DropPost {
    success: boolean;
    message: string;
}

// 좋아요 및 조회수 조회
export interface IParam_CountList {
    boadId: number;
}

export interface IResult_CountList {
    viewCnt: string;
    likeCnt: string;
}

// 좋아요 클릭
export interface IParam_UpdateLike {
    boadid: number;
}

export interface IResult_UpdateLike {
    viewCnt: string;
    likeCnt: string;
}

// 댓글 목록 조회
export interface IParam_CmmtList {
    boadId: number;
}

export interface IResult_CmmtList {
    cmtId: number;
    boadId: number;
    userId: string;
    userName: string;
    profilePicPath: string;
    cmt: string;
    writeDate: string;
}

// 댓글 작성
export interface IParam_InsertCmmt {
    boadId: number;
    cmt: string;
}

export interface IResult_InsertCmmt {
    cmtId: number;
    boadId: number;
    userId: string;
    userName: string;
    profilePicPath: string;
    cmt: string;
    writeDate: string;
}

// 댓글 삭제
export interface IParam_DeleteCmmt {
    cmtId: number;
    boadId: number;
}

export interface IResult_DeleteCmmt {
    cmtId: number;
    boadId: number;
    userId: string;
    userName: string;
    profilePicPath: string;
    cmt: string;
    writeDate: string;
}