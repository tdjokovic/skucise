import { Component, Input, OnInit } from '@angular/core';
import { Buyer } from 'src/app/services_back/back/types/interfaces';
import { DEFAULT_PROFILE_PICTURE } from 'src/app/services_back/constants/raw-data';

@Component({
  selector: 'app-buyer-info-card',
  templateUrl: './buyer-info-card.component.html',
  styleUrls: ['./buyer-info-card.component.css']
})
export class BuyerInfoCardComponent implements OnInit {

  @Input() public buyer : Buyer | null = null;

  public defaultPicture: string = DEFAULT_PROFILE_PICTURE;
  
  @Input() dissableDelete: boolean = false;
  
  constructor() { }

  ngOnInit(): void {
  }

}
