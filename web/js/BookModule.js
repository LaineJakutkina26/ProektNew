import {httpModule} from './HttpModule.js';
import {userModule} from './UserModule.js';

class BookModule{
    printNewBookForm(){
      document.getElementById('info').innerHTML='&nbsp;';
        document.getElementById('content').innerHTML = 
             `<div class="row mt-5">
                 <div class="col-sm-6 m-auto">
                   <div class="card">
                     <div class="card-body">
                       <h5 class="card-title w-100 text-center">Добавить книгу</h5>
                       <p class="card-text w-100 text-center">Заполните все поля</p>
                          <div class="input-group mb-3">
                            <input id="name" type="text" class="form-control" placeholder="Название книги" aria-label="Название книги">
                          </div>
                          <div class="input-group mb-3">
                            <input id="author" type="text" class="form-control" placeholder="Автор книги" aria-label="Автор книги">
                          </div>
                          <div class="input-group mb-3">
                            <input id="publishedYear" type="text" class="form-control" placeholder="Год изнания" aria-label="Год изнания">
                            <input id="price" type="text" class="form-control" placeholder="Цена" aria-label="Цена">
                          </div>
                          <div class="input-group mb-3">
                            <input id="coverUrl" type="text" class="form-control" placeholder="Вставьте ссылку на обложку из Интернета" aria-label="Вставьте ссылку на обложку из Интернета">
                          </div>
                          <div class="input-group mb-3">
                             <textarea id="textBook" class="form-control" cols="160" aria-label="Тексе книги" placeholder="Текст книги"></textarea>
                          </div>
                       <a id="btnAddBook" href="#" class="btn btn-danger w-100">Добавить книгу</a>
                     </div>
                   </div>
                 </div>
              </div>`;
          document.getElementById('btnAddBook').addEventListener('click', bookModule.createBook);
        }
        
    createBook(){
        let name = document.getElementById('name').value;
        let author = document.getElementById('author').value;
        let publishedYear = document.getElementById('publishedYear').value;
        let coverUrl = document.getElementById('coverUrl').value;
        let price = document.getElementById('price').value;
        let textBook = document.getElementById('textBook').value;
        
        if(name === null || name === undefined
              || author === null || author === undefined
              || publishedYear === null || publishedYear === undefined
              || coverUrl === null || coverUrl === undefined
              || price === null || price === undefined
              || textBook === null || textBook === undefined){
          document.getElementById('info').innerHTML='Заполните все поля';
          return;
        }
        let newBook = {
          "name": name,
          "author": author,
          "publishedYear": publishedYear,
          "coverUrl": coverUrl,
          "price": price,
          "textBook": textBook,
        }
        httpModule.http('createBook','POST',newBook)
                .then(function(response){
                  if(response === null || response === undefined){
                    document.getElementById('info').innerHTML='Ошибна на сервере';
                    return;
                  }
                  if(response.authStatus === 'false'){
                    document.getElementById('info').innerHTML='Войдите';
                    return;
                  }
                  if(response.actionStatus === 'false'){
                    document.getElementById('info').innerHTML='';
                    return;
                  }
                  document.getElementById('info').innerHTML='Книга добавлена';
                  bookModule.printNewBookForm();
                });
    }
    printListBook(){
      httpModule.http('listBooks','GET')
                .then(function(response){
                  if(response === null || response === undefined){
                    document.getElementById('info').innerHTML='Ошибна на сервере';
                    return;
                  }
                  if(response.authStatus === 'false'){
                    document.getElementById('info').innerHTML='Войдите';
                    return;
                  }
                  if(response.actionStatus === 'false'){
                    document.getElementById('info').innerHTML='';
                    return;
                  }
                                
                  document.getElementById('content').innerHTML =
                          `<h2 class="w-100 text-center">Список книг</h2>
                           <div id="boxBooks" class="row row-cols-1 row-cols-md-3 mt-4"></div>`;
                  let boxBooks = document.getElementById('boxBooks');
                  let books = response.data;
                  for(let i=0;i< books.length;i++){
                    boxBooks.insertAdjacentHTML('afterbegin', 
                        `<div class="col mb-4">
                          <div class="card h-100" style="width: 18em;">
                            <img src="${books[i].coverUrl}" class="card-img-top" alt="..." >
                            <div class="card-body">
                              <h5 class="card-title">Название: ${books[i].name}</h5>
                              <p class="card-text">Автор: ${books[i].author}</p>
                              <p class="card-text">Цена: ${books[i].price}</p>
                              <div class="card-footer d-flex justify-content-center">
                                <button id='btnToRead${books[i].id}' class="btn bg-danger text-white p-1 m-1">Ознакомиться</button>
                                <button id='btnToBuy${books[i].id}' class="btn bg-danger text-white p-1 m-1">Купить</button>
                              </div>
                            </div>
                          </div>
                        </div>`
                    );
                    document.getElementById('btnToRead'+books[i].id).onclick=function(){
                      bookModule.readBook(books[i].id);
                    }
                    document.getElementById('btnToBuy'+books[i].id).onclick=function(){
                      bookModule.buyBook(books[i].id);
                    }
                  }
                });
    }
    readBook(bookId){
      let url = 'readBook?bookId='+bookId;
      httpModule.http(url,'GET')
                .then(function(response){
                  if(response === null || response === undefined){
                    document.getElementById('info').innerHTML='Ошибна на сервере';
                    return;
                  }
                  if(response.authStatus === 'false'){
                    document.getElementById('info').innerHTML='Войдите';
                    return;
                  }
                  if(response.actionStatus === 'false'){
                    document.getElementById('info').innerHTML='';
                    return;
                  }
                  document.getElementById('content').innerHTML=
                          `<div class="text-justify mt-5">
                           (Ознакомительный отрывок)<br>       
                             ${response.data}...
                            <br> 
                            ( конец ознакомительного отрывка )<br>
                            Чтобы продолжить чтение необходимо <a id="buyBook" href="#">купить</a> книгу
                          </div>`;
                  document.getElementById('buyBook').onclick = function(){
                    bookModule.buyBook(bookId);
                  }
                });
                
  }
    buyBook(bookId){
      let url = 'buyBook?bookId='+bookId;
      httpModule.http(url,'GET')
                .then(function(response){
                  if(response === null || response === undefined){
                    document.getElementById('info').innerHTML='Ошибка на сервере';
                    return;
                  }
                  if(response.authStatus === 'false'){
                    document.getElementById('info').innerHTML='Войдите';
                    return;
                  }
                  if(response.actionStatus === 'false'){
                    document.getElementById('info').innerHTML='';
                    return;
                  }
                  sessionStorage.setItem('user',response.user);
                  document.getElementById('content').innerHTML=
                          `<div class="text-justify mt-5">
                             ${response.data}
                          </div>`;
                });
    }
    
}
let bookModule = new BookModule();
export {bookModule};