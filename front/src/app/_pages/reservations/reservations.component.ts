import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Reservation } from 'src/app/services_back/back/types/interfaces';
import { AuthorizeService } from 'src/app/services_back/services/authorize.service';
import { ReservationService } from 'src/app/services_back/services/reservation.service';

@Component({
  selector: 'app-reservations',
  templateUrl: './reservations.component.html',
  styleUrls: ['./reservations.component.css']
})
export class ReservationsComponent implements OnInit {

  reservationsByMe : Reservation[] = [];
  reservationsForMe : Reservation[] = [];
  activeReservationsByMe : boolean = true;
  activeReservationsForMe : boolean = false;
  isActive : string = "byMe";

  constructor(private activatedRoute : ActivatedRoute,
    private authorizationService : AuthorizeService,
    private reservationService:ReservationService) { }


  ngOnInit(): void {
    this.authorizationService.checkAccess(this.activatedRoute, this, 
      (self: any) =>{
        self.pageLoaded = true;
        
        
        this.reservationService.getReservationsByUser(this,this.cbSuccessReservationsByUser);
        
      }
    )

  }

  showReservationsByMe() {
    this.isActive = "byMe";
    this.activeReservationsForMe = false;
    this.activeReservationsByMe = true;
    
  }
  
  showReservationsForMe(){
    this.isActive = "forMe";
    this.activeReservationsByMe = false;
    this.activeReservationsForMe = true;
    
  }
  cbSuccessReservationsByUser(self: any, reservations : any)
  {
    console.log(reservations)
    self.reservationsByMe = reservations;
    console.log(self.reservationsByMe);
    console.log(self.reservationsByMe.length)
  }

}
