import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';

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
    TestimonyComponent
  ],
  imports: [
    BrowserModule,
    AppRoutingModule
  ],
  providers: [],
  bootstrap: [AppComponent]
})
export class AppModule { }
