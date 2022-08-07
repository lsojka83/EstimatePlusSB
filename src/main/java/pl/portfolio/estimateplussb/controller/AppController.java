package pl.portfolio.estimateplussb.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.util.WebUtils;
import pl.portfolio.estimateplussb.entity.User;
import pl.portfolio.estimateplussb.model.Messages;
import pl.portfolio.estimateplussb.model.Security;
import pl.portfolio.estimateplussb.model.URL;
import pl.portfolio.estimateplussb.repository.UserRepository;
import pl.portfolio.estimateplussb.service.EmailService;
import pl.portfolio.estimateplussb.validator.PasswordValidator;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Controller
public class AppController {

    private String incorrectLoginData = "Wrong user name or password";


    private final UserRepository userRepository;
    private final PasswordValidator passwordValidator;
    private final EmailService emailService;


    private Logger logger = LoggerFactory.getLogger(AppController.class);


    public AppController(UserRepository userRepository, PasswordValidator passwordValidator, EmailService emailService) {
        this.userRepository = userRepository;
        this.passwordValidator = passwordValidator;
        this.emailService = emailService;
    }

    @GetMapping("")
    public String loginPage(HttpSession httpSession,
                            @RequestParam(required = false) String userId,
                            HttpServletRequest request
    ) {
        if (userId != null) {  //temporary!!!

            if (userRepository.findById(Long.parseLong(userId)).isEmpty()) {
                return "login";

            }
            httpSession.setAttribute("user", userRepository.findById(Long.parseLong(userId)).get());
            if (userRepository.findById(Long.parseLong(userId)).get().isAdmin()) {
                return "redirect:/admin";
            } else {
                return "redirect:/user";
            }
        } else {
            User user = null;

            Cookie c = WebUtils.getCookie(request, "remember_user");
            if (c != null) {
                Optional<User> userOptional = userRepository.findByUuid(c.getValue());
                if (userOptional.isPresent()) {
                    user = userOptional.get();
                        httpSession.setAttribute("user", user);

                        if (user.isAdmin()) {
                            return "redirect:/admin";
                        } else {
                            return "redirect:/user";
                        }
                }
            }
        }

        return "login";
    }

    @PostMapping("")
    public String loginPage(
            HttpSession httpSession,
            @RequestParam String userName,
            @RequestParam String password,
            Model model,
            @RequestParam(required = false) String userId, //temporary!!!l
            @RequestParam String button,
            @RequestParam(required = false) String inputRememberPassword,
            HttpServletResponse response

    ) {
        if (button.equals("login")) {
            User user = null;


            if (userRepository.findByUserName(userName) == null) {
                model.addAttribute("incorrectLoginData", incorrectLoginData);
                return "login";
            }

            user = userRepository.findByUserName(userName);

            if (!Security.checkPassword(password, user.getPassword())) {
                model.addAttribute("incorrectLoginData", incorrectLoginData);
                return "login";
            }
            httpSession.setAttribute("user", user);

            if (inputRememberPassword != null && inputRememberPassword.equals("yes")) {
                Cookie cookie = new Cookie("remember_user", user.getUuid());
                cookie.setPath("/");
                response.addCookie(cookie);
//                user.setRememberLogin(1);
                userRepository.save(user);
            }

            if (user.isAdmin()) {
                return "redirect:/admin";
            } else {
                return "redirect:/user";
            }
        } else {
            return "redirect:/newaccount";
        }
    }

    @GetMapping("/logout")
    public String logout(
            HttpSession httpSession,
            HttpServletRequest request,
            HttpServletResponse response
    ) {
//        User userToLogout = (User) httpSession.getAttribute("user");
//        userToLogout.setRememberLogin(0);
//        userRepository.save(userToLogout);
        Cookie c = WebUtils.getCookie(request, "remember_user");
        if (c != null) {
            c.setMaxAge(0);
            response.addCookie(c);
        }

        httpSession.invalidate();
        return "redirect:/";
    }

    @GetMapping("/addaccount")
    public String addAccount(Model model) {

        model.addAttribute("user", new User());
        return "add-account";
    }

    @PostMapping("/addaccount")
    public String addAccount(Model model,
                             @Valid User user,
                             BindingResult bindingResult,
                             @RequestParam String password2) {

        if (!passwordValidator.isValid(user.getPassword(), null)) {
            model.addAttribute("invalidPassword", Messages.INVALID_PASSWORD);
        }
        if (!user.getPassword().equals(
                password2
        )) {
            model.addAttribute("invalidPassword", Messages.PASSWORD_ARE_NOT_EQUAL);
        }

        if (bindingResult.hasErrors()) {
            return "add-account";
        }

        if (model.getAttribute("invalidPassword") != null) {
            return "add-account";
        }

        user.setUuid(UUID.randomUUID().toString());
        user.setPasswordUnhashed(user.getPassword());
        user.setPassword(Security.hashPassword(user.getPassword()));
        userRepository.save(user);
        return "redirect:/";
    }

    @GetMapping("/resetpassword")
    public String resetPassword() {
        return "reset-password";
    }

    @PostMapping("/resetpassword")
    public String resetPassword(@RequestParam String email,
                                Model model) {
        List<User> userList = userRepository.findAll();
        if (userList.stream()
                .noneMatch(user -> user.getEmail()
                        .equals(email))) {
            model.addAttribute("emailnotexists", "yes");
            return "reset-password";
        }

        User user = userList.stream().filter(u -> u.getEmail().equals(email)).findFirst().get();
//        user.setUuid(UUID.randomUUID().toString());
        user.setSentResetRequest(1);
        userRepository.save(user);

        // send an email
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setText("Clink link to reset passwodrd: " + URL.APP_URL + "/reset?uuid=%s");
            String text = String.format(message.getText(), user.getUuid());
            emailService.sendSimpleMessage(user.getEmail(), Messages.APP_NAME + " - " + Messages.PASSWORD_RESET_LINK, text);
        } catch (Exception e) {
            // catch error
            logger.info("Error Sending Email: " + e.getMessage());
        }

        model.addAttribute("message", "Email sent");
        return "show-message";
    }


    @GetMapping("/reset")
    public String resetPasswordForm(@RequestParam String uuid,
                                    Model model) {

        if (userRepository.findByUuid(uuid).isEmpty()) {
            model.addAttribute("message", "Link is not active");
            return "show-message";
        }
        if (userRepository.findByUuid(uuid).get().getSentResetRequest() == 0) {
            model.addAttribute("message", "Link is not active");
            return "show-message";
        }

        model.addAttribute("uuid", uuid);
        return "reset-password-new-password";
    }

    @PostMapping("/reset")
    public String resetPasswordFormPost(Model model,
                                        @RequestParam String password,
                                        @RequestParam String password2,
                                        @RequestParam(required = false) String uuid
    ) {
        if (!passwordValidator.isValid(password, null)) {
            model.addAttribute("invalidPassword", Messages.INVALID_PASSWORD);
        }
        if (!password.equals(
                password2
        )) {
            model.addAttribute("invalidPassword", Messages.PASSWORD_ARE_NOT_EQUAL);
        }

        if (model.getAttribute("invalidPassword") != null) {
            return "reset-password-new-password";
        }

        Optional<User> userOptional = userRepository.findByUuid(uuid);

        if (userOptional.get() == null) {
            return "redirect:/";
        }

        User user = userOptional.get();

        user.setPassword(password);
        user.setSentResetRequest(0);
//        user.setUuid(null);

        user.setPasswordUnhashed(user.getPassword());
        user.setPassword(Security.hashPassword(user.getPassword()));
        userRepository.save(user);
        model.addAttribute("message", "Password was changed");
        return "show-message";
    }
}
