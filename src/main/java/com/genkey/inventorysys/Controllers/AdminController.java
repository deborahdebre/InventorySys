package com.genkey.inventorysys.Controllers;

import com.genkey.inventorysys.Model.*;
import com.genkey.inventorysys.RepositoriesDAO.*;
import com.genkey.inventorysys.RepositoriesDAO.OldEmployeeRepository;
import org.hibernate.Session;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.persistence.EntityManager;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.List;

// This is the controller for the admin role, to perform admin functionalities

@Controller
@RequestMapping("/admin")
public class AdminController {
    // Required repositories to help implement needed methods
    @Autowired
    public EmployeeRepository employeerepo;

    @Autowired
    AssetRepository assetrepo;

    @Autowired
    LoginRepository loginrepo;


    @Autowired
    PairRepository pairrepo;

    @Autowired
    CheckedOutRepository checkedrepo;

    @Autowired
    DiscardedRepository discardedrepo;

    @Autowired
    OldEmployeeRepository oldrepo;

    @Autowired
    private EntityManager entityManager;


    //This method displays all company assets for the admin to see
    // list of assets is gotten using the findAll() method extended from CRUDrepository
    @GetMapping("/viewAllAssets")
    public String displayAssets(Model model, HttpServletRequest request){

        // Session Management, to keep track of the user currently logged in
        HttpSession session = request.getSession(false);
        Integer staffID = (Integer) session.getAttribute("staffID");

        List<EmployeeDetails> Emp = getStaff(staffID);
        model.addAttribute("loggedInEmployee",Emp.get(0));

    // list of assets is gotten using the findAll() method extended from CRUDrepository
        List<AssetDetails> assetList = assetrepo.findAll();
        model.addAttribute("allAsset", assetList);
        return "admin/viewAssets";
    }
    // This method is to get employee details based on a given staff id
    public List<EmployeeDetails> getStaff(int staffID) {

        Session currentSession = entityManager.unwrap(Session.class);
        String hql = "FROM EmployeeDetails Emp WHERE Emp.staffId = :id";
        Query query = currentSession.createQuery(hql);
        query.setParameter("id",staffID);
        List<EmployeeDetails> results = query.getResultList();

        return results;
    }

    // This method displays pending request page
    @GetMapping("/requests")
    public String displayPendingRequests(Model model,HttpServletRequest request){

        HttpSession session = request.getSession(false);
        Integer staffID = (Integer) session.getAttribute("staffID");

        List<EmployeeDetails> Emp = getStaff(staffID);
        model.addAttribute("loggedInEmployee",Emp.get(0));

        List<PairingDetails> pairs = pairrepo.findAll();
        model.addAttribute("pairs", pairs);
        return "admin/requests";
    }

    @PostMapping("/process")
    @Transactional
    public String processRequest(@RequestParam int req,@RequestParam int id ,HttpServletRequest request) {
        request.getSession(false);
        System.out.println("The values is " + req);
        System.out.println("The pairid is" + id);
            if(req==1){
                Session session = entityManager.unwrap(Session.class);
                String hql = " delete from PairingDetails where assetId = :listed_id ";
                String insert = "INSERT INTO CheckedOutAssets(checkedAssetID,employeeID, EmpName,job_title,assetNum,assetCategory,assetBrand,assetCondition)" + "SELECT assetId,staffId,name,job_title,assetNum,assetCategory,assetBrand,assetCondition FROM PairingDetails where assetId = :listed_id";
                String update = "update AssetDetails set status = 2 where assetId = :given_id ";

                Query query0 = session.createQuery(insert);
                query0.setParameter("listed_id", id);
                query0.executeUpdate();

                Query query = session.createQuery(hql);
                query.setParameter("listed_id", id);
                query.executeUpdate();

                Query query1 = session.createQuery(update);
                query1.setParameter("given_id",id);
                query1.executeUpdate();
            }
            else if (req==0){
                Session session = entityManager.unwrap(Session.class);
                String hql = " delete from PairingDetails where assetId = :listed_id ";
                String update = "update AssetDetails set status = 1 where assetId = :given_id ";

                Query query = session.createQuery(hql);
                query.setParameter("listed_id", id);
                query.executeUpdate();

                Query query1 = session.createQuery(update);
                query1.setParameter("given_id",id);
                query1.executeUpdate();

            }

        return "redirect:/admin/requests";
    }

    // This method displays generate reports page
    @GetMapping("/reports")
    public String displayReports(Model model,HttpServletRequest request){

        HttpSession session = request.getSession(false);
        Integer staffID = (Integer) session.getAttribute("staffID");

        List<EmployeeDetails> Emp = getStaff(staffID);
        model.addAttribute("loggedInEmployee", Emp.get(0));

        Session thissession = entityManager.unwrap(Session.class);

        Query query = thissession.createQuery("select count(*) from EmployeeDetails where jobDescription ='CEO'");
        Long ceo = (Long)query.uniqueResult();

        Query query1 = thissession.createQuery("select count(*) from EmployeeDetails where jobDescription ='CTO'");
        Long cto = (Long) query1.uniqueResult();

        Query query2 = thissession.createQuery("select count(*) from EmployeeDetails where jobDescription ='QA officer'");
        Long qa = (Long)query2.uniqueResult();

        Query query3 = thissession.createQuery("select count(*) from EmployeeDetails where jobDescription ='Senior Dev'");
        Long sd = (Long)query3.uniqueResult();

        Query query4 = thissession.createQuery("select count(*) from EmployeeDetails where jobDescription ='Junior Dev'");
        Long jd = (Long)query4.uniqueResult();



        Query query5 = thissession.createQuery("select count(*) from CheckedOutAssets where assetCategory ='Laptop' ");
        Long lap = (Long)query5.uniqueResult();

        Query query6 = thissession.createQuery("select count(*) from CheckedOutAssets where assetCategory ='Desktop' ");
        Long desk = (Long)query6.uniqueResult();

        Query query7 = thissession.createQuery("select count(*) from CheckedOutAssets where assetCategory = 'Mouse' ");
        Long mou = (Long)query7.uniqueResult();

        Query query8 = thissession.createQuery("select count(*) from CheckedOutAssets where assetCategory = 'Keyboard' ");
        Long key = (Long)query8.uniqueResult();

        Query query9 = thissession.createQuery("select count(*) from CheckedOutAssets where assetCategory = 'Server' ");
        Long ser = (Long)query9.uniqueResult();

        model.addAttribute("ceo", ceo);
        model.addAttribute("cto", cto);
        model.addAttribute("qa", qa);
        model.addAttribute("jd", jd);
        model.addAttribute("sd", sd);

        model.addAttribute("laptop", lap);
        model.addAttribute("desktop", desk);
        model.addAttribute("mouse", mou);
        model.addAttribute("keyboard", key);
        model.addAttribute("server", ser);

        return "admin/reports";
    }

    @GetMapping("/curEmpReport")
    public String currentEmployeeReport(Model model,HttpServletRequest request){
        request.getSession(false);
        List<EmployeeDetails> employeeList = employeerepo.findAll();
        model.addAttribute("allStaff", employeeList);
        return "report/currentEmployees";
    }

    @GetMapping("/formerEmpReport")
    public String formerEmployeeReport(Model model,HttpServletRequest request){
        request.getSession(false);
        List<OldEmployees> oldemp = oldrepo.findAll();
        model.addAttribute("staffs", oldemp);
        return "report/formerEmployees";
    }

    @GetMapping("/curAssetsReport")
    public String currentAssetsReport(Model model,HttpServletRequest request){
        request.getSession(false);
        List<AssetDetails> assetList = assetrepo.findAll();
        model.addAttribute("asset", assetList);
        return "report/currentAssets";
    }

    @GetMapping("/discAssetsReport")
    public String discardedAssetsReport(Model model,HttpServletRequest request){
        request.getSession(false);
        List<DiscardedAssets> assetList = discardedrepo.findAll();
        model.addAttribute("asset", assetList);
        return "report/discardedAssets";
    }

    @GetMapping("/checkedOutAssetsReport")
    public String checkedOutAssetsReport(Model model,HttpServletRequest request){
        request.getSession(false);
        List<CheckedOutAssets> checkedList = checkedrepo.findAll();
        model.addAttribute("checkedOut",checkedList);
        return "report/checkedOutAssets";
    }

    // This method displays admin settings page
    @GetMapping("/settings")
    public String displaySettings(Model model,HttpServletRequest request){
        HttpSession session = request.getSession(false);
        Integer staffID = (Integer) session.getAttribute("staffID");

        List<EmployeeDetails> Emp = getStaff(staffID);
        model.addAttribute("loggedInEmployee",Emp.get(0));

        EmployeeDetails tempEmployee = new EmployeeDetails();
        model.addAttribute("changePass", tempEmployee);
        return "admin/settings";
    }


    // This method creates a new Employee, returns the employee creation form
    @PostMapping("/createnewaccount")
    public String displayCreateNewUser(Model model,HttpServletRequest request) {
        request.getSession(false);

        EmployeeDetails newEmployee = new EmployeeDetails();
        model.addAttribute("employee_details", newEmployee);
        return "admin/newUser";
    }
    // This method saves the details of a new employee and populates the login details table
    // the login details table will be used for verification to crosscheck provided login details
    @PostMapping("/newUserSave")
    public String saveUser(EmployeeDetails employee_details,HttpServletRequest request){
        request.getSession(false);
        LoginDetails newLogin = new LoginDetails();
        // employee_details.setRole();
        // set role id to 1 automatically
        newLogin.setUsername(employee_details.getEmail());
        newLogin.setPassword(employee_details.getPassword());
        newLogin.setRole_(employee_details.getRole());
        loginrepo.save(newLogin);
        employeerepo.save(employee_details);
        return "redirect:/admin/manage";
    }
    // This method displays the staff management page; admin can add,update or delete staff records
    @GetMapping("/manage")
    public String displayStaffManagement(Model model,HttpServletRequest request){
        HttpSession session = request.getSession(false);
        List<EmployeeDetails> employeeList = employeerepo.findAll();
        model.addAttribute("allStaff", employeeList);
        return "admin/staffManagement";
    }

    @PostMapping("/update")
    public String updateForm(Model model, @RequestParam int staff, HttpServletRequest request) {
        request.getSession(false);

        List<EmployeeDetails> person = getStaff(staff);
        model.addAttribute("updateEmployee", person.get(0));
        return "admin/recordUpdate";
    }

    @PostMapping("/updateRecord")
    @Transactional
    public String updateRecord(EmployeeDetails employeeDetails, HttpServletRequest request) {
        request.getSession(false);
        Session session = entityManager.unwrap(Session.class);
        System.out.println(employeeDetails.getFName());
        String hql = " update EmployeeDetails set fName = :fname,lName=:lname,jobDescription=:job,email=:email, role =:role where staffId = :listed_id ";
        Query query = session.createQuery(hql);
        query.setParameter("listed_id", employeeDetails.getStaffId());
        query.setParameter("fname", employeeDetails.getFName());
        query.setParameter("lname", employeeDetails.getLName());
        query.setParameter("job", employeeDetails.getJobDescription());
        query.setParameter("email", employeeDetails.getEmail());
        query.setParameter("role", employeeDetails.getRole());
        query.executeUpdate();
        return "redirect:/admin/manage";
    }


    @PostMapping("/deleteRecord")
    @Transactional
    public String deleteRecord(@RequestParam List<Integer> staff) {

        for (int i : staff) {
            Session session = entityManager.unwrap(Session.class);
            String hql = " delete from EmployeeDetails where staffId = :listed_id ";
            String insert = "INSERT INTO OldEmployees(empId,firstName, lastName,jobDescription,email)" + "SELECT staffId,fName,lName,jobDescription,email FROM EmployeeDetails where staffId = :listed_id";
            Query query0 = session.createQuery(insert);
            query0.setParameter("listed_id", i);
            query0.executeUpdate();
            Query query = session.createQuery(hql);
            query.setParameter("listed_id", i);
            query.executeUpdate();
        }
        return "redirect:/admin/manage";

    }
    @GetMapping("/loginAs")
    public String loginAs(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        return "/admin/loginPage3";
    }


    @GetMapping("/updatePassword")
    @Transactional
    public String updatePassword(EmployeeDetails changePass,HttpServletRequest request, Model model) {
        //to get logged in employee
        HttpSession session = request.getSession(false);
        Integer staffID = (Integer) session.getAttribute("staffID");
        List<EmployeeDetails> Emp = getStaff(staffID);
        Session currentSession = entityManager.unwrap(Session.class);
        // parse through employee details table in database to obtain record of user with specified username
        String hql = "FROM EmployeeDetails E WHERE E.email= :userName and E.password= :passWord";
        Query query = currentSession.createQuery(hql);
        query.setParameter("userName", Emp.get(0).getEmail());
        query.setParameter("passWord", Emp.get(0).getPassword());
        List<EmployeeDetails> results = query.getResultList();
        if(changePass.getPassword().equals(results.get(0).getPassword())){
            String upQl = "UPDATE EmployeeDetails E SET E.password= :newPass WHERE E.staffId= :oldUser";
            Query queryUpdate = currentSession.createQuery(upQl);
            queryUpdate.setParameter("oldUser", Emp.get(0).getStaffId());
            queryUpdate.setParameter("newPass", changePass.getEmail());
            queryUpdate.executeUpdate();

        }
        return "redirect:/admin/settings";
    }


}
