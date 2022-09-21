import { HttpErrorResponse, HttpStatusCode } from "@angular/common/http";
import { Injectable } from "@angular/core";
import { BuyerApiService } from "../back/apis/buyer-api.service";
import { NewBuyer, Property } from "../back/types/interfaces";
import { AuthorizeService } from "./authorize.service";

@Injectable({
    providedIn: 'root'
})

export class BuyerService{

    constructor(private api : BuyerApiService, private authorizationService : AuthorizeService){}

    getBuyers(notApproved?: boolean , self?: any, successCallback?: Function){
        this.api.getBuyers((notApproved == undefined) ? false : notApproved).subscribe(

            //success
            (response) => {
                if(response.body){
                    if(self && successCallback){
                        successCallback(self, response.body);
                    }
                }
            },

            //error
            (error: HttpErrorResponse) => {
                this.authorizationService.redirectIfSessionExpired(error.status);
            }
        );
    }

    getBuyer(id: number, self? : any, successCallback? : Function, notFoundCallback?: Function){
        this.api.getBuyer(id).subscribe(
            //success
            (response) => {
                if(response.body){
                    if(self && successCallback){
                        successCallback(self, response.body);
                    }
                }
            },

            //error
            (error : HttpErrorResponse) => {
                console.log("Blabla");
                this.authorizationService.redirectIfSessionExpired(error.status);

                if(error.status == HttpStatusCode.NotFound || error.status == HttpStatusCode.Forbidden){
                    if(self && notFoundCallback) notFoundCallback(self);
                }
            }
        )
    }

    createBuyer(buyerData : NewBuyer, self?: any, successCallback? : Function, conflictCallback?: Function){
        this.api.createBuyer(buyerData).subscribe(
            //success
            (response) => {
                console.log("New buyer created! "+ response.status);
                if(self && successCallback){
                    successCallback(self);
                }
            },
            //error
            (error: HttpErrorResponse)=>{
                this.authorizationService.redirectIfSessionExpired(error.status);

                if(error.status == HttpStatusCode.Conflict){
                    if(self && conflictCallback) conflictCallback(self);
                }
            }
        );
    }

    deleteBuyer(id: number, self?: any, successCallback?: Function){
        this.api.deleteBuyer(id).subscribe(
            //success
            (response) => {
                console.log("Buyer deleted! "+response.status);
                if(self && successCallback) successCallback(self);
            },
            (error : HttpErrorResponse) => {
                this.authorizationService.redirectIfSessionExpired(error.status);
            }
        );
    }

    approveBuyer(id: number, self?: any, successCallback?: Function){
        this.api.approveBuyer(id).subscribe(
            //success
            (response) => {
                console.log("Buyer approved! "+response.status);
                if(self && successCallback) successCallback(self);
            },
            (error : HttpErrorResponse) => {
                this.authorizationService.redirectIfSessionExpired(error.status);
            }
        );
    }

    getBuyersProperties(id: number, self?: any, successCallback?: Function) {
        this.api.getBuyersProperties(id).subscribe(
          // Success
          (response) => {
            if (response.body) {
              console.log('Got buyers Properties '+response.status);
              if (self && successCallback) { successCallback(self, response.body) };
            }
          },
    
          // Error
          (error: HttpErrorResponse) => {
            this.authorizationService.redirectIfSessionExpired(error.status);
          }
        );
    }

    changeToSeller(newProperty : Property, self? : any, successCallback? : Function, failCallback? : Function)
    {
        this.api.changeToSeller().subscribe(
            (response) => {
                console.log("Buyer changed to seller! " + response.status);
                if(self && successCallback) successCallback(self, newProperty);
            },
            (error : HttpErrorResponse) => {
                this.authorizationService.redirectIfSessionExpired(error.status);
                if (self && failCallback) failCallback(self);
            }
        );
    }
}