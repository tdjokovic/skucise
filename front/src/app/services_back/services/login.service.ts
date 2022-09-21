import { HttpErrorResponse, HttpResponse, HttpStatusCode } from '@angular/common/http';
import { Injectable, Output } from '@angular/core';
import { Router } from '@angular/router';
import { Subject } from 'rxjs';
import { LoginApiService } from '../back/apis/login-api.service';
import { Buyer, Credentials, Seller } from '../back/types/interfaces';
import { JWT_HEADER_NAME } from '../back/types/vars';
import { RedirectRoutes } from '../constants/routing.properties';
import { PasswdHash } from '../helpers/hashing_helper';
import { JWTUtil } from '../helpers/jwt_helper';
import { AuthorizeService } from './authorize.service';
import { BuyerService } from './buyer.service';
import { SellerService } from './seller.service';

@Injectable({
  providedIn: 'root'
})
export class LoginService {

  _newLogin = new Subject<boolean>();

  newLogin(bo:boolean) {
    this._newLogin.next(bo);
  }

  constructor(private api : LoginApiService,
              private router : Router,
              private authorizationService : AuthorizeService,
              private buyerService : BuyerService,
              private sellerService : SellerService) { }

  login(email:string, password:string, self?:any, callbackSuccess?:Function, callbackWrongCredentials?:Function, callbackNotApproved?:Function){

    //console.log("Kredencijali koji su uneseni:\n "+password);

    let body : Credentials = {
      email : email,
      hashedPassword: PasswdHash.encrypt(password)
    }

    //console.log("Kredencijali nakon hesiranja:\n "+body);

    
    this.api.login(body).subscribe(
      //ako je login uspesan
      
      (response: HttpResponse<null>) => {
        //alert("odgovor od apija za login uspesan");
        JWTUtil.store(response.headers.get(JWT_HEADER_NAME));

        if (this.authorizationService.isBuyer())
        {
          //alert("buyer")
          this.buyerService.getBuyer(JWTUtil.getID(),this,this.cbSuccess,this.cbNotFound);
        }
        else if (this.authorizationService.isSeller())
        {
          //alert("seller");
          this.sellerService.getSeller(JWTUtil.getID(),this,this.cbSuccess,this.cbNotFound);
        }
        else if (this.authorizationService.isAdmin()){
          window.localStorage.setItem('first-name', 'Admin');
          window.localStorage.setItem('last-name', body.email);
          this.newLogin(true);
        }
        
        if(self && callbackSuccess) 
          callbackSuccess(self);
      },

      //ako je doslo do neke greske
      (error: HttpErrorResponse) => {
        //alert("odgovor od apija za login pogresan");
        this.authorizationService.redirectIfSessionExpired(error.status);

        switch(error.status){
          //unauthorized
          case HttpStatusCode.Unauthorized:
            if(JWTUtil.get() == ''){
              if(self && callbackWrongCredentials) callbackWrongCredentials(self);
            }
            JWTUtil.delete();
            break;
          
          //forbidden
          case HttpStatusCode.Forbidden:
            if(JWTUtil.exists()){
              this.router.navigate(RedirectRoutes.ON_FORBIDDEN);
            }else{
              //not approved
              if(self && callbackNotApproved) callbackNotApproved(self);
            }
            break;
          
          //bad req
          case HttpStatusCode.BadRequest:
            JWTUtil.delete();
            break;
        }
      }
    );
  }

  cbSuccess(self: any, user: Buyer | Seller) {
    //alert("success");
    window.localStorage.setItem('first-name', (user == null)? '' : user.firstName);
    window.localStorage.setItem('last-name', (user == null)? '' : user.lastName);
    //alert("Storovali smo sve");
    self.newLogin(true);
  }
  cbNotFound(self: any) {
    self.router.navigate(RedirectRoutes.HOME);
  }
}
