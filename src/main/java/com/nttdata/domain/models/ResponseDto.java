package com.nttdata.domain.models;

import com.nttdata.infraestructure.entity.Account;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ResponseDto {
  private String status;
  private String message;
  private List<Account> account;
}
