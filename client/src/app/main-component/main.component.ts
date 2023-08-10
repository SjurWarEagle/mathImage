import {Component} from '@angular/core';
import {HttpClient} from "@angular/common/http";

@Component({
    selector: 'app-main-component',
    templateUrl: './main.component.html',
    styleUrls: ['./main.component.scss']
})
export class MainComponent {
    public fileName: string = '';
    public image?: string = undefined;
    public imageWithSolution?: string = undefined;

    constructor(private http: HttpClient) {
    }

    public onFileSelected(event: any): void {
        const file: File = event.target.files[0];

        if (file) {

            this.fileName = file.name;

            const formData = new FormData();

            formData.append("sourceImage", file);

            const upload$ = this.http.post("/api/image-upload", formData);

            upload$.subscribe((value:any) => {
                this.image = value.image;
                this.imageWithSolution = value.imageWithSolution;
            });
        }
    }
}
