package com.example.demo.model;

import org.hibernate.mapping.Array;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name="Account")
public class AccountNumModel {
    @Id
    private String id;
    @Column
    private List<String> testList = new ArrayList<String>();
}
