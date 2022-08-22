import { Type } from "@angular/core";

export interface StandardHeaders{
    'Json-Web-Token' : string;
}

export interface Credentials{
    email:string;
    hashedPassword:string;
}

//za filter oglasa
export interface Filters{
    title:string;
    tagList?:number[];
    sellerId:number;
    cityId:number;
    pageNumber:number;
    propertiesPerPage:number;
    categoryId:number; // koji je tip objekta
    propertySize:number; //kvadratura stana

    //ovo bi mogli da bude i 
    //sellingTypeId:number; gde ce da se 0 uparuje sa izdavanjem a 1 sa prodajom
    sellingProperty:boolean;
    ascendingOrder:boolean;
}

//model oglasa za stan
export interface Property{
    title:string;
    description:string;
    seller:Seller;
    type:propertyType;
    city:City;
    tags:Tag[];
    price:number;
    adType:AdType;

    id:number;
    postingDate:Date;
}

export interface Seller{
    id:number;
    firstName:string;
    lastName:string;
    email:string;
    hashedPassword:string;
    picture:string | null; //slika u Base64
    phoneNumber:string;
    tin:string;
}

export interface Buyer{
    id:number;
    firstName:string;
    lastName:string;
    email:string;
    hashedPassword:string;
    picture:string | null; //slika u Base64
    address:string;
    phoneNumber:string;
}

export interface City{
    id:number;
    name:string;
}

export interface Tag{//tagovi oglasa (kao kljucne reci)
    id:number;
    name:string;
}

export interface propertyType{ //da li je stan,kuca,plac...
    id:number;
    name:string;
}

export interface AdType{//da li se prodaje ili se izdaje...
    id:number;
    name:string;
}

export interface PropertyComment{
    id:number;
    postId:number; //id oglasa za koji je taj komentar
    authorName:string; //ime komentatora
    body:string; //sam sadrzaj komentara
    postingDate:Date;
}

export interface Likes{
    totalLikes:number;
    liked:boolean; //ako je lajkovano da ne moze opet
}

export interface Rating{
    id:number;
    name:string;
}

export interface NewSeller{
    firstName:String;
    lastName:string;
    email:string;
    hashedPassword:string;
    picture:string;
    phoneNumber:string;
    tin:string;
}

export interface NewBuyer{
    firstName:String;
    lastName:string;
    email:string;
    hashedPassword:string;
    picture:string;
    phoneNumber:string;
}