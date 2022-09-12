import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { AuthorizeService } from '../services_back/services/authorize.service';

@Component({
  selector: 'app-contact',
  templateUrl: './contact.component.html',
  styleUrls: ['./contact.component.css']
})
export class ContactComponent implements OnInit {

  public pageLoaded : boolean = false;

  constructor(private authorizationService : AuthorizeService, private activatedRoute : ActivatedRoute) { }

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
