import { Component, Input, OnInit } from '@angular/core';
import { Property } from 'src/app/services_back/back/types/interfaces';

@Component({
  selector: 'app-propertiesgrid',
  templateUrl: './propertiesgrid.component.html',
  styleUrls: ['./propertiesgrid.component.css']
})
export class PropertiesgridComponent implements OnInit {

  @Input() properties  : Property [] = [];
  
  constructor() { }

  ngOnInit(): void {
  }

}
