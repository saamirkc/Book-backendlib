import {Component, signal} from '@angular/core';
import {AuthenticationRequest} from "../../services/models/authentication-request";

import {AuthenticationService} from "../../services/services/authentication.service";
import {Router, RouterModule} from "@angular/router";
import {TokenService} from "../../services/token/token.service";

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrl: './login.component.scss'
})
export class LoginComponent {
  // hide = true;
constructor(
  private route:Router,
  private authService:AuthenticationService,
  private tokenService:TokenService
) {
}

  authRequest: AuthenticationRequest = {
    email: '',
    password: ''
  };
  errorMsg:Array<string>=[];



  login() {
this.errorMsg=[];
this.authService.authenticate({
  body:this.authRequest
  }
).subscribe({
  next:(res)=>{
    //save the token(as since after entering correct email and password you are authenticated so it generates a token, this token has to be saved in local storage so it can be used )
    this.tokenService.token= res.token as string;
    this.route.navigate(['books']);
  },
  error:(err)=>{
    console.log(err);
    if(err.error.validationErrors){
      this.errorMsg=err.error.validationErrors;
    }
    else{
      this.errorMsg.push(err.error.errorMsg)
    }

  }


})
  }
  register() {
this.route.navigate(['register'])
  }

}
