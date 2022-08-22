import { Component, Input, OnInit } from '@angular/core';
import { Seller } from 'src/app/services_back/back/types/interfaces';
import { DEFAULT_PROFILE_PICTURE } from 'src/app/services_back/constants/raw-data';

@Component({
  selector: 'app-seller-card',
  templateUrl: './seller-card.component.html',
  styleUrls: ['./seller-card.component.css']
})
export class SellerCardComponent implements OnInit {

  @Input() public data!: Seller;

  public defProfilePic : string = DEFAULT_PROFILE_PICTURE;

  constructor() { }

  ngOnInit(): void {
  }

}
