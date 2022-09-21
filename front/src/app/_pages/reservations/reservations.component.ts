import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { UserRoles } from 'src/app/services_back/back/types/enums';
import { Reservation } from 'src/app/services_back/back/types/interfaces';
import { JWTUtil } from 'src/app/services_back/helpers/jwt_helper';
import { AuthorizeService } from 'src/app/services_back/services/authorize.service';
import { ReservationService } from 'src/app/services_back/services/reservation.service';
import { ViewportScroller } from '@angular/common';

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
  olderReservations : Reservation[] = [];
  activeReservationsByMe : boolean = true;
  activeReservationsForMe : boolean = false;
  openedReservationsForMe : boolean = true;
  openedOlderReservations : boolean = false;
  openedAcceptedReservations : boolean = false;
  isActive : string = "byMe";

  public currentPageRbm: number = 1;
  public currentPageRfm: number = 1;
  public totalRbmNum: number = 0
  public totalPagesNumRbm: number = 0 ;
  public totalPagesArrayRbm : number [] = [];
  public totalPagesNumRfm: number = 0 ;
  public totalPagesArrayRfm : number [] = [];
  public reservationsPerPage: number = 3;

  constructor(private activatedRoute : ActivatedRoute,
    private authorizationService : AuthorizeService,
    private reservationService:ReservationService,
    private viewportScroller: ViewportScroller) { }


  ngOnInit(): void {
    this.authorizationService.checkAccess(this.activatedRoute, this, 
      (self: any) =>{
        self.pageLoaded = true;
        
        
        self.reservationService.getReservationsByUser(self,self.cbSuccessReservationsByUser);
        self.reservationService.getReservationsForUser(JWTUtil.getID(),true,false,self,self.cbSuccessReservationsForUser);
        
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
  getOlderReservations()
  {
    this.openedReservationsForMe = false;
    this.openedAcceptedReservations = false;
    this.openedOlderReservations = true;
    
    this.reservationService.getReservationsForUser(JWTUtil.getID(),false,false,this,this.cbSuccessReservationsForUser);
  }
  getReservationsForMe()
  {
    this.openedOlderReservations = false;
    this.openedAcceptedReservations = false;
    this.openedReservationsForMe = true;
    
    this.reservationService.getReservationsForUser(JWTUtil.getID(),true,false,this,this.cbSuccessReservationsForUser);
  }

  getAcceptedReservations()
  {
    this.openedReservationsForMe = false;
    this.openedOlderReservations = false;
    this.openedAcceptedReservations = true;
    
    this.reservationService.getReservationsForUser(JWTUtil.getID(),true,true,this,this.cbSuccessReservationsForUser);

  }

  cbSuccessReservationsByUser(self: any, reservations : any)
  {
    self.reservationsByMe = reservations;

    self.rbmShow = reservations.slice(0, self.reservationsPerPage);

    self.totalRbmNum = reservations.length;
    self.totalPagesNumRbm = Math.ceil(self.totalRbmNum / self.reservationsPerPage);
    for(let i = 1; i<=self.totalPagesNumRbm ; i ++ ){
      self.totalPagesArrayRbm.push(i);
    }
  }

  cbSuccessReservationsForUser(self: any, reservations : any)
  {
    self.reservationsForMe = reservations;
    //console.log(self.reservationsForMe);

    self.rfmShow = reservations.slice(0, self.reservationsPerPage);

    self.totalRfmNum = reservations.length;
    self.totalPagesNumRfm = Math.ceil(self.totalRfmNum / self.reservationsPerPage);
    self.totalPagesArrayRfm = [];
    for(let i = 1; i<=self.totalPagesNumRfm ; i ++ ){
      self.totalPagesArrayRfm.push(i);
    }

    self.viewportScroller.scrollToAnchor('top_container');
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
      this.rfmShow = this.reservationsForMe.slice(startIndex,endIndex);
      this.currentPageRfm = p;
    }

    this.viewportScroller.scrollToAnchor('top_container');
  }

  isSeller(){
    return JWTUtil.getRole() == UserRoles.Reg_seller;
  }

}
