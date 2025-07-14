import {join} from "path";
import fs from "fs";
import matter from "gray-matter";

import {
    IParam_CreatePost,
    IParam_DropPost,
    IPostData,
    IResult_CreatePost,
    IResult_DropPost
} from "@/types/interfaces/post-interface";

const postsDirectory = join(process.cwd(), "_posts");

export function getPostSlugs(): string[] {
    return fs.readdirSync(postsDirectory);
}

export function getPostBySlug(slug: string): IPostData | undefined {
    if (!slug) return;

    const realSlug = slug.replace(/\.md$/, "");
    const fullPath = join(postsDirectory, `${realSlug}.md`);
    const fileContents = fs.readFileSync(fullPath, "utf8");
    const { data, content } = matter(fileContents);

    // Process the keywords field to be an array of strings
    const keywords = data.keywords && typeof data.keywords === 'string'
        ? data.keywords.split(',').map(keyword => keyword.trim())
        : [];

    return { ...data, slug: realSlug, content, keywords } as IPostData;
}

export function getAllPosts(): IPostData[] {
    const slugs = getPostSlugs();
    const posts = slugs
        .map((slug) => getPostBySlug(slug))
        .filter((post): post is IPostData => post !== undefined) // undefined 제거
        .sort((post1, post2) => (post1.dateModified > post2.dateModified ? -1 : 1));
    return posts;
}

export function getPostByTerm(searchTerm: string): IPostData[] {
    if (!searchTerm) return [];

    const allPosts = getAllPosts();

    return allPosts.filter((post) => {
        const { title, description, content, author } = post;
        const mdContent = content || "";

        // 제목, 발췌문, 본문 내용에서 검색어를 찾습니다
        // 작성자의 경우 완전 일치의 경우에만 검색
        return (
            title.includes(searchTerm) ||
            description.includes(searchTerm) ||
            mdContent.includes(searchTerm) ||
            author === searchTerm
        );
    }).sort((post1, post2) => (post1.dateModified > post2.dateModified ? -1 : 1));
}

export function createPost(param: IParam_CreatePost): IResult_CreatePost {
    try {
        const fileName = `${param.slug}.md`;
        const filePath = join(postsDirectory, fileName);

        // param을 사용하여 메타데이터 구성
        const data = {
            title: param.title,
            description: param.description,
            thumbnail: param.thumbnail,
            keywords: param.keywords.join(", "),
            author: param.author,
            datePublished: param.datePublished,
            dateModified: param.dateModified,
        };

        // 포스트 내용과 메타데이터를 포함한 파일 내용을 작성
        let fileContents = matter.stringify(param.content, data);

        // 메타데이터와 내용 사이에 공백 한 줄 추가
        fileContents = fileContents.replace(/\n---\n/, "\n---\n\n");

        // 파일을 `_posts` 디렉토리에 저장
        fs.writeFileSync(filePath, fileContents, "utf8");

        // 성공 시 성공 메시지와 파일 경로를 반환
        return {
            success: true,
            message: "파일 저장 성공",
            filePath,
        };
    } catch (error) {
        // 실패 시 실패 메시지와 에러 내용을 반환
        return {
            success: false,
            message: `파일 저장 실패: ${(error as Error).message}`,
        };
    }
}

export function deletePost(param: IParam_DropPost): IResult_DropPost {
    try {
        const fileName = `${param.slug}.md`;
        const filePath = join(postsDirectory, fileName);

        // 파일 존재 여부 확인 후 삭제
        if (fs.existsSync(filePath)) {
            fs.unlinkSync(filePath);

            return {
                success: true,
                message: "파일 삭제 성공"
            };
        } else {
            return {
                success: false,
                message: "파일을 찾을 수 없습니다."
            };
        }
    } catch (error) {
        // 실패 시 에러 메시지 반환
        return {
            success: false,
            message: `파일 삭제 실패: ${(error as Error).message}`,
        };
    }
}