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
import { LogoutComponent } from './_pages/logout/logout.component';
import { BuyerInfoComponent } from './_pages/buyer-info/buyer-info.component';
import { SellerInfoComponent } from './_pages/seller-info/seller-info.component';
import { RegisterComponent } from './_pages/register/register.component';
import { PropertySingleComponent } from './_pages/property-single/property-single.component';
import { DashboardComponent } from './_pages/dashboard/dashboard.component';


// data - Niz uloga koje imaju pristup ruti/stranici, prazan niz daje dozvolu svim ulogama  
const routes: Routes = [
  {path:'', component:HomeComponent,                 data: { allowedRoles: [] }},
  {path:'home', component:HomeComponent,             data: { allowedRoles: [] }},
  {path:'contactus', component:ContactComponent,     data: { allowedRoles: [] }},
  {path:'properties', component:PropertiesComponent, data: { allowedRoles: [] }},
  {path:'properties/:adCategory', component:PropertiesComponent, data: { allowedRoles: [] }},
  {path:'property/:id', component:PropertySingleComponent, data: { allowedRoles: [] }},
  {path:'about', component:AboutusComponent,         data: { allowedRoles: [] }},
  
  { path: 'buyer/:id', component: BuyerInfoComponent, data: { allowedRoles: [UserRoles.Admin, UserRoles.Reg_seller, UserRoles.Reg_buyer] }},
  //{ path: 'buyers', component: ApplicantsPageComponent,       data: { allowedRoles: [UserRoles.Admin] }},
  { path: 'seller/:id', component: SellerInfoComponent,   data: { allowedRoles: [] }},
  //{ path: 'sellers', component: EmployersPageComponent,         data: { allowedRoles: [] }},
  
  {path:'login', component:LoginComponent,           data: { allowedRoles: [UserRoles.Visitor]}}, //samo on moze da se loguje
  {path:'logout', component:LogoutComponent,           data: { allowedRoles: [UserRoles.Admin, UserRoles.Reg_buyer, UserRoles.Reg_seller]}}, //treba da se kreira komponenta Logout
  {path:'register', component:RegisterComponent,        data: { allowedRoles: [UserRoles.Visitor]}}, 

  { path: 'dashboard', component: DashboardComponent,   data: { allowedRoles: [UserRoles.Admin] }},

  { path: 'alert/:cause/:param', component: AlertComponent,   data: { allowedRoles: [] }},
  { path: 'alert/:cause', component: AlertComponent,          data: { allowedRoles: [] }},
  
];

@NgModule({
  imports: [RouterModule.forRoot(routes), HttpClientModule],
  exports: [RouterModule]
})
export class AppRoutingModule { }
