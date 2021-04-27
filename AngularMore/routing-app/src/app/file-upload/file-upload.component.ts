import { HttpClient, HttpEventType } from '@angular/common/http';
import { Component, Input, OnInit } from '@angular/core';
import { Subscription } from 'rxjs';

@Component({
  selector: 'app-file-upload',
  templateUrl: './file-upload.component.html',
  styleUrls: ['./file-upload.component.css']
})
export class FileUploadComponent implements OnInit {
  @Input()
  requiredFileType = "text/plain";

  fileName = "";
  uploadProgrss: number;
  uploadSub: Subscription;
  
  constructor(private http: HttpClient) { }

  onFileSelected(event) {

    const file:File = event.target.files[0];

    if (file) {

        this.fileName = file.name;
        const formData = new FormData();
        formData.append("thumbnail", file);

        // const upload$ = this.http.post("/api/thumbnail-upload", formData, {
        //   reportProgrss: true,
        //   observe: 'events'
        // })
        // .pipe(
        //   finalize(() => this.reset())
        // );

        // this.uploadSub = upload$.subscribe(event => {
        //   if(event.type == HttpEventType.UploadProgress){
        //     this.uploadProgrss = Math.round(100 * (event.loaded / event.total));
        //   }
        // });
      
    }
  }

  cancelUpload(){
    this.uploadSub.unsubscribe();
    this.reset();
  }

  reset(){
    this.uploadProgrss = null;
    this.uploadSub = null;
  }


  ngOnInit(): void {
  }

}
