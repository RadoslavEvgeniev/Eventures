package eventures.web.controllers;

import org.springframework.web.servlet.ModelAndView;

public abstract class BaseController {

    protected ModelAndView view(String view, String objectName, Object object) {
        ModelAndView modelAndView = new ModelAndView("fragments/layout");

        modelAndView.addObject("view", view);
        modelAndView.addObject(objectName, object);

        return modelAndView;
    }

    protected ModelAndView view(String view) {
        ModelAndView modelAndView = new ModelAndView("fragments/layout");

        modelAndView.addObject("view", view);

        return modelAndView;
    }

    protected ModelAndView redirect(String url) {
        return new ModelAndView("redirect:" + url);
    }
}
