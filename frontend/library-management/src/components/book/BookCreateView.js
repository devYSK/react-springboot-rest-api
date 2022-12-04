import {useState} from "react";
import axios from "axios";

export function BookCreateView() {

  const [bookCreateRequest, setBookCreateRequest] = useState({
    bookName: '',
    bookType: ''
  })

  const onChangeBookAdd = (e) => {
    const {name, value} = e.target;

    const nextBookInfo = {
      ...bookCreateRequest,
      [name]: value
    }

    setBookCreateRequest(nextBookInfo);
  }

  function validateBookInfo(bookCreateRequest) {
    if (bookCreateRequest.bookName === '' || bookCreateRequest.bookType === '') {
      alert("입력값을 확인해주세요!");
      return false;
    }

    return true;
  }

  const onclickBookAdd = () => {

    if (validateBookInfo(bookCreateRequest)) {
      axios
      .post("http://localhost:8080/api/v1/books", JSON.stringify(bookCreateRequest), {
        headers: {
          "Content-Type": `application/json`,
        },
      }).then(success => {
        console.log(success);
        alert("책이 등록되었습니다. id : " + success.data.bookId + ", bookName : " + success.data.bookName);
        window.location.replace("/")
      })
      .catch(error => {
        console.log("status :" + error.response.status);
        console.log(error.response.data);
        alert(error.response.data.message);
      });

    }

  }

  const optionValue = ["COMPUTER", "SCIENCE", "ECONOMY", "COMIC", "SPORTS"]

  return (


      <div className="sec_box content_box">
        <div className="content">
          <img src="img/book_add.png"/>
          <p className="title">책 등록</p>
          <div className="input_box">
            <p>책 이름</p>
            <input type="text" id="small-input" name="bookName"
                   onChange={onChangeBookAdd}/>
          </div>
          <div className="input_box">
            <p>책 유형</p>
            <select
                defaultValue="default"
                onChange={onChangeBookAdd}
                name="bookType"
                aria-label="옵션선택"
            >
              <option value="default" disabled style={{color: "#ccc"}}>
                ---책 유형 선택---
              </option>
              {(optionValue || []).map((options, idx) => (
                  <option key={idx} value={options} style={{color: "#000"}}>
                    {options}
                  </option>
              ))}
            </select>
          </div>

          <button className="save" onClick={onclickBookAdd}>책 등록</button>

        </div>
      </div>

  );
}

