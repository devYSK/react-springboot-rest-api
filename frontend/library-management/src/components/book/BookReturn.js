import {useState} from "react";
import axios from "axios";

export function BookReturn() {

  const [returnRequest, setReturnRequest] = useState({
    bookName: '',
    email: ''
  })

  const onChangeRequestAdd = (e) => {
    const {name, value} = e.target;

    const nextBookInfo = {
      ...returnRequest,
      [name]: value
    }

    setReturnRequest(nextBookInfo);
  }

  function validateRequest(rentalRequest) {
    if (rentalRequest.bookName === '' || rentalRequest.bookType === '') {
      alert("입력값을 확인해주세요!");
      return false;
    }

    return true;
  }

  const onclickBookRental = () => {

    if (validateRequest(returnRequest)) {
      axios
      .patch("http://localhost:8080/api/v1/books/return",
          JSON.stringify(returnRequest), {
            headers: {
              "Content-Type": `application/json`,
            },
          })
      .then(success => {

        console.log("responseStatus : " + success.status);
        console.log("console.data : " + success.data);
        console.log(success);

        if (success.status === 200) {

          alert("반납에 성공했습니다. ");
        }
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
        <img src="img/book_return.png"/>
        <p className="title">책 반납</p>
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
        <button className="save" onClick={onclickBookRental}>반납</button>
      </div>
  );
}