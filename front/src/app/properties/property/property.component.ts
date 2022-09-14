import { Component, Input, OnInit } from '@angular/core';
import { Property } from 'src/app/services_back/back/types/interfaces';
import { DEFAULT_PROPERTY_PICTURE } from 'src/app/services_back/constants/raw-data';

@Component({
  selector: 'app-property',
  templateUrl: './property.component.html',
  styleUrls: ['./property.component.css']
})
export class PropertyComponent implements OnInit {

  @Input() property!  : Property ;
  defPropertyPic : string = DEFAULT_PROPERTY_PICTURE;

  constructor() { }

  ngOnInit(): void {
  }

}
