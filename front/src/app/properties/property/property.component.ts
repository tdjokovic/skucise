import { Component, Input, OnInit } from '@angular/core';
import { UserRoles } from 'src/app/services_back/back/types/enums';
import { Property } from 'src/app/services_back/back/types/interfaces';
import { DEFAULT_PROPERTY_PICTURE } from 'src/app/services_back/constants/raw-data';
import { JWTUtil } from 'src/app/services_back/helpers/jwt_helper';
import { PropertyService } from 'src/app/services_back/services/property.service';
import { Modal } from 'bootstrap';
import * as bootstrap from 'bootstrap';

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
  failedDeletion : boolean = false;
  deleteModal : Modal | undefined;

  constructor(private propertyService : PropertyService) { }

  ngOnInit(): void {
    if (this.property)
      this.postingDate = new Date(this.property.postingDate);
  }

  openDeleteModal()
  {
    this.deleteModal = new bootstrap.Modal(document.getElementById('deleteModal' + this.property.id)!,{
      keyboard:false
    });
    this.deleteModal.show();
  }
  deleteProperty()
  {
    if (this.additionalOptions)
    {
      this.propertyService.deleteProperty(this.property.id!,this,this.cbSuccess,this.cbFail);
    }

  }
  
  isAdmin(): boolean{
    return JWTUtil.getUserRole() == UserRoles.Admin;
  }

  cbSuccess(self? : any)
  {
    window.location.reload();

  }
  cbFail(self :any)
  {
    self.failedDeletion = true;
  }

  ispisiInfo()
  {
    console.log(this.property.id + " " + this.property.city.name + " " + this.property.price);
  }
}
