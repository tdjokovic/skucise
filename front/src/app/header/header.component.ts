import { Component, OnInit } from '@angular/core';
import { Router, NavigationEnd } from '@angular/router'
import { UserRoles } from '../services_back/back/types/enums';
import { JWTUtil } from '../services_back/helpers/jwt_helper';

@Component({
  selector: 'app-header',
  templateUrl: './header.component.html',
  styleUrls: ['./header.component.css']
})
export class HeaderComponent implements OnInit {

  constructor(private router:Router) { }

  
  public isActive: string = "";

  ngOnInit(): void {
    this.routeChanged();
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
