package com.genkey.inventorysys.Controllers;

import com.genkey.inventorysys.Model.*;
import com.genkey.inventorysys.RepositoriesDAO.*;
import org.hibernate.Session;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.persistence.EntityManager;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.List;

// This is the controller for the regular user role, to perform regular user functionalities

@Controller
@RequestMapping("/regular")
public class RegularController {

    // Required repositories to help implement needed methods
    @Autowired
    AssetRepository assetrepo;

    @Autowired
    LoginRepository loginrepo;

    @Autowired
    EmployeeRepository employeerepo;

    @Autowired
    PairRepository pairrepo;

    @Autowired
    CheckedOutRepository checkedoutrepo;

    @Autowired
    AMPendingRepo amPendingRepo;

    @Autowired
    private EntityManager entityManager;



    public List<CheckedOutAssets> getCheckedOutAssets(int staffID) {
        Session sessionNew = entityManager.unwrap(Session.class);
        String hql = "FROM CheckedOutAssets Checked WHERE Checked.employeeID = :id";
        Query query = sessionNew.createQuery(hql);
        query.setParameter("id",staffID);
        List<CheckedOutAssets> checked = query.getResultList();
        return checked;
    }



    // This method returns the regular user settings
    @GetMapping("/settings")
    public String displaySettings(Model model,HttpServletRequest request) {

        HttpSession session = request.getSession(false);
        Integer staffID = (Integer) session.getAttribute("staffID");

        List<EmployeeDetails> Emp = getStaff(staffID);
        model.addAttribute("loggedInEmployee",Emp.get(0));

        EmployeeDetails tempEmployee = new EmployeeDetails();
        model.addAttribute("changePass", tempEmployee);

        return "regular/settings";
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

    public List<CheckedOutAssets> getChAsset(int assetID) {
        Session sessionNew2 = entityManager.unwrap(Session.class);
        String retrieve = "FROM CheckedOutAssets Checked WHERE Checked.checkedAssetID = :id";
        Query query = sessionNew2.createQuery(retrieve);
        query.setParameter("id",assetID);
        List<CheckedOutAssets> checked = query.getResultList();
        return checked;
    }
    //
    @PostMapping("/pend")
    // @ResponseBody
    @Transactional
    public String pendingRequest(@RequestParam List<Integer> id,HttpServletRequest request) {
        HttpSession session1 = request.getSession(false);
        Integer staffID = (Integer) session1.getAttribute("staffID");
        List<EmployeeDetails> Emp = getStaff(staffID);
        for(int i:id){
            Session session = entityManager.unwrap(Session.class);
            String hql = "update AssetDetails set status = 4 where assetId = :listed_id ";
            Query query = session.createQuery(hql);
            query.setParameter("listed_id",i);
            query.executeUpdate();
            List<AssetDetails> asset = getAsset(i);
            PairingDetails pair = new PairingDetails();
            pair.setStaffId(staffID);
            pair.setAssetId(i);
            String name = Emp.get(0).getFName() + " " + Emp.get(0).getLName();
            pair.setName(name);
            pair.setJob_title(Emp.get(0).getJobDescription());
            pair.setAssetNum(asset.get(0).getSerialNum());
            pair.setAssetCategory(asset.get(0).getCategory().getCategory());
            pair.setAssetBrand(asset.get(0).getBrand());
            pair.setAssetCondition(asset.get(0).getCondition());
            pairrepo.save(pair);
        }
        //return "Your request is being processed, kindly go back and refresh the page";
        return "redirect:/regular/available";
    }

    // This method returns the regular user dashboard
    @GetMapping("/dashboard")
    public String displayDashboard(Model model, HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        Integer staffID = (Integer) session.getAttribute("staffID");
        List<CheckedOutAssets> checkedForEmployee = getCheckedOutAssets(staffID);
        List<EmployeeDetails> Emp = getStaff(staffID);
        model.addAttribute("loggedInEmployee",Emp.get(0));
        model.addAttribute("checkedForEmployee",checkedForEmployee);
        Session sessionNew = entityManager.unwrap(Session.class);
        String hql = "FROM AMPending Checked WHERE Checked.employeeID = :id";
        Query query = sessionNew.createQuery(hql);
        query.setParameter("id",staffID);
        List<CheckedOutAssets> checked = query.getResultList();
        model.addAttribute("pendingCheckIn",checked);
//        Session sessionNew = entityManager.unwrap(Session.class);
//        String hql = "FROM CheckedOutAssets Checked WHERE Checked.employeeID = :id";
//        Query query = sessionNew.createQuery(hql);
//        query.setParameter("id",staffID);
//        List<CheckedOutAssets> checkedForEmployee = query.getResultList();
        return "regular/regularDashboard";
    }


    public List<AssetDetails> getAsset(int assetID) {

        Session currentSession = entityManager.unwrap(Session.class);
        String hql = "FROM AssetDetails Asset WHERE Asset.assetId = :id";
        Query query = currentSession.createQuery(hql);
        query.setParameter("id",assetID);
        List<AssetDetails> results = query.getResultList();

        return results;
    }


    // This method displays available assets so the regular user can make requests for them
    @GetMapping("/available")
    public String displayAvailableAssets(Model model, HttpServletRequest request) {
        //to get logged in employee
        HttpSession session = request.getSession(false);
        Integer staffID = (Integer) session.getAttribute("staffID");

        List<EmployeeDetails> Emp = getStaff(staffID);
        model.addAttribute("loggedInEmployee",Emp.get(0));

        // to get list of available assets
        List<AssetDetails> assetList = assetrepo.findAll();
        List<AssetDetails> list = new ArrayList<AssetDetails>();
        for (AssetDetails asset : assetList) {
            if (asset.getStatus().getStatusId() == 1) {
                list.add(asset);
            }
        }
        model.addAttribute("availableAsset", list);
        return "regular/viewAssets";
    }

    @Transactional
    @GetMapping("/updatePassword")
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
        return "redirect:/regular/settings";
    }

    @PostMapping("/pendingCheckInRequest")
    @Transactional
    public String pendingCheckInRequest(@RequestParam List<Integer> assetCheck,HttpServletRequest request,Model model) {
        HttpSession session2 = request.getSession(false);
        for (int i=0;i<assetCheck.size();i++) {
            List<CheckedOutAssets> chA = getChAsset(assetCheck.get(i));
            AMPending amPending = new AMPending();
            amPending.setAssetBrand(chA.get(0).getAssetBrand());
            amPending.setAssetCategory(chA.get(0).getAssetCategory());
            amPending.setAssetNum(chA.get(0).getAssetNum());
            amPending.setCheckedAssetID(chA.get(0).getCheckedAssetID());
            amPending.setAssetCondition(chA.get(0).getAssetCondition());
            amPending.setJob_title(chA.get(0).getJob_title());
            amPending.setEmpName(chA.get(0).getEmpName());
            amPending.setEmployeeID(chA.get(0).getEmployeeID());
            amPendingRepo.save(amPending);
        }
        return "redirect:/regular/dashboard";
    }

}
