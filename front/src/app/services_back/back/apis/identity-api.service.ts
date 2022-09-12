import { HttpClient, HttpResponse } from "@angular/common/http";
import { Injectable } from "@angular/core";
import { Observable } from "rxjs";
import { apiProperties } from "../../constants/api.properities";
import { HeaderUtil } from "../../helpers/http_helper";

@Injectable({
    providedIn: 'root'
  })
  
export class IdentityApiService{
    private url: string = apiProperties.url + '/api/authorization';

    constructor(private http : HttpClient) {}

    getCurrent(): Observable<HttpResponse<null>>{
        return this.http.get<null>(
            this.url,
            {
                headers: HeaderUtil.jwtOnlyHeaders(),
                observe: 'response'
            }
        );
    }
}