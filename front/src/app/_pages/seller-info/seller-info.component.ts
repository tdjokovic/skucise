import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Seller } from 'src/app/services_back/back/types/interfaces';
import { RedirectRoutes } from 'src/app/services_back/constants/routing.properties';
import { JWTUtil } from 'src/app/services_back/helpers/jwt_helper';
import { AuthorizeService } from 'src/app/services_back/services/authorize.service';
import { SellerService } from 'src/app/services_back/services/seller.service';

@Component({
  selector: 'app-seller-info',
  templateUrl: './seller-info.component.html',
  styleUrls: ['./seller-info.component.css']
})
export class SellerInfoComponent implements OnInit {


  public sellerID : number = 0;
  public seller : Seller | null = null;

  constructor(private authorizationService : AuthorizeService,
    private activatedRoute : ActivatedRoute,
    private sellerService : SellerService) { }

  ngOnInit(): void {
    this.checkIsUserAuthorized();
  }

  checkIsUserAuthorized(){
    this.authorizationService.checkAccess(this.activatedRoute, this, 
      (self: any) =>{
        self.pageLoaded = true;

        let p = this.activatedRoute.snapshot.paramMap.get("id");
        if (p != null) this.sellerID = p as unknown as number;

        this.sellerService.getSeller(self.sellerID, self, self.cbSuccess, self.cbNotFound);
      }
    )
  }

  isMe():boolean{
    return JWTUtil.getID() == this.sellerID;
  }

  //callbacks
  cbSuccess(self: any, seller: Seller | null) {
    self.seller = seller;
    self.empID = (seller) ? seller.id : 0;
  }

  cbNotFound(self: any) {
    self.router.navigate(RedirectRoutes.HOME);
  }

}
