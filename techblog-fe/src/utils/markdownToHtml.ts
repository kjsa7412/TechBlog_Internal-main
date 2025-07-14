import { remark } from 'remark';
import html from 'remark-html';
import gfm from 'remark-gfm';

// 추후 사용시 정리가 필요한 부분 : img width 수정이 좀 필요하고, 마크다운 에디터에서 입력 가능한 모든 기능이 잘 적용 되는지 css 먹여지는지 봐야함
export default async function markdownToHtml(markdown: string) {
    const result = await remark().use(gfm).use(html).process(markdown);
    return result.toString();
}
