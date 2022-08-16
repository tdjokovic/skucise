import * as CryptoJS from 'crypto-js'

export class PasswdHash {
    
    static encrypt(text: string) {
        return CryptoJS.SHA512(text).toString(CryptoJS.enc.Hex);
    }

}

