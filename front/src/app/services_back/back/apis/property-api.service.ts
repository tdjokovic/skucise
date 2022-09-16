import { HttpClient, HttpParams, HttpResponse } from "@angular/common/http";
import { Injectable } from "@angular/core";
import { Observable } from "rxjs";
import { apiProperties } from "../../constants/api.properities";
import { HeaderUtil } from "../../helpers/http_helper";
import { Buyer, Filters, Likes, NewProperty, NewSeller, PagedProperty, Property, PropertyComment, Rating, Seller } from "../types/interfaces";

@Injectable({
    providedIn: 'root'
})

export class PropertyApiService{
    private url : string = apiProperties.url + '/api/properties'

    constructor(private http : HttpClient){}

    getProperties(filters : Filters) : Observable<HttpResponse<PagedProperty>>{
        let par : HttpParams = new HttpParams({
            encoder: {encodeKey: k=>k,encodeValue:v=>encodeURIComponent(v),
            decodeKey:k=>k, decodeValue: v=>decodeURIComponent(v)}
        });

        let key : keyof typeof filters;

        for(key in filters){
            let x = filters[key];
            if(x != undefined){
                par = par.set(key, (typeof x == typeof []) ? (x as number[]).join(',') : (x as string | number | boolean));
            }
        }

        console.log("parameter ", par.toString());

        return this.http.get<PagedProperty>(
            this.url,
            {
                observe:'response',
                headers: HeaderUtil.jwtOnlyHeaders(),
                params:par
            }
        )
    }

    getProperty(id : number) : Observable<HttpResponse<Property>>{
        return this.http.get<Property>(
            this.url + `/${id}`,
            {
                observe: 'response',
                headers: HeaderUtil.jwtOnlyHeaders()
            }
        )
    }
    
    getPropertyApplicants(id : number) : Observable<HttpResponse<Buyer[]>>{
        return this.http.get<Buyer[]>(
            this.url + `/${id}/applicants`,
            {
                observe: 'response',
                headers: HeaderUtil.jwtOnlyHeaders()
            }
        )
    }

    createProperty(propertyData : Property) : Observable<HttpResponse<null>>{
        return this.http.post<null>(
            this.url,
            propertyData,
            {
                observe: 'response',
                headers: HeaderUtil.jwtOnlyHeaders()
            }
        )
    }

    applyToProperty(id : number) : Observable<HttpResponse<null>>{
        return this.http.post<null>(
            this.url + `/${id}/applicants`,
            {},
            {
                observe: 'response',
                headers: HeaderUtil.jwtOnlyHeaders()
            }
        )
    }

    deleteProperty(id : number) : Observable<HttpResponse<null>>{
        return this.http.delete<null>(
            this.url + `/${id}`,
            {
                observe: 'response',
                headers: HeaderUtil.jwtOnlyHeaders()
            }
        )
    }

    //comments

    getPropertyComments(id : number) : Observable<HttpResponse<PropertyComment[]>>{
        return this.http.get<PropertyComment[]>(
            this.url + `/${id}/comments`,
            {
                observe: 'response',
                headers: HeaderUtil.jwtOnlyHeaders()
            }
        )
    }

    postNewPropertyComment(id:number, comment:PropertyComment) : Observable<HttpResponse<null>>{
        return this.http.post<null>(
            this.url + `/${id}/comments`,
            comment,
            {
                observe: 'response',
                headers: HeaderUtil.jwtOnlyHeaders()
            }
        )
    }

    deletePropertyComment(propertyId : number, commentId:number) : Observable<HttpResponse<null>>{
        return this.http.delete<null>(
            this.url + `/${propertyId}/comments/${commentId}`,
            {
                observe: 'response',
                headers: HeaderUtil.jwtOnlyHeaders()
            }
        )
    }

    //likes

    getPropertyLikes(propertyId:number) : Observable<HttpResponse<Likes>>{
        return this.http.get<Likes>(
            this.url + `/${propertyId}/likes`,
            {
                observe: 'response',
                headers: HeaderUtil.jwtOnlyHeaders()
            }
        )
    }

    likeProperty(propertyId : number) : Observable<HttpResponse<null>>{
        return this.http.post<null>(
            this.url + `/${propertyId}/likes`,
            {},
            {
                observe: 'response',
                headers: HeaderUtil.jwtOnlyHeaders()
            }
        )
    }

    deletePropertyLike(propertyId : number) : Observable<HttpResponse<null>>{
        return this.http.delete<null>(
            this.url + `/${propertyId}/likes`,
            {
                observe: 'response',
                headers: HeaderUtil.jwtOnlyHeaders()
            }
        )
    }

}