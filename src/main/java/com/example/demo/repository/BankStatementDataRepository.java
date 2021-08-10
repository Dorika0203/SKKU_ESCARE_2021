package com.example.demo.repository;

import com.example.demo.model.BankStatementDataModel;
import com.example.demo.model.SignInDataModel;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BankStatementDataRepository extends JpaRepository<BankStatementDataModel, String> {
    List<BankStatementDataModel> findAllByAccount (long account);
    List<BankStatementDataModel> findAllByDepositAccount (long depositAccount);
}
