import {authModule} from './AuthModule.js';
import {bookModule} from './BookModule.js';

document.getElementById('enter-menu').onclick=function(){
    toogleMenuActive('enter-menu');
    authModule.printLoginForm();
}
document.getElementById('sysout').onclick=function(){
    toogleMenuActive('sysout');
    authModule.logout();
}
document.getElementById('printNewBookForm').onclick=function(){
    toogleMenuActive('printNewBookForm');
    bookModule.printNewBookForm();
}
document.getElementById('printListBooksForm').onclick=function(){
    toogleMenuActive('printListBooksForm');
    bookModule.printListBook();
}

function toogleMenuActive(elementId){
  let activeElement = document.getElementById(elementId);
  let passiveElements = document.getElementsByClassName("nav-item");
  for(let i = 0; i < passiveElements.length; i++){
    if(activeElement === passiveElements[i]){
      passiveElements[i].classList.add("active-menu");
    }else{
      if(passiveElements[i].classList.contains("active-menu")){
        passiveElements[i].classList.remove("active-menu");
      }
    }
  }
}
authModule.toogleVisibleMenus();