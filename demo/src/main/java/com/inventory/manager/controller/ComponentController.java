package com.inventory.manager.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ComponentController {
    @GetMapping({"/components"})
    public String listComponents(Model model) { return "components/list"; }
}

