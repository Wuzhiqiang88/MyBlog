package com.sao.controller;

import com.alibaba.fastjson.JSON;
import com.qiniu.common.QiniuException;

import com.sao.config.qiniuConfig.QiNiuService;
import com.sao.domain.Blog;
import com.sao.domain.Catalog;
import com.sao.domain.User;
import com.sao.service.BlogService;
import com.sao.service.CatalogService;
import com.sao.service.UserService;
import com.sao.service.impl.CustomUserService;


import com.sao.util.ConstraintViolationExceptionHandler;
import com.sao.util.SimpleSummariserAlgorithm;
import com.sao.vo.Response;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import javax.validation.ConstraintViolationException;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;
import java.util.UUID;

/**
 * 用户主页控制器
 */
@Controller
@RequestMapping("/u")
public class UserspaceController {

    @Value("${qiniu.cdnPrefix}")
    private String cdnPrefix;

    @Autowired
    private CustomUserService customUserService;

    @Autowired
    private UserService userService;

    @Autowired
    private QiNiuService qiNiuService;

    @Autowired
    private BlogService blogService;

    @Autowired
    private CatalogService catalogService;

    /**
     * 跳转帐号设置页面
     * @param username
     * @param model
     * @return
     */
    @GetMapping("/{username}/profile")
    @PreAuthorize("authentication.name.equals(#username)")
    public ModelAndView profile(@PathVariable("username") String username, Model model) {
        User  user = (User)customUserService.loadUserByUsername(username);
        model.addAttribute("user", user);
        return new ModelAndView("/userspace/profile", "userModel", model);
    }
    /**
     * 跳转写博客的界面
     * @param model
     * @return
     */
    @GetMapping("/{username}/blogsEdit")
    public ModelAndView createBlog(@PathVariable("username") String username, Model model) {
        User  user = (User)customUserService.loadUserByUsername(username);
        model.addAttribute("blog", new Blog(null, null, null));
        model.addAttribute("user",user);
        return new ModelAndView("/userspace/blogedit", "blogModel", model);
    }

    /**
     * 跳转我的博客页面
     * @param username
     * @param model
     * @return
     */
    @GetMapping("/{username}")
    public String userSpace(@PathVariable("username") String username, Model model) {
        User  user = (User)customUserService.loadUserByUsername(username);
        model.addAttribute("user", user);
        return "redirect:/u/" + username + "/blogs";
    }

    @GetMapping("/{username}/blogs")
    public String listBlogsByOrder(@PathVariable("username") String username,
                                   @RequestParam(value="order",required=false,defaultValue="new") String order,
                                   @RequestParam(value="catalog",required=false ) Long catalogId,
                                   @RequestParam(value="keyword",required=false,defaultValue="" ) String keyword,
                                   @RequestParam(value="async",required=false) boolean async,
                                   @RequestParam(value="pageIndex",required=false,defaultValue="0") int pageIndex,
                                   @RequestParam(value="pageSize",required=false,defaultValue="10") int pageSize,
                                   Model model) {

        User  user = (User)customUserService.loadUserByUsername(username);

        Page<Blog> page = null;

        if (catalogId != null && catalogId > 0) { // 分类查询
            Catalog catalog = catalogService.getCatalogById(catalogId);
            Pageable pageable = new PageRequest(pageIndex, pageSize);
            page = blogService.listBlogsByCatalog(catalog, pageable);
            order = "";
        } else if (order.equals("hot")) { // 最热查询
            Sort sort = new Sort(Sort.Direction.DESC,"readSize","commentSize","voteSize");
            Pageable pageable = new PageRequest(pageIndex, pageSize, sort);
            page = blogService.listBlogsByTitleVoteAndSort(user, keyword, pageable);
        } else if (order.equals("new")) { // 最新查询
            Pageable pageable = new PageRequest(pageIndex, pageSize);
            page = blogService.listBlogsByTitleVote(user, keyword, pageable);
        }


        List<Blog> list = page.getContent();	// 当前所在页面数据列表

        model.addAttribute("user", user);
        model.addAttribute("order", order);
        model.addAttribute("catalogId", catalogId);
        model.addAttribute("keyword", keyword);
        model.addAttribute("page", page);
        model.addAttribute("blogList", list);
        return (async==true?"/userspace/u :: #mainContainerRepleace":"/userspace/u");
    }

    /**
     * 保存博客
     * @param username
     * @param blog
     * @return
     */
    @PostMapping("/{username}/blogsEdit")
    @PreAuthorize("authentication.name.equals(#username)")
    public ResponseEntity<Response> saveBlog(@PathVariable("username") String username, @RequestBody Blog blog) {
        // 对 Catalog 进行空处理
        if (blog.getCatalog().getId() == null) {
            return ResponseEntity.ok().body(new Response(false,"未选择分类"));
        }
        try {

            // 判断是修改还是新增

            if (blog.getId()!=null) {
                Blog orignalBlog = blogService.getBlogById(blog.getId());
                orignalBlog.setTitle(blog.getTitle());
                if(blog.getSummary()==null&&blog.getSummary().trim().equals("")){
                    blog.setSummary(SimpleSummariserAlgorithm.summarise(blog.getContent(),10));
                }
                orignalBlog.setContent(blog.getContent());
                orignalBlog.setSummary(blog.getSummary());
                orignalBlog.setCatalog(blog.getCatalog());
                orignalBlog.setTags(blog.getTags());
                blogService.saveBlog(orignalBlog);
            } else {
                if(blog.getSummary()==null&&blog.getSummary().trim().equals("")){
                    blog.setSummary(SimpleSummariserAlgorithm.summarise(blog.getContent(),10));
                }
                User user = (User)customUserService.loadUserByUsername(username);
                blog.setUser(user);
                blogService.saveBlog(blog);
            }

        } catch (ConstraintViolationException e)  {
            return ResponseEntity.ok().body(new Response(false, ConstraintViolationExceptionHandler.getMessage(e)));
        } catch (Exception e) {
            return ResponseEntity.ok().body(new Response(false, e.getMessage()));
        }

        String redirectUrl = "/u/" + username + "/blogs/";
        return ResponseEntity.ok().body(new Response(true, "发布成功", redirectUrl));
    }

    /**
     * 修改个人资料
     * @return
     */
    @PostMapping("/{username}/profile")
    @PreAuthorize("authentication.name.equals(#username)")
    public String saveProfile(@PathVariable("username") String username,User user,Model model) {
        SecurityContext ctx = SecurityContextHolder.getContext();
        Authentication auth = ctx.getAuthentication();
        User originalUser = (User) auth.getPrincipal();
        originalUser.setNick(user.getNick());
        originalUser.setEmail(user.getEmail());
        userService.saveOrUpateUser(originalUser);
        return "redirect:/u/" + username + "/profile";
    }
    /**
     * 修改密码
     * @return
     */
    @PostMapping("/{username}/changePwd")
    @PreAuthorize("authentication.name.equals(#username)")
    public String changePwd(@PathVariable("username") String username,User user,Model model) {
        SecurityContext ctx = SecurityContextHolder.getContext();
        Authentication auth = ctx.getAuthentication();
        User originalUser = (User) auth.getPrincipal();
        // 判断密码是否做了变更
        String rawPassword = originalUser.getPassword();
        PasswordEncoder  encoder = new BCryptPasswordEncoder();
        String encodePasswd = encoder.encode(user.getPassword());
        boolean isMatch = encoder.matches(rawPassword, encodePasswd);
        if (!isMatch) {
            originalUser.setEncodePassword(user.getPassword());
        }
        userService.saveOrUpateUser(originalUser);
        return "redirect:/u/" + username + "/profile";
    }

    /**
     * 通过username获取用户
     */
    @GetMapping("/{username}/getUser")
    @PreAuthorize("authentication.name.equals(#username)")
    @ResponseBody
    public User getUser(@PathVariable("username") String username){
        User  user = (User)customUserService.loadUserByUsername(username);
        return user;
    }

    /**
     * 保存头像
     * @param username
     * @return
     */
    @PostMapping("/{username}/avatar")
    @PreAuthorize("authentication.name.equals(#username)")
    public ResponseEntity<Response> saveAvatar(@PathVariable("username") String username, @RequestParam("imagefile") MultipartFile multipartfile) throws Exception{

        String key=UUID.randomUUID().toString();
        if(!multipartfile.isEmpty()){
            File file = null;
            if(multipartfile.equals("")||multipartfile.getSize()<=0){
                multipartfile = null;
            }else{
                InputStream ins = multipartfile.getInputStream();
                file=new File(multipartfile.getOriginalFilename());
                inputStreamToFile(ins, file);
            }
            try {
                qiNiuService.uploadFile(file,key);
                User user = (User)customUserService.loadUserByUsername(username);
                String avatar="http://"+cdnPrefix+"/"+key;
                user.setAvatar(avatar);
                userService.saveOrUpateUser(user);
                return ResponseEntity.ok().body(new Response(true, "操作成功", avatar));
            } catch (ConstraintViolationException e)  {
                return ResponseEntity.ok().body(new Response(false, ConstraintViolationExceptionHandler.getMessage(e)));
            } catch (QiniuException e) {
                return ResponseEntity.ok().body(new Response(false, "操作失败", e.getMessage()));
            }finally {
                //如果不需要File文件可删除
                if(file.exists()){
                    file.delete();
                }
            }
        }
        return ResponseEntity.ok().body(new Response(false, "操作失败", null));
    }

    public static void inputStreamToFile(InputStream ins,File file) {
        try {
            OutputStream os = new FileOutputStream(file);
            int bytesRead = 0;
            byte[] buffer = new byte[8192];
            while ((bytesRead = ins.read(buffer, 0, 8192)) != -1) {
                os.write(buffer, 0, bytesRead);
            }
            os.close();
            ins.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
