import {NgModule} from '@angular/core';
import {RouterModule, Routes} from '@angular/router';
import {MainComponent} from "./main-component/main.component";

const routes: Routes = [
  {path: '', component: MainComponent},
  // { path: 'show', component: MazeDisplayComponent },
  // directs all other routes to the main page
  {path: '**', redirectTo: ''},
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule {
}
