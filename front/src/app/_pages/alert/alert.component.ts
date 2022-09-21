import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { UserRoles } from 'src/app/services_back/back/types/enums';
import { Property } from 'src/app/services_back/back/types/interfaces';
import { RedirectRoutes } from 'src/app/services_back/constants/routing.properties';
import { AlertPageUtil } from 'src/app/services_back/helpers/alert_helper';
import { JWTUtil } from 'src/app/services_back/helpers/jwt_helper';
import { AuthorizeService } from 'src/app/services_back/services/authorize.service';
import { BuyerService } from 'src/app/services_back/services/buyer.service';
import { SellerService } from 'src/app/services_back/services/seller.service';

@Component({
  selector: 'app-alert',
  templateUrl: './alert.component.html',
  styleUrls: ['./alert.component.css']
})
export class AlertComponent implements OnInit {

  public cause: string | null = null;
  public param: any = null;

  public jwtExpired: boolean = true;
  
  // za: apply-to-property-successfull
  public appliedToProperty: boolean = false;
  
  // za: create-property-successfull
  public createdProperty: boolean = false;

  constructor(
    private router: Router,
    private activatedRoute: ActivatedRoute,
    private authService: AuthorizeService,
    private sellerService: SellerService,
    private buyerService: BuyerService
  ) { }

  ngOnInit(): void {
    if(!AlertPageUtil.checkAccess())
      this.router.navigate(RedirectRoutes.HOME);


    AlertPageUtil.denyAccess();

    this.cause = this.activatedRoute.snapshot.paramMap.get('cause');
    let par = this.activatedRoute.snapshot.paramMap.get('param');
    this.param = JSON.parse((par) ? par : '{}');

    console.log("ALERT PAGE");
    console.log("Alert page cause: ", this.cause);
    console.log("Alert page parameter: ", this.param);

    //ostatak

    if(JWTUtil.getUserRole() == UserRoles.Reg_buyer && this.cause == 'apply-to-property-successfull'){
      this.buyerService.getBuyersProperties(JWTUtil.getID(), this, (self:any, properties: Property[]) => {
        self.appliedToProperty = properties.find(p => p.id == self.param.id) != undefined;
      });
    }

    if(JWTUtil.getUserRole() == UserRoles.Reg_seller){
      this.createdProperty = true;
    }

  }


  checkCause(c : string) : boolean{
    if(this.cause == null)
      return false;
    return c == this.cause;
  }


  checkSessionExpired() {
    return this.checkCause('session-expired') && JWTUtil.get() == '';
  }

  checkApplyToPropertySuccessfull() {
    return this.checkCause('apply-to-property-successfull') && this.appliedToProperty;
  }

  checkRegisterSuccessful() {
    return this.checkCause('register-successful') && JWTUtil.get() == '';
  }

  checkCreatePropertySuccessfull() {
    return this.checkCause('create-property-successfull') && this.createdProperty;
  }

  checkChangedToSellerSuccessfully()
  {
    return this.checkCause('seller-successful');
  }

}
