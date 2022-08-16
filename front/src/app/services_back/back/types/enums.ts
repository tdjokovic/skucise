export enum UserRoles{
    Visitor = '',
    Admin = 'admin',
    Reg_seller = 'reg_seller',
    Reg_buyer = 'reg_buyer'
}

export enum ResponseCodes{
    Unknown,
    Forbidden,
    Success,

    //user login
    SessionExpired,
    WrongCredentials
}