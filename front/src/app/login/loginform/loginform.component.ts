import { Component, Input, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { LoginService } from 'src/app/services_back/services/login.service';

@Component({
  selector: 'app-loginform',
  templateUrl: './loginform.component.html',
  styleUrls: ['./loginform.component.css']
})
export class LoginformComponent implements OnInit {

  constructor(public loginService : LoginService, public router : Router) { }

  @Input() public email: string = '';
  @Input() public password: string = '';

  public wrongCredentials: boolean = false;
  public accessNotApproved: boolean = false;
  public passwordNotEntered: boolean = false;
  
  ngOnInit(): void {
  }

  emailPattern : RegExp = /^[a-zA-Z0-9]+([\.\-\+][a-zA-Z0-9]+)*\@([a-zA-Z\-0-9]+\.)+[a-zA-Z]{2,}$/;
  onSubmit(){
    this.accessNotApproved = false;
    this.wrongCredentials = false;
    this.passwordNotEntered = false;

    if(this.emailPattern.test(this.email)){
      //email koji je unet ima dobar pattern
      if (this.password.length == 0){
        this.passwordNotEntered = true;
      }
      else{
        //sve je ok , logujemo se 
        //alert("logujemo");
        this.loginService.login(this.email,this.password, this, this.callbackSuccess, this.callbackWrongCredentials, this.callbackNotApproved);
      }
    }
    else{
      //nisu dobri kredencijali tj email
      this.wrongCredentials = true;
    }
  }

  //callbacks
  callbackSuccess(self:any){
    self.wrongCredentials=false;
    self.accessNotApproved = false;
    self.passwordNotEntered = false;
    self.router.navigate(['']);  
  }

  callbackWrongCredentials(self:any){
    self.wrongCredentials = true;
  }

  callbackNotApproved(self:any){
    self.accessNotApproved = true;
  }

}

//tiksi1306@gmail.com
//tdjokovic - lozinka
//bd897771b7482f02c9430e072ec8d4ce27cd7265ab036fa5666755bdec9211e3bbfed6dda6a73bde7192f2d2c6d5d45d8d275853641bf7d8834fe7bfb2f2268e hesirano


//mika@gmail.com
//mikamikic
//62b2bfe5969be53b8cff215c0b9fd2a226fe676185239c35f3cdbf04866c4db273a5d00e5ba0505cedb285bd28bd48041d5d519f8daaa5aaecf63d3d08db4b1e

//pera@gmail.com
//peraperic
//6deba4ff5e1fc7285faa0c01433fec7574f854cc7878df6220f3565bc105da2013bffe5b6df608cdabadbab8eaa98f63857dcd774839ee6ae8132597c2788cd2