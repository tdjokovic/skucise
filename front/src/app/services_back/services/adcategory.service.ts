import { Injectable } from "@angular/core";
import { AdCategoriesApiService } from "../back/apis/adcategories-api.service";
import { AuthorizeService } from "./authorize.service";

@Injectable({
    providedIn: 'root'
})

export class AdCategoryService{
    constructor(private api : AdCategoriesApiService,
                private authorizationService : AuthorizeService){}
    
    getCategories(self?: any, successCallback?: Function){
        this.api.getCategories().subscribe(
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