import { HttpClient, HttpResponse } from "@angular/common/http";
import { Injectable } from "@angular/core";
import { Observable } from "rxjs";
import { apiProperties } from "../../constants/api.properities";
import { AdCategory, City } from "../types/interfaces";
import { HeaderUtil } from "../../helpers/http_helper";

@Injectable({
    providedIn: 'root'
})

export class AdCategoriesApiService{
    private url : string = apiProperties.url + '/api/adcategories'

    constructor(private http : HttpClient) {   }

    getCategories() : Observable<HttpResponse<AdCategory[]>> {
        return this.http.get<AdCategory[]>(
            this.url,
            {
                observe: 'response',
                headers: HeaderUtil.jwtOnlyHeaders()
            }
        );
    }

}