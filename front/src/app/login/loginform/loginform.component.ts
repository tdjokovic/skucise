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
  
  ngOnInit(): void {
  }

  emailPattern : RegExp = /^[a-zA-Z0-9]+([\.\-\+][a-zA-Z0-9]+)*\@([a-zA-Z\-0-9]+\.)+[a-zA-Z]{2,}$/;
  onSubmit(){
    this.accessNotApproved = false;
    this.wrongCredentials = false;

    if(this.emailPattern.test(this.email)){
      //email koji je unet ima dobar pattern
      this.loginService.login(this.email,this.password, this, this.callbackSuccess, this.callbackWrongCredentials, this.callbackNotApproved);
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
    self.router.navigate(['']);  
  }

  callbackWrongCredentials(self:any){
    self.wrongCredentials = true;
  }

  callbackNotApproved(self:any){
    self.accessNotApproved = true;
  }

}
