import { Component, Input, OnInit } from '@angular/core';

@Component({
  selector: 'app-propertybg',
  templateUrl: './propertybg.component.html',
  styleUrls: ['./propertybg.component.css']
})
export class PropertybgComponent implements OnInit {

  @Input() adCategory : string | null = null;

  constructor() { }

  ngOnInit(): void {
  }

}
