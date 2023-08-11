import {Component} from '@angular/core';
import {HttpClient, HttpErrorResponse} from "@angular/common/http";
import {GenerateImageResponse} from "../types/generate-image-response";

@Component({
  selector: 'app-main-component',
  templateUrl: './main.component.html',
  styleUrls: ['./main.component.scss']
})
export class MainComponent {

  public showLoading: boolean = false;
  public file?: File;
  public fileName: string = '';
  public errors: string[] = [];
  public selectedMath: string = 'SMALL_MULTIPLY';
  public selectedDisplay: string = 'FULL_IMAGE';
  public image?: string = undefined;
  public imageWithSolution?: string = undefined;

  constructor(private http: HttpClient) {
  }

  public mathChanged(): void {
    this.updateImages();
  }

  public onFileSelected(event: any): void {
    const file: File = event.target.files[0];

    if (file) {
      this.fileName = file.name;
      this.file = file;
      this.updateImages();
    }
  }

  private async updateImages() {
    if (!this.file) {
      //no file selected
      return;
    }
    if (!this.selectedMath) {
      //no math selected
      return;
    }

    this.showLoading = true;
    const formData = new FormData();

    formData.append("sourceImage", this.file);
    formData.append("selectedMath", this.selectedMath);

    this.errors = [];
    try {

      let value: GenerateImageResponse = (await this.http.post("/api/image-upload", formData).toPromise()) as GenerateImageResponse;

      this.showLoading = false;
      this.image = value.image;
      this.imageWithSolution = value.imageWithSolution;
    } catch (e: any) {
      if (e && e.error && e.error.errors) {
        this.errors=e.error.errors;
      }
      this.showLoading = false;
      this.image = undefined;
      this.imageWithSolution = undefined;
    }

  }
}
