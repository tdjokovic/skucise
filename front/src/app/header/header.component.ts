import { Component, OnInit } from '@angular/core';
import { Router, NavigationEnd, ActivatedRoute } from '@angular/router'
import { UserRoles } from '../services_back/back/types/enums';
import { AdCategory } from '../services_back/back/types/interfaces';
import { JWTUtil } from '../services_back/helpers/jwt_helper';
import { AdCategoryService } from '../services_back/services/adcategory.service';

@Component({
  selector: 'app-header',
  templateUrl: './header.component.html',
  styleUrls: ['./header.component.css']
})
export class HeaderComponent implements OnInit {

  constructor(private router:Router, private adCategoryService: AdCategoryService) { }

  
  public isActive: string = "";
  public adCategories : AdCategory [] = [];

  ngOnInit(): void {
    this.routeChanged();
    this.adCategoryService.getCategories(this, (self: any, data : AdCategory[]) => {
      this.adCategories = data;
    });
  }

  routeChanged(){
    this.isActive = this.router.url;
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
}
