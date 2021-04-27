import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { Transaction } from './second/second.model';

@Injectable({
  providedIn: 'root'
})
export class SecondService {

  constructor(private httpService : HttpClient) { }

  public showAllTransactions(): Observable<Transaction[]>{
    return this.httpService.get<Transaction[]>('http://localhost:8086/transactions');
  }
}
