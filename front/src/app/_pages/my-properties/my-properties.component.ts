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

  constructor(private sellerService: SellerService) { }

  ngOnInit(): void {
    this.sellerService.getSellersProperties(JWTUtil.getID(),this,this.cbSuccessProperty);
  }

  propertiesBySeller : Property[] = [];

  cbSuccessProperty(self:any, properties? : Property[]){
    if(properties){
      console.log("Success fetching properties");
      self.propertiesBySeller = properties;
      console.log("properties set");
      console.log(self.propertiesBySeller);
    }
  }

}
