import { Component, OnInit } from '@angular/core';
import { Router, NavigationEnd } from '@angular/router'

@Component({
  selector: 'app-header',
  templateUrl: './header.component.html',
  styleUrls: ['./header.component.css']
})
export class HeaderComponent implements OnInit {

  constructor(private router:Router) {

   }

  public isActive: string = "";

  ngOnInit(): void {
    this.routeChanged();
  }

  routeChanged(){
    this.isActive = this.router.url;
  }



}
