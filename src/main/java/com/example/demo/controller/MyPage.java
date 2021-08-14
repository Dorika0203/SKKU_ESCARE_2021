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

import javax.servlet.http.HttpSession;

@Controller
@RequestMapping("mypage")
public class MyPage {

    @Autowired
    private SignInDataRepository signInDataRepository;
    @Autowired
    private SignOutDataRepository signOutDataRepository;
    @Autowired
    private AccountDataRepository accountDataRepository;
    @Autowired
    private BankStatementDataRepository bankStatementDataRepository;

    @GetMapping
    public String mypage(Model model, HttpSession session) {

        Time time = new Time(signInDataRepository, signOutDataRepository);

        //check if user is login
        String userID = (String) session.getAttribute("userID");
        if (userID != null) {
            if (time.isClientLoginTimeLessThan5Minute(userID)) {
                JSONArray transactionLog = new JSONArray();
                List<AccountDataModel> AccountList = accountDataRepository.findAllByUserId(userID);

                for (int i = 0; i < AccountList.size(); i++) {

                    AccountDataModel thisAcc = AccountList.get(i);
                    JSONObject accountJSON = new JSONObject();
                    JSONArray transferLogJSON = new JSONArray();

                    accountJSON.put("accountID", thisAcc.getAccount());
                    accountJSON.put("balance", thisAcc.getBalance());

                    // get maximum 10 transfer log.
                    List<BankStatementDataModel> accountTransferInfo = bankStatementDataRepository.findAllByAccount(thisAcc.getAccount());
                    accountTransferInfo = Stream.concat(accountTransferInfo.stream(), bankStatementDataRepository.findAllByDepositAccount(thisAcc.getAccount()).stream()).collect(Collectors.toList());

                    for (int j = 0; j < accountTransferInfo.size(); j++) {
                        BankStatementDataModel thisTransfer = accountTransferInfo.get(j);
                        if (j == 10) break;
                        JSONObject addingOneTransfer = new JSONObject();
                        if (thisTransfer.getAccount() == thisAcc.getAccount()) {
                            addingOneTransfer.put("sendTo", thisTransfer.getDepositAccount());
                            addingOneTransfer.put("gold", thisTransfer.getTransactionAmount());
                        } else {
                            addingOneTransfer.put("sendTo", thisTransfer.getAccount());
                            addingOneTransfer.put("gold", thisTransfer.getTransactionAmount() * -1);
                        }
                        addingOneTransfer.put("time", thisTransfer.getTransactionTime());
                        addingOneTransfer.put("result", thisTransfer.getAfterBalance());
                        transferLogJSON.put(addingOneTransfer);
                    }
                    accountJSON.put("transferLog", transferLogJSON);
                    transactionLog.put(accountJSON);
                }
                model.addAttribute("myAccountsData", transactionLog.toString());
                return "my_page";
            } else
                return "my_page_fail";

        } else
            return "my_page_fail";

    }


}