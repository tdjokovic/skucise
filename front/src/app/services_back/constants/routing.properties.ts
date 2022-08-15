export const RedirectRoutes = {
    DEFAULT: [''],
    HOME: [''],

    ON_FORBIDDEN: [''],
    ON_UNAUTHORIZED: ['login'],
    ON_SESSION_EXPIRED: ['alert','session-expired'],
    
    ON_LOGOUT: [''],
    ON_LOGIN: [''],
    
    ON_REGISTER_SUCCESSFUL: ['alert','register-successful'],
    ON_CREATE_JOB_SUCCESSFUL: ['alert','create-job-successful'],
    ON_APPLY_TO_JOB_SUCCESSFUL: ['alert', 'apply-to-job-successful']
}