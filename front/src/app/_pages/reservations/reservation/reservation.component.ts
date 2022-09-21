import { Component, Input, OnInit } from '@angular/core';
import { Reservation } from 'src/app/services_back/back/types/interfaces';
import { DEFAULT_PROPERTY_PICTURE } from 'src/app/services_back/constants/raw-data';
import { DatePipe } from '@angular/common';
import { ReservationService } from 'src/app/services_back/services/reservation.service';
import { Modal } from 'bootstrap';
import * as bootstrap from 'bootstrap';

@Component({
  selector: 'app-reservation',
  templateUrl: './reservation.component.html',
  styleUrls: ['./reservation.component.css']
})
export class ReservationComponent implements OnInit {

  @Input() reservation! : Reservation;
  @Input() byUser! : boolean;
  @Input() older! : boolean;
  @Input() accepted! : boolean;
  
  testModal : Modal | undefined;
  reservationChange! : Reservation;
  public defaultPicture: string = DEFAULT_PROPERTY_PICTURE;
  acceptFlag : boolean = false;
  alertSuccessIndicatior : boolean = false;
  alertFailIndicatior : boolean = false;

  constructor(private datePipe : DatePipe,
              private reservationService : ReservationService) { }

  ngOnInit(): void {
  }



  openModal(flag : boolean)
  {
    this.acceptFlag = flag;
    this.testModal = new bootstrap.Modal(document.getElementById('confirmModal'+ this.reservation.id)!, {
      keyboard:false
    });
    this.testModal.show();
    
    //console.log(this.acceptFlag);
  }

  acceptReservation()
  {
    if (this.reservation.id)
      this.reservationService.approveReservation(this.reservation.id, true, this,this.cbSuccessAccept,this.cbFailAccept);
  }
  rejectReservation()
  {
    if (this.reservation.id)
      this.reservationService.approveReservation(this.reservation.id, false, this,this.cbSuccessReject,this.cbFailReject);
  }
  cbSuccessAccept(self: any)
  {
    self.alertSuccessIndicatior = true;
  }
  cbFailAccept(self: any)
  {
    self.alertFailIndicatior = true;
  }
  cbSuccessReject(self: any)
  {
    self.alertSuccessIndicatior = true;
  }
  cbFailReject(self: any)
  {
    self.alertFailIndicatior = true;
  }

}
