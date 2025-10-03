import fs from 'fs';
import path from 'path';

// 문서 루트 디렉토리 ('.vitepress'의 부모)
const docsRoot = path.resolve(__dirname, '..');

/**
 * 파일 이름을 Title Case 형태의 텍스트로 변환합니다.
 * e.g., 'error-subscription.md' -> 'Error Subscription'
 * @param {string} filename - 변환할 파일 이름
 */
function toTitleCase(filename) {
  return filename.replace(/-/g, ' ').replace(/\.md$/, '')
  .split(' ')
  .map(word => word.charAt(0).toUpperCase() + word.slice(1))
  .join(' ');
}

/**
 * 지정된 디렉토리 경로에서 사이드바 그룹 객체를 생성합니다.
 * @param {string} dirPath - 'docs' 기준의 상대 경로 (e.g., 'api/rest')
 * @param {string} groupText - 사이드바에 표시될 그룹 이름
 */
function getSidebarGroup(dirPath, groupText) {
  const fullPath = path.join(docsRoot, dirPath);
  try {
    const files = fs.readdirSync(fullPath);

    const items = files
    .filter(file => file.endsWith('.md'))
    .map(file => {
      const link = `/${dirPath}/${file.replace(/\.md$/, '')}`;
      return {text: toTitleCase(file), link};
    });

    return {text: groupText, items};
  } catch (error) {
    console.warn(`Could not read directory for sidebar group: ${fullPath}`,
        error);
    return {text: groupText, items: []};
  }
}

// 사이드바 설정을 동적으로 생성
export const sidebar = [
  getSidebarGroup('api/rest', 'REST APIs'),
  getSidebarGroup('api/websocket', 'WebSocket APIs')
];
