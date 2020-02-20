import {HttpErrorResponse} from '@angular/common/http';
import {throwError as _throw} from 'rxjs';

export default function handleError(error: HttpErrorResponse) {
    if (error.error instanceof ErrorEvent) {
        // A client-side or network error occurred. Handle it accordingly.
        console.error('An error occurred:', error.error.message);
    } else {
        // The backend returned an unsuccessful response code.
        // The response body may contain clues as to what went wrong,
        console.error(
            `Backend returned code ${error.status}, ` +
            `body was: ${JSON.stringify(error.error)}`);
    }
    // return an observable with a user-facing error message
    return _throw(
        'Something bad happened; please try again later.');
}
