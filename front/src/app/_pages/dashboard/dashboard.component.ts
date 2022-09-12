import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { AuthorizeService } from 'src/app/services_back/services/authorize.service';

@Component({
  selector: 'app-dashboard',
  templateUrl: './dashboard.component.html',
  styleUrls: ['./dashboard.component.css']
})
export class DashboardComponent implements OnInit {

  public pageLoaded :boolean = false;

  constructor(private authorizationService : AuthorizeService, private activatedRoute : ActivatedRoute, private router : Router) { }

  ngOnInit(): void {
    this.checkIsUserAuthorized();
  }

  checkIsUserAuthorized(){
    this.authorizationService.checkAccess(this.activatedRoute, this, 
      (self: any) =>{
        self.pageLoaded = true;
      }
    )
  }


}
