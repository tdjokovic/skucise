import { HttpClient, HttpResponse } from "@angular/common/http";
import { Injectable } from "@angular/core";
import { Observable } from "rxjs";
import { apiProperties } from "../../constants/api.properities";
import { AdCategory, AdType, City } from "../types/interfaces";
import { HeaderUtil } from "../../helpers/http_helper";

@Injectable({
    providedIn: 'root'
})

export class TypesApiService{
    private url : string = apiProperties.url + '/api/adtypes'

    constructor(private http : HttpClient) {   }

    getTypes() : Observable<HttpResponse<AdType[]>> {
        return this.http.get<AdType[]>(
            this.url,
            {
                observe: 'response',
                headers: HeaderUtil.jwtOnlyHeaders()
            }
        );
    }

}