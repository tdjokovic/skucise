import { HttpClient, HttpParams, HttpResponse } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { apiProperties } from '../../constants/api.properities';
import { HeaderUtil } from '../../helpers/http_helper';
import { Reservation } from '../types/interfaces';

@Injectable({
  providedIn: 'root'
})
export class ReservationApiService {

  private url : string = apiProperties.url + '/api/reservations'

    constructor(private http : HttpClient){}

    getReservationsByUser() : Observable<HttpResponse<Reservation[]>>{
        return this.http.get<Reservation[]>(
            this.url,
            {
                observe:'response',
                headers: HeaderUtil.jwtOnlyHeaders()
            }
        )
    }

    getReservationsForUser(id : number, is_new: boolean, is_accepted : boolean) : Observable<HttpResponse<Reservation[]>>{
        return this.http.get<Reservation[]>(
            this.url + `/${id}/${is_new}/${is_accepted}`,
            {
                observe:'response',
                headers: HeaderUtil.jwtOnlyHeaders()
            }
        )
    }

    createReservation(reservation : Reservation) : Observable<HttpResponse<boolean>>{
        return this.http.post<boolean>(
            this.url,
            reservation,
            {
                observe: 'response',
                headers: HeaderUtil.jwtOnlyHeaders()
            }
        )
    }
    approveReservation(id : number, approved: boolean) : Observable<HttpResponse<null>>
    {
        return this.http.put<null>(
            this.url + `/${id}`,
            approved,
            {
                observe: 'response',
                headers: HeaderUtil.jwtOnlyHeaders()
            }
        )
    }

}
