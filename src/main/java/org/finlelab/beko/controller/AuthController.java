package org.finlelab.beko.controller;

import org.finlelab.beko.dto.UserDTO;
import org.finlelab.beko.entity.User;
import org.finlelab.beko.service.UserService;
import jakarta.validation.Valid;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class AuthController {

    private final UserService userService;

    public AuthController(UserService userService){

        this.userService=userService;
    }

    @GetMapping("/register")
    public String showRegistrationForm(Model model){

        UserDTO userDto = new UserDTO();
        model.addAttribute("user",userDto);
        return "register";

    }


    @PostMapping("/register/save")
    public String registration(@Valid @ModelAttribute("user") UserDTO userDto, BindingResult result, Model model) {
        System.out.println("UserDto: " + userDto);

        User existingUser = userService.findByUsername(userDto.getUsername());
        if(existingUser != null && existingUser.getUsername() != null && !existingUser.getUsername().isEmpty()){
            result.rejectValue("username", null, "An account with this username already exists");
        }

        if(result.hasErrors()){
            model.addAttribute("user", userDto);
            return "register";
        }

        userService.saveUser(userDto);
        return "redirect:/register?success";
    }


    @GetMapping("/login")
    public String showLoginPage() {
        return "login";
    }

    @GetMapping("/dashboard")
    public String dashboard(Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        model.addAttribute("username", authentication.getName());
        return "task-list";
    }
}
