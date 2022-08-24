import { Component, Input, OnInit } from '@angular/core';
import { Property } from 'src/app/services_back/back/types/interfaces';

@Component({
  selector: 'app-property',
  templateUrl: './property.component.html',
  styleUrls: ['./property.component.css']
})
export class PropertyComponent implements OnInit {

  @Input() property!  : Property ;

  constructor() { }

  ngOnInit(): void {
  }

}
