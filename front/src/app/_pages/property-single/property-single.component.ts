import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { UserRoles } from 'src/app/services_back/back/types/enums';
import { Property } from 'src/app/services_back/back/types/interfaces';
import { MONTHS } from 'src/app/services_back/constants/date';
import { DEFAULT_PROPERTY_PICTURE } from 'src/app/services_back/constants/raw-data';
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
  defPropertyPic : string = DEFAULT_PROPERTY_PICTURE;
  months : any = MONTHS;
  days : Array<number> = [];
  years: Array<number> = [];
  hours: Array<number> = [];
  minutes: Array<number> = [0,10,20,30,40,50]
  thisYear : number = 0;
  thisMonth : number = 0;
  thisDay : number = 0; 

  constructor(private activatedRoute : ActivatedRoute,
    private authorizationService : AuthorizeService,
    private propertyService : PropertyService,
    private citySerice : CityService,
    private sellerService : SellerService,
    private router: Router) { }

  ngOnInit(): void {
    this.checkIsUserAuthorized();
  }

  checkIsUserAuthorized(){
    this.authorizationService.checkAccess(this.activatedRoute, this, 
      (self: any) =>{
        self.pageLoaded = true;
        
        this.propertyId = this.activatedRoute.snapshot.paramMap.get("id") as unknown as number;
        
        this.propertyService.getProperty(this.propertyId, this, this.cbSuccess, this.cbNotFound);

        const now = new Date();
        this.thisYear = now.getFullYear();
        this.thisMonth = now.getMonth() + 1;
        this.thisDay = now.getDate();

        this.days = Array(31).fill(1).map((x,i)=> i  + 1); 
        this.years = Array(2).fill(1).map((x,i)=> i + this.thisYear);
        this.hours = Array(24).fill(1).map((x,i)=> i);
        
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
  isVisitor()
  {
    return JWTUtil.getUserRole() == UserRoles.Visitor;
  }

  applyForPropery()
  {
    

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

  getHour(hour:number)
  {
    if (hour < 10)
    {
      return '0' + hour;
    }
    else return hour;
  }

  getMinute(minute:number)
  {
    if (minute == 0)
    {
      return '0' + minute;
    }
    else return minute;
  }
}
