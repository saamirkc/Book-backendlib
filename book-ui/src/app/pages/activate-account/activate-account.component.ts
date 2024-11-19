import { Component } from '@angular/core';
import {AuthenticationService} from "../../services/services/authentication.service";
import {Router} from "@angular/router";

@Component({
  selector: 'app-activate-account',
  templateUrl: './activate-account.component.html',
  styleUrl: './activate-account.component.scss'
})
export class ActivateAccountComponent {

  message='';
  isOkay=true;
  submitted=false

  constructor(
    private router:Router,
    private authService:AuthenticationService
  ) {
  }


  onCodeCompleted(token:string) {
    this.authService.confirm(
      {
        token
      }
    )
      .subscribe({
        next:()=>{
          this.message='Your account has been successfully activated!\n Now proceed to login.'
          this.submitted=true;
    },
        error:()=>{
          this.message='Token has been either expired or is invalid!'
          this.submitted=true;
          this.isOkay=false;


        }



      })

  }

  redirectToLogin() {
    this.router.navigate(['login']);
  }
}
