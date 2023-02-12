package com.nttdata.infraestructure.entity;

import io.quarkus.hibernate.orm.panache.PanacheEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "Account")
public class Account extends PanacheEntity {
  private int idTypeAccount;
  private String numberAccount;
  private double amount;
  private String registrationDate;
  private int idTypeCustomer;
  private int idCustomer;
  private String created_datetime;
  private String updated_datetime;
  private String active;
}
