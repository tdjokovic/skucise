export class AlertPageUtil{
    // Indikator da li je alert strana aktivirana na regularan nacin
    private static statusVarName: string = 'alert-page-activated-regularly';

    static allowAccess() {
        window.localStorage.setItem(this.statusVarName, 'OK');
    }

    static denyAccess() {
        window.localStorage.removeItem(this.statusVarName);
    }

    static checkAccess(): boolean {
        return window.localStorage.getItem(this.statusVarName) == 'OK';
    }
}