import { Component, OnInit } from '@angular/core';

@Component({
  selector: 'app-reservations',
  templateUrl: './reservations.component.html',
  styleUrls: ['./reservations.component.css']
})
export class ReservationsComponent implements OnInit {

  reservationsByMe : boolean = true;
  reservationsForMe : boolean = false;
  isActive : string = "byMe";

  constructor() { }


  ngOnInit(): void {
  }

  showReservationsByMe() {
    this.isActive = "byMe";
    this.reservationsForMe = false;
    this.reservationsByMe = true;
    
  }
  
  showReservationsForMe(){
    this.isActive = "forMe";
    this.reservationsByMe = false;
    this.reservationsForMe = true;
    
  }
}
