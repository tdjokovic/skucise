import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Property } from '../services_back/back/types/interfaces';
import { AuthorizeService } from '../services_back/services/authorize.service';

@Component({
  selector: 'app-properties',
  templateUrl: './properties.component.html',
  styleUrls: ['./properties.component.css']
})
export class PropertiesComponent implements OnInit {

  public adCategory : string | null = null;
  public properites : Property | null = null;

  constructor(private authorizationService : AuthorizeService,
    private activatedRoute : ActivatedRoute) { }

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
        }
        else if(this.adCategory == 'prodaja'){
          //prodaja
        }
        else if(this.adCategory == 'izdavanje'){
          //izdavanje
        }
      } 
    )
  }

  //takodje da se razdvoji prikaz ako je nekretnina kupca, ako je visitor, i ako je admin



  //callbacks
  cbSuccess(self:any){

  }

  cbNotFound(){

  }

}
