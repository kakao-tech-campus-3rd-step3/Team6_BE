import { defineConfig } from 'vitepress'
import { sidebar } from './sidebar-generator.mjs'

// https://vitepress.dev/reference/site-config
export default defineConfig({
  title: "얼음땡 API 문서화",
  description: "얼음땡 API 문서화",
  themeConfig: {
    // https://vitepress.dev/reference/default-theme-config
    nav: [
      { text: 'Home', link: '/' },
      { text: 'API Docs', link: '/api/rest/health' }
    ],

    sidebar: sidebar,

    socialLinks: [
      { icon: 'github', link: 'https://github.com/vuejs/vitepress' }
    ]
  }
})
