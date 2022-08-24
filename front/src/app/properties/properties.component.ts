import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { AdCategory, AdType, City, Filters, Property, Seller, Tag } from '../services_back/back/types/interfaces';
import { AuthorizeService } from '../services_back/services/authorize.service';
import { CityService } from '../services_back/services/city.service';
import { PropertyService } from '../services_back/services/property.service';
import { SellerService } from '../services_back/services/seller.service';

@Component({
  selector: 'app-properties',
  templateUrl: './properties.component.html',
  styleUrls: ['./properties.component.css']
})
export class PropertiesComponent implements OnInit {

  public adCategory : string | null = null;

  public properties : Property[] = [];
  public sellers : Seller [] = [];
  public cities : City [] = [];
  public adCategories : AdCategory [] = [];
  public adTypes : AdType [] = [];
  public tags : Tag [] = [];

  public currentPage: number = 1;
  public totalPropertiesNum: number = 0
  public totalPagesNum: number = 0 
  public propertiesPerPage: number = 6;
  public sortDateAscending: boolean = false;

  public checkedTags: number[] = [];
  propertyName: string = '';

  public filtersFromPage! : Filters;

  public newConstruction : boolean = false;

  toggleNewConstruction(){
    this.newConstruction = !this.newConstruction;
  }
  

  selectedCityId: number = 0;
  selectedAdCategoryId: number = 0;
  selectedAdTypeId : number = 0;
  selectedSellerId: number = 0;
  public showMoreBool: boolean = false;

 
  
  // Dropdown Tags List
  tagsListVisible: boolean = false;
  toggleTagsListVisibility() {
    this.tagsListVisible = !this.tagsListVisible;
  }

  constructor(private authorizationService : AuthorizeService,
    private activatedRoute : ActivatedRoute,
    private propertyService : PropertyService,
    private cityService : CityService,
    private sellerService : SellerService) { }

  ngOnInit(): void {

    this.activatedRoute.params.subscribe(
      (routeUrl) => {
        this.checkIsUserAuthorized();
      }
    );
        
  }
  

  checkIsUserAuthorized(){
    this.authorizationService.checkAccess(this.activatedRoute, this, 
      (self: any) =>{
        self.pageLoaded = true;


        let par = this.activatedRoute.snapshot.paramMap.get("adCategory");
        if (par != null) this.adCategory = par as unknown as string;

        //u zavisnosti od parametra, da li prikazujemo
        //1 sve nekretnine, ako nema parametra
        //samo prodaju, ako je parametar prodaja
        //samo izdavanje, ako je parametar izdavanje
        if(this.adCategory == null){
          //prikazi sve
          this.selectedAdCategoryId = 0;
          this.getAllProperties(null);
        }
        else{
          this.getAllProperties(this.adCategory);
        }
      } 
    )
  }

  getAllProperties(category : string | null){
    if(category == null){
      this.sellerService.getSellers(undefined, this, (self: any, data : Seller[]) => {
        this.sellers = data;
      });

      this.cityService.getCities(this, (self: any, data : City[]) => {
        this.cities = data;
      });

      //servis za adCategories
      //servis za adTypes

      this.propertyService.getProperties(this, this.cbSuccessProperty);
    }
    else{

      let filters! : Filters;

      if(category == 'prodaja'){
        this.selectedAdCategoryId = 2;
        //filter prodaja
        filters = {
          newConstruction:false,
          sellerId:0,
          cityId:0,
          adCategoryId:2, // koji je tip oglasa
          typeId: 0, //koji je tip nekretnine

          pageNumber:1,
          propertiesPerPage:6,
          ascendingOrder:false
        }

      }
      else if (category == 'izdavanje'){
        this.selectedAdCategoryId = 1;
        //filter izdavanje
        filters = {
          newConstruction:false,
          sellerId:0,
          cityId:0,
          adCategoryId:1, // koji je tip oglasa
          typeId: 0, //koji je tip nekretnine

          pageNumber:1,
          propertiesPerPage:6,
          ascendingOrder:false
        }
      }

      this.propertyService.getFilteredProperties(filters, this, this.cbSuccessProperty);
    }
  }


  //paginacija
  nextPage(){
    if(this.currentPage >= this.totalPagesNum){
      this.currentPage = this.totalPagesNum;
      return;
    }

    this.packFilters(this.currentPage + 1);
    this.propertyService.getFilteredProperties(this.filtersFromPage, this, this.cbSuccessNextPage);
    
  }

  previousPage(){
    if(this.currentPage <= 1){
      this.currentPage = 1;
      return;
    }

    this.packFilters(this.currentPage - 1);
    this.propertyService.getFilteredProperties(this.filtersFromPage, this, this.cbSuccessPreviousPage);
  }

  //filteri
  packFilters(pageNumber? : number){
    this.filtersFromPage = {
      sellerId: this.selectedSellerId,
      cityId: this.selectedCityId,
      adCategoryId: this.selectedAdCategoryId,
      typeId: this.selectedAdTypeId,

      newConstruction: this.newConstruction,

      pageNumber : (pageNumber) ? pageNumber : 1,
      propertiesPerPage : this.propertiesPerPage,
      ascendingOrder : this.sortDateAscending
    }

    if (this.checkedTags.length > 0) //ako su selektonani tagovi
      this.filtersFromPage.tagList = this.checkedTags;

    console.log('Filters from page selected ');
    console.log(this.filtersFromPage);
  }

  showMore(){
    let p = <HTMLSelectElement>document.getElementById('showMore');

    if(this.showMoreBool){
      p.innerHTML = "Prikazi sve filtere";
    }
    else{
      p.innerHTML = "Prikazi osnovne filtere";
    }

    this.showMoreBool = ! this.showMoreBool;
  }

  onSearch(){
    //treba da se odradi da se filtrira po descriptionu to sto je korisnik uneo 
  }

  //tagovi
  getNewTages(){
    console.log("Tags new");

    this.tagsListVisible = false;
    this.checkedTags = [];
    this.tags = [];

    if(this.selectedAdTypeId > 0){
      //get tags
    }
  }

  toggleTags(tagId : number){
    console.log("tag id ", tagId);

    let pom : boolean = false;

    for(let t of this.checkedTags){
      if(t == tagId){
        //tag je cekiran vec
        console.log("Deleting tag from checked");
        let ind : number = this.checkedTags.indexOf(t);
        this.checkedTags.splice(ind, 1);
        pom = true;
        return;
      }
    }
    
    if(pom == false){
      this.checkedTags.push(tagId);
    }
  }



  //callbacks
  cbSuccessProperty(self:any, properties? : Property[] , propertiesNumber? : number){
    if(properties){
      console.log("Success fetching properties");
      self.properties = properties;
      console.log("properties set");
    }

    self.currentPage = 1;
    self.totalPropertiesNum = propertiesNumber;
    self.totalPagesNum = Math.ceil(self.totalPropertiesNum / self.propertiesPerPage);

    console.log(`After fetching properties, total pages is ${self.totalPagesNum}, jobs per page is ${self.propertiesPerPage}, and total properties is ${self.totalPropertiesNum}.`);
  }

  cbSuccessNextPage(self: any, properties? : Property[], propertiesNumber? : number){
    if(properties) self.properties = properties;
    self.currentPage ++ ;

    console.log(`Page changed : ${self.currentPage -1} -> ${self.currentPage}` );
  }

  cbSuccessPreviousPage(self:any, properties?: Property[] , propertiesNumber? : number){
    if(properties) self.properties = properties;
    self.currentPage -- ;

    console.log(`Page changed : ${self.currentPage +1} -> ${self.currentPage}` );
  }

}
