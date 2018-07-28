package eventures.web.controllers;

import eventures.domain.models.binding.UserLoginBindingModel;
import eventures.domain.models.binding.UserRegisterBindingModel;
import eventures.domain.models.service.UserServiceModel;
import eventures.service.services.UserService;
import eventures.utils.MappingUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.validation.Valid;

@Controller
public class UserController extends BaseController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/login")
    @PreAuthorize("isAnonymous()")
    public ModelAndView login(@ModelAttribute("userLoginBindingModel")UserLoginBindingModel userLoginBindingModel) {
        return super.view("users/login", "userLoginBindingModel", userLoginBindingModel);
    }

    @GetMapping("/register")
    @PreAuthorize("isAnonymous()")
    public ModelAndView register(@ModelAttribute("userRegisterBindingModel")UserRegisterBindingModel userRegisterBindingModel) {
        return super.view("users/register", "userRegisterBindingModel", userRegisterBindingModel);
    }

    @PostMapping("/register")
    @PreAuthorize("isAnonymous()")
    public ModelAndView registerConfirm(@Valid @ModelAttribute("userRegisterBindingModel")UserRegisterBindingModel userRegisterBindingModel, BindingResult bindingResult) {
        if (!userRegisterBindingModel.getPassword().equals(userRegisterBindingModel.getConfirmPassword())) {
            FieldError passwordError = new FieldError("userRegisterBindingModel", "password", "Passwords don't match.");
            bindingResult.addError(passwordError);

            return super.view("users/register", "userRegisterBindingModel", userRegisterBindingModel);
        } else if (bindingResult.hasErrors()) {
            return super.view("users/register", "userRegisterBindingModel", userRegisterBindingModel);
        }

        this.userService.registerUser((UserServiceModel) MappingUtil.map(userRegisterBindingModel, UserServiceModel.class));

        return super.redirect("/home");
    }
}
