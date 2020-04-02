import {httpModule} from './HttpModule.js';
import {userModule} from './UserModule.js';
class AuthModule{ 
    printLoginForm(){
        document.getElementById('content').innerHTML = 
            ` <div class="row mt-5 w-100 d-flex justify-content-center">
               <div class="card border-primary p-2" style="max-width: 30rem;">
                  <div class="card-header text-center">Введите логин и пароль</div>
                  <div class="card-body">
                    <p class="card-text d-flex justify-content-between">Логин: <input class="ml-2" type="text" id="login"></p>
                    <p class="card-text d-flex justify-content-between">Пароль: <input class="ml-2" type="text" id="password"></p>
                    <p class="card-text"><button class="btn btn-light w-100" type="button" id="btnEnter">Войти</button</p>
                  </div>
                  <p class="w-100 text-center">Нет учетной записи?<br><a id="registration" href="#">Зарегистрироваться</a></p>
               </div>
             </div>;`;
        document.getElementById('btnEnter').onclick = function(){
            authModule.auth();
        }
        document.getElementById('registration').addEventListener('click', userModule.printRegistrationForm);
    }
    auth(){
        let login = document.getElementById('login').value;
        let password = document.getElementById('password').value;
        let credential = {
          "login": login,
          "password": password,
        }
        httpModule.http('login','POST',credential)
                .then(function(response){
                  if(response === null || response === undefined){
                    document.getElementById('info').innerHTML='Авторизация не произошла';
                    return;
                  }
                  if(response.authStatus == 'false'){
                    document.getElementById('info').innerHTML='Неправильный логин или пароль';
                    return;
                  }
                  document.getElementById('info').innerHTML='Вы вошли как '+ response.user.login;
                  sessionStorage.setItem('user',JSON.stringify(response.user));
                });
    }
}
let authModule = new AuthModule();
export {authModule};