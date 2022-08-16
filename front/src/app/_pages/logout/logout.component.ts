import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { RedirectRoutes } from 'src/app/services_back/constants/routing.properties';
import { JWTUtil } from 'src/app/services_back/helpers/jwt_helper';
import { AuthorizeService } from 'src/app/services_back/services/authorize.service';

@Component({
  selector: 'app-logout',
  templateUrl: './logout.component.html',
  styleUrls: ['./logout.component.css']
})
export class LogoutComponent implements OnInit {

  constructor(private authorizationService : AuthorizeService, private activatedRoute : ActivatedRoute, private router : Router) { }

  ngOnInit(): void {
    this.checkIsUserAuthorized();
    this.logout();
  }

  checkIsUserAuthorized(){
    this.authorizationService.checkAccess(this.activatedRoute, this, 
      (self: any) =>{
        self.pageLoaded = true;
      }
    )
  }

  logout(){
    //odjava korisnika sa servera
    JWTUtil.delete();
    this.router.navigate(RedirectRoutes.HOME);
  }

}
