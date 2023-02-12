package com.nttdata.domain.contract;

import com.nttdata.domain.models.AccountDto;
import com.nttdata.infraestructure.entity.Account;

import java.util.List;

public interface AccountRepository {
  List<Account> getAllAccount();
  Account getByIdAccount(Long id);
  Account addAccount(AccountDto accountDto);

  List<Account> updateAccountById(Long id,AccountDto accountDto);
  List<Account> deleteAccountById(Long id);

  Account updateAccountToDeposit(AccountDto accountDto);

  Account updateAccountToWithdrawal(AccountDto accountDto);
}
