import { HttpErrorResponse } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { ReservationApiService } from '../back/apis/reservation-api.service';
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
                console.log("PROPERTIES");
                console.log(response.body);
                //dobili smo nekretnine
                //this.properties = response.body.properties;
                //this.totalProperties = response.body.totalProperties;

                if(self && cbSuccess) {
                    console.log("cbsuccess");
                    //cbSuccess(self,this.properties, this.totalProperties);
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
}
