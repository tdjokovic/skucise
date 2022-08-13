import { Component, OnInit } from '@angular/core';
import { Router, NavigationEnd } from '@angular/router'

@Component({
  selector: 'app-header',
  templateUrl: './header.component.html',
  styleUrls: ['./header.component.css']
})
export class HeaderComponent implements OnInit {

  constructor(private router:Router) {

   }

  
  public isActive: string = "";
   private userLoggedIn:boolean = false;

  ngOnInit(): void {
    this.routeChanged();
  }

  routeChanged(){
    this.isActive = this.router.url;
  }

  check(userAuthor:string){
    if(userAuthor == 'login'){
      //ako nije ulogovan moze da se uloguje
      if(!this.userLoggedIn) return true;
      else return false;
    }
    else if(userAuthor == 'logout'){
      //ako je ulogovan moze da se izloguje
      if(this.userLoggedIn)return true;
      else return false;
    }
    return false;
  }

  login(){
    this.userLoggedIn = true;
  }

  logout(){
    this.userLoggedIn = false;
  }

}
