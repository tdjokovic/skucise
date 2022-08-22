import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';
import { FormsModule } from '@angular/forms';
import { HttpClientModule } from '@angular/common/http';
import { RouterModule } from '@angular/router';

import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { HomeComponent } from './home/home.component';
import { HeaderComponent } from './header/header.component';
import { FooterComponent } from './footer/footer.component';
import { SearchComponent } from './home/search/search.component';
import { CarouselComponent } from './home/carousel/carousel.component';
import { CategoriesComponent } from './home/categories/categories.component';
import { TestimonialsComponent } from './home/testimonials/testimonials.component';
import { OurstatsComponent } from './home/ourstats/ourstats.component';
import { OuragentsComponent } from './home/ouragents/ouragents.component';
import { ContactComponent } from './contact/contact.component';
import { ContactbgComponent } from './contact/contactbg/contactbg.component';
import { ContactformComponent } from './contact/contactform/contactform.component';
import { PropertiesComponent } from './properties/properties.component';
import { PropertybgComponent } from './properties/propertybg/propertybg.component';
import { PropertiesgridComponent } from './properties/propertiesgrid/propertiesgrid.component';
import { PropertyComponent } from './properties/property/property.component';
import { TestimonyComponent } from './home/testimonials/testimony/testimony.component';
import { AboutusComponent } from './aboutus/aboutus.component';
import { LoginComponent } from './login/login.component';
import { LoginformComponent } from './login/loginform/loginform.component';
import { AlertComponent } from './_pages/alert/alert.component';
import { LogoutComponent } from './_pages/logout/logout.component';
import { SellersComponent } from './aboutus/sellers/sellers.component';
import { SellerCardComponent } from './aboutus/sellers/seller-card/seller-card.component';
import { SellerInfoComponent } from './_pages/seller-info/seller-info.component';
import { BuyerInfoComponent } from './_pages/buyer-info/buyer-info.component';
import { SellerInfoCardComponent } from './_pages/seller-info/seller-info-card/seller-info-card.component';
import { RegisterComponent } from './_pages/register/register.component';
import { RegisterFormComponent } from './_pages/register/register-form/register-form.component';

@NgModule({
  declarations: [
    AppComponent,
    HomeComponent,
    HeaderComponent,
    FooterComponent,
    SearchComponent,
    CarouselComponent,
    CategoriesComponent,
    TestimonialsComponent,
    OurstatsComponent,
    OuragentsComponent,
    ContactComponent,
    ContactbgComponent,
    ContactformComponent,
    PropertiesComponent,
    PropertybgComponent,
    PropertiesgridComponent,
    PropertyComponent,
    TestimonyComponent,
    AboutusComponent,
    LoginComponent,
    LoginformComponent,
    AlertComponent,
    LogoutComponent,
    SellersComponent,
    SellerCardComponent,
    SellerInfoComponent,
    BuyerInfoComponent,
    SellerInfoCardComponent,
    RegisterComponent,
    RegisterFormComponent
  ],
  imports: [
    BrowserModule,
    AppRoutingModule,
    FormsModule,
    RouterModule,
    HttpClientModule
  ],
  providers: [],
  bootstrap: [AppComponent]
})
export class AppModule { }
