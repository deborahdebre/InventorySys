package com.genkey.inventorysys.Controllers;

import com.genkey.inventorysys.Model.*;
import com.genkey.inventorysys.RepositoriesDAO.*;
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

// This is the controller for the asset Manager role, to perform asset Manager functionalities
@Controller
@RequestMapping("/assetmanager")
public class AssetManagerController {

    // Required repositories to help implement needed methods
    @Autowired
    AssetRepository assetrepo;

    @Autowired
    EmployeeRepository employeerepo;

    @Autowired
    DiscardedRepository discardedrepo;

    @Autowired
    CheckedOutRepository checkedrepo;

    @Autowired
    private EntityManager entityManager;

    @Autowired
    AMPendingRepo amPendingRepo;


    // This method creates a new asset
    @PostMapping("/newAsset")
    public String displayNewAssetForm(Model model,HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        Integer staffID = (Integer) session.getAttribute("staffID");

        List<EmployeeDetails> Emp = getStaff(staffID);
        model.addAttribute("loggedInEmployee",Emp.get(0));

        AssetDetails newAsset = new AssetDetails();
        model.addAttribute("asset_details", newAsset);
        return "assetManager/newAsset";
    }

    // This method displays pending request page
//    @GetMapping("/requests")
//    public String displayPendingRequests(Model model, HttpServletRequest request){
//
//        HttpSession session = request.getSession(false);
//        Integer staffID = (Integer) session.getAttribute("staffID");
//
//        List<EmployeeDetails> Emp = getStaff(staffID);
//        model.addAttribute("loggedInEmployee",Emp.get(0));
//        return "assetManager/requests";
//    }

    // This method displays generate reports page
    @GetMapping("/reports")
    public String displayReports(Model model, HttpServletRequest request){
        HttpSession session = request.getSession(false);
        /*Integer staffID = (Integer) session.getAttribute("staffID");

        List<EmployeeDetails> Emp = getStaff(staffID);
        model.addAttribute("loggedInEmployee",Emp.get(0));*/


        Session thissession = entityManager.unwrap(Session.class);

        Query query = thissession.createQuery("select count(*) from AssetDetails where category.categoryId =1");
        Long lap1 = (Long)query.uniqueResult();

        Query query1 = thissession.createQuery("select count(*) from AssetDetails where category.categoryId = 2");
        Long des1 = (Long) query1.uniqueResult();

        Query query2 = thissession.createQuery("select count(*) from AssetDetails where category.categoryId =3");
        Long key1 = (Long)query2.uniqueResult();

        Query query3 = thissession.createQuery("select count(*) from AssetDetails where category.categoryId =4");
        Long mou1 = (Long)query3.uniqueResult();

        Query query4 = thissession.createQuery("select count(*) from AssetDetails where category.categoryId =5");
        Long ser1 = (Long)query4.uniqueResult();



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

        model.addAttribute("laptop1", lap1);
        model.addAttribute("desktop1", des1);
        model.addAttribute("keyboard1", key1);
        model.addAttribute("mouse1", mou1);
        model.addAttribute("server1", ser1);

        model.addAttribute("laptop", lap);
        model.addAttribute("desktop", desk);
        model.addAttribute("mouse", mou);
        model.addAttribute("keyboard", key);
        model.addAttribute("server", ser);

        return "assetManager/reports";
    }


    @GetMapping("/curAssetsReport")
    public String currentAssetsReport(Model model,HttpServletRequest request){
        request.getSession(false);
        List<AssetDetails> assetList = assetrepo.findAll();
        model.addAttribute("asset", assetList);
        return "report2/currentAssets";
    }

    @GetMapping("/discAssetsReport")
    public String discardedAssetsReport(Model model,HttpServletRequest request){
        request.getSession(false);
        List<DiscardedAssets> assetList = discardedrepo.findAll();
        model.addAttribute("asset", assetList);
        return "report2/discardedAssets";
    }

    @GetMapping("/checkedOutAssetsReport")
    public String checkedOutAssetsReport(Model model,HttpServletRequest request){
        request.getSession(false);
        List<CheckedOutAssets> checkedList = checkedrepo.findAll();
        model.addAttribute("checkedOut",checkedList);
        return "report2/checkedOutAssets";
    }
    // This method displays asset manager settings page
    @GetMapping("/settings")
    public String displaySettings(Model model,HttpServletRequest request){

        HttpSession session = request.getSession(false);
        Integer staffID = (Integer) session.getAttribute("staffID");

        List<EmployeeDetails> Emp = getStaff(staffID);
        model.addAttribute("loggedInEmployee",Emp.get(0));

        EmployeeDetails tempEmployee = new EmployeeDetails();
        model.addAttribute("changePass", tempEmployee);

        return "assetManager/settings";
    }
    // This method adds new asset to the database
    @PostMapping("/newAssetSave")
    public String saveAsset(AssetDetails asset_details,Model model,HttpServletRequest request){
        HttpSession session = request.getSession(false);
        Integer staffID = (Integer) session.getAttribute("staffID");

        List<EmployeeDetails> Emp = getStaff(staffID);
        model.addAttribute("loggedInEmployee",Emp.get(0));
        assetrepo.save(asset_details);
        return "redirect:/assetmanager/manage";
    }

    public List<EmployeeDetails> getStaff(int staffID) {
        Session currentSession = entityManager.unwrap(Session.class);
        String hql = "FROM EmployeeDetails Emp WHERE Emp.staffId = :id";
        Query query = currentSession.createQuery(hql);
        query.setParameter("id",staffID);
        List<EmployeeDetails> results = query.getResultList();
        return results;
    }

    // This method enables asset manager manipulate asset records: add, update,delete
    @GetMapping("/manage")
    public String displayAssetManagement(Model model, HttpServletRequest request){

        HttpSession session = request.getSession(false);
        Integer staffID = (Integer) session.getAttribute("staffID");

        List<EmployeeDetails> Emp = getStaff(staffID);
        model.addAttribute("loggedInEmployee",Emp.get(0));

        List<AssetDetails> assetList = assetrepo.findAll();
        model.addAttribute("allAsset", assetList);
        return "assetManager/assetManagement";
    }
    @PostMapping("/updateAsset")
    public String updateAsset(Model model,@RequestParam int asset,HttpServletRequest request){
        HttpSession session = request.getSession(false);
        List<AssetDetails>  assetDets = getAsset(asset);
        model.addAttribute("updateAsset", assetDets.get(0));
        return "assetManager/updateAsset";
    }
    // This method is to get employee details based on a given staff id
    public List<AssetDetails> getAsset(int assetId) {
        Session currentSession = entityManager.unwrap(Session.class);
        String hql = "FROM AssetDetails A WHERE A.assetId= :id";
        Query query = currentSession.createQuery(hql);
        query.setParameter("id", assetId);
        List<AssetDetails> Assetresults = query.getResultList();
        return Assetresults;
    }
    @PostMapping("/update")
    @Transactional
    public String update(AssetDetails updateAsset,@RequestParam int asset, HttpServletRequest request) {
        request.getSession(false);
        Session session = entityManager.unwrap(Session.class);
        String hql = "UPDATE AssetDetails A set brand = :brand,serialNum=:serialNum,category=:category,condition=:condition, location =:location, status = :status WHERE assetId = :id";
        Query query = session.createQuery(hql);
        AssetDetails assid = getAsset(asset).get(0);
        query.setParameter("id",assid.getAssetId() );
        query.setParameter("brand", updateAsset.getBrand());
        query.setParameter("serialNum", updateAsset.getSerialNum());
        query.setParameter("category", updateAsset.getCategory());
        query.setParameter("condition", updateAsset.getCondition());
        query.setParameter("location", updateAsset.getLocation());
        query.setParameter("status", updateAsset.getStatus());
        query.executeUpdate();
        return "redirect:/assetmanager/manage";
    }

    @PostMapping("/deleteAsset")
    @Transactional
    public String deleteAsset(@RequestParam List<Integer> asset) {
        for (int i : asset) {
            Session session = entityManager.unwrap(Session.class);
            String select =  "FROM AssetDetails where assetId = :listed_id";
            Query query1 = session.createQuery(select);
            query1.setParameter("listed_id", i);
            List<AssetDetails> Assetresults = query1.getResultList();
            AssetDetails temp = Assetresults.get(0);
            DiscardedAssets newDis = new DiscardedAssets();
            newDis.setAssetId(temp.getAssetId());
            newDis.setStatus(temp.getStatus().getStatus());
            newDis.setLocation(temp.getLocation().getLocation());
            newDis.setCategory(temp.getCategory().getCategory());
            newDis.setSerialNum(temp.getSerialNum());
            newDis.setBrand(temp.getBrand());
            newDis.setCondition(temp.getCondition());
            discardedrepo.save(newDis);
            String hql = " delete from AssetDetails where assetId = :listed_id ";
            Query query = session.createQuery(hql);
            query.setParameter("listed_id", i);
            query.executeUpdate();
        }
        return "redirect:/assetmanager/manage";
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
        return "redirect:/assetmanager/settings";
    }
    @GetMapping("/loginAs")
    public String loginAs(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        return "assetManager/loginPage2";
    }
    public List<CheckedOutAssets> getChAsset(int assetID) {
        Session sessionNew2 = entityManager.unwrap(Session.class);
        String retrieve = "FROM CheckedOutAssets Checked WHERE Checked.checkedAssetID = :id";
        Query query = sessionNew2.createQuery(retrieve);
        query.setParameter("id",assetID);
        List<CheckedOutAssets> checked = query.getResultList();
        return checked;
    }

    @GetMapping("/pendingCheckInRequest")
    // @ResponseBody
    @Transactional
    public String pendingCheckInRequest(HttpServletRequest request,Model model) {
        HttpSession session2 = request.getSession(false);
        List<AMPending> chA = amPendingRepo.findAll();
        if (chA.isEmpty()){
            return "assetManager/request2";
        }
        model.addAttribute("requests", chA);
        System.out.println(chA.get(0).getCheckedAssetID());
        return "assetManager/requests";
    }


    @PostMapping("/process")
    @Transactional
    public String processRequests(@RequestParam Integer req, @RequestParam Integer id, HttpServletRequest request,Model model) {
        System.out.println("Here");
        request.getSession(false);
        System.out.println("The values is " + req);
        System.out.println("The pairid is " + id);
        if(req==1){
            Session session = entityManager.unwrap(Session.class);
            String update = "update AssetDetails set status = 1 where assetId = :given_id ";
            String hql = "delete from CheckedOutAssets where checkedAssetID = :listed_id ";

            Query query0 = session.createQuery(update);
            query0.setParameter("given_id", id);
            query0.executeUpdate();

            Query query = session.createQuery(hql);
            query.setParameter("listed_id", id);
            query.executeUpdate();
            amPendingRepo.deleteById(id);
        }
        else if(req==0){
            amPendingRepo.deleteById(id);
        }

        return "redirect:/assetmanager/pendingCheckInRequest";

    }
}
