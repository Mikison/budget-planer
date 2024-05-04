import {inject, Injectable} from "@angular/core";
import {HttpClient} from "@angular/common/http";


@Injectable({
    providedIn: 'root'
  })
export class CategoryService {

  private _httpClient = inject(HttpClient);


}
