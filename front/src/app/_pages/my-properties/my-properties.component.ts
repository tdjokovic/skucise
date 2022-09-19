import { Component, OnInit } from '@angular/core';
import { Property } from 'src/app/services_back/back/types/interfaces';
import { JWTUtil } from 'src/app/services_back/helpers/jwt_helper';
import { SellerService } from 'src/app/services_back/services/seller.service';

@Component({
  selector: 'app-my-properties',
  templateUrl: './my-properties.component.html',
  styleUrls: ['./my-properties.component.css']
})
export class MyPropertiesComponent implements OnInit {

  public currentPage: number = 1;
  public totalPropertiesNum: number = 0
  public totalPagesNum: number = 0 ;
  public totalPagesArray : number [] = [];
  public propertiesPerPage: number = 6;

  constructor(private sellerService: SellerService) { }
  
  newPropModal : boolean = false;

  ngOnInit(): void {
    this.sellerService.getSellersProperties(JWTUtil.getID(),this,this.cbSuccessProperty);
  }

  toggleNewProp(){
    this.newPropModal = !this.newPropModal;
  }

  propertiesBySeller : Property[] = [];
  propertiesToShow : Property[] = [];

  cbSuccessProperty(self:any, properties? : Property[]){
    if(properties){
      console.log("Success fetching properties");
      self.propertiesBySeller = properties;
      self.propertiesToShow = properties.slice(0, self.propertiesPerPage);
      console.log(self.propertiesToShow);
      self.totalPropertiesNum = properties.length;
      self.totalPagesNum = Math.ceil(self.totalPropertiesNum / self.propertiesPerPage);
      for(let i = 1; i<=self.totalPagesNum ; i ++ ){
        self.totalPagesArray.push(i);
      }
      console.log("properties set");
      console.log(self.propertiesBySeller);
    }
  }

  changePage(p:number){

    let startIndex = (p-1) * this.propertiesPerPage;
    let endIndex = startIndex + this.propertiesPerPage
    this.propertiesToShow = this.propertiesBySeller.slice(startIndex, endIndex);
    this.currentPage = p;
  }

}
