package com.example.demo.repository;

import com.example.demo.model.BankStatementDataModel;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BankStatementDataRepository extends JpaRepository<BankStatementDataModel, String> {
    List<BankStatementDataModel> findAllByAccount (String account);
    List<BankStatementDataModel> findAllByDepositAccount (String depositAccount);
}
