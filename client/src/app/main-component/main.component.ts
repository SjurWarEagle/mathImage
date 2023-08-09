import {Component} from '@angular/core';
import {HttpClient} from "@angular/common/http";

@Component({
    selector: 'app-main-component',
    templateUrl: './main.component.html',
    styleUrls: ['./main.component.scss']
})
export class MainComponent {
    public fileName: string = '';

    constructor(private http: HttpClient) {
    }

    public onFileSelected(event: any): void {
        const file: File = event.target.files[0];

        if (file) {

            this.fileName = file.name;

            const formData = new FormData();

            formData.append("thumbnail", file);

            const upload$ = this.http.post("/api/image-upload", formData);

            upload$.subscribe();
        }
    }
}
