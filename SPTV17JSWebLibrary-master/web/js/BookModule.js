import {httpModule} from './HttpModule.js';
import {userModule} from './UserModule.js';

class BookModule {
    printNewBookForm() {
        document.getElementById('info').innerHTML = '&nbsp;';
        document.getElementById('content').innerHTML =
                `<div class="row mt-5">
                 <div class="col-sm-6 m-auto">
                   <div class="card">
                     <div class="card-body">
                       <h5 class="card-title w-100 text-center">Add book</h5>
                       <p class="card-text w-100 text-center">Fill in all the fields</p>
                          <div class="input-group mb-3">
                            <input id="name" type="text" class="form-control" placeholder="Name of the book" aria-label="Name of the book">
                          </div>
                          <div class="input-group mb-3">
                            <input id="author" type="text" class="form-control" placeholder="Book author" aria-label="Book author">
                          </div>
                          <div class="input-group mb-3">
                            <input id="publishedYear" type="text" class="form-control" placeholder="Year of wear" aria-label="Year of wear">
                            <input id="price" type="text" class="form-control" placeholder="Price" aria-label="Price">
                          </div>
                          <div class="input-group mb-3">
                            <input id="coverUrl" type="text" class="form-control" placeholder="Paste the link to the cover from the Internet" aria-label="Paste the link to the cover from the Internet">
                          </div>
                          <div class="input-group mb-3">
                             <textarea id="textBook" class="form-control" cols="160" aria-label="Book text" placeholder="Book text"></textarea>
                          </div>
                       <a id="btnAddBook" href="#" class="btn btn-danger w-100 rounded-pill">Add book</a>
                     </div>
                   </div>
                 </div>
              </div>`;
        document.getElementById('btnAddBook').addEventListener('click', bookModule.createBook);
    }

    createBook() {
        let name = document.getElementById('name').value;
        let author = document.getElementById('author').value;
        let publishedYear = document.getElementById('publishedYear').value;
        let coverUrl = document.getElementById('coverUrl').value;
        let price = document.getElementById('price').value;
        let textBook = document.getElementById('textBook').value;

        if (name === null || name === undefined
                || author === null || author === undefined
                || publishedYear === null || publishedYear === undefined
                || coverUrl === null || coverUrl === undefined
                || price === null || price === undefined
                || textBook === null || textBook === undefined) {
            document.getElementById('info').innerHTML = 'Fill in all the fields';
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
        httpModule.http('createBook', 'POST', newBook)
                .then(function (response) {
                    if (response === null || response === undefined) {
                        document.getElementById('info').innerHTML = '';
                        return;
                    }
                    if (response.authStatus === 'false') {
                        document.getElementById('info').innerHTML = 'Sign in';
                        return;
                    }
                    if (response.actionStatus === 'false') {
                        document.getElementById('info').innerHTML = '';
                        return;
                    }
                    document.getElementById('info').innerHTML = 'Book added';
                    bookModule.printNewBookForm();
                });
    }
    printListBook() {
        httpModule.http('listBooks', 'GET')
                .then(function (response) {
                    if (response === null || response === undefined) {
                        document.getElementById('info').innerHTML = '';
                        return;
                    }
                    if (response.authStatus === 'false') {
                        document.getElementById('info').innerHTML = 'Sign in';
                        return;
                    }
                    if (response.actionStatus === 'false') {
                        document.getElementById('info').innerHTML = '';
                        return;
                    }

                    document.getElementById('content').innerHTML =
                            `<h2 class="w-100 text-center">Book list</h2>
                           <div id="boxBooks" class="row row-cols-1 row-cols-md-3 mt-4"></div>`;
                    let boxBooks = document.getElementById('boxBooks');
                    let books = response.data;
                    for (let i = 0; i < books.length; i++) {
                        boxBooks.insertAdjacentHTML('afterbegin',
                                `<div class="col mb-4">
                          <div class="card h-100" style="width: 18em;">
                            <img src="${books[i].coverUrl}" class="card-img-top" alt="..." >
                            <div class="card-body">
                              <h5 class="card-title">Title: ${books[i].name}</h5>
                              <p class="card-text">Author: ${books[i].author}</p>
                              <p class="card-text">Price: ${books[i].price}</p>
                              <div class="card-footer d-flex justify-content-center">
                                <button id='btnToRead${books[i].id}' class="btn bg-danger text-white p-1 m-1">Familiarize</button>
                                <button id='btnToBuy${books[i].id}' class="btn bg-danger text-white p-1 m-1">Buy</button>
                              </div>
                            </div>
                          </div>
                        </div>`
                                );
                        document.getElementById('btnToRead' + books[i].id).onclick = function () {
                            bookModule.readBook(books[i].id);
                        }
                        document.getElementById('btnToBuy' + books[i].id).onclick = function () {
                            bookModule.buyBook(books[i].id);
                        }
                    }
                });
    }
    readBook(bookId) {
        let url = 'readBook?bookId=' + bookId;
        httpModule.http(url, 'GET')
                .then(function (response) {
                    if (response === null || response === undefined) {
                        document.getElementById('info').innerHTML = '';
                        return;
                    }
                    if (response.authStatus === 'false') {
                        document.getElementById('info').innerHTML = 'Sign in';
                        return;
                    }
                    if (response.actionStatus === 'false') {
                        document.getElementById('info').innerHTML = '';
                        return;
                    }
                    document.getElementById('content').innerHTML =
                            `<div class="text-justify mt-5">
                           (Fact sheet)<br>       
                             ${response.data}...
                            <br> 
                            ( end of fact sheet )<br>
                            To continue reading you must <a id="buyBook" href="#">Buy</a> book
                          </div>`;
                    document.getElementById('buyBook').onclick = function () {
                        bookModule.buyBook(bookId);
                    }
                });

    }
    buyBook(bookId) {
        let url = 'buyBook?bookId=' + bookId;
        httpModule.http(url, 'GET')
                .then(function (response) {
                    if (response === null || response === undefined) {
                        document.getElementById('info').innerHTML = '';
                        return;
                    }
                    if (response.authStatus === 'false') {
                        document.getElementById('info').innerHTML = 'Sign in';
                        return;
                    }
                    if (response.actionStatus === 'false') {
                        document.getElementById('info').innerHTML = '';
                        return;
                    }
                    sessionStorage.setItem('user', response.user);
                    document.getElementById('content').innerHTML =
                            `<div class="text-justify mt-5">
                             ${response.data}
                          </div>`;
                });
    }

}
let bookModule = new BookModule();
export {bookModule};