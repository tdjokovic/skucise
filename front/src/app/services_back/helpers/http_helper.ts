import { HttpHeaders } from "@angular/common/http";
import { JWT_HEADER_NAME } from "../back/types/vars";
import { JWTUtil } from "./jwt_helper";


export class HeaderUtil{
    static jwtOnlyHeaders(): HttpHeaders
    {
        let obj: { [key: string]: string } = {};
        obj[JWT_HEADER_NAME] = JWTUtil.get();
        
        return new HttpHeaders(obj);
    }
}

export class ParamUtil {
    static toString(params: any) {
        return params.join(', ')
    }
}