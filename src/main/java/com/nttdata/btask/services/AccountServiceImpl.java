package com.nttdata.btask.services;

import com.nttdata.btask.interfaces.AccountService;
import com.nttdata.domain.contract.AccountRepository;
import com.nttdata.domain.models.AccountDto;
import com.nttdata.infraestructure.entity.Account;
import jakarta.enterprise.context.ApplicationScoped;

import java.util.List;
@ApplicationScoped
public class AccountServiceImpl implements AccountService {
  private final AccountRepository accountRepository;

  public AccountServiceImpl(AccountRepository accountRepository) {
    this.accountRepository = accountRepository;
  }

  @Override
  public List<Account> getAllAccount() {
    return accountRepository.getAllAccount();
  }

  @Override
  public Account getByIdAccount(Long id) {
    return accountRepository.getByIdAccount(id);
  }

  @Override
  public Account addAccount(AccountDto accountDto) {
    return accountRepository.addAccount(accountDto);
  }

  @Override
  public List<Account> updateAccountById(Long id, AccountDto accountDto) {
    return accountRepository.updateAccountById(id, accountDto);
  }

  @Override
  public List<Account> deleteAccountById(Long id) {
    return accountRepository.deleteAccountById(id);
  }

  @Override
  public Account updateAccountToDeposit(AccountDto accountDto) {
    return accountRepository.updateAccountToDeposit(accountDto);
  }

  @Override
  public Account updateAccountToWithdrawal(AccountDto accountDto) {
    return accountRepository.updateAccountToWithdrawal(accountDto);
  }
}
