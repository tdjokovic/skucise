import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { AboutusComponent } from './aboutus/aboutus.component';
import { ContactComponent } from './contact/contact.component';
import { HomeComponent } from './home/home.component';
import { PropertiesComponent } from './properties/properties.component';

// data - Niz uloga koje imaju pristup ruti/stranici, prazan niz daje dozvolu svim ulogama  
const routes: Routes = [
  {path:'', component:HomeComponent,                 data: { allowedRoles: [] }},
  {path:'home', component:HomeComponent,             data: { allowedRoles: [] }},
  {path:'contactus', component:ContactComponent,     data: { allowedRoles: [] }},
  {path:'properties', component:PropertiesComponent, data: { allowedRoles: [] }},
  {path:'about', component:AboutusComponent,         data: { allowedRoles: [] }},
  
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
