import { Component, Input, OnInit } from '@angular/core';
import { Reservation } from 'src/app/services_back/back/types/interfaces';
import { DEFAULT_PROPERTY_PICTURE } from 'src/app/services_back/constants/raw-data';
import { DatePipe } from '@angular/common';

@Component({
  selector: 'app-reservation',
  templateUrl: './reservation.component.html',
  styleUrls: ['./reservation.component.css']
})
export class ReservationComponent implements OnInit {

  @Input() reservation! : Reservation;
  public defaultPicture: string = DEFAULT_PROPERTY_PICTURE;

  constructor(private datePipe : DatePipe) { }

  ngOnInit(): void {
    if (this.reservation)
    console.log(this.datePipe.transform(this.reservation.date,'MM/dd/yyyy'));
  }

}
