import { HttpErrorResponse, HttpStatusCode } from "@angular/common/http";
import { Injectable } from "@angular/core";
import { PropertyApiService } from "../back/apis/property-api.service";
import { Buyer, Filters, NewProperty, Property, PropertyComment } from "../back/types/interfaces";
import { AuthorizeService } from "./authorize.service";

@Injectable({
    providedIn: 'root'
})

export class PropertyService{

    constructor(private api : PropertyApiService,
        private authService : AuthorizeService){}

    public properties : Property [] = [];
    public totalProperties : number = 0;

    public property! : Property;
    public propertyApplicants : Buyer [] = [];

    //property

    getFilteredProperties(filters: Filters, self?: any, cbSuccess?: Function){
        console.log("Getting filtered properties");
        this.api.getProperties(filters).subscribe(
            (response) => {
                if(response.body != null && response.body.properties != null){
                    console.log("PROPERTIES");
                    console.log(response.body);
                    console.log(response.body.properties);
                    //dobili smo nekretnine
                    this.properties = response.body.properties;
                    this.totalProperties = response.body.totalProperties;

                    if(self && cbSuccess) {
                        console.log("cbsuccess");
                        cbSuccess(self,this.properties, this.totalProperties);
                    }
                    else{
                        console.log("notcbsuccess");
                    }

                    console.log("Properties found");
                }
                else{
                    console.log("No properties found");
                }
            },
            (error : HttpErrorResponse) => {
                this.authService.redirectIfSessionExpired(error.status);
            }
        );
    }

    getProperties(self?: any, cbSuccess?: Function){
        let filters : Filters = {
            newConstruction:false,
            sellerId:0,
            cityId:0,
            adCategoryId:0, // koji je tip oglasa
            typeId: 0, //koji je tip nekretnine

            pageNumber:1,
            propertiesPerPage:6,
            ascendingOrder:false
        }
        console.log("Getting properties");
        this.getFilteredProperties(filters, self, cbSuccess);
    }

    getProperty(id: number, self?: any, cbSuccess?: Function, cbNotFound?:Function){
        this.api.getProperty(id).subscribe(
            (response) => {
                console.log("Property found!!!!!! :");
                console.log(response.body);

                if(self && cbSuccess) cbSuccess(self, response.body);
            },
            (error : HttpErrorResponse) => {
                this.authService.redirectIfSessionExpired(error.status);
                if(error.status == HttpStatusCode.NotFound){
                    if(self && cbNotFound) cbNotFound(self);
                }
            }
        );
    }

    getPropertyApplicants(id:number, self?: any, cbSuccess?: Function){
        this.api.getPropertyApplicants(id).subscribe(
            (response) => {
                //ako je pronadjena lista aplikanata, ako nije onda je prazna lista
                this.propertyApplicants = (response.body == null) ? [] :  response.body;

                if(response.body){
                    if(self && cbSuccess) cbSuccess(self);
                }

                console.log("Property applicants ");
                console.log(this.propertyApplicants);
            },
            (error : HttpErrorResponse) => {
                this.authService.redirectIfSessionExpired(error.status);
            }
        );
    }

    createProperty(propertyData : NewProperty, self?: any, cbSuccess?: Function){
        this.api.createProperty(propertyData).subscribe(
            (response) => {
                console.log("Property created ", response.status);

                if(self && cbSuccess) cbSuccess(self);
            },
            (error : HttpErrorResponse) => {
                this.authService.redirectIfSessionExpired(error.status);
            }
        );
    }

    applyForProperty(id : number, self?: any, cbSuccess?: Function){
        this.api.applyToProperty(id).subscribe(
            (response) => {
                console.log("Applied for property ",response.status);

                if(self && cbSuccess) cbSuccess(self);
            },
            (error : HttpErrorResponse) => {
                this.authService.redirectIfSessionExpired(error.status);
            }
        );
    }

    deleteProperty(id : number, self?: any, cbSuccess?: Function){
        this.api.deleteProperty(id).subscribe(
            (response) => {
                console.log("Property deleted ", response.status);

                if(self && cbSuccess) cbSuccess(self);
            },
            (error : HttpErrorResponse) => {
                this.authService.redirectIfSessionExpired(error.status);
            }
        );
    }


    //comments

    getPropertyComments(id : number, self?: any, cbSuccess?: Function){
        this.api.getPropertyComments(id).subscribe(
            (response) => {
                console.log("Comments fetched ");
                console.log(response.body);

                if(self && cbSuccess) cbSuccess(self);
            },
            (error : HttpErrorResponse) => {
                this.authService.redirectIfSessionExpired(error.status);
            }
        );
    }

    postNewPropertyComment(id : number,comment: PropertyComment, self?: any, cbSuccess?: Function){
        this.api.postNewPropertyComment(id, comment).subscribe(
            (response) => {
                console.log("Comment posted ", response.status);

                if(self && cbSuccess) cbSuccess(self);
            },
            (error : HttpErrorResponse) => {
                this.authService.redirectIfSessionExpired(error.status);
            }
        );
    }

    deletePropertyComment(propertyId : number, commentId : number, self?: any, cbSuccess?: Function){
        this.api.deletePropertyComment(propertyId, commentId).subscribe(
            (response) => {
                console.log("Comment deleted ", response.status);

                if(self && cbSuccess) cbSuccess(self);
            },
            (error : HttpErrorResponse) => {
                this.authService.redirectIfSessionExpired(error.status);
            }
        );
    }



    //likes

    getPropertyLikes(propertyId: number, self?: any, cbSuccess?: Function){
        this.api.getPropertyLikes(propertyId).subscribe(
            (response) => {
                console.log("Likes fetched ", response.status);

                if(self && cbSuccess) cbSuccess(self);
            },
            (error : HttpErrorResponse) => {
                this.authService.redirectIfSessionExpired(error.status);
            }
        );
    }

    likeProperty(propertyId : number, self?: any, cbSuccess?: Function){
        this.api.likeProperty(propertyId).subscribe(
            (response) => {
                console.log("Like posted ", response.status);

                if(self && cbSuccess) cbSuccess(self);
            },
            (error : HttpErrorResponse) => {
                this.authService.redirectIfSessionExpired(error.status);
            }
        );
    }

    deletePropertyLike(propertyId : number, self?: any, cbSuccess?: Function){
        this.api.deletePropertyLike(propertyId).subscribe(
            (response) => {
                console.log("Like retrieved ", response.status);

                if(self && cbSuccess) cbSuccess(self);
            },
            (error : HttpErrorResponse) => {
                this.authService.redirectIfSessionExpired(error.status);
            }
        );
    }
}