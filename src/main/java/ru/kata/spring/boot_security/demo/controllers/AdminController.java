package ru.kata.spring.boot_security.demo.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.kata.spring.boot_security.demo.entities.User;
import ru.kata.spring.boot_security.demo.services.RoleService;
import ru.kata.spring.boot_security.demo.services.UserService;

import java.util.List;
@PreAuthorize("hasRole('ROLE_ADMIN')")
@Controller
@RequestMapping("/admin")
public class AdminController {

    private final UserService userService;
    private final RoleService roleService;

    @Autowired
    public AdminController(UserService userService, RoleService roleService) {
        this.userService = userService;
        this.roleService = roleService;
    }

    @GetMapping
    public String adminPage(@AuthenticationPrincipal User currentUser, Model model) {
        List<User> users = userService.getAllUsers();
        model.addAttribute("users", users);
        model.addAttribute("currentUser", currentUser);
        model.addAttribute("roles", roleService.getAllRoles());
        model.addAttribute("newUser", new User());
        return "admin-page";
    }

    @PostMapping("/add")
    public String addUser(@ModelAttribute("newUser") User user) {
        userService.saveUser(user);
        return "redirect:/admin";
    }

    @PutMapping ("/edit/{id}") // использование правильных HTTP-методов
    public String updateUser(@PathVariable Long id, @ModelAttribute User user) {
        user.setId(id); // Устанавливаем ID пользователя
        userService.updateUser(id, user);
        return "redirect:/admin";
    }

    @DeleteMapping ("/delete/{id}") // использование правильных HTTP-методов
    public String deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
        return "redirect:/admin";
    }
}
