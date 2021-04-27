import { Component, Input, OnInit } from '@angular/core';
import { FirstService } from '../first.service';

@Component({
  selector: 'app-first',
  templateUrl: './first.component.html',
  styleUrls: ['./first.component.css']
})
export class FirstComponent implements OnInit {

  constructor(private service: FirstService) { }
  

  ngOnInit(): void {
  }

  result: Object;

  public inputFile(){
    // alert("Upload file ts!");
    var response = this.service.inputFile().subscribe(response => this.result = response);
    // console.log("response: "+ response);
    // console.log("result: "+ this.result);
    alert(response);
  }

  //response.toString()
    
}
