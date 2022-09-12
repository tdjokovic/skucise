import { UserRoles } from "../back/types/enums";

export class JWTUtil{
    static localStorageKey = "Json-Web-Token";

    static get():string{
        const jwt = window.localStorage.getItem(this.localStorageKey);
        if(jwt==null){
            return '';
        }
        return jwt;
    }

    
    static store(jwt: string | null) {
        window.localStorage.setItem(this.localStorageKey, (jwt == null)? '' : jwt);
    }

    static delete() {
        // this.store('');
        window.localStorage.removeItem(this.localStorageKey);
    }

    static decodePayload(jwt: string | null): JWT | null {
        if (jwt == null || jwt == '')
            return null;
            
        const payload = jwt.split('.')[1];    
        return JSON.parse(atob(payload));
    }

    static getPayload(): JWT | null {
        return this.decodePayload(this.get());
    }

    static getRole(): string {
        let g = this.getPayload();
        return (g == null)? '' : g.rol;
    }

    static getUserRole(): UserRoles {
        return this.getRole() as UserRoles;
    }

    static getID(): number {
        let g = this.getPayload();
        return (g == null)? 0 : g.uid;
    }

    static exists() {
        return this.get() !== '';
    }
}

interface JWT {
    exp: number;
    iss: string;
    rol: string;
    uid: number;
}
