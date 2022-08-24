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
    tagList?:number[];
    newConstruction:boolean;

    sellerId:number;
    cityId:number;
    adCategory:number; // koji je tip oglasa
    type: number; //koji je tip nekretnine

    pageNumber:number;
    propertiesPerPage:number;
    ascendingOrder:boolean;
}

//model oglasa za stan
export interface Property{
    seller:Seller;
    city:City;
    adCategory:AdCategory; //prodaja ili izdavanje
    type:AdType; //da li je stan,kuca...
    tags:Tag[];

    id:number;
    description:string;
    price:number;
    postingDate:Date;
    area:string; //kvadratura
    newConstruction:boolean;
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

export interface AdType{ //da li je stan,kuca,plac...
    id:number;
    name:string;
}

export interface AdCategory{//da li se prodaje ili se izdaje...
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
    totalRating:number;
    alreadyRated:boolean;//ako je ocenjeno da ne moze opet
}

export interface NewSeller{
    firstName:String;
    lastName:string;
    email:string;
    hashedPassword:string;
    picture:string | null;
    phoneNumber:string;
    tin:string;
}

export interface NewBuyer{
    firstName:String;
    lastName:string;
    email:string;
    hashedPassword:string;
    picture:string | null;
    phoneNumber:string;
}

export interface PagedProperty{
    totalProperties:number;
    properties:Property[];
}

export interface NewProperty{
    // Obavezni podaci. Regularni format
    description: string;
    area:string;
    workFromHome: boolean;
    price:string;

    // Format: Objekat, name = '', bitan samo id
    adCategory : AdCategory;
    adType: AdType;
    city: City;    
    
    tags: Tag[];            // [], ako nema tagova

    // Uvek null
    postDate: null;
    employer: null;
}