import { Component, Input, OnInit } from '@angular/core';
import { Seller } from 'src/app/services_back/back/types/interfaces';

@Component({
  selector: 'app-seller-info-card',
  templateUrl: './seller-info-card.component.html',
  styleUrls: ['./seller-info-card.component.css']
})
export class SellerInfoCardComponent implements OnInit {

  @Input() public seller: Seller | null = null;

  constructor() { }

  ngOnInit(): void {
  }

}
