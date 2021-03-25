package com.lianshuwang.controller;

import com.lianshuwang.domin.User;
import com.lianshuwang.helper.UploadHelper;
import com.lianshuwang.helper.UserHelper;
import com.lianshuwang.service.UserService;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

//个人资料
@Controller
public class UserController {
    @Autowired
    private UserService userService;

    private static final Log logger = LogFactory.getLog(UserController.class);

    //获取用户信息
    @RequestMapping(value = "/person")
    public String getPerosonInfo(Model model, HttpSession session) {
        UserHelper userHelper = (UserHelper) session.getAttribute("userHelper");
        if (null == userHelper) {
            return "redirect:index";
        }
        model.addAttribute("user", userHelper);
        logger.info("The user(" + userHelper.getUserName() + ") is coming to person information page!");
        return "personInfo";
    }

    //修改个人信息
    @RequestMapping(value = "/infoModify")
    public String infoModify(String name, String email, String avatarImg, HttpSession session) {
        logger.info("The user is modifying his information!");
        UserHelper userHelper = (UserHelper) session.getAttribute("userHelper");
        User user = new User();
        user.setId(userHelper.getId());
        user.setUserName(name);
        user.setEmail(email);
        int avatarId = userService.getAvatarId(avatarImg);
        user.setAvatarNum(avatarId);
        userService.updateUserInfo(user);
        User user1;
        user1 = userService.queryById(userHelper.getId());
        UserHelper newUserHelper;
        newUserHelper = userService.getLoginUser(user1.getUserCode(),user1.getUserPassword());
        session.setAttribute("userHelper",newUserHelper);
        return "redirect:/person";
    }

    //修改密码
    @RequestMapping(value = "/pwdModify")
    public String pwdModify(String newPwd, HttpSession session) {
        logger.info("The user is modifying his password!");
        UserHelper userHelper = (UserHelper) session.getAttribute("userHelper");
        userService.updateUserPassword(userHelper.getId(), newPwd);
        session.invalidate();
        return "redirect:/index";
    }

    //获取上传历史
    @RequestMapping(value = "/getUploadHistory")
    @ResponseBody
    public Map<String,Object> getUploadHistory(HttpSession session) {
        Map<String,Object> resultMap = new HashMap<String,Object>();
        UserHelper userHelper = (UserHelper) session.getAttribute("userHelper");
        List<UploadHelper> uploadHelperList = userService.getUploadedBook(userHelper.getId());
        resultMap.put("uploadList", uploadHelperList);
        logger.info("you are looking up the uploaded books");
        return resultMap;
    }

    //反馈意见
    @RequestMapping(value = "/feedback")
    @ResponseBody
    public void feedback(String contact, String suggestion,HttpSession session) {
        UserHelper userHelper = (UserHelper) session.getAttribute("userHelper");
        userService.addFeedback(userHelper.getId(),contact,suggestion);
        logger.info("you are posting the suggestion!");
        logger.info("contact:" + contact);
        logger.info("suggestion:" + suggestion);
    }
}
