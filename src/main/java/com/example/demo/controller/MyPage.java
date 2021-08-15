package com.example.demo.controller;

import com.example.demo.model.AccountDataModel;
import com.example.demo.model.BankStatementDataModel;
import com.example.demo.repository.AccountDataRepository;
import com.example.demo.repository.BankStatementDataRepository;
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
import static com.example.demo.date.Time.getTime;
import static com.example.demo.date.Time.getCurrentTime;

import static com.example.demo.bank.LoginClient.*;

@Controller
@RequestMapping("mypage")
public class MyPage {

    @Autowired
    private AccountDataRepository accountDataRepository;
    @Autowired
    private BankStatementDataRepository bankStatementDataRepository;

    @GetMapping
    public String mypage(Model model, HttpSession session) {

        //check if user is login
        String userID = getSessionUserID(session);

        if (userID != null) {

            System.out.println(" --------------------------------- MyPage -------------------------");
            System.out.println("current Time: " + getCurrentTime());
            System.out.println("Last Accessed Time: " + getTime(session.getLastAccessedTime()));
            System.out.println("session created Time: " + getTime(session.getCreationTime()));
            System.out.println("session ID: " + session.getId());



            // create multiple accounts Info in JSON ARRAY.
            // Each account info is JSON OBJECT.
            // Each account info has JSON ARRAY child data ----> indicating multiple transfer log for that single Account.

            // 여러개 계좌의 정보를 JSON ARRAY로 정리한다.
            // 각 계좌의 정보는 JSON OBJECT 이다.
            // 각 계좌의 정보(JSON OBJECT)는 해당 계좌의 입출금 내역을 저장하는 자식(JSON ARRAY)을 가진다.

            JSONArray multipleAccountInfo = new JSONArray();
            List<AccountDataModel> accountList = accountDataRepository.findAllByUserId(userID);

            for (int i = 0; i < accountList.size(); i++)
            {
                AccountDataModel ith_Account = accountList.get(i);
                JSONObject singleAccountInfo = new JSONObject();
                JSONArray multipleTransferLog_For_SingleAccountInfo = new JSONArray();

                singleAccountInfo.put("accountID", ith_Account.getAccount());
                singleAccountInfo.put("balance", ith_Account.getBalance());

                List<BankStatementDataModel> transferList = bankStatementDataRepository.findAllByAccount(ith_Account.getAccount()); // i번째 계좌의 출금한 내역
                transferList = Stream.concat(transferList.stream(), bankStatementDataRepository.findAllByDepositAccount(ith_Account.getAccount()).stream()).collect(Collectors.toList()); // i번째 계좌의 입금된 내역


                // 각 계좌의 송금 내역은 최대 10개까지만 가진다.
                for (int j = 0; j < transferList.size(); j++)
                {
                    BankStatementDataModel jth_Transfer_For_ith_Account = transferList.get(j);
                    if (j == 10) break;
                    JSONObject singleTransferInfo = new JSONObject();

                    // 출금, 입금 구분
                    if (jth_Transfer_For_ith_Account.getAccount() == ith_Account.getAccount())
                    {
                        singleTransferInfo.put("sendTo", jth_Transfer_For_ith_Account.getDepositAccount());
                        singleTransferInfo.put("gold", jth_Transfer_For_ith_Account.getTransactionAmount());
                    }
                    else
                    {
                        singleTransferInfo.put("sendTo", jth_Transfer_For_ith_Account.getAccount());
                        singleTransferInfo.put("gold", jth_Transfer_For_ith_Account.getTransactionAmount() * -1);
                    }
                    
                    singleTransferInfo.put("time", jth_Transfer_For_ith_Account.getTransactionTime());
                    singleTransferInfo.put("result", jth_Transfer_For_ith_Account.getAfterBalance());
                    multipleTransferLog_For_SingleAccountInfo.put(singleTransferInfo);
                }
                singleAccountInfo.put("transferLog", multipleTransferLog_For_SingleAccountInfo);
                multipleAccountInfo.put(singleAccountInfo);
            }

            model.addAttribute("myAccountsData", multipleAccountInfo.toString());
            return "my_page";
        } else
            return "fail";

    }


}