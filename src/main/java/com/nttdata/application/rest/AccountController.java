package com.nttdata.application.rest;

import com.nttdata.btask.interfaces.AccountService;
import com.nttdata.domain.models.AccountDto;
import com.nttdata.domain.models.ResponseDto;
import com.nttdata.infraestructure.entity.Account;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.Response;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Path("/account")
public class AccountController {
  private final AccountService accountService;

  public AccountController(AccountService accountService) {
    this.accountService = accountService;
  }
  @GET
  public Response getAllCustomer() {
    ResponseDto responseDto = new ResponseDto();
    List<Account> collectCustomer = accountService.getAllAccount();
    responseDto.setStatus("204");
    responseDto.setMessage("customer not found");
    if(collectCustomer.size() == 0){
      return Response.ok(responseDto).status(204).build();
    }else{
      List<Account> list = collectCustomer.stream()
          .filter(customer -> customer.getActive().equals("S"))
          .peek(c->{
            responseDto.setStatus("200");
            responseDto.setMessage("Se proceso Correctamente");
          })
          .collect(Collectors.toList());

      responseDto.setAccount(list);
      return Response.ok(responseDto).build();

    }
  }
  @GET
  @Path("{id}")
  public Response getByIdAccount(@PathParam("id") Long id) {

    ResponseDto responseDto = new ResponseDto();
    Account account = this.accountService.getByIdAccount(id);
    responseDto.setStatus("204");
    responseDto.setMessage("customer not found");
    if(account == null){
      return Response.ok(responseDto).status(204).build();
    }else{

      List<Account> collect = new ArrayList<>();
      collect.add(account);
      collect = collect.stream().filter(c -> c.getActive().equals("S"))
          .peek(c->{
            responseDto.setStatus("200");
            responseDto.setMessage("Se proceso Correctamente");
          })
          .collect(Collectors.toList());
      responseDto.setAccount(collect);
      return Response.ok(responseDto).build();

    }

  }
  @POST
  public Response addAccount(AccountDto accountDto) {
    if(accountDto == null){
      return Response.status(415).build();
    }else{
      return Response.ok(this.accountService.addAccount(accountDto)).status(201).build();
    }
  }

  @PUT
  @Path("{id}")
  public Response updateAccount(@PathParam("id") Long id, AccountDto accountDto) {
    ResponseDto responseDto = new ResponseDto();
    List<Account> account = this.accountService.updateAccountById(id, accountDto);
    if(account.size() == 0){
      responseDto.setStatus("204");
      responseDto.setMessage("customer not found");
      return Response.ok(responseDto).status(204).build();

    }else{
      responseDto.setStatus("200");
      responseDto.setMessage("Se proceso Correctamente");
      responseDto.setAccount(account);
      return Response.ok(responseDto).build();
    }

  }
  @DELETE
  @Path("{id}")
  public Response deleteAccount(@PathParam("id") Long id) {
    ResponseDto responseDto = new ResponseDto();
    List<Account> customer = this.accountService.deleteAccountById(id);

    if(customer.size() == 0){
      responseDto.setStatus("204");
      responseDto.setMessage("customer not found");
      return Response.ok(responseDto).status(204).build();

    }else{
      responseDto.setStatus("200");
      responseDto.setMessage("Se proceso Correctamente");
      responseDto.setAccount(customer);
      return Response.ok(responseDto).build();
    }

  }
  @POST
  @Path("/deposit")
  public Response updateAccountAmount(AccountDto accountDto) {
    ResponseDto responseDto = new ResponseDto();
    List<Account> list = new ArrayList<>();


     Account _account=this.accountService.updateAccountToDeposit(accountDto);

    return Response.ok(_account).status(200).build();
  }

  @POST
  @Path("/withdrawal")
  public Response updateAccountAmountWithdrawal(AccountDto accountDto) {
    return Response.ok(accountService.updateAccountToWithdrawal(accountDto)).status(200).build();
  }
}
