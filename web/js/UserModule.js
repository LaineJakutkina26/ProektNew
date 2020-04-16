import {httpModule} from './HttpModule.js';
class UserModule{
    printRegistrationForm(){
       document.getElementById('content').innerHTML=
        `<div class="w-100 mt-5 d-flex justify-content-center">
            <div class="card w-50">
                <div class="card-body">
                  <h5 class="card-title w-100 text-center">Зарегистрировать пользователя</h5>
                  <p class="card-text d-flex justify-content-end">Имя пользователя: <input class="w-50 ml-3" type="text" id="firstname"></p>
                  <p class="card-text d-flex justify-content-end">Фамилия пользователя: <input class="w-50 ml-3" type="text" id="lastname"></p>
                  <p class="card-text d-flex justify-content-end">email пользователя: <input class="w-50 ml-3" type="email" id="email"></p>
                  <p class="card-text d-flex justify-content-end">Кошелек: <input class="w-50 ml-3" type="text" id="money"></p>
                  <p class="card-text d-flex justify-content-end">Город проживания: <input class="w-50 ml-3" type="text" id="city"></p>
                  <p class="card-text d-flex justify-content-end">Улица: <input class="w-50 ml-3" type="text" id="street"></p>
                  <p class="card-text d-flex justify-content-end">Дом: <input class="w-50 ml-3" type="text" id="house"></p>
                  <p class="card-text d-flex justify-content-end">Квартира: <input class="w-50 ml-3" type="text" id="room"></p>
                  <p class="card-text d-flex justify-content-end">Логин: <input class="w-50 ml-3" type="text" id="login"></p>
                  <p class="card-text d-flex justify-content-end">Пароль: <input class="w-50 ml-3" type="password" id="password"></p>
                  <a href="#" id="btnAddUser" class="btn btn-danger w-100">Зарегистрировать пользователя</a>
                </div>
            </div>
          </div>`;
            document.getElementById('btnAddUser').onclick=function(){
                userModule.createUser();
            }
      }
      createUser(){
          let firstname = document.getElementById('firstname').value;
          let lastname = document.getElementById('lastname').value;
          let email = document.getElementById('email').value;
          let money = document.getElementById('money').value;
          let city = document.getElementById('city').value;
          let street = document.getElementById('street').value;
          let house = document.getElementById('house').value;
          let room = document.getElementById('room').value;
          let login = document.getElementById('login').value;
          let password = document.getElementById('password').value;
          let user = {
              "firstname": firstname,
              "lastname": lastname,
              "email": email,
              "money": money,
              "city": city,
              "street": street,
              "house": house,
              "room": room,
              "login": login,
              "password": password,
          }

          httpModule.http('createUser', 'POST', user)
                  .then(function(response){
                      if(response === null || response === undefined){
                          document.getElementById('info').innerHTML = 'Пользователя добавить неудалось';
                          userModule.printRegistrationForm(); 
                      }
                      if(response.actionStatus === 'false'){
                          document.getElementById('info').innerHTML = 'Пользователя добавить неудалось';
                          userModule.printRegistrationForm(); 
                      }
                      document.getElementById('info').innerHTML = 'Новый пользователь добавлен';
                      document.getElementById('content').innerHTML = '';
                  })     
      }
}
let userModule = new UserModule();
export {userModule}
