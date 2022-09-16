import { Component, Input, OnInit } from '@angular/core';
import { Property } from 'src/app/services_back/back/types/interfaces';
import { DEFAULT_PROPERTY_PICTURE } from 'src/app/services_back/constants/raw-data';
import { PropertyService } from 'src/app/services_back/services/property.service';

@Component({
  selector: 'app-property',
  templateUrl: './property.component.html',
  styleUrls: ['./property.component.css']
})
export class PropertyComponent implements OnInit {

  @Input() property!  : Property ;
  @Input() additionalOptions : boolean = false;
  defPropertyPic : string = DEFAULT_PROPERTY_PICTURE;
  postingDate : Date | null = null;
  isDeleted : boolean = false;

  constructor(private propertyService : PropertyService) { }

  ngOnInit(): void {
    if (this.property)
      this.postingDate = new Date(this.property.postingDate);
  }

  deleteProperty()
  {
    if (this.additionalOptions)
    {
      this.propertyService.deleteProperty(this.property.id!,this,this.cbSuccess);
    }

  }
  
  cbSuccess(self? : any)
  {
    window.location.reload();

  }
}
