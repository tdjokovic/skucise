import { Component, Input, OnInit } from '@angular/core';
import { Seller } from 'src/app/services_back/back/types/interfaces';
import { DEFAULT_PROFILE_PICTURE } from 'src/app/services_back/constants/raw-data';

@Component({
  selector: 'app-seller-info-card',
  templateUrl: './seller-info-card.component.html',
  styleUrls: ['./seller-info-card.component.css']
})
export class SellerInfoCardComponent implements OnInit {

  @Input() public seller: Seller | null = null;
  @Input() public rating: number = -1;
  @Input() public rateDissabled: boolean = true;
  @Input() public dissableDelete: boolean = false;

  public myRating: number = 0;
  public showRateOption: boolean = false;
  alreadyRated: boolean = false;

  public defaultPicture: string = DEFAULT_PROFILE_PICTURE;

  constructor() { }

  ngOnInit(): void {
  }

}
