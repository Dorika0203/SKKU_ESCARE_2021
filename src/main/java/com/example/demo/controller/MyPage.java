package com.example.demo.controller;

import com.example.demo.date.Time;
import com.example.demo.model.AccountDataModel;
import com.example.demo.model.BankStatementDataModel;
import com.example.demo.repository.AccountDataRepository;
import com.example.demo.repository.BankStatementDataRepository;
import com.example.demo.repository.SignInDataRepository;
import com.example.demo.repository.SignOutDataRepository;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static com.example.demo.bank.LoginClient.getUserID;

@Controller
@RequestMapping("mypage")
public class MyPage {
    private String server = "https://sdkms.fortanix.com";
    private String username = "a025eafd-5977-4924-8087-9b262315a974";
    private String password = "vxYLi9s8_GXmNIBLBeUgV8caHqSyUZtTqvR2qoMFU3PVPlg64_vPIDkI0mpScqDH_p3g2Q5P0SdhIEr0TpEghQ";

    @Autowired
    private SignInDataRepository signInDataRepository;
    @Autowired
    private SignOutDataRepository signOutDataRepository;
    @Autowired
    private AccountDataRepository accountDataRepository;
    @Autowired
    private BankStatementDataRepository bankStatementDataRepository;

    @GetMapping
    public String mypage(Model model) {

        Time time = new Time(signInDataRepository,signOutDataRepository);

        //check if user is login
        String userID = getUserID();
        if (userID.isEmpty()) {
            return "my_page_fail";
        }


        // session checked. show my_page
        if (time.isClientLoginTimeLessThan5Minute(userID)) {
            JSONArray myAccountsData = new JSONArray();
            List<AccountDataModel> myAccounts = accountDataRepository.findAllByUserId(userID);

            for (int i = 0; i < myAccounts.size(); i++) {

                AccountDataModel thisAcc = myAccounts.get(i);
                JSONObject addingAccount = new JSONObject();
                JSONArray addingTransfer = new JSONArray();

                addingAccount.put("accountID", thisAcc.getAccount());
                addingAccount.put("balance", thisAcc.getBalance());

                // get maximum 10 transfer log.
                List<BankStatementDataModel> accountTransferInfo = bankStatementDataRepository.findAllByAccount(thisAcc.getAccount());
                accountTransferInfo = Stream.concat(accountTransferInfo.stream(), bankStatementDataRepository.findAllByDepositAccount(thisAcc.getAccount()).stream()).collect(Collectors.toList());

                for (int j = 0; j < accountTransferInfo.size(); j++) {
                    BankStatementDataModel thisTransfer = accountTransferInfo.get(j);
                    if (j == 10) break;
                    JSONObject addingOneTransfer = new JSONObject();
                    if(thisTransfer.getAccount() == thisAcc.getAccount())
                    {
                        addingOneTransfer.put("sendTo", thisTransfer.getDepositAccount());
                        addingOneTransfer.put("gold", thisTransfer.getTransactionAmount());
                    }
                    else
                    {
                        addingOneTransfer.put("sendTo", thisTransfer.getAccount());
                        addingOneTransfer.put("gold", thisTransfer.getTransactionAmount() * -1);
                    }
                    addingOneTransfer.put("time", thisTransfer.getTransactionTime());
                    addingOneTransfer.put("result", thisTransfer.getAfterBalance());
                    addingTransfer.put(addingOneTransfer);
                }
                addingAccount.put("transferLog", addingTransfer);
                myAccountsData.put(addingAccount);
            }
            model.addAttribute("myAccountsData", myAccountsData.toString());
            return "my_page";
        } else
            return "my_page_fail";
    }



}