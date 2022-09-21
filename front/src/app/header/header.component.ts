import { Component, Input, OnInit } from '@angular/core';
import { Router, NavigationEnd, ActivatedRoute } from '@angular/router'
import { BuyerService } from 'src/app/services_back/services/buyer.service';
import { UserRoles } from '../services_back/back/types/enums';
import { AdCategory, Buyer, Seller } from '../services_back/back/types/interfaces';
import { RedirectRoutes } from '../services_back/constants/routing.properties';
import { JWTUtil } from '../services_back/helpers/jwt_helper';
import { AdCategoryService } from '../services_back/services/adcategory.service';
import { LoginService } from '../services_back/services/login.service';
import { SellerService } from '../services_back/services/seller.service';
import { NgbOffcanvas, OffcanvasDismissReasons } from '@ng-bootstrap/ng-bootstrap'


@Component({
  selector: 'app-header',
  templateUrl: './header.component.html',
  styleUrls: ['./header.component.css']
})
export class HeaderComponent implements OnInit {

  closeResult = '';

  constructor(private router:Router, 
              private adCategoryService: AdCategoryService,
              private buyerService : BuyerService,
              private sellerService : SellerService,
              private loginService : LoginService,
              private offcanvasService : NgbOffcanvas
              ) { }

  
  @Input() public active: string = '';
  public adCategories : AdCategory [] = [];
  firstName : string = '';
  lastName : string = '';
  id: number = 0;

  ngOnInit(): void {

    this.loginService._newLogin.subscribe({
      next: user =>{
        //alert("Nova prijava");
        console.log(this.firstName);
        console.log(this.lastName);
        this.updateName();
      }
    });

    this.adCategoryService.getCategories(this, (self: any, data : AdCategory[]) => {
      this.adCategories = data;
    });
    this.updateName();
    /*
    if (this.isBuyer())
    {
      this.buyerService.getBuyer(JWTUtil.getID(),this,this.cbSuccess,this.cbNotFound);
    }
    else if (this.isSeller())
    {
      this.sellerService.getSeller(JWTUtil.getID(),this,this.cbSuccess,this.cbNotFound);
    }*/
  }


  isVisitor():boolean{
    return JWTUtil.getUserRole() == UserRoles.Visitor;
  }

  isBuyer():boolean{
    return JWTUtil.getUserRole() == UserRoles.Reg_buyer;
  }

  isSeller():boolean{
    return JWTUtil.getUserRole() == UserRoles.Reg_seller;
  }

  isAdmin(): boolean{
    return JWTUtil.getUserRole() == UserRoles.Admin;
  }

  /*
  //callbacks
  cbSuccess(self: any, user: Buyer | Seller) {
    self.firstName = (user) ? user.firstName : '';
    self.lastName = (user) ? user.lastName : '';
  }

  cbNotFound(self: any) {
    self.router.navigate(RedirectRoutes.HOME);
}*/

checkActive(name: string): boolean {
  return name == this.active;
}

  updateName()
  {
    this.firstName = (localStorage.getItem('first-name')) ? localStorage.getItem('first-name')!.toString() : '';
    this.lastName = (localStorage.getItem('first-name')) ? localStorage.getItem('last-name')!.toString() : '';
    this.id = JWTUtil.getID();
  }

  
  open(content:any) {
    this.offcanvasService.open(content, {position:'end' ,ariaLabelledBy: 'offcanvas-basic-title'}).result.then((result) => {
      this.closeResult = `Closed with: ${result}`;
    }, (reason) => {
      this.closeResult = `Dismissed ${this.getDismissReason(reason)}`;
    });
  }

  private getDismissReason(reason: any): string {
    if (reason === OffcanvasDismissReasons.ESC) {
      return 'by pressing ESC';
    } else if (reason === OffcanvasDismissReasons.BACKDROP_CLICK) {
      return 'by clicking on the backdrop';
    } else {
      return `with: ${reason}`;
    }
  }

}