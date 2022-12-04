import {useState} from "react";
import axios from "axios";

export function BookRentalView() {

  const [rentalRequest, setRentalRequest] = useState({
    bookName: '',
    email: ''
  })

  const onChangeRequestAdd = (e) => {
    const {name, value} = e.target;

    const nextBookInfo = {
      ...rentalRequest,
      [name]: value
    }

    setRentalRequest(nextBookInfo);
  }

  function validateRequest(rentalRequest) {
    if (rentalRequest.bookName === '' || rentalRequest.bookType === '') {
      alert("입력값을 확인해주세요!");
      return false;
    }

    return true;
  }

  const onclickBookRental = () => {

    if (validateRequest(rentalRequest)) {
      axios
      .patch("http://localhost:8080/api/v1/books/rental", JSON.stringify(rentalRequest), {
        headers: {
          "Content-Type": `application/json`,
        },
      })
      .then(success => {
        alert("대여에 성공했습니다. bookRentalHistoryId : " + success.data.bookRentalHistoryId)
      })
      .catch(error => {
        console.log(error);

        alert("status : " + error.response.status + ", "
            + error.response.data.message);
      });

    }

  }

  return (
      <div className="content">
        <img src="img/book_rental.png"/>
        <p className="title">책 대여</p>
        <div className="input_box">
          <p>유저 이메일</p>
          <input name="email"
                 onChange={onChangeRequestAdd}/>
        </div>
        <div className="input_box">
          <p>책 이름</p>
          <input name="bookName"
                 onChange={onChangeRequestAdd}/>
        </div>
        <button className="save" onClick={onclickBookRental}>대여</button>
      </div>
  );
}