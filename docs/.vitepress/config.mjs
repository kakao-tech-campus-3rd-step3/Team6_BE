import {defineConfig} from 'vitepress'
import {sidebar} from './sidebar-generator.mjs'

// https://vitepress.dev/reference/site-config
export default defineConfig({
  title: "얼음땡 API 문서화",
  description: "얼음땡 API 문서화",
  themeConfig: {
    nav: [
      {text: 'Home', link: '/'},
      {text: 'API Docs', link: '/api/websocket/error-subscription.html'}
    ],

    sidebar: sidebar,
    socialLinks: [
      {
        icon: 'github',
        link: 'https://github.com/kakao-tech-campus-3rd-step3/Team6_BE'
      }
    ]
  }
})
