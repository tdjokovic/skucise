import { Injectable } from '@angular/core';
import { apiProperties } from '../../constants/api.properities';
import { HttpClient, HttpResponse, HttpStatusCode } from '@angular/common/http'
import { Observable } from 'rxjs';
import { HeaderUtil } from '../../helpers/http_helper';
import { Credentials } from '../types/interfaces';

@Injectable({
  providedIn: 'root'
})

export class LoginApiService {

  private url:string = apiProperties.url + '/api/login';


  constructor(private http:HttpClient) { }

  login(body:Credentials) : Observable<HttpResponse<null>>
  {
    let response = this.http.post<null>(
      this.url,
      body,
      {
        observe:'response',
        headers: HeaderUtil.jwtOnlyHeaders()
      }
    );

    response.subscribe();

    return response;
  }

}
