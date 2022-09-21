import { HttpErrorResponse, HttpStatusCode } from "@angular/common/http";
import { Injectable } from "@angular/core";
import { SellerApiService } from "../back/apis/seller-api.service";
import { NewSeller, NewUserData } from "../back/types/interfaces";
import { AuthorizeService } from "./authorize.service";

@Injectable({
    providedIn: 'root'
})

export class SellerService{

    constructor(private api : SellerApiService, private authorizationService : AuthorizeService){}

    getSellers(notApproved?: boolean , self?: any, successCallback?: Function){
        this.api.getSellers((notApproved == undefined) ? false : notApproved).subscribe(
            
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

    getSeller(id: number, self? : any, successCallback? : Function, notFoundCallback?: Function){
        this.api.getSeller(id).subscribe(
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
                this.authorizationService.redirectIfSessionExpired(error.status);

                if(error.status == HttpStatusCode.NotFound){
                    if(self && notFoundCallback) notFoundCallback(self);
                }
            }
        )
    }

    createSeller(sellerData : NewSeller, self?: any, successCallback? : Function, conflictCallback?: Function){
        this.api.createSeller(sellerData).subscribe(
            //success
            (response) => {
                console.log("New seller created! "+ response.status);
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

    deleteSeller(id: number, self?: any, successCallback?: Function){
        this.api.deleteSeller(id).subscribe(
            //success
            (response) => {
                console.log("Seller deleted! "+response.status);
                if(self && successCallback) successCallback(self);
            },
            (error : HttpErrorResponse) => {
                this.authorizationService.redirectIfSessionExpired(error.status);
            }
        );
    }

    approveSeller(id: number, self?: any, successCallback?: Function){
        this.api.approveSeller(id).subscribe(
            //success
            (response) => {
                console.log("Seller approved! "+response.status);
                if(self && successCallback) successCallback(self);
            },
            (error : HttpErrorResponse) => {
                this.authorizationService.redirectIfSessionExpired(error.status);
            }
        );
    }

    getSellerRating(id: number, self?: any, successCallback?: Function){
        this.api.getSellerRating(id).subscribe(
            //success
            (response) => {
                if(self && successCallback) successCallback(self, response.body);
            },
            (error : HttpErrorResponse) => {
                this.authorizationService.redirectIfSessionExpired(error.status);
            }
        );
    }

    rateSeller(id: number, rating:number, self?: any, successCallback?: Function){
        this.api.rateSeller(id, rating).subscribe(
            //success
            (response) => {
                if(self && successCallback) successCallback(self);
            },
            (error : HttpErrorResponse) => {
                this.authorizationService.redirectIfSessionExpired(error.status);
            }
        );
    }

    getSellersProperties(id: number, self?: any, successCallback?: Function) {
        this.api.getSellersProperties(id).subscribe(
          // Success
          (response) => {
            if (response.body) {
              console.log('Got sellers Properties '+response.status);
              if (self && successCallback) { successCallback(self, response.body) };
            }
          },
    
          // Error
          (error: HttpErrorResponse) => {
            this.authorizationService.redirectIfSessionExpired(error.status);
          }
        );
      }

    editSellerData(id: number, data : NewUserData, self? : any, successCallback?: Function){
        this.api.editSellerData(id, data).subscribe(
            // Success
            (response) => {
              if (response.body) {
                console.log('Success editing data! '+response.status);
                if (self && successCallback) { successCallback(self) };
              }
            },
      
            // Error
            (error: HttpErrorResponse) => {
              this.authorizationService.redirectIfSessionExpired(error.status);
            }
          );
    }
}