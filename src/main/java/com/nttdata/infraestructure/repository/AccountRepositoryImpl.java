package com.nttdata.infraestructure.repository;

import com.nttdata.domain.contract.AccountRepository;
import com.nttdata.domain.models.AccountDto;
import com.nttdata.infraestructure.entity.Account;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@ApplicationScoped
public class AccountRepositoryImpl implements AccountRepository {

  @Override
  public List<Account> getAllAccount() {
    return Account.listAll();
  }

  @Override
  public Account getByIdAccount(Long id) {
    return Account.findById(id);
  }

  @Override
  @Transactional
  public Account addAccount(AccountDto accountDto) {
    Account account = new Account();
    account.setIdTypeAccount(accountDto.getIdTypeAccount());
    account.setNumberAccount(accountDto.getNumberAccount());
    account.setAmount(accountDto.getAmount());
    account.setIdCustomer(accountDto.getIdCustomer());
    account.setIdTypeCustomer(accountDto.getIdTypeCustomer());
    account.setRegistrationDate(this.getDateNow());
    account.setCreated_datetime(this.getDateNow());
    account.setUpdated_datetime(this.getDateNow());
    account.setActive("S");
    account.persist();
    return account;
  }

  @Override
  @Transactional
  public List<Account> updateAccountById(Long id, AccountDto accountDto) {
    List<Account> collect = new ArrayList<>();
    Account customerOp = Account.findById(id);

    if(customerOp == null){
      return collect;
    }else{
      customerOp.setIdTypeAccount(accountDto.getIdTypeAccount());
      customerOp.setNumberAccount(accountDto.getNumberAccount());
      customerOp.setAmount(accountDto.getAmount());
      customerOp.setIdCustomer(accountDto.getIdCustomer());
      customerOp.setIdTypeCustomer(accountDto.getIdTypeCustomer());
      customerOp.setUpdated_datetime(this.getDateNow());
      customerOp.persist();
      collect.add(customerOp);
    }
    return collect;
  }

  @Override
  @Transactional
  public List<Account> deleteAccountById(Long id) {
    List<Account> collect = new ArrayList<>();
    Account accountOp = Account.findById(id);

    if(accountOp == null){
      return collect;
    }else{
      Account customer = new Account();
      accountOp.setUpdated_datetime(this.getDateNow());
      accountOp.setActive("N");
      accountOp.persist();
      collect.add(accountOp);
    }
    return collect;
  }


  @Override
  @Transactional
  public Account updateAccountToWithdrawal(AccountDto accountDto) {

    List<Account> customerOp = Account.listAll();
    List<Account> customer = customerOp.stream()
        .peek(c->System.out.println(c.getNumberAccount() + "==" + accountDto.getNumberAccount()))
        .filter(cc-> cc.getNumberAccount().equals(accountDto.getNumberAccount()))
        .filter(cc-> cc.getAmount() >= accountDto.getAmount())
        .limit(1)
        .map(b->{
          b.setAmount(b.getAmount() - accountDto.getAmount());
          b.setRegistrationDate(this.getDateNow());
          b.setUpdated_datetime(this.getDateNow());
          b.persist();
          return b;
        })
        .collect(Collectors.toList());
    if(customer.size() == 0){
      return customerOp.get(0);
    }else{
      return customer.get(0);
    }

    /*if(customerOp.size() == 0){
      return customerOp.get(0);
    }else{
        double _amount = customerOp.get(0).getAmount() - accountDto.getAmount();

        if(_amount >= 0){
          customerOp.get(0).setAmount(_amount);
          customerOp.get(0).setRegistrationDate(this.getDateNow());
          customerOp.get(0).setUpdated_datetime(this.getDateNow());
          customerOp.get(0).persist();
        }




      return customerOp.get(0);

    }*/
  }

  @Override
  @Transactional
  public Account updateAccountToDeposit(AccountDto accountDto) {

    List<Account> customerOp = Account.listAll();
    List<Account> customer = customerOp.stream()
        .peek(c->System.out.println(c.getNumberAccount() + "==" + accountDto.getNumberAccount()))
        .peek(c->System.out.println(c.getAmount() + "==" + accountDto.getAmount()))
        .filter(cc-> cc.getNumberAccount().equals(accountDto.getNumberAccount()))
        .limit(1)
        .map(b->{
          b.setAmount(b.getAmount() + accountDto.getAmount());
          b.setRegistrationDate(this.getDateNow());
          b.setUpdated_datetime(this.getDateNow());
          b.persist();
          return b;
        })
        .collect(Collectors.toList());
    if(customer.size() == 0){
      customerOp.get(0).setRegistrationDate("R");
      return customerOp.get(0);
    }else{
      customerOp.get(0).setRegistrationDate("X");
      return customer.get(0);
    }
  }
  private static String getDateNow(){
    Date date = new Date();
    SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    return formatter.format(date).toString();
  }
}
