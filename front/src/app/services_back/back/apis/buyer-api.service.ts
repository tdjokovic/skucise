import { HttpClient, HttpParams, HttpResponse } from "@angular/common/http";
import { Injectable } from "@angular/core";
import { Observable } from "rxjs";
import { apiProperties } from "../../constants/api.properities";
import { HeaderUtil } from "../../helpers/http_helper";
import { Buyer, NewBuyer, NewUserData, Property } from "../types/interfaces";

@Injectable({
    providedIn: 'root'
})

export class BuyerApiService{
    private url : string = apiProperties.url + '/api/buyers'

    constructor(private http : HttpClient) {   }

    getBuyers(notApprovedRequested : boolean) : Observable<HttpResponse<Buyer[]>>{
        let par : HttpParams = new HttpParams();
        par = par.set('notApprovedRequested', notApprovedRequested);

        return this.http.get<Buyer[]>(
            this.url,
            {
                observe:'response',
                headers: HeaderUtil.jwtOnlyHeaders(),
                params:par
            }
        );
    }

    getBuyer(id: number) : Observable<HttpResponse<Buyer>>{
        
        return this.http.get<Buyer>(
            this.url + `/${id}`,
            {
                observe:'response',
                headers: HeaderUtil.jwtOnlyHeaders()
            }
        );
    }

    getBuyersProperties(id: number) : Observable<HttpResponse<Property[]>>{
        
        return this.http.get<Property[]>(
            this.url + `/${id}/properties`,
            {
                observe:'response',
                headers: HeaderUtil.jwtOnlyHeaders()
            }
        );
    }

    createBuyer(buyerData: NewBuyer) : Observable<HttpResponse<null>>{
        return this.http.post<null>(
            this.url,
            buyerData,
            {
                observe:'response',
                headers: HeaderUtil.jwtOnlyHeaders()
            }
        );
    }

    deleteBuyer(id: number) : Observable<HttpResponse<null>>{
        
        return this.http.delete<null>(
            this.url + `/${id}`,
            {
                observe:'response',
                headers: HeaderUtil.jwtOnlyHeaders()
            }
        );
    }

    approveBuyer(id: number) : Observable<HttpResponse<null>>{
        
        return this.http.put<null>(
            this.url + `/${id}`,
            {},
            {
                observe:'response',
                headers: HeaderUtil.jwtOnlyHeaders()
            }
        );
    }

    editBuyerData(id: number, data : NewUserData): Observable<HttpResponse<null>>{
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