package ru.kata.spring.boot_security.demo.controllers;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ru.kata.spring.boot_security.demo.models.User;
import ru.kata.spring.boot_security.demo.models.Role;
import ru.kata.spring.boot_security.demo.service.RoleService;
import ru.kata.spring.boot_security.demo.service.UserService;
import ru.kata.spring.boot_security.demo.service.UserServiceImpl;
import java.util.List;

@Controller
@RequestMapping("/admin")
@Validated
public class AdminController {
    private final UserService userService;
    private final RoleService roleService;
    private PasswordEncoder passwordEncoder;

    @Autowired
    public AdminController(UserServiceImpl userService, RoleService roleService, RoleService roleService1, PasswordEncoder passwordEncoder) {
        this.userService = userService;
        this.roleService = roleService1;

        this.passwordEncoder = passwordEncoder;
    }

    @GetMapping
    public String showUsers(Authentication authentication, Model model) {
        User user =userService.findByUsername(authentication.getName());
        List<User> users = userService.getAllUsers();
        List <Role> roles =roleService.getAllRoles();
        model.addAttribute("users", users);
        model.addAttribute("user",user);
        model.addAttribute("roles",roles);
        model.addAttribute("newUser", new User());
        return "adminPanel";
    }
    @PostMapping("/update")
    public String updateUser(@ModelAttribute User user) {
        userService.updateUser(user);
        return "redirect:/admin";
    }
    @PostMapping("/user/new")
    public String addUser(@ModelAttribute User user) {
        userService.saveUser(user);
        return "redirect:/admin";
    }
    @GetMapping("/user/{id}")
    public String editUserForm(@Valid @PathVariable Long id, Model model) {
        User user = userService.findById(id);
        List<Role> roles = roleService.getAllRoles();
        model.addAttribute("user", user);
        model.addAttribute("roles", roles);
        return "redirect:/admin";
    }

    @PostMapping("/saveUser")
    public String saveUser(@ModelAttribute("user") User user) {
        if (user.getPassword() != null && !user.getPassword().isEmpty()) {
            String encodedPassword = passwordEncoder.encode(user.getPassword());
            user.setPassword(encodedPassword);
        }
        userService.saveUser(user);
        return "redirect:/admin";
    }

    @PostMapping("/deleteUser")
    public String deleteUser(@Valid @RequestParam("id") long id) {
        userService.deleteUser(id);
        return "redirect:/admin";
    }

    @PostMapping("/user")
    public String save(@ModelAttribute("user") User user) {
        userService.updateUser(user);
        return "redirect:/admin";
    }

}
