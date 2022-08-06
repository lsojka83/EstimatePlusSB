package pl.portfolio.estimateplussb.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import pl.portfolio.estimateplussb.entity.Estimate;
import pl.portfolio.estimateplussb.entity.PriceList;
import pl.portfolio.estimateplussb.entity.PriceListItem;
import pl.portfolio.estimateplussb.entity.User;
import pl.portfolio.estimateplussb.model.ColumnAssigment;
import pl.portfolio.estimateplussb.model.Excel;
import pl.portfolio.estimateplussb.model.Messages;
import pl.portfolio.estimateplussb.model.Security;
import pl.portfolio.estimateplussb.repository.*;
import pl.portfolio.estimateplussb.validator.PasswordValidator;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Controller
@SessionAttributes("user")
@RequestMapping("/admin")
public class AdminController {

    private static final Logger logger = LoggerFactory.getLogger(AdminController.class);


    private final PriceListRepository priceListRepository;
    private final PriceListItemRepository priceListItemRepository;
    private final UserRepository userRepository;
    private final EstimateRepository estimateRepository;
    private final EstimateItemRepository estimateItemRepository;
    private final PasswordValidator passwordValidator;


    public AdminController(PriceListRepository priceListRepository, PriceListItemRepository priceListItemRepository, UserRepository userRepository, EstimateRepository estimateRepository, EstimateItemRepository estimateItemRepository, PasswordValidator passwordValidator) {
        this.priceListRepository = priceListRepository;
        this.priceListItemRepository = priceListItemRepository;
        this.userRepository = userRepository;
        this.estimateRepository = estimateRepository;
        this.estimateItemRepository = estimateItemRepository;
        this.passwordValidator = passwordValidator;
    }

    @GetMapping("")
    public String dashboard(Model model,
                            HttpSession httpSession)
    {
        model.addAttribute("userCount", userRepository.count());
        model.addAttribute("priceListCount", priceListRepository.count());
        return "admin-dashboard";
    }

    @GetMapping("/uploadfile")
    public String uploadFile() {
        return "admin-file-upload-form";
    }

    @RequestMapping(value = "/uploadfilecolumnchooser", method = RequestMethod.POST)
    public String submit(
            @RequestParam("file") MultipartFile file,
            @RequestParam(required = false) String firstRowIsColumnsNames,
            Model model,
            HttpSession httpSession

    ) {
        if (file.getContentType().equals("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet")) {
            Map<Integer, String> columnsAssignmentMap = new HashMap<>();
            columnsAssignmentMap = Excel.getColumnsNames(file, firstRowIsColumnsNames);
            String fileName = Excel.getFileName(file);
            Map<Integer, List<String>> fileData = Excel.getExcelData(file, firstRowIsColumnsNames);
            model.addAttribute("columnsFromFile", columnsAssignmentMap);
            System.out.println("!!!"+columnsAssignmentMap);
            httpSession.setAttribute("data", fileData);
            model.addAttribute("firstRowIsColumnsNames","yes");
            model.addAttribute("fileName",fileName);
            return "admin-upload-file-select-columns";

        }
        return "file-upload-view";
    }

    @RequestMapping(value = "/uploadfile", method = RequestMethod.POST)
    public String submit(
            Model model,
            @RequestParam String preset,
            @RequestParam String referenceNumber,
            @RequestParam String description,
            @RequestParam String brand,
            @RequestParam String comment,
            @RequestParam String unitNetPrice,
            @RequestParam String unit,
            @RequestParam String baseVatRate,
            @RequestParam(required = false) String firstRowIsColumnsNames,
            @RequestParam String fileName,
            HttpSession httpSession

    ) {
        ColumnAssigment columnAssigment = new ColumnAssigment();

        if(!preset.equals("-"))
        {
            if(preset.equals("legrand"))
            {
                columnAssigment.referenceNumberColumnNumber = 6;
                columnAssigment.descriptionColumnNumber = 7;
                columnAssigment.brandColumnNumber = 5;
                columnAssigment.commentColumnNumber = 4;
                columnAssigment.unitNetPriceColumnNumber = 9;
                columnAssigment.unitColumnNumber = 10;
                columnAssigment.baseVatRateColumnNumber = 14;
            }
        }
        else {
            columnAssigment.referenceNumberColumnNumber = Integer.valueOf(referenceNumber);
            columnAssigment.descriptionColumnNumber = Integer.valueOf(description);
            columnAssigment.brandColumnNumber = Integer.valueOf(brand);
            columnAssigment.commentColumnNumber = Integer.valueOf(comment);
            columnAssigment.unitNetPriceColumnNumber = Integer.valueOf(unitNetPrice);
            columnAssigment.unitColumnNumber = Integer.valueOf(unit);
            columnAssigment.baseVatRateColumnNumber = Integer.valueOf(baseVatRate);
        }

        final PriceList priceList;
            PriceList existingPriceList;
        priceList = Excel.importFromExcelData((Map<Integer, List<String>>)httpSession.getAttribute("data"), columnAssigment,firstRowIsColumnsNames, fileName);


            if (priceListRepository.findByName(priceList.getName()) != null) {
                existingPriceList = priceListRepository.findByName(priceList.getName());
                logger.info("!!!" + priceList.getPriceListItems());

                existingPriceList.getPriceListItems().stream() //update existing PLIs with elements from loaded PL
                        .forEach(pi ->
                        {
                            if (priceList.getPriceListItems().stream()
                                    .filter(npi -> npi.getReferenceNumber()
                                            .equals(pi.getReferenceNumber()))
                                    .collect(Collectors.toList()) != null
                                    &&
                                    priceList.getPriceListItems().stream()
                                            .filter(npi -> npi.getReferenceNumber()
                                                    .equals(pi.getReferenceNumber()))
                                            .collect(Collectors.toList()).size() > 0
                            ) {
                                if (priceList.getPriceListItems().stream()
                                        .filter(npi -> npi.getReferenceNumber()
                                                .equals(pi.getReferenceNumber()))
                                        .collect(Collectors.toList()).get(0) != null) {
                                    PriceListItem newPLI = priceList.getPriceListItems().stream()
                                            .filter(npi -> npi.getReferenceNumber().equals(pi.getReferenceNumber())).collect(Collectors.toList()).get(0);
                                    pi.setVendorName(newPLI.getVendorName());
                                    pi.setDescription(newPLI.getDescription());
                                    pi.setBrand(newPLI.getBrand());
                                    pi.setComment(newPLI.getComment());
                                    pi.setUnitNetPrice(newPLI.getUnitNetPrice());
                                    pi.setUnit(newPLI.getUnit());
                                    pi.setBaseVatRate(newPLI.getBaseVatRate());
                                    priceListItemRepository.save(pi);
                                }
                            }
                        });
                priceList.getPriceListItems().stream() // add new items from loaded PL to existing PL
                        .forEach(npi ->
                        {
                            if (existingPriceList.getPriceListItems().stream()
                                    .anyMatch(pi -> pi.getReferenceNumber().equals(npi.getReferenceNumber()))) {
                            } else {
                                priceListItemRepository.save(npi);
                                existingPriceList.getPriceListItems().add(npi);
                            }
                        });

                // TODO comment removed items from loaded PL to existing PL

                existingPriceList.countItems();
                priceListRepository.save(existingPriceList);
                model.addAttribute("priceList", existingPriceList);
            } else if(priceList.getPriceListItems()!=null && priceList.getErrorMessage().isBlank()){
                priceList.getPriceListItems().stream().forEach(pi -> priceListItemRepository.save(pi));
                priceListRepository.save(priceList);

                model.addAttribute("priceList", priceList);
            }else{
                model.addAttribute("errorMessage", priceList.getErrorMessage());
                return "admin-error-message";
            }
            return "admin-show-pricelist";
    }

    @GetMapping("/selectpricelist")
    public String selectPriceList(
            Model model
    ) {
        model.addAttribute("availablePriceLists", priceListRepository.findAll());
        return "admin-select-price-list-to-show";
    }

    @PostMapping("/showpricelist")
    public String showPriceList(
            Model model,
            @RequestParam String selectedPriceListId
    ) {
        PriceList priceList = priceListRepository.findByIdWithPriceListItems(Long.parseLong(selectedPriceListId));
        model.addAttribute("priceList", priceList);
        return "admin-show-pricelist";
    }


    @GetMapping("/deletepricelist")
    public String deletePriceList(
            @RequestParam String deletePriseListId
    ) {
        List<PriceListItem> priceListItems = priceListRepository.findByIdWithPriceListItems(Long.parseLong(deletePriseListId)).getPriceListItems();
        priceListRepository.deleteById(Long.parseLong(deletePriseListId));

        //remove from all estimates, where PRI is present
        for (PriceListItem pli : priceListItems) {
            removeEstimateItemByPriceListItemId(pli.getId());
        }
        priceListItemRepository.deleteAll(priceListItems);

        //update all estimates, where PRI is present
        recalculateALlEstimates();

        return "forward:/admin/selectpricelist";
    }

    //Edit pricelist item
    @GetMapping("/edititem")
    public String editPriceListItem(Model model,
                                    @RequestParam String id,
                                    @RequestParam String priceListId
    ) {
        logger.info("!!! " + priceListId);
        model.addAttribute("priceListId", priceListId);
        model.addAttribute("priceListItem", priceListItemRepository.findById(Long.parseLong(id)));
        return "admin-edit-pricelistitem-form";
    }

    @PostMapping("/edititem")
    public String editPriceListItem(
            @Valid @ModelAttribute("priceListItem") PriceListItem priceListItem, BindingResult result,
            @RequestParam String priceListId,
            @RequestParam String button,
            Model model

    ) {

        if (result.hasErrors()) {
            System.out.println(result);
            return "admin-edit-pricelistitem-form";
        }

        logger.info("!!! " + priceListId);
        if(button.equals("save")) {
            priceListItemRepository.save(priceListItem);
        }
        model.addAttribute("priceList", priceListRepository.findByIdWithPriceListItems(Long.parseLong(priceListId)));
        return "admin-show-pricelist";
    }

    //Delete pricelist item
    @GetMapping("/deleteitem")
    public String deleteUserItem(
            Model model,
            @RequestParam String id,
            @RequestParam String priceListId

    ) {
        try {
            PriceList currentPriceList = priceListRepository.findByIdWithPriceListItems(Long.parseLong(priceListId));
            currentPriceList.getPriceListItems().removeIf(i -> i.getId() == Long.parseLong(id));
            currentPriceList.countItems();
            priceListRepository.save(currentPriceList);

            //remove from all estimates, where PRI is present
            removeEstimateItemByPriceListItemId(Long.parseLong(id));

            priceListItemRepository.delete(priceListItemRepository.findById(Long.parseLong(id)).get());

            //update all estimates, where PRI is present
            recalculateALlEstimates();

            if (currentPriceList.getNumberOfItems().equals(0l)) {
                if (!currentPriceList.isUserOwned()) {
                    currentPriceList = priceListRepository.findById(currentPriceList.getId()).get();
                    priceListRepository.delete(currentPriceList);
                    return "forward:/admin/selectpricelist";
                }
            }
            model.addAttribute("priceList", currentPriceList);
            return "admin-show-pricelist";
        } catch (NumberFormatException e) {
            logger.warn(e.getMessage());
            model.addAttribute("priceList", priceListRepository.findByIdWithPriceListItems(Long.parseLong(priceListId)));
            return "user-show-pricelist";
        }

    }

    //edit admin account
    @GetMapping("/edit")
    public String editAdmin(HttpSession httpSession,
                            Model model
    ) {

        model.addAttribute("user", userRepository.findById(((User) httpSession.getAttribute("user")).getId()).get());
        return "admin-edit-account";
    }

    @PostMapping("/edit")
    public String editAdmin(
            @Valid User user,
            BindingResult results,
            Model model,
            @RequestParam String password,
            @RequestParam String password2
    ) {
//        if (!user.getPassword().equals(userRepository.findById(user.getId()).get().getPassword()))
//            if (!passwordValidator.isValid(user.getPassword(), null)) {
//                model.addAttribute("invalidPassword", Messages.INVALID_PASSWORD);
//            }
//
//        if (results.hasErrors()) {
//            return "admin-edit-account";
//        }
//        if (model.getAttribute("invalidPassword") != null) {
//            return "admin-edit-account";
//        }
////        user.setPasswordUnhashed(user.getPassword());
////        user.setPassword(Security.hashPassword(user.getPassword()));
//        if (!user.getPassword().equals(userRepository.findById(user.getId()).get().getPassword())) {
//            user.setPasswordUnhashed(user.getPassword());
//            user.setPassword(Security.hashPassword(user.getPassword()));
//        }
//        userRepository.save(user);
//        model.addAttribute("userCount", userRepository.count());
//        return "admin-dashboard";




        boolean updatePassword = false;

        if (!passwordValidator.isValid(user.getPassword(), null)) {
            model.addAttribute("invalidPassword", Messages.INVALID_PASSWORD);
        }

        if (results.hasErrors()) {
            return "admin-edit-account";
        }

        if (!password.isEmpty() || !password2.isEmpty())
        {
            if (!password.equals(password2))
            {
                model.addAttribute("invalidPassword", Messages.PASSWORD_ARE_NOT_EQUAL);
            } else {
                updatePassword = true;
            }
            if (model.getAttribute("invalidPassword") != null) {
                return "admin-edit-account";
            }
        }
        if(!updatePassword) {
            user.setPasswordUnhashed(userRepository.findById(user.getId()).get().getPasswordUnhashed());
            user.setPassword(userRepository.findById(user.getId()).get().getPassword());
        }
        else {
            user.setPasswordUnhashed(password);
            user.setPassword(Security.hashPassword(password));
        }

        userRepository.save(user);
        model.addAttribute("userCount", userRepository.count());
        return "admin-dashboard";



    }


    @ModelAttribute("priceListItems")
    public List<PriceListItem> findAllPriceListItems() {
        return priceListItemRepository.findAll();
    }


    public void removeEstimateItemByPriceListItemId(Long id) {
        //remove from all estimates, where PRI is present
        if (estimateItemRepository.findAllByPriceListItemId(id) != null) //get unempty list
        {

            List<Long> estimateItemIds = estimateItemRepository.findAllByPriceListItemId(id)
                    .stream()
                    .map(ei -> ei.getId())
                    .collect(Collectors.toList());

            estimateItemIds.stream()
                    .forEach(eiId -> estimateItemRepository.deleteFromParentRelationTableById(eiId)); //remove from parent table

            if (estimateItemRepository.findAllById(estimateItemIds).size() > 0) {
                estimateItemIds.stream()
                        .forEach(eiId -> estimateItemRepository.deleteById(eiId)); //remove from table
            }
        }
    }

    @GetMapping("/list")
    public String list() {
        return "admin-show-pricelist";
    }

    private void recalculateALlEstimates() {
        List<Estimate> allEstimates = estimateRepository.findAll();
        for (Estimate ue : allEstimates) {
            ue.calculateAmounts();
            estimateRepository.save(ue);
        }
    }




}
