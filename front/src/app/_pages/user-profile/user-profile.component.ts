import { Component, Injectable, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { UserRoles } from 'src/app/services_back/back/types/enums';
import { Buyer, NewUserData, Seller } from 'src/app/services_back/back/types/interfaces';
import { JWTUtil } from 'src/app/services_back/helpers/jwt_helper';
import { AuthorizeService } from 'src/app/services_back/services/authorize.service';
import { BuyerService } from 'src/app/services_back/services/buyer.service';
import { SellerService } from 'src/app/services_back/services/seller.service';
import { IndividualConfig } from 'ngx-toastr';
import { ToastrService } from 'ngx-toastr';


@Component({
  selector: 'app-user-profile',
  templateUrl: './user-profile.component.html',
  styleUrls: ['./user-profile.component.css']
})
export class UserProfileComponent implements OnInit {

  pageLoaded : boolean = false;
  userType:string = '';

  public firstName : string = '';
  public lastName : string = '';
  public email : string = '';
  public phoneNumber : string = '';
  public picture : string = '';

  //patterns
  pattName: RegExp = /^[a-zA-ZšŠđĐčČćĆžŽ]+([ \-][a-zA-ZšŠđĐčČćĆžŽ]+)*$/;
  pattTwoSpaces: RegExp = /  /;
  pattEmail: RegExp = /^[a-zA-Z0-9]+([\.\-\+][a-zA-Z0-9]+)*\@([a-zA-Z\-0-9]+\.)+[a-zA-Z]{2,}$/;
  pattPhone: RegExp = /^(0|(\+[1-9][0-9]{0,2}))[1-9][0-9][0-9]{6,7}$/;
  pattPassword: RegExp = /.{6,30}$/;

  public wrongNewFirstName : boolean = false;
  public wrongNewLastName : boolean = false;
  public wrongNewEmail : boolean = false;
  public wrongNewPhoneNumber : boolean = false;
  public wrongNewPicture : boolean = false;

  public id : number = 0;
  public user : Seller | Buyer | null = null;


  constructor(private authorizationService : AuthorizeService,
              private activatedRoute : ActivatedRoute,
              private sellerService : SellerService,
              private buyerService : BuyerService,
              private toastr:ToastrService) 
  {   }

    //univerzalne fje
    isName(input: string) : boolean{
      if(this.pattName.test(input) && !(this.pattTwoSpaces.test(input)) && (input.length >=1 && input.length <=30)){
        return true;
      }
      return false;
    }
  
    isCorrectMail(input:string):boolean{
      if(this.pattEmail.test(input.toLowerCase()) && input.length <= 320){
        return true;
      }
      return false;
    }
  
    isCorrectPhoneNumber(input:string):boolean{
      if (this.pattPhone.test(input))
        return true;
      return false;
    }
  
    isCorrectPassword(input:string):boolean{
      if (this.pattPassword.test(input))
        return true;
      return false;
    }

    //validation
    firstNameValidation(){
      if(this.isName(this.firstName)){
        this.wrongNewFirstName = false;
        return;
      }
      (<HTMLSelectElement>document.getElementById('firstName')).focus();
      this.wrongNewFirstName = true;
    }
  
    lastNameValidation(){
      if(this.isName(this.lastName)){
        this.wrongNewLastName = false;
        return;
      }
      (<HTMLSelectElement>document.getElementById('lastName')).focus();
      this.wrongNewLastName = true;
    }
  
    emailValidation(){
      if(this.isCorrectMail(this.email)){
        this.wrongNewEmail = false;
        return;
      }
      (<HTMLSelectElement>document.getElementById('email')).focus();
      this.wrongNewEmail = true;
    }
  
    phoneValidation(){
      if(this.isCorrectPhoneNumber(this.phoneNumber)){
        this.wrongNewPhoneNumber = false;
        return;
      }
      (<HTMLSelectElement>document.getElementById('phoneNumber')).focus();
      this.wrongNewPhoneNumber = true;
    }

    /*
    pictureValidation(){
      let reader = new FileReader();
      let inputPicture = document.getElementById("picture") as HTMLInputElement;
  
      if(inputPicture == null || inputPicture.files == null || inputPicture.files.length == 0){
        console.log("Slika nije izabrana kako treba, ne postoji!");
        return;
      }
  
      let self = this;
  
      reader.readAsDataURL(inputPicture.files[0]);
      reader.onload = function(){
        self.sPicture = reader.result as string;
        let len = self.sPicture.length;
        console.log(self.sPicture);
  
        if(len < 5000 || len > 65000) // duzina tj velicina slike nije dobra
        {
          (<HTMLSelectElement>document.getElementById('sellerPicture')).focus();
          self.sWrongPicture = true;
          return;
        }
  
        //sve je ok
        self.sWrongPicture = false;
      }
    } */

  ngOnInit(): void {
    this.checkAccess();
    this.id = this.activatedRoute.snapshot.paramMap.get("id") as unknown as number;
    this.fetchUserData();
  }

  fetchUserData(){
    this.sellerService.getSeller(this.id, this, this.cbFoundSeller, this.cbNotFound);
    this.buyerService.getBuyer(this.id, this, this.cbFoundBuyer, this.cbNotFound);
  }

  checkAccess(){
    this.authorizationService.checkAccess(this.activatedRoute, this, 
      (self: any) =>{
        self.pageLoaded = true;
      }
    )
  }


  isMe(){
    
    if(this.id == JWTUtil.getID()){
      return true;
    }

    return false;
  }

  fillData(){
    if (this.user){
      console.log("User data is being filled ");
      this.firstName = this.user.firstName;
      this.lastName = this.user.lastName;
      this.email = this.user.email;
      this.phoneNumber = this.user.phoneNumber;
      this.picture = (this.user.picture)?this.user.picture:'';
    }
  }

  changeData(){
    this.firstName = this.firstName.trim();
    this.lastName = this.lastName.trim();
    this.email = this.email.trim();
    this.phoneNumber = this.phoneNumber.trim();
    this.firstNameValidation();
    this.lastNameValidation();
    this.phoneValidation();
    this.emailValidation();

    //sada predajemo nove ako nema greske
    if(!(this.wrongNewFirstName || this.wrongNewLastName || this.wrongNewEmail || this.wrongNewPhoneNumber)){

        //nije doslo ni do jedne greske

        let newUser : NewUserData = {
          firstName : this.firstName,
          lastName : this.lastName,
          email : this.email,
          phoneNumber : this.phoneNumber
        }
        //azuriraj podatke!!!!!!!!!!!!!!!!!!!!!!!!
        if(JWTUtil.getRole() == UserRoles.Reg_seller){
          this.sellerService.editSellerData(this.id, newUser, this, this.cbSuccessEditedData);
        }
        else if (JWTUtil.getRole() == UserRoles.Reg_buyer){
          this.buyerService.editBuyerData(this.id, newUser, this, this.cbSuccessEditedData);
        }
        
      }
  }

  cbSuccessEditedData(self:any){
    console.log("SUCCESSFULLY EDITED DATA");
    self.toastr.success("You've successfully edited your profile data","User data editing!");
  }

  cbNotEditedData(self:any){
    console.error("ERROR EDITING DATA");
    self.toastr.error("Profile data is not edited! There has been an error","User data editing!");
  }
  


  cbFoundSeller(self: any, user: Seller){
    if(user){
      console.log("found user ", user.firstName);
      self.user = user;
      self.userType = 'seller';
      //podesi podatke koji su pronadjeni
      self.fillData();
    }
  }

  cbFoundBuyer(self: any, user: Buyer){
    if(user){
      console.log("found user ", user.firstName);
      self.user = user;
      self.userType = 'buyer';
      //podesi podatke koji su pronadjeni
      self.fillData();
    }
  }

  cbNotFound(self:any){
    console.error("User is not found!");
    self.userType = '';
  }



}
