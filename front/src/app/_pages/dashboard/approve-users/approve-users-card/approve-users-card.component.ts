import { Component, EventEmitter, Input, OnInit, Output } from '@angular/core';
import { Buyer, RegistrationBrief, Seller } from 'src/app/services_back/back/types/interfaces';
import { DEFAULT_PROFILE_PICTURE } from 'src/app/services_back/constants/raw-data';

@Component({
  selector: 'app-approve-users-card',
  templateUrl: './approve-users-card.component.html',
  styleUrls: ['./approve-users-card.component.css']
})
export class ApproveUsersCardComponent implements OnInit {

  @Input() public buyerData: Buyer | null = null;
  @Input() public sellerData: Seller | null = null;

  @Output() public cardClick = new EventEmitter();

  public brief!: RegistrationBrief;

  public defaultPicture: string = DEFAULT_PROFILE_PICTURE;


  
  constructor() { }

  ngOnInit(): void {

    if(this.sellerData) {
      this.brief = {
        id: this.sellerData.id,
        name: this.sellerData.firstName + ' ' + this.sellerData.lastName,
        email: this.sellerData.email,
        pictureBase64: this.sellerData.picture
      }
    }
    else if (this.buyerData) {
      this.brief = {
        id: this.buyerData.id,
        name: this.buyerData.firstName + ' ' + this.buyerData.lastName,
        email: this.buyerData.email,
        pictureBase64: this.buyerData.picture
      }
    }

  }


  onClick(){
    this.cardClick.emit();
  }
}
