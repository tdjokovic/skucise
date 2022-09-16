import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { AdCategory, AdType, City, Property, Seller } from 'src/app/services_back/back/types/interfaces';
import { RedirectRoutes } from 'src/app/services_back/constants/routing.properties';
import { JWTUtil } from 'src/app/services_back/helpers/jwt_helper';
import { AdCategoryService } from 'src/app/services_back/services/adcategory.service';
import { AdTypesService } from 'src/app/services_back/services/adtype.service';
import { AuthorizeService } from 'src/app/services_back/services/authorize.service';
import { CityService } from 'src/app/services_back/services/city.service';
import { PropertyService } from 'src/app/services_back/services/property.service';
import { SellerService } from 'src/app/services_back/services/seller.service';

@Component({
  selector: 'app-add-property-form',
  templateUrl: './add-property-form.component.html',
  styleUrls: ['./add-property-form.component.css']
})
export class AddPropertyFormComponent implements OnInit {

  public adCategory : string | null = null;

  public my_properties : Property[] = [];
  public cities : City [] = [];
  public adCategories : AdCategory [] = [];
  public adTypes : AdType [] = [];

  pattNumber: RegExp = /^[0-9]+$/;

  description : string = '';
  price: string = '';
  area: string = '';
  picture: string = '';
  newConstruction: string = '';
  selectedCityId: number = 0;
  selectedAdCategoryId: number = 0;
  selectedAdTypeId : number = 0;
  this_seller! : Seller;


  //alert polja
  invalidPrice : boolean = false;
  invalidArea : boolean = false;
  invalidCity  : boolean = false;
  invalidAdCategory : boolean = false;
  invalidType : boolean = false;
  invalidPicture : boolean = false;
  invalidNewConstruction : boolean = false;

  constructor(private authorizationService : AuthorizeService, 
    private activatedRoute : ActivatedRoute,
    private propertyService : PropertyService,
    private adCategoryService : AdCategoryService,
    private adTypeService : AdTypesService,
    private cityService : CityService,
    private sellerService : SellerService) { }

  ngOnInit(): void {
    this.checkIsUserAuthorized();
  }
  
  checkIsUserAuthorized(){
    this.authorizationService.checkAccess(this.activatedRoute, this, 
      (self: any) =>{
        self.pageLoaded = true;

        this.cityService.getCities(this, (self: any, data : City[]) => {
          this.cities = data;
        });
  
        this.adTypeService.getAdTypes(this, (self: any, data : AdType[]) => {
          this.adTypes = data;
        });
  
        this.adCategoryService.getCategories(this, (self: any, data : AdCategory[]) => {
          this.adCategories = data;
        });
  
       
      }
    )
  }
  setNewConstruction(event : Event)
  {
    this.newConstruction = (event.target as HTMLInputElement).value;
  }
  checkValidation()
  {
    
    this.invalidPrice = !this.validationNumber(this.price);
    this.invalidArea = !this.validationNumber(this.area);
    this.invalidCity = !this.validationCity();
    this.invalidAdCategory = !this.validationAdCategory();
    this.invalidType = !this.validationType();
    //this.validationPicture();
    this.invalidNewConstruction = !this.validationNewConstruction();

    if (!(this.invalidPrice || this.invalidArea || this.invalidCity || this.invalidAdCategory || 
          this.invalidType || this.invalidPicture || this.invalidNewConstruction))
          {
            this.sellerService.getSeller(JWTUtil.getID(), this, this.cbSuccess, this.cbNotFound);

            let newProperty : Property = {
              id:null,
              sellerUser: this.this_seller,
              description:this.description,
              city:{id:this.selectedCityId, name:this.cities.find(x => this.selectedCityId == x.id)!.name},
              adCategory:{id:this.selectedAdCategoryId, name:this.adCategories.find(x => this.selectedAdCategoryId == x.id)!.name}, //prodaja ili izdavanje
              type: {id:this.selectedAdTypeId, name: this.adTypes.find(x => this.selectedAdTypeId == x.id)!.name},
              tags:[],
              price:parseFloat(this.price),
              postingDate:new Date(),
              area:this.area, //kvadratura
              newConstruction:(this.newConstruction == '1')? true : false,
              picture:(this.picture == '')? null : this.picture, //slika u Base64
            }

            this.propertyService.createProperty(newProperty,this, this.cbSuccessAddProperty);
          }
    
  }
  validationNumber(x : string)
  {
    if (this.pattNumber.test(x)) return true;
    return false;
  }
  
  validationCity()
  {
    if (this.selectedCityId != 0) return true;
    return false;
  }
  validationAdCategory()
  {
    if (this.selectedAdCategoryId != 0) return true;
    return false;
  }
  validationType()
  {
    if (this.selectedAdTypeId != 0) return true;
    return false;
  }
  validationPicture()
  {
    let reader = new FileReader();
    let inputPicture = document.getElementById("propertyPicture") as HTMLInputElement;

    if(inputPicture == null || inputPicture.files == null || inputPicture.files.length == 0){
      console.log("Slika nije izabrana kako treba, ne postoji!");
      return;
    }

    let self = this;

    reader.readAsDataURL(inputPicture.files[0]);
    reader.onload = function(){
      self.picture = reader.result as string;
      console.log(self.picture);
      let len = self.picture.length;

      if(len < 5000 || len > 65000) // duzina tj velicina slike nije dobra
      {
        (<HTMLSelectElement>document.getElementById('propertyPicture')).focus();
        self.invalidPicture = true;
        return;
      }

      //sve je ok
      self.invalidPicture = false;
    }
  }

  validationNewConstruction()
  {
    if (this.newConstruction == '0' || this.newConstruction == '1') return true;
    return false;
  }
   //callbacks
  cbSuccess(self: any, seller: Seller | null) {
    self.this_seller = seller;
  }
  cbSuccessAddProperty(self: any) {
    window.location.reload();
  }

  cbNotFound(self: any) {
    self.router.navigate(RedirectRoutes.HOME);
  }
}
