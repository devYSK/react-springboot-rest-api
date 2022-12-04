import Header from "./components/header/Header";
import {BookCreateView} from "./components/book/BookCreateView";
import {UserCreateView} from "./components/user/UserCreateView";

import './index.css'
import {BookRentalView} from "./components/book/BookRentalView";
import {BookReturn} from "./components/book/BookReturn";

export function Main() {
  return (
      <div>

        <div className="wrapper">
          <Header headername={"도서관리 애플리케이션"}/>

          <div className="container">
            <UserCreateView/>

            <BookCreateView/>


            <div className="thi_box content_box">
              <BookRentalView/>
            </div>
            <div className="fou_box content_box">
              <BookReturn/>
            </div>
          </div>
        </div>


      </div>

  );

}
