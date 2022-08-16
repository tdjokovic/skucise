import { NgModule } from '@angular/core';
import { RouterModule, Routes, Router } from '@angular/router';
import { AboutusComponent } from './aboutus/aboutus.component';
import { ContactComponent } from './contact/contact.component';
import { HomeComponent } from './home/home.component';
import { PropertiesComponent } from './properties/properties.component';
import { HttpClient, HttpClientModule } from '@angular/common/http';
import { UserRoles } from './services_back/back/types/enums';
import { LoginComponent } from './login/login.component';
import { AlertComponent } from './_pages/alert/alert.component';
import { SessionexpiredComponent } from './_pages/sessionexpired/sessionexpired.component';


// data - Niz uloga koje imaju pristup ruti/stranici, prazan niz daje dozvolu svim ulogama  
const routes: Routes = [
  {path:'', component:HomeComponent,                 data: { allowedRoles: [] }},
  {path:'home', component:HomeComponent,             data: { allowedRoles: [] }},
  {path:'contactus', component:ContactComponent,     data: { allowedRoles: [] }},
  {path:'properties', component:PropertiesComponent, data: { allowedRoles: [] }},
  {path:'about', component:AboutusComponent,         data: { allowedRoles: [UserRoles.Admin] }},
  
  {path:'login', component:LoginComponent,           data: { allowedRoles: [UserRoles.Visitor]}}, //samo on moze da se loguje
  {path:'logout', component:LoginComponent,           data: { allowedRoles: [UserRoles.Admin, UserRoles.Reg_buyer, UserRoles.Reg_seller]}}, //treba da se kreira komponenta Logout

  {path:'alert', component:AlertComponent,          data: {allowedRoles: []}},
  {path:'session-expired', component:SessionexpiredComponent,          data: {allowedRoles: []}}
  
];

@NgModule({
  imports: [RouterModule.forRoot(routes), HttpClientModule],
  exports: [RouterModule]
})
export class AppRoutingModule { }
