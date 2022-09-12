import { HttpClient, HttpResponse } from "@angular/common/http";
import { Injectable } from "@angular/core";
import { Observable } from "rxjs";
import { apiProperties } from "../../constants/api.properities";
import { City } from "../types/interfaces";
import { HeaderUtil } from "../../helpers/http_helper";

@Injectable({
    providedIn: 'root'
})

export class CityApiService{
    private url : string = apiProperties.url + '/api/cities'

    constructor(private http : HttpClient) {   }

    getCities() : Observable<HttpResponse<City[]>> {
        return this.http.get<City[]>(
            this.url,
            {
                observe: 'response',
                headers: HeaderUtil.jwtOnlyHeaders()
            }
        );
    }


    getCity(id : number) : Observable<HttpResponse<City>> {
        return this.http.get<City>(
            this.url + `/${id}`,
            {
                observe: 'response',
                headers: HeaderUtil.jwtOnlyHeaders()
            }
        );
    }

}