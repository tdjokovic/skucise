import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { NewBuyer, NewSeller } from 'src/app/services_back/back/types/interfaces';
import { RedirectRoutes } from 'src/app/services_back/constants/routing.properties';
import { AlertPageUtil } from 'src/app/services_back/helpers/alert_helper';
import { PasswdHash } from 'src/app/services_back/helpers/hashing_helper';
import { AuthorizeService } from 'src/app/services_back/services/authorize.service';
import { BuyerService } from 'src/app/services_back/services/buyer.service';
import { SellerService } from 'src/app/services_back/services/seller.service';

@Component({
  selector: 'app-register-form',
  templateUrl: './register-form.component.html',
  styleUrls: ['./register-form.component.css']
})
export class RegisterFormComponent implements OnInit {

  constructor(private sellerService : SellerService,
    private buyerService : BuyerService,
    private router : Router,
    private authorizationService : AuthorizeService) { }

  showMeBuyerFormBool : boolean = true;
  showMeSellerFormBool : boolean = false;
  isActive : string = "buyer";

  //polja za kupca
  bFirstName : string = '';
  bLastName : string = '';
  bPhoneNumber : string = '';
  bEmail : string = '';
  bPasswordOne : string = '';
  bPasswordTwo : string = '';
  bPicture : string = '';
  //alert polja
  bWrongFirstName : boolean = false;
  bWrongLastName  : boolean = false;
  bWrongPhoneNumber : boolean = false;
  bWrongEmail : boolean = false;
  bWrongPasswordOne : boolean = false;
  bWrongPasswordTwo : boolean = false;
  bWrongPicture : boolean = false;


  //polja za prodavca
  sFirstName : string = '';
  sLastName : string = '';
  sPhoneNumber : string = '';
  sEmail : string = '';
  sPasswordOne : string = '';
  sPasswordTwo : string = '';
  sPicture : string = '';
  sPIB : string = '';
  //alert polja
  sWrongFirstName : boolean = false;
  sWrongLastName  : boolean = false;
  sWrongPhoneNumber : boolean = false;
  sWrongEmail : boolean = false;
  sWrongPasswordOne : boolean = false;
  sWrongPasswordTwo : boolean = false;
  sWrongPicture : boolean = false;
  sWrongPIB : boolean = false;


  //patterns
  pattName: RegExp = /^[a-zA-ZšŠđĐčČćĆžŽ]+([ \-][a-zA-ZšŠđĐčČćĆžŽ]+)*$/;
  pattTwoSpaces: RegExp = /  /;
  pattEmail: RegExp = /^[a-zA-Z0-9]+([\.\-\+][a-zA-Z0-9]+)*\@([a-zA-Z\-0-9]+\.)+[a-zA-Z]{2,}$/;
  pattPhone: RegExp = /^(0|(\+[1-9][0-9]{0,2}))[1-9][0-9][0-9]{6,7}$/;
  pattPassword: RegExp = /.{6,30}$/;
  pattPIB: RegExp = /^[0-9]{9}$/;


  ngOnInit(): void {
  }

  //switch za kupca i prodavca

  showMeBuyerForm(){
    if(!this.showMeBuyerFormBool){
      this.showMeBuyerFormBool =  true;
      this.showMeSellerFormBool = false;

      this.isActive = "buyer";
    }
  }

  
  showMeSellerForm(){
    if(!this.showMeSellerFormBool){
      this.showMeSellerFormBool =  true;
      this.showMeBuyerFormBool = false;

      this.isActive = "seller";
    }
  }

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

  isCorrectPIB(input:string):boolean{
    if (this.pattPIB.test(input))
      return true;
    return false;
  }

  
  //SELLER

  sellerFirstNameValidation(){
    if(this.isName(this.sFirstName)){
      this.sWrongFirstName = false;
      return;
    }
    (<HTMLSelectElement>document.getElementById('sellerFirstName')).focus();
    this.sWrongFirstName = true;
  }

  sellerLastNameValidation(){
    if(this.isName(this.sLastName)){
      this.sWrongLastName = false;
      return;
    }
    (<HTMLSelectElement>document.getElementById('sellerLasttName')).focus();
    this.sWrongLastName = true;
  }

  sellerEmailValidation(){
    if(this.isCorrectMail(this.sEmail)){
      this.sWrongEmail = false;
      return;
    }
    (<HTMLSelectElement>document.getElementById('sellerEmail')).focus();
    this.sWrongEmail = true;
  }

  sellerPhoneValidation(){
    if(this.isCorrectPhoneNumber(this.sPhoneNumber)){
      this.sWrongPhoneNumber = false;
      return;
    }
    (<HTMLSelectElement>document.getElementById('sellerPhoneNumber')).focus();
    this.sWrongPhoneNumber = true;
  }

  sellerPIBValidation(){
    if(this.isCorrectPIB(this.sPIB)){
      this.sWrongPIB = false;
      return;
    }
    (<HTMLSelectElement>document.getElementById('sellerPIB')).focus();
    this.sWrongPIB = true;
  }

  sellerPasswordValidation(){
    if (this.isCorrectPassword(this.sPasswordOne) && this.isCorrectPassword(this.sPasswordTwo) && this.sPasswordOne == this.sPasswordTwo) {
      this.sWrongPasswordOne = false;
      this.sWrongPasswordTwo = false;
      return;
    }
    if(!this.isCorrectPassword(this.sPasswordOne)){
      this.sPasswordOne = ''; //brisi obe ukucane lozinke
      this.sPasswordTwo = '';
      (<HTMLSelectElement>document.getElementById('sellerPasswordOne')).focus(); 
      this.sWrongPasswordOne = true;
    }
    else if(this.sPasswordOne != this.sPasswordTwo){
      this.sPasswordTwo = '';
      (<HTMLSelectElement>document.getElementById('sellerPasswordTwo')).focus(); 
      this.sWrongPasswordTwo = true;
    }
  }



  sellerPictureValidation(){
    let reader = new FileReader();
    let inputPicture = document.getElementById("sellerPicture") as HTMLInputElement;

    if(inputPicture == null || inputPicture.files == null || inputPicture.files.length == 0){
      console.log("Slika nije izabrana kako treba, ne postoji!");
      return;
    }

    let self = this;

    reader.readAsDataURL(inputPicture.files[0]);
    reader.onload = function(){
      self.sPicture = reader.result as string;
      let len = self.sPicture.length;

      if(len < 5000 || len > 65000) // duzina tj velicina slike nije dobra
      {
        (<HTMLSelectElement>document.getElementById('sellerPicture')).focus();
        self.sWrongPicture = true;
        return;
      }

      //sve je ok
      self.sWrongPicture = false;
    }
  }



  sellerValidation(){
    this.sFirstName = this.sFirstName.trim();
    this.sLastName = this.sLastName.trim();
    this.sEmail = this.sEmail.trim();
    this.sPhoneNumber = this.sPhoneNumber.trim();
    this.sPIB = this.sPIB.trim();
    this.sellerFirstNameValidation();
    this.sellerLastNameValidation();
    this.sellerEmailValidation();
    this.sellerPhoneValidation();
    this.sellerPIBValidation();
    this.sellerPictureValidation();
    this.sellerPasswordValidation();

    if(!(this.sWrongFirstName || this.sWrongLastName || this.sWrongEmail || this.sWrongPIB || this.sWrongPicture || this.sWrongPhoneNumber || 
      this.sWrongPasswordOne || this.sWrongPasswordTwo)){

        //nije doslo ni do jedne greske

        let newSeller : NewSeller = {
          firstName : this.sFirstName,
          lastName : this.sLastName,
          email : this.sEmail,
          phoneNumber : this.sPhoneNumber,
          tin : this.sPIB,
          picture : (this.sPicture == '')? null : this.sPicture,
          hashedPassword : PasswdHash.encrypt(this.sPasswordOne)
        }

        this.sellerService.createSeller(newSeller, this, this.cbSuccess, this.cbConflict);
      }
    
    
  }






  // BUYER //


  buyerFirstNameValidation(){
    if(this.isName(this.bFirstName)){
      this.bWrongFirstName = false;
      return;
    }
    (<HTMLSelectElement>document.getElementById('buyerFirstName')).focus();
    this.bWrongFirstName = true;
  }

  buyerLastNameValidation(){
    if(this.isName(this.bLastName)){
      this.bWrongLastName = false;
      return;
    }
    (<HTMLSelectElement>document.getElementById('buyerLasttName')).focus();
    this.bWrongLastName = true;
  }

  buyerEmailValidation(){
    if(this.isCorrectMail(this.bEmail)){
      this.bWrongEmail = false;
      return;
    }
    (<HTMLSelectElement>document.getElementById('buyerEmail')).focus();
    this.bWrongEmail = true;
  }

  buyerPhoneValidation(){
    if(this.isCorrectPhoneNumber(this.bPhoneNumber)){
      this.bWrongPhoneNumber = false;
      return;
    }
    (<HTMLSelectElement>document.getElementById('buyerPhoneNumber')).focus();
    this.bWrongPhoneNumber = true;
  }

  buyerPasswordValidation(){
    if (this.isCorrectPassword(this.bPasswordOne) && this.isCorrectPassword(this.bPasswordTwo) && this.bPasswordOne == this.bPasswordTwo) {
      this.bWrongPasswordOne = false;
      this.bWrongPasswordTwo = false;
      return;
    }
    if(!this.isCorrectPassword(this.bPasswordOne)){
      this.bPasswordOne = ''; //brisi obe ukucane lozinke
      this.bPasswordTwo = '';
      (<HTMLSelectElement>document.getElementById('buyerPasswordOne')).focus(); 
      this.bWrongPasswordOne = true;
    }
    else if(this.bPasswordOne != this.bPasswordTwo){
      this.bPasswordTwo = '';
      (<HTMLSelectElement>document.getElementById('buyerPasswordTwo')).focus(); 
      this.bWrongPasswordTwo = true;
    }
  }

  

  buyerPictureValidation(){
    let reader = new FileReader();
    let inputPicture = document.getElementById("buyerPicture") as HTMLInputElement;
    if(inputPicture == null || inputPicture.files == null || inputPicture.files.length == 0){
      console.log("Slika nije izabrana kako treba, ne postoji!");
      return;
    }

    let self = this;

    reader.readAsDataURL(inputPicture.files[0]);
    reader.onload = function(){
      self.bPicture = reader.result as string;
      let len = self.bPicture.length;
      if(len < 5000 || len > 65000) // duzina tj velicina slike nije dobra
      {
        console.log("picture error");
        (<HTMLSelectElement>document.getElementById('buyerPicture')).focus();
        self.bWrongPicture = true;
        return;
      }

      //sve je ok
      self.bWrongPicture = false;
      console.log("Picture len is ", self.bPicture.length);
    }
  }

  buyerValidation(){
    this.bFirstName = this.bFirstName.trim();
    this.bLastName = this.bLastName.trim();
    this.bEmail = this.bEmail.trim();
    this.bPhoneNumber = this.bPhoneNumber.trim();
    this.buyerFirstNameValidation();
    this.buyerLastNameValidation();
    this.buyerEmailValidation();
    this.buyerPhoneValidation();
    this.buyerPictureValidation();
    this.buyerPasswordValidation();

    if(!(this.bWrongFirstName || this.bWrongLastName || this.bWrongEmail || this.bWrongPicture || this.bWrongPhoneNumber || 
      this.bWrongPasswordOne || this.bWrongPasswordTwo)){

        //nije doslo ni do jedne greske
        let newBuyer : NewBuyer = {
          firstName : this.bFirstName,
          lastName : this.bLastName,
          email : this.bEmail,
          phoneNumber : this.bPhoneNumber,
          picture : (this.bPicture == '')? null : this.bPicture,
          hashedPassword : PasswdHash.encrypt(this.bPasswordOne)
        }

        this.buyerService.createBuyer(newBuyer, this, this.cbSuccess, this.cbConflict);
      }
    
  }


  //callbacks
  cbSuccess(self: any) {
    AlertPageUtil.allowAccess();
    self.router.navigate(RedirectRoutes.ON_REGISTER_SUCCESSFUL);
  }

  cbConflict(self: any) {
    alert('Nalog sa unetim email-om već postoji!');
  }


}

