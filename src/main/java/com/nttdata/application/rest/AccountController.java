package com.nttdata.application.rest;

import com.nttdata.btask.interfaces.AccountService;
import com.nttdata.domain.models.*;
import com.nttdata.infraestructure.entity.Account;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.Response;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * clase que tiene los endpoints de Account con las funcionalidades :
 *  - listado de todos los registros activos de Account
 *  - busqueda por id de un Account
 *  - creacion de un Account
 *  - actualizado la data de Account
 *  - borrar logico solo quitarle la actividad al Account para que no se muestre ...
 *  - deposito a la Account
 *  - retiro de la Account
 *  - tranferir de cuentas propias
 */

@Path("/account")
public class AccountController {
  private final AccountService accountService;

  public AccountController(AccountService accountService) {
    this.accountService = accountService;
  }

  /**
   * Metodo que devuelve una lista de Cuenta que estean activa
   * @return Lista de Cuenta
   */
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

  /**
   * Metodo que devuelve una cuenta por id de cuentas activas
   * @return Cuenta por numero de cuenta
   */
  @GET
  @Path("{id}")
  public Response getByIdAccount(@PathParam("id") Long id) {

    ResponseDto responseDto = new ResponseDto();
    Account account = this.accountService.getByIdAccount(id);
    responseDto.setStatus("204");
    responseDto.setMessage("account not found");
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

  /**
   * Metodo que agregar una nueva cuenta
   * @return devuelve la cuenta registrada
   */
  @POST
  public Response addAccount(AccountDto accountDto) {
    if(accountDto == null){
      return Response.status(415).build();
    }else{
      return Response.ok(this.accountService.addAccount(accountDto)).status(201).build();
    }
  }

  /**
   * Metodo que actualiza los datos de una cuenta
   * @param id de cuenta
   * @return devuelve la cuenta modificada
   */
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

  /**
   * Metodo que da de baja una cuenta
   * @param id de cuenta
   * @return devuelve la cuenta dado de baja
   */
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

  /**
   * Metodo que actualiza monto de cuenta
   * @param AccountDto
   * @return devuelve lista con actualizacion de monto
   */
  @POST
  @Path("/deposit")
  public Response updateAccountAmount(AccountDto accountDto) {
    ResponseDto responseDto = new ResponseDto();
    List<Account> list = new ArrayList<>();

     Account _account=this.accountService.updateAccountToDeposit(accountDto);
     list.add(_account);
    responseDto.setStatus("200");
    responseDto.setMessage("Se Proceso Correctamente");
    responseDto.setAccount(list);

    return Response.ok(responseDto).status(200).build();
  }

  /**
   * Metodo que resta monto de cuenta
   * @param AccountDto
   * @return devuelve lista con actualizacion de monto
   */
  @POST
  @Path("/withdrawal")
  public Response updateAccountAmountWithdrawal(AccountDto accountDto) {
    ResponseDto responseDto = new ResponseDto();
    List<Account> list = new ArrayList<>();

    Account _account = accountService.updateAccountToWithdrawal(accountDto);
    list.add(_account);
    responseDto.setStatus("200");
    responseDto.setMessage("Se Proceso Correctamente ...");
    responseDto.setAccount(list);
    return Response.ok(responseDto).status(200).build();
  }

  /**
   * Metodo que realiza una transferencia entre cuentas propias
   * @param TransferDto
   * @return devuelve lista de datos de objecto de trasferencia
   */
  @POST
  @Path("/transfer")
  public Response transferBetweenAccountOwn(TransferDto transferDto) {
    ResponseTransferDto responseDto = new ResponseTransferDto();
    List<TransferDto> list = new ArrayList<>();

    if(transferDto.getNumberAccountOrigin().equals("") || transferDto.getNumberAccountOrigin() == null){
      responseDto.setStatus("204");
      responseDto.setMessage("Number de Cuenta Origen esta vacio o no existe ...");
      responseDto.setTransferDto(new ArrayList<>());
      return Response.ok(responseDto).status(204).build();
    }

    if(transferDto.getNumberAccountDestination().equals("") || transferDto.getNumberAccountDestination() == null){
      responseDto.setStatus("204");
      responseDto.setMessage("Number de Cuenta Destino esta vacio o no existe ...");
      responseDto.setTransferDto(new ArrayList<>());
      return Response.ok(responseDto).status(204).build();
    }

    AccountDto accountDto = new AccountDto();
    accountDto.setNumberAccount(transferDto.getNumberAccountOrigin());
    accountDto.setAmount(transferDto.getAmount());
    Account _accountDeposit=this.accountService.updateAccountToDeposit(accountDto);
    accountDto.setNumberAccount(transferDto.getNumberAccountDestination());

    Account _accountWithdrawal = accountService.updateAccountToWithdrawal(accountDto);


    transferDto.setIdTypeTransfer(1);
    transferDto.setStateTransferAccountOrigin("200");
    transferDto.setStateTransferAccountDestination("200");
    list.add(transferDto);

    responseDto.setStatus("200");
    responseDto.setMessage("Se Proceso Correctamente ...");
    responseDto.setTransferDto(list);
    return Response.ok(responseDto).status(200).build();
  }

  /**
   * Metodo que busca una tarjeta por criterio de busqueda
   * @param cardDto
   * @return devuelve tarjeta buscada
   */
  @POST
  @Path("/search/balance")
  public Response getByIdAccount(CardDto cardDto) {
    System.out.println(cardDto);
    ResponseDto responseDto = new ResponseDto();
    List<Account> account = this.accountService.getAllAccount();
    System.out.println(account);
    List<Account> accountList = account
        .stream()
        .peek(c-> System.out.println(c))
        .filter(card -> card.getNumberAccount().equals(cardDto.getNumberAccountAssociated()))
        .limit(1)
        .collect(Collectors.toList());

      return Response.ok(accountList).build();

    }

}
