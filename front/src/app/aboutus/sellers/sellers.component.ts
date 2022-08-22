import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { AuthorizeService } from 'src/app/services_back/services/authorize.service';
import { SellerService } from 'src/app/services_back/services/seller.service';
import { Seller } from 'src/app/services_back/back/types/interfaces';

@Component({
  selector: 'app-sellers',
  templateUrl: './sellers.component.html',
  styleUrls: ['./sellers.component.css']
})
export class SellersComponent implements OnInit {

  public sellers : Seller [] = [];

  constructor(private activatedRoute : ActivatedRoute,
    private authorizationService : AuthorizeService,
    private sellerService : SellerService) { }

  ngOnInit(): void {
    this.checkIsUserAuthorized();
  }

  checkIsUserAuthorized(){
    this.authorizationService.checkAccess(this.activatedRoute, this, 
      (self: any) =>{
        self.pageLoaded = true;
        this.sellerService.getSellers(undefined, self, self.cbSuccess);
      }
    )
  }

  cbSuccess(self: any, sellers?: Seller[]) {
    if(sellers) {
      self.sellers = sellers;
    }
  }

}
