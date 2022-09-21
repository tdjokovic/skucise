import { HttpErrorResponse, HttpResponse, HttpStatusCode } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { IdentityApiService } from '../back/apis/identity-api.service';
import { UserRoles } from '../back/types/enums';
import { JWT_HEADER_NAME } from '../back/types/vars';
import { RedirectRoutes } from '../constants/routing.properties';
import { AlertPageUtil } from '../helpers/alert_helper';
import { JWTUtil } from '../helpers/jwt_helper';

@Injectable({
  providedIn: 'root'
})
export class AuthorizeService {

  public redirectRoute:string = '';

  public roleType = {
    Visitor: UserRoles.Visitor,
    Admin: UserRoles.Admin,
    Reg_buyer : UserRoles.Reg_buyer,
    Reg_seller : UserRoles.Reg_seller
  }
  constructor(private api : IdentityApiService,
              private router : Router) { }


  checkRole(role : UserRoles, allowedRoles: UserRoles[]){
    if (allowedRoles.length == 0) return true;
    else{
      if(allowedRoles.includes(role)) return true;
      else return false;
    }
  }

  //autorizacija
  checkAccess(activatedRoute: ActivatedRoute, self?:any, callbackSuccess?: Function, callbackForbidden?:Function, callbackUnauthorized?:Function){
    let allowedRoles: UserRoles[] = activatedRoute.snapshot.data.allowedRoles;
    
    this.api.getCurrent().subscribe(
      (response) => {
        //jwt je validan
        JWTUtil.store(response.headers.get(JWT_HEADER_NAME));
        let role = JWTUtil.getUserRole();
        let allowed = this.checkRole(role, allowedRoles);

        //proveravamo da li je tom korisniku dozvoljen pristup
        if (allowed){
          //jeste
          console.log("Succcess while loading page, user access granted");
          if(self && callbackSuccess) callbackSuccess(self);
        }
        else{
          //nije
          console.log("Not Succcess, user access not granted");
          if(self && callbackForbidden) 
            callbackForbidden(self); //nije mu dozvoljen pristup
          else{
            this.router.navigate([this.redirectRoute]);
          }
        }
      },
      (error: HttpErrorResponse) => {
        //greska jer je sesija istekla
        if(error.status == HttpStatusCode.Unauthorized){
          //nema autorizovan pristup
          JWTUtil.delete();
        }

        if(self && callbackUnauthorized) callbackUnauthorized(self);
        else{
          AlertPageUtil.allowAccess();
          this.router.navigate([RedirectRoutes.ON_SESSION_EXPIRED]);
        }
      }
    );
  }

  //sesije
  checkIsSessionExpired(status: HttpStatusCode){
    return status == HttpStatusCode.Unauthorized && JWTUtil.get() != '';
  }

  redirectIfSessionExpired(status:HttpStatusCode){
    if (this.checkIsSessionExpired(status))
      this.router.navigate([RedirectRoutes.ON_SESSION_EXPIRED]);
  }

  isBuyer():boolean{
    return JWTUtil.getUserRole() == UserRoles.Reg_buyer;
  }

  isSeller():boolean{
    return JWTUtil.getUserRole() == UserRoles.Reg_seller;
  }

  isAdmin():boolean{
    return JWTUtil.getUserRole() == UserRoles.Admin;
  }
}
