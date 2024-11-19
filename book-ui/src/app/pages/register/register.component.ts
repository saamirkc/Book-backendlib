import { Component } from '@angular/core';
import {RegistrationRequest} from "../../services/models/registration-request";
import {Router} from "@angular/router";
import {AuthenticationService} from "../../services/services/authentication.service";

@Component({
  selector: 'app-register',
  templateUrl: './register.component.html',
  styleUrl: './register.component.scss'
})
export class RegisterComponent {
constructor(
  private route:Router,
  private authService:AuthenticationService


) {
}
  registerRequest:RegistrationRequest={
    email:'',
    firstname:'',
    lastname:'',
    password:''
  };
  errorMsg:Array<string>=[];

  register() {
this.errorMsg=[];
this.authService.register({
  body:this.registerRequest
})
  .subscribe({
    next:()=>{
      this.route.navigate(['activate-account']);
    },
    error: (err)=>{
this.errorMsg=err.error.validationErrors;
    }



  })
  }

  login() {
this.route.navigate(['login'])
  }
}
