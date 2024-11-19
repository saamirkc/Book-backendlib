import {Component, OnInit} from '@angular/core';
import {Router} from "@angular/router";
import {BookControllerService} from "../../../../services/services/book-controller.service";
import {PageResponseBookResponse} from "../../../../services/models/page-response-book-response";

@Component({
  selector: 'app-book-list',
  templateUrl: './book-list.component.html',
  styleUrl: './book-list.component.scss'
})
export class BookListComponent implements OnInit{
    bookResponse:PageResponseBookResponse={};
     page: number=0;
     size: number=5;
  constructor(
    private router: Router,
    private bookService:BookControllerService
  ) {
  }

    ngOnInit(): void {
    this.findAllBooks()
    }

    private findAllBooks() {
      this.bookService.findAllBooks({
        page:this.page,
        size:this.size
      })
        .subscribe({
          next:(books)=>{
    this.bookResponse=books;
          }
        })
    }
  }
