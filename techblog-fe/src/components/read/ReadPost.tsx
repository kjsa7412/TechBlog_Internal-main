import markdownStyles from '@/styles/_markdown.module.scss';

import styles from './ReadPost.module.scss';

type Props = {
    content: string;
};

const ReadPost = ({ content }: Props) => {

    const processHtmlContent = (htmlContent: string) => {
        // 리스트 아이템이 있는 경우 사전 가공 (줄별로 체크)
        const lines: string[] = htmlContent.split('\n');
        let formattedContent = '';

        for (let i = 0; i < lines.length; i++) {
            const temp: string = lines[i]
                // <li></li>에 대해 처리
                .replace(/<li>\s*<\/li>/g, '<li><p>&nbsp;</p></li>')

                // <li> 내 <input>이 있는 경우에 대해 처리
                .replace(/<li class="task-list-item">(<input[^>]*>)([^<]*)<\/li>/g, (match, inputTag, text) => {
                    return `<li class="task-list-item"><p>${inputTag}${text.trim()}</p></li>`;
                })

                // 내용이 있는 <li> 태그 처리
                .replace(/<li>(?!<p>)([^<]*)<\/li>/g, (match, text) => {
                    return `<li><p>${text}</p></li>`;
                })

                // <li> 2차 가공
                .replace(/<li>(.*?)$/, (match) => {
                    // replace가 실행된 경우 무시하도록 플래그 설정
                    if (match.includes('<p>')) {
                        return match; // <p>가 포함되어 있으면 그대로 반환
                    }

                    // <li> 태그만 있을 경우 처리
                    if (match === '<li>' && lines[i + 1] === '<ul>') {
                        return `<li><p>&nbsp;</p>`; // 다음 태그가 <ul>인 경우 줄바꿈이 필요하여 태그 변환
                    } else if (match === '<li>') {
                        return '<li>' // <li> 태그만 있는 경우 변환 X
                    }

                    // <li> 태그와 내용이 같이 있는 케이스
                    // ex) <li>내용 --> 이런식의 케이스
                    return `<li><p>${match.slice(4)}</p>`;
                })

                // <li><input> 2차 가공
                .replace(/<li class="task-list-item">(<input[^>]*>)([^<]*)(<\/li>)?/g, (match, inputTag, text) => {
                    // replace가 실행된 경우 무시하도록 플래그 설정
                    if (match.includes('<p>')) {
                        return match; // <p>가 포함되어 있으면 그대로 반환
                    }

                    // <li><input> 태그 케이스
                    // ex) <li class="task-list-item"><input type="checkbox" checked disabled> 체크 --> 이런식의 케이스
                    return `<li class="task-list-item"><p>${inputTag}${text.trim()}</p>`;
                })

                // <input type="checkbox" checked disabled> 이 값을 체크된 svg로 변경
                .replace(/<input type="checkbox" checked disabled>/g, `
                <svg xmlns="http://www.w3.org/2000/svg" width="24" height="24" viewBox="0 0 24 24" style="vertical-align: bottom; margin-right: 8px">
                    <g clip-path="url(#clip0_1803_1151)">
                        <path d="M19 3H5C3.9 3 3 3.9 3 5V19C3 20.1 3.9 21 5 21H19C20.1 21 21 20.1 21 19V5C21 3.9 20.1 3 19 3ZM10.71 16.29C10.32 16.68 9.69 16.68 9.3 16.29L5.71 12.7C5.32 12.31 5.32 11.68 5.71 11.29C6.1 10.9 6.73 10.9 7.12 11.29L10 14.17L16.88 7.29C17.27 6.9 17.9 6.9 18.29 7.29C18.68 7.68 18.68 8.31 18.29 8.7L10.71 16.29Z"></path>
                    </g>
                    <defs>
                        <clipPath id="clip0_1803_1151">
                            <rect width="24" height="24"></rect>
                        </clipPath>
                    </defs>
                </svg>`)

                // <input type="checkbox" disabled> 이 값을 체크가 해제된 svg로 변경
                .replace(/<input type="checkbox" disabled>/g, `
                <svg xmlns="http://www.w3.org/2000/svg" width="24" height="24" viewBox="0 0 24 24" style="vertical-align: bottom; margin-right: 8px;">
                    <g clip-path="url(#clip0_1803_535)">
                        <rect width="24" height="24" fill="none" />
                        <path d="M18 19H6C5.45 19 5 18.55 5 18V6C5 5.45 5.45 5 6 5H18C18.55 5 19 5.45 19 6V18C19 18.55 18.55 19 18 19ZM19 3H5C3.9 3 3 3.9 3 5V19C3 20.1 3.9 21 5 21H19C20.1 21 21 20.1 21 19V5C21 3.9 20.1 3 19 3Z"></path>
                    </g>
                    <defs>
                        <clipPath id="clip0_1803_535">
                            <rect width="24" height="24"></rect>
                        </clipPath>
                    </defs>
                </svg>`);

            if (lines[i] !== temp) {
                formattedContent += temp; // 교체된 태그값 추가
            } else {
                formattedContent += lines[i]; // 기존 태그값 추가
            }
            formattedContent += '\n'; // 각 줄 끝에 줄바꿈 추가
        }

        return formattedContent
            // <p> 태그로 감싸진 <img> 태그에서 <p> 태그 제거
            .replace(/<p>(<img[^>]+>)<\/p>/g, '$1')
        
            /// 아래의 이미지 변환은 일부 코드 수정이 필요
            // // alt 속성 값을 기반으로 width 스타일을 동적으로 추가, alt 값 * 100 적용
            // .replace(/<img([^>]+alt="([\d.]+)"[^>]*)>/g, (_, attributes, altValue) => {
            //     const width = parseFloat(altValue) * 100;
            //     return `<img${attributes} style="width: ${width}%; margin: auto; display: block">`;
            // })
            // // alt 속성이 없거나 다른 텍스트인 경우 기본 width를 100%로 설정
            // .replace(/<img((?!style)[^>]*)>/g, '<img$1 style="width: 100%;">')
    };

    return (
        <div className={styles.baseContainer} >
            <div
                className={markdownStyles.markdown}
                style={{color: `var(--color-text-1)`}}
                dangerouslySetInnerHTML={{ __html: processHtmlContent(content) }}
            />
        </div>
    );
}

export default ReadPost