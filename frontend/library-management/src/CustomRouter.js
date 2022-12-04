import React from 'react';
import {
  BrowserRouter,
  Routes,
  Route,
} from "react-router-dom";
import {BookList} from "./components/book/BookList";
import {Main} from "./Main";
import {UserList} from "./components/user/UserList";
import {UserRentalList} from "./components/user/UserRentalList";

export function CustomRouter() {
  return (
      <BrowserRouter>
        <Routes>
          <Route path="/" element={<Main/>}/>
          <Route path="/books" element={<BookList/>}/>
          <Route path="/users" element={<UserList/>}/>
          <Route path="/users/rental" element={<UserRentalList/>}/>
        </Routes>
      </BrowserRouter>
  );
}
