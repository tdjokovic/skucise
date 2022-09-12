import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { Buyer, Seller } from 'src/app/services_back/back/types/interfaces';
import { BuyerService } from 'src/app/services_back/services/buyer.service';
import { SellerService } from 'src/app/services_back/services/seller.service';

@Component({
  selector: 'app-approve-users',
  templateUrl: './approve-users.component.html',
  styleUrls: ['./approve-users.component.css']
})
export class ApproveUsersComponent implements OnInit {

  public sellersListActive: boolean = false;

  public activeSeller: Seller | undefined = undefined;
  public activeBuyer: Buyer | undefined = undefined;


  public sellers: Seller[] = [];
  public buyers: Buyer[] = [];


  constructor(
    public route: ActivatedRoute,
    public router: Router,
    public sellerService: SellerService,
    public buyerService: BuyerService
  ) { }

  ngOnInit(): void {
    this.sellerService.getSellers(true, this, this.cbSuccessGetSellers);
    this.buyerService.getBuyers(true, this, this.cbSuccessGetBuyers);
  }

  hideRegInfo() {
    this.activeSeller = undefined;
    this.activeBuyer = undefined;
  }

  showRegInfo(id: number) {
    this.activeSeller = this.sellers.find(x => x.id == id);
    if (!this.activeSeller)
      this.activeBuyer = this.buyers.find(x => x.id == id);
  }


  //prihvatanje ili odbijanje zahteva za registraciju

  onApprove() {
    if(this.activeSeller) {
      this.sellerService.approveSeller(this.activeSeller.id, this, this.cbSuccessPutDeleteSeller);
      console.log('Approve registration for user with id ' + this.activeSeller.id);
    }
    else if(this.activeBuyer) {
      this.buyerService.approveBuyer(this.activeBuyer.id, this, this.cbSuccessPutDeleteBuyer)
      console.log('Approve registration for user with id ' + this.activeBuyer.id);
    }

    this.hideRegInfo();
  }

  onReject() {
    if(this.activeSeller) {
      this.sellerService.deleteSeller(this.activeSeller.id, this, this.cbSuccessPutDeleteSeller);
      console.log('Reject registration for user with id ' + this.activeSeller.id);
    }
    else if(this.activeBuyer) {
      this.buyerService.deleteBuyer(this.activeBuyer.id, this, this.cbSuccessPutDeleteBuyer);
      console.log('Reject registration for user with id ' + this.activeBuyer.id);
    }

    this.hideRegInfo();
  }


    // Biranje tipa liste

    onSellersListSelect() {
      this.sellersListActive = true;
      this.hideRegInfo();
    }
  
    onBuyersListSelect() {
      this.sellersListActive = false;
      this.hideRegInfo();
    }


  // API Callbacks

  cbSuccessGetBuyers(self: any, buyers?: Buyer[]) {
    if(buyers) self.buyers = buyers;
  }

  cbSuccessGetSellers(self: any, sellers?: Seller[]) {
    if(sellers) self.sellers = sellers;
  }

  cbSuccessPutDeleteBuyer(self: any) {
    self.buyerService.getBuyers(true, self, self.cbSuccessGetBuyers);
  }

  cbSuccessPutDeleteSeller(self: any) {
    self.sellerService.getSellers(true, self, self.cbSuccessGetSellers);
  }


}
