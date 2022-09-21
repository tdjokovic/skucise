import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { AdCategory, AdType, City, Filters, Property, Seller, Tag } from '../services_back/back/types/interfaces';
import { AdCategoryService } from '../services_back/services/adcategory.service';
import { AdTypesService } from '../services_back/services/adtype.service';
import { AuthorizeService } from '../services_back/services/authorize.service';
import { CityService } from '../services_back/services/city.service';
import { PropertyService } from '../services_back/services/property.service';
import { SellerService } from '../services_back/services/seller.service';
import { ViewportScroller } from '@angular/common';
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
  public totalPagesNum: number = 0 ;
  public totalPagesArray : number [] = [];
  public propertiesPerPage: number = 6;
  public sortByPriceAsc: boolean = true;
  public sortByPriceText: string = "Cena rastuce";

  public checkedTags: number[] = [];
  propertyName: string = '';

  public filtersFromPage! : Filters;

  public newConstruction : boolean = false;

  toggleNewConstruction(){
    this.newConstruction = !this.newConstruction;
  }
  
  setAdCategory(id:number){
    this.selectedAdCategoryId = id;
  }

  selectedCityId: number = 0;
  selectedAdCategoryId: number = 0;
  selectedAdTypeId : number = 0;
  selectedSellerId: number = 0;
  public showMoreBool: boolean = false;

  counterPages(){
    this.totalPagesArray = [];
    for(let i = 1; i<=this.totalPagesNum ; i ++ ){
      this.totalPagesArray.push(i);
    }

  }
 
  
  // Dropdown Tags List
  tagsListVisible: boolean = false;
  toggleTagsListVisibility() {
    this.tagsListVisible = !this.tagsListVisible;
  }

  togglePriceSort(){
    const priceSelect = document.getElementById(
      'sortByPrice'
    ) as HTMLInputElement | null;
    if(priceSelect != null){
      if(priceSelect.value == "0"){
        this.sortByPriceAsc = true;
      }
      else if (priceSelect.value == "1"){
        this.sortByPriceAsc = false;
      }
    }
  }

  constructor(private authorizationService : AuthorizeService,
    private activatedRoute : ActivatedRoute,
    private propertyService : PropertyService,
    private adCategoryService : AdCategoryService,
    private adTypeService : AdTypesService,
    private cityService : CityService,
    private sellerService : SellerService,
    private viewportScroller: ViewportScroller) { }

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

        
      this.sellerService.getSellers(undefined, this, (self: any, data : Seller[]) => {
        this.sellers = data;
      });

      this.cityService.getCities(this, (self: any, data : City[]) => {
        this.cities = data;
      });

      this.adTypeService.getAdTypes(this, (self: any, data : AdType[]) => {
        this.adTypes = data;
      });

      this.adCategoryService.getCategories(this, (self: any, data : AdCategory[]) => {
        this.adCategories = data;

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
      });

      } 
    )
  }

  getAllProperties(category : string | null){
    if(category == null){

      //servis za adCategories
      //servis za adTypes
      this.propertyService.getProperties(this, this.cbSuccessProperty);
    }
    else{
      this.selectedAdCategoryId = 0;

      let filters! : Filters;

      if(category == 'prodaja'){
        this.selectedAdCategoryId = this.adCategories.find(s => s.name == 'Prodaja')?.id as unknown as number; 
        console.log("selected category id is "+this.selectedAdCategoryId);
        //filter prodaja
        filters = {
          newConstruction:false,
          sellerId:0,
          cityId:0,
          adCategoryId:2, // koji je tip oglasa
          typeId: 0, //koji je tip nekretnine

          pageNumber:1,
          propertiesPerPage:6,
          ascendingOrder:true
        }

      }
      else if (category == 'izdavanje'){
        this.selectedAdCategoryId  = this.adCategories.find(s => s.name == 'Izdavanje')?.id as unknown as number; 
        console.log("selected category id is "+this.selectedAdCategoryId);
        //filter izdavanje
        filters = {
          newConstruction:false,
          sellerId:0,
          cityId:0,
          adCategoryId:1, // koji je tip oglasa
          typeId: 0, //koji je tip nekretnine

          pageNumber:1,
          propertiesPerPage:6,
          ascendingOrder:true
        }
      }

      this.propertyService.getFilteredProperties(filters, this, this.cbSuccessProperty);
    }
  }


  //paginacija

  changePage(p:number){
    //alert("current page is " + this.currentPage+" target page is "+p)
    let res : number;
    if(p>this.currentPage){
      res = p-this.currentPage;
      for(let i = 0; i< res; i++){
        this.nextPage(p);
      }
    }
    else if(p<this.currentPage){
      res = this.currentPage - p;
      for(let i = 0; i< res; i++){
        this.previousPage(p);
      }
    }
    //this.viewportScroller.scrollToAnchor("beggining_container");

    //zumiraj gore
    document.getElementById('searchForm')?.scrollIntoView({
      behavior: "smooth",
      block: "start",
      inline: "nearest"
    })
  }

  nextPage(targetPage:number){
    //alert("next page");
    if(this.currentPage >= this.totalPagesNum){
      
      this.currentPage = this.totalPagesNum;
      return;
    }

    if(this.currentPage + 1 == targetPage){
      //alert('stigli smo na stranu')
      this.packFilters(this.currentPage + 1);
      this.propertyService.getFilteredProperties(this.filtersFromPage, this, this.cbSuccessNextPage);
    }
    else{
      //alert('jos strana');
      this.currentPage ++ ;
    }
    
  }

  previousPage(targetPage:number){
    if(this.currentPage <= 1){
      this.currentPage = 1;
      return;
    }
    if(this.currentPage - 1  == targetPage){
      //alert('stigli smo na stranu')
      this.packFilters(this.currentPage - 1);
      this.propertyService.getFilteredProperties(this.filtersFromPage, this, this.cbSuccessPreviousPage);
    }
    else{
      //alert('jos strana');
      this.currentPage --;
    }

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
      ascendingOrder : this.sortByPriceAsc
    }

    console.log("FILTERS ARE");
    console.log(`City id ${this.filtersFromPage.cityId}, adCategoryId ${this.filtersFromPage.adCategoryId}, adTypeId ${this.filtersFromPage.typeId}, newConstruction ${this.filtersFromPage.newConstruction}`)

    if (this.checkedTags.length > 0) //ako su selektonani tagovi
      this.filtersFromPage.tagList = this.checkedTags;

    console.log('Filters from page selected ');
    console.log(this.filtersFromPage);
  }

  resetFilters(){
    this.selectedSellerId = 0;
    this.selectedCityId = 0;    

    if(this.adCategory == 'prodaja'){
      this.selectedAdCategoryId = this.adCategories.find(s => s.name == 'Prodaja')?.id as unknown as number; 
      const radio = document.getElementById(
        'ProdajaRadio'
      ) as HTMLInputElement | null;
      if(radio != null){
        radio.checked = true;
      }
    }
    else if(this.adCategory == 'izdavanje'){
      this.selectedAdCategoryId = this.adCategories.find(s => s.name == 'Izdavanje')?.id as unknown as number; 
      const radio = document.getElementById(
        'IzdavanjeRadio'
      ) as HTMLInputElement | null;
      if(radio != null){
        radio.checked = true;
      }
    }
    else{
      this.selectedAdCategoryId = 0;
      const radio1 = document.getElementById('IzdavanjeRadio') as HTMLInputElement | null;
      const radio2 = document.getElementById('ProdajaRadio') as HTMLInputElement | null;
      
      if(radio1 != null){
        radio1.checked = false;
      }
      if(radio2 != null){
        radio2.checked = false;
      }

    }

    this.selectedAdTypeId = 0

    this.newConstruction= false; //checkbox
    const checkbox = document.getElementById(
      'novogradnjaCheckbox',
    ) as HTMLInputElement | null;
    if (checkbox != null) {
      checkbox.checked = false;
    }

    this.totalPagesNum = 0
    this.sortByPriceAsc = true;
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

  onSubmit(){
    this.packFilters(undefined);

    this.propertyService.getFilteredProperties(this.filtersFromPage, this, this.cbSuccessProperty);
  }


  //callbacks
  cbSuccessProperty(self:any, properties? : Property[] , propertiesNumber? : number){
    if(properties){
      console.log("Success fetching properties");
      self.properties = properties;
      console.log("properties set");
      //fokus na pocetak nekretnina

      document.getElementById('prikazSvihNekretnina')?.scrollIntoView({
        behavior: "smooth",
        block: "start",
        inline: "nearest"
      })
    }

    self.currentPage = 1;
    self.totalPropertiesNum = propertiesNumber;
    self.totalPagesNum = Math.ceil(self.totalPropertiesNum / self.propertiesPerPage);
    self.counterPages();


    console.log(`After fetching properties, total pages is ${self.totalPagesNum}, properties per page is ${self.propertiesPerPage}, and total properties is ${self.totalPropertiesNum}.`);
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
