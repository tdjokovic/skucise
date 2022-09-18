import { HttpErrorResponse } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { ReservationApiService } from '../back/apis/reservation-api.service';
import { Reservation } from '../back/types/interfaces';
import { AuthorizeService } from './authorize.service';

@Injectable({
  providedIn: 'root'
})
export class ReservationService {

  constructor(private api : ReservationApiService,
              private authService : AuthorizeService) { }

  getReservationsByUser(self?: any, cbSuccess?: Function){
    console.log("Getting reservations by user");
    this.api.getReservationsByUser().subscribe(
        (response) => {
            if(response.body != null){
                console.log("RESEVATIONS");
                console.log(response.body);

                if(self && cbSuccess) {
                    console.log("cbsuccess");
                    cbSuccess(self,response.body);
                }
                else{
                    console.log("notcbsuccess");
                }

                console.log("Reservations found");
            }
            else{
                console.log("No reservations found");
            }
        },
        (error : HttpErrorResponse) => {
            this.authService.redirectIfSessionExpired(error.status);
        }
        );
    }

    createReservation(reservationData : Reservation, self?: any, cbSuccess?: Function){
        this.api.createReservation(reservationData).subscribe(
            (response) => {
                console.log("Reservation created ", response.status);

                if(self && cbSuccess) cbSuccess(self);
            },
            (error : HttpErrorResponse) => {
                this.authService.redirectIfSessionExpired(error.status);
            }
        );
    }
}