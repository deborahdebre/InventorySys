package com.genkey.inventorysys.Controllers;

import com.genkey.inventorysys.Model.EmployeeDetails;
import com.genkey.inventorysys.Model.LoginDetails;
import com.genkey.inventorysys.RepositoriesDAO.LoginRepository;
import org.hibernate.Session;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.persistence.EntityManager;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.List;

// This is the login controller to handle login operations

@Controller
@RequestMapping("/assetportal")

public class LoginController {
    // Required repositories to help implement needed methods
    @Autowired
    public LoginRepository loginrepo;

    @Autowired
    private EntityManager entityManager;

    // This method allows a new login and displays the login page
    @GetMapping("/login")
    public String displayLoginPage(Model model) {
        LoginDetails newLogin = new LoginDetails();
        model.addAttribute("login_details", newLogin);
        return "loginPage";
    }

    @GetMapping("/loginError")
    public String displayLoginPageError(Model model) {
        LoginDetails newLogin = new LoginDetails();
        model.addAttribute("login_details", newLogin);
        return "ErrorLoginPage";
    }

    // This is the logout method, to destroy the session of the currently logged in user
    @GetMapping("/logout")
    public String logout(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        session.invalidate();
        return "redirect:/assetportal/login";
    }



    // This method authenticates login details provided by users
    @PostMapping("/loginVerify")
    public String validateLogin(LoginDetails login_details,HttpServletRequest request,Model model) {
        Session currentSession = entityManager.unwrap(Session.class);
    // parse through employee details table in database to obtain record of user with specified username
        String hql = "FROM EmployeeDetails E WHERE E.email = :userName and E.password= :passWord";
        Query query = currentSession.createQuery(hql);
        query.setParameter("userName", login_details.getUsername());
        query.setParameter("passWord", login_details.getPassword());
        List<EmployeeDetails> results = query.getResultList();
        // if result is obtained call home page for employee. if not redirect to login
        if (results.size() >= 1) {
            HttpSession session=request.getSession();
            session.setAttribute("staffID",results.get(0).getStaffId());
            model.addAttribute("loggedInEmployee",results.get(0).getFName());

            // if the user is an asset manager, redirect to page asking which role user wants to log into
            if (results.get(0).getRole().getRoleId() == 1) {
                request.getSession(false);
                return "redirect:/regular/dashboard";
            }
            // if the user is an admin, redirect to page asking which role user wants to log into
            else if (results.get(0).getRole().getRoleId() == 2) {
                request.getSession(false);
                return "assetManager/loginPage2";
            }
            // if the user is a regular user, redirect to dashboard
            else if (results.get(0).getRole().getRoleId() == 3) {
                request.getSession(false);
                return "admin/loginPage3";
            }
        }
            // redirect to login page
            return "redirect:/assetportal/loginError";
    }


    @GetMapping("/loginAs")
    public String loginAs(HttpServletRequest request, Model model) {
        HttpSession session = request.getSession(false);
        Integer staffID = (Integer) session.getAttribute("staffID");
        List<EmployeeDetails> curEmpl = getStaff(staffID);
        model.addAttribute("loggedInEmployee",curEmpl.get(0).getFName());

       if (curEmpl.get(0).getRole().getRoleId() == 2) {
            request.getSession(false);
            return "assetManager/loginPage2";
        }
        // if the user is a regular user, redirect to dashboard
        else if (curEmpl.get(0).getRole().getRoleId() == 3) {
            request.getSession(false);
            return "admin/loginPage3";
        }

       return "redirect:/assetportal/login";
    }

    public List<EmployeeDetails> getStaff(int staffID) {
        Session currentSession = entityManager.unwrap(Session.class);
        String hql = "FROM EmployeeDetails Emp WHERE Emp.staffId = :id";
        Query query = currentSession.createQuery(hql);
        query.setParameter("id",staffID);
        List<EmployeeDetails> results = query.getResultList();
        return results;
    }
//    @GetMapping("/reset")
//    public String displayResetPage(Model model) {
//        LoginDetails newLogin = new LoginDetails();
//        model.addAttribute("login_details", newLogin);
//        return "loginPage";
//    }



//    String email = ServletUtil.getAttribute(request, "email");
//    User user = userRepository.findUserByEmail(email);
//
//        if(user == null)
//    {
//        model.addAttribute("error", "We didn't find this user");
//        return "forgotpassword";
//    }
//    PasswordResetToken token = new PasswordResetToken();
//        token.setToken(UUID.randomUUID().toString());
//        token.setUser(user);
//        token.setExpiryDate(30);
//        passwordResetTokenRepository.save(token);
//
//    Mail mail = new Mail();
//    Map<String, Object> modelObj = new HashMap<>();
//        modelObj.put("token",token);
//        modelObj.put("user", user);
//    String url =
//            request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort();
//        modelObj.put("resetUrl", url + "/resetpassword?token=" + token.getToken());
//        mail.setModel(modelObj);
//        emailService.sendEmail(mail);
//
//


}


