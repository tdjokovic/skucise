import { ViewportScroller } from '@angular/common';
import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { UserRoles } from 'src/app/services_back/back/types/enums';
import { Buyer } from 'src/app/services_back/back/types/interfaces';
import { RedirectRoutes } from 'src/app/services_back/constants/routing.properties';
import { JWTUtil } from 'src/app/services_back/helpers/jwt_helper';
import { AuthorizeService } from 'src/app/services_back/services/authorize.service';
import { BuyerService } from 'src/app/services_back/services/buyer.service';


@Component({
  selector: 'app-buyer-info',
  templateUrl: './buyer-info.component.html',
  styleUrls: ['./buyer-info.component.css']
})
export class BuyerInfoComponent implements OnInit {


  public buyerID : number = 0;
  public buyer : Buyer | null = null;

  constructor(private authorizationService : AuthorizeService,
    private activatedRoute : ActivatedRoute,
    private buyerService : BuyerService,
    private router : Router,
    private viewport : ViewportScroller) { }

    ngOnInit(): void {
      this.checkIsUserAuthorized();
      this.viewport.scrollToAnchor('top_container')
    }
  
    checkIsUserAuthorized(){
      this.authorizationService.checkAccess(this.activatedRoute, this, 
        (self: any) =>{
          self.pageLoaded = true;
  
          let p = this.activatedRoute.snapshot.paramMap.get("id");
          if (p != null) this.buyerID = p as unknown as number;
  
          this.buyerService.getBuyer(self.buyerID, self, self.cbSuccess, self.cbNotFound);
        }
      )
    }

    isMe(): boolean {
      return JWTUtil.getID() == this.buyerID;
    }
  
    isAdmin(): boolean {
      return JWTUtil.getUserRole() == UserRoles.Admin;
    }
    

      //callbacks
    cbSuccess(self: any, buyer: Buyer | null) {
      self.buyer = buyer;
      self.buyerID = (buyer) ? buyer.id : 0;
    }

    cbNotFound(self: any) {
      console.log("Dosao je ovde");
      self.router.navigate(RedirectRoutes.HOME);
  }
}
