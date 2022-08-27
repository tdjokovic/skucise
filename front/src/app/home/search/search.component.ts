import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { AdCategory, AdType, City, Filters } from 'src/app/services_back/back/types/interfaces';
import { AdCategoryService } from 'src/app/services_back/services/adcategory.service';
import { AdTypesService } from 'src/app/services_back/services/adtype.service';
import { AuthorizeService } from 'src/app/services_back/services/authorize.service';
import { CityService } from 'src/app/services_back/services/city.service';

@Component({
  selector: 'app-search',
  templateUrl: './search.component.html',
  styleUrls: ['./search.component.css']
})
export class SearchComponent implements OnInit {

  public cities : City[] = [];
  public adCategories : AdCategory [] = [];
  public adTypes : AdType [] = [];

  constructor(private cityService : CityService,
    private adCategoryService : AdCategoryService,
    private adTypeService : AdTypesService,
    private authorizationService : AuthorizeService,
    private activatedRoute : ActivatedRoute) { }


  ngOnInit(): void {
    this.checkIsUserAuthorized();
  }

  checkIsUserAuthorized(){
    this.authorizationService.checkAccess(this.activatedRoute, this, 
      (self: any) =>{
        self.pageLoaded = true;

        this.findCities();
        this.findAdCategories();
        this.findAdTypes();
      }
    )
  }

  findCities(){
    this.cityService.getCities(this, this.cbCitiesSucces);
  }

  findAdCategories(){
    this.adCategoryService.getCategories(this, this.cbAdCategoriesSucces);
  }

  findAdTypes(){
    this.adTypeService.getAdTypes(this, this.cbAdTypesSuccess);
  }


  onSubmit(){
    //pretrazi po ovim filterima
    //treba da se prosledi filter !
    //mozemo da kreiramo neki interfejs sa filterima
    //i da pozovemo neku fju u properties component koja ce da okine pretraguu sa filterima
    let filter : Filters = {
    newConstruction: false,
    sellerId: 0,
    cityId:0,
    adCategoryId:0,
    typeId: 0,//koji je tip nekretnine

    pageNumber:1,
    propertiesPerPage:6,
    ascendingOrder:false
    }
  }

  //callbacks
  cbCitiesSucces(self: any, cities ?: City[]){
    if(cities){
      self.cities = cities;
      self.citiesLen = cities.length;
      console.log("Succes finding cities ",self.cities.length);
    }
  }

  cbAdTypesSuccess(self: any, adCategories ?: AdCategory[]){
    if(adCategories){
      self.adCategories = adCategories;
    }
  }

  cbAdCategoriesSucces(self: any, adTypes ?: AdType[]){
    if(adTypes){
      self.adTypes = adTypes;
    }
  }

}
