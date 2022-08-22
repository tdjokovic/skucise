import { Injectable } from "@angular/core";
import { CityApiService } from "../back/apis/city-api.service";
import { AuthorizeService } from "./authorize.service";

@Injectable({
    providedIn: 'root'
})

export class CityService{
    constructor(private api : CityApiService,
                private authorizationService : AuthorizeService){}
    
    getCities(self?: any, successCallback?: Function){
        this.api.getCities().subscribe(
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