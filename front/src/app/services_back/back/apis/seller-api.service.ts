import { HttpClient, HttpParams, HttpResponse } from "@angular/common/http";
import { Injectable } from "@angular/core";
import { Observable } from "rxjs";
import { apiProperties } from "../../constants/api.properities";
import { HeaderUtil } from "../../helpers/http_helper";
import { NewSeller, NewUserData, Property, Rating, Seller } from "../types/interfaces";

@Injectable({
    providedIn: 'root'
})

export class SellerApiService{
    private url : string = apiProperties.url + '/api/sellers'

    constructor(private http : HttpClient) {   }

    getSellers(notApprovedRequested : boolean) : Observable<HttpResponse<Seller[]>>{
        let par : HttpParams = new HttpParams();
        par = par.set('notApprovedRequested', notApprovedRequested);

        return this.http.get<Seller[]>(
            this.url,
            {
                observe:'response',
                headers: HeaderUtil.jwtOnlyHeaders(),
                params:par
            }
        );
    }

    getSeller(id: number) : Observable<HttpResponse<Seller>>{
        
        return this.http.get<Seller>(
            this.url + `/${id}`,
            {
                observe:'response',
                headers: HeaderUtil.jwtOnlyHeaders()
            }
        );
    }

    getSellersProperties(id: number) : Observable<HttpResponse<Property[]>>{
        
        return this.http.get<Property[]>(
            this.url + `/${id}/properties`,
            {
                observe:'response',
                headers: HeaderUtil.jwtOnlyHeaders()
            }
        );
    }

    createSeller(sellerData: NewSeller) : Observable<HttpResponse<null>>{
        
        return this.http.post<null>(
            this.url,
            sellerData,
            {
                observe:'response',
                headers: HeaderUtil.jwtOnlyHeaders()
            }
        );
    }

    deleteSeller(id: number) : Observable<HttpResponse<null>>{
        
        return this.http.delete<null>(
            this.url + `/${id}`,
            {
                observe:'response',
                headers: HeaderUtil.jwtOnlyHeaders()
            }
        );
    }

    approveSeller(id: number) : Observable<HttpResponse<null>>{
        
        return this.http.put<null>(
            this.url + `/${id}`,
            {},
            {
                observe:'response',
                headers: HeaderUtil.jwtOnlyHeaders()
            }
        );
    }

    getSellerRating(id: number) : Observable<HttpResponse<Rating>>{
        
        return this.http.get<Rating>(
            this.url + `/${id}/rating`,
            {
                observe:'response',
                headers: HeaderUtil.jwtOnlyHeaders()
            }
        );
    }

    rateSeller(id: number, rating : number) : Observable<HttpResponse<null>>{
        let par: HttpParams = new HttpParams();
        par = par.set('feedbackValue', rating);

        return this.http.post<null>(
            this.url + `/${id}/rating`,
            {},
            {
                observe:'response',
                headers: HeaderUtil.jwtOnlyHeaders(),
                params:par
            }
        );
    }

    editSellerData(id: number, data : NewUserData): Observable<HttpResponse<null>>{
        return this.http.post<null>(
            this.url + `/${id}/editData`,
            data,
            {
                observe:'response',
                headers: HeaderUtil.jwtOnlyHeaders()
            }
        );
    }


}