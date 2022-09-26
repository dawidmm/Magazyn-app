package pl.ekookna.magazynapp.warehouse.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class TemplateController {

    @GetMapping("/")
    public String getHomePage() {
        return "home";
    }

    @GetMapping("/user")
    public String getAddUserPage() {
        return "user";
    }

    @GetMapping("/article")
    public String getAddArticlePage() {
        return "article";
    }

    @GetMapping("/warehouse")
    public String getAddWarehousePage() {
        return "warehouse";
    }

    @GetMapping("/article/request")
    public String getAddArticleToWarehousePage() {
        return "article_warehouse";
    }
}
