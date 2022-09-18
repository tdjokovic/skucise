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
  rbmShow : Reservation[] = [];
  reservationsForMe : Reservation[] = [];
  rfmShow : Reservation[] = [];
  activeReservationsByMe : boolean = true;
  activeReservationsForMe : boolean = false;
  isActive : string = "byMe";

  public currentPageRbm: number = 1;
  public currentPageRfm: number = 1;
  public totalRbmNum: number = 0
  public totalPagesNumRbm: number = 0 ;
  public totalPagesArrayRbm : number [] = [];
  public totalPagesNumRfm: number = 0 ;
  public totalPagesArrayRfm : number [] = [];
  public reservationsPerPage: number = 6;

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
    self.reservationsByMe = reservations;
    console.log(self.reservationsByMe);

    self.rbmShow = reservations.slice(0, self.reservationsPerPage);

    self.totalRbmNum = reservations.length;
    self.totalPagesNumRbm = Math.ceil(self.totalRbmNum / self.reservationsPerPage);
    for(let i = 1; i<=self.totalPagesNumRbm ; i ++ ){
      self.totalPagesArrayRbm.push(i);
    }

  }

  changePage(p:number, indicator : boolean){

    let startIndex = (p-1) * this.reservationsPerPage;
    let endIndex = startIndex + this.reservationsPerPage

    if (indicator)
    {
      this.rbmShow = this.reservationsByMe.slice(startIndex, endIndex);
      this.currentPageRbm = p;
    }
    else{
      this.rfmShow = this.reservationsForMe.splice(startIndex,endIndex);
      this.currentPageRbm = p;
    }
  }

}
