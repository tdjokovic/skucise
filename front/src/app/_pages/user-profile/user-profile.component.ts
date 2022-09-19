import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { JWTUtil } from 'src/app/services_back/helpers/jwt_helper';
import { AuthorizeService } from 'src/app/services_back/services/authorize.service';

@Component({
  selector: 'app-user-profile',
  templateUrl: './user-profile.component.html',
  styleUrls: ['./user-profile.component.css']
})
export class UserProfileComponent implements OnInit {

  constructor(private authorizationService : AuthorizeService,
    private activatedRoute : ActivatedRoute,) { }

  ngOnInit(): void {

  }

  isMe(){
    let id = this.activatedRoute.snapshot.paramMap.get("id") as unknown as number;
    if(id == JWTUtil.getID()){
      console.log("Ovo je profil prijavljanog korisnika");
      return true;
    }

    return false;
  }

}
