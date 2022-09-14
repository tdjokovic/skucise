import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { AuthorizeService } from 'src/app/services_back/services/authorize.service';

@Component({
  selector: 'app-add-property-form',
  templateUrl: './add-property-form.component.html',
  styleUrls: ['./add-property-form.component.css']
})
export class AddPropertyFormComponent implements OnInit {

  constructor(private authorizationService : AuthorizeService, private activatedRoute : ActivatedRoute) { }

  ngOnInit(): void {
    this.checkIsUserAuthorized();
  }
  
  checkIsUserAuthorized(){
    this.authorizationService.checkAccess(this.activatedRoute, this, 
      (self: any) =>{
        self.pageLoaded = true;

        let p = this.activatedRoute.snapshot.paramMap.get("id");
       
      }
    )
  }

}
