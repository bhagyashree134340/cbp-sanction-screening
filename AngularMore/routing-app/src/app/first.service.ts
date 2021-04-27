import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class FirstService {

  constructor(private httpService : HttpClient) { }

  //
  public inputFile(){
    console.log("first-comp service layer");
    var result = this.httpService.get('http://localhost:8086/uploadfile');
    // console.log("result: "+result);
    return result;
  }
}
