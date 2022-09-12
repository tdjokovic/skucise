import { Injectable } from "@angular/core";
import { CityApiService } from "../back/apis/city-api.service";
import { TypesApiService } from "../back/apis/types-api.service";
import { AuthorizeService } from "./authorize.service";

@Injectable({
    providedIn: 'root'
})

export class AdTypesService{
    constructor(private api : TypesApiService,
                private authorizationService : AuthorizeService){}
    
    getAdTypes(self?: any, successCallback?: Function){
        this.api.getTypes().subscribe(
            //success
            (response) => {
                if(response.body){
                    if(self && successCallback){
                        successCallback(self, response.body);
                    }
                }
            },
            //error
            (error) => {
                this.authorizationService.redirectIfSessionExpired(error.status);
            }
        )
    }

}