import {Injectable} from "@angular/core";
import {
    HttpErrorResponse,
    HttpEvent,
    HttpHandler,
    HttpInterceptor,
    HttpRequest,
    HttpResponse
} from "@angular/common/http";
import {Observable} from "rxjs";

/**
 * This interceptor parses a 200 status ok response on a POST request as success instead of an error.
 *
 * Idea taken from: https://stackoverflow.com/questions/47207264/httpclient-cant-parse-empty-response
 *
 */
@Injectable()
export class EmptyResponseBodyErrorInterceptor implements HttpInterceptor {
    intercept(req: HttpRequest<any>, next: HttpHandler): Observable<HttpEvent<any>> {
        return next.handle(req)
            .catch((err: HttpErrorResponse) => {
                if (err.status == 200 && req.method === "POST") {
                    const res = new HttpResponse({
                        body: null,
                        headers: err.headers,
                        status: err.status,
                        statusText: err.statusText,
                        url: err.url
                    });
                    return Observable.of(res);
                } else {
                    return Observable.throw(err);
                }
            });
    }
}
