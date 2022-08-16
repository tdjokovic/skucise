import { HttpErrorResponse, HttpResponse, HttpStatusCode } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Router } from '@angular/router';
import { LoginApiService } from '../back/apis/login-api.service';
import { Credentials } from '../back/types/interfaces';
import { JWT_HEADER_NAME } from '../back/types/vars';
import { RedirectRoutes } from '../constants/routing.properties';
import { PasswdHash } from '../helpers/hashing_helper';
import { JWTUtil } from '../helpers/jwt_helper';
import { AuthorizeService } from './authorize.service';

@Injectable({
  providedIn: 'root'
})
export class LoginService {

  constructor(private api : LoginApiService,
              private router : Router,
              private authorizationService : AuthorizeService) { }

  login(email:string, password:string, self?:any, callbackSuccess?:Function, callbackWrongCredentials?:Function, callbackNotApproved?:Function){

    console.log("Kredencijali koji su uneseni:\n "+password);

    let body : Credentials = {
      email : email,
      hashedPassword: PasswdHash.encrypt(password)
    }

    console.log("Kredencijali nakon hesiranja:\n "+body);

    
    this.api.login(body).subscribe(
      //ako je login uspesan
      
      (response: HttpResponse<null>) => {
        //alert("odgovor od apija za login uspesan");
        JWTUtil.store(response.headers.get(JWT_HEADER_NAME));
        if(self && callbackSuccess) callbackSuccess(self);
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
}
