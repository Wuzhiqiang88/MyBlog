package com.sao.controller;

import com.sao.domain.Catalog;
import com.sao.domain.User;
import com.sao.service.CatalogService;
import com.sao.service.impl.CustomUserService;
import com.sao.util.ConstraintViolationExceptionHandler;
import com.sao.vo.CatalogVO;
import com.sao.vo.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.validation.ConstraintViolationException;
import java.util.List;

@Controller
@RequestMapping("/catalogs")
public class CatalogController {

    @Autowired
    private CustomUserService customUserService;

    @Autowired
    private CatalogService catalogService;
    /**
     * 添加分类
     */
    @PostMapping("/add")
    @PreAuthorize("authentication.name.equals(#catalogVO.username)")// 指定用户才能操作方法
    public ResponseEntity<Response> create(@RequestBody CatalogVO catalogVO) {
        System.out.println("添加分类");
        String username = catalogVO.getUsername();
        Catalog catalog = catalogVO.getCatalog();

        User user = (User)customUserService.loadUserByUsername(username);

        try {
            catalog.setUser(user);
            catalogService.saveCatalog(catalog);
        } catch (ConstraintViolationException e)  {
            return ResponseEntity.ok().body(new Response(false, ConstraintViolationExceptionHandler.getMessage(e)));
        } catch (Exception e) {
            return ResponseEntity.ok().body(new Response(false, e.getMessage()));
        }

        return ResponseEntity.ok().body(new Response(true, "添加成功", null));
    }

    /**
     * 获取分类列表
     * @param model
     * @return
     */
    @GetMapping("/getCatalog")
    public String listComments(@RequestParam(value="username",required=true) String username, Model model) {
        User user = (User)customUserService.loadUserByUsername(username);
        List<Catalog> catalogs = catalogService.listCatalogs(user);

        model.addAttribute("catalogs", catalogs);
        return "/userspace/blogedit :: #listCatalog";
    }

}
