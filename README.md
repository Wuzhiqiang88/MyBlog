# 仿CSDN博客系统    
# 开发环境：
IDEA+JDK1.8+SpringBoot1.5+SpringSecurity+MySQL+Thymleaf+Windows
# 项目描述： 
基于SpringBoot搭建的仿CSDN前后端博客系统，主要功能模块分为用户管理，权限管理，博客管理，点赞、评论管理，标签、分类管理，搜索排序功能。技术上采用Maven进行依赖管理，集成SpringSecurity实现对角色权限的管理，使用QiNiu的对象存储实现对博客中图片和用户头像的上传，集成ElasticSearch全文搜索引擎实现全文搜索，使用BCrypt加密帐号密码，前端使用Bootstrap对页面的快速搭建以及使用Thymleaf模版引擎进行视图渲染。
# 项目进度：
持续更新...
## 功能TODO
- [x] 用户注册登录功能
- [x] 用户管理员权限功能
- [x] BCrypt加密功能
- [x] Markdown文章发布功能
- [x] 文章分页显示
- [x] 文章分类
- [x] 文章标签/标签云
- [x] 文章点击量 
- [x] 阅读排行
- [x] 赞踩功能
- [x] 全文搜索
- [x] 头像图片上传
- [ ] 评论/回复邮件提醒功能
- [ ] 评论敏感词过滤
- [ ] Archives/按月归档
- [ ] 第三方登录
- [ ] 文章分享
