import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { UserRoles } from 'src/app/services_back/back/types/enums';
import { City, Property, Seller } from 'src/app/services_back/back/types/interfaces';
import { JWTUtil } from 'src/app/services_back/helpers/jwt_helper';
import { AuthorizeService } from 'src/app/services_back/services/authorize.service';
import { CityService } from 'src/app/services_back/services/city.service';
import { PropertyService } from 'src/app/services_back/services/property.service';
import { SellerService } from 'src/app/services_back/services/seller.service';

@Component({
  selector: 'app-property-single',
  templateUrl: './property-single.component.html',
  styleUrls: ['./property-single.component.css']
})
export class PropertySingleComponent implements OnInit {

  public  property! : Property;
  public propertyId : number = 0;

  constructor(private activatedRoute : ActivatedRoute,
    private authorizationService : AuthorizeService,
    private propertyService : PropertyService,
    private citySerice : CityService,
    private sellerService : SellerService) { }

  ngOnInit(): void {
    this.checkIsUserAuthorized();
  }

  checkIsUserAuthorized(){
    this.authorizationService.checkAccess(this.activatedRoute, this, 
      (self: any) =>{
        self.pageLoaded = true;
        
        this.propertyId = this.activatedRoute.snapshot.paramMap.get("id") as unknown as number;
        
        this.propertyService.getProperty(this.propertyId, this, this.cbSuccess, this.cbNotFound);
      }
    )
  }

  isAdmin(){ //ako je admin
    return JWTUtil.getUserRole() == UserRoles.Admin;
  }
  isMe():boolean{
    return JWTUtil.getID() == this.property.sellerUser.id;
  }

  isBuyer(){ //ako je registrovan kupac moze da aplicira
    return JWTUtil.getUserRole() == UserRoles.Reg_buyer;
  }


  //api callbacks
  cbSuccess(self:any, property: Property){
    if(property){
      self.property = property;
      console.log("Property found!");
    }
  }

  cbNotFound(self:any){
    console.error("Property with this id is not found!");
  }

}
