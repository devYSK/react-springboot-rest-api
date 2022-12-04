import {useState} from "react";
import axios from "axios";

export function UserCreateView() {

  const [user, setUser] = useState({
    email: '',
    name: ''
  });

  const onChangeUser = (e) => {
    const {name, value} = e.target;

    const userInfo = {
      ...user,
      [name]: value
    }

    setUser(userInfo);
  }

  function validateUser(user) {
    console.log("user : " + user);
    console.log(user.email);
    console.log(user.name);

    if (user.email === '' || user.name === '') {
      alert("입력값을 확인해주세요!");
      return false;
    }

    return true;
  }

  const onclickUserAdd = () => {
    if (validateUser(user)) {
      axios
      .post("http://localhost:8080/api/v1/users", JSON.stringify(user), {
        headers: {
          "Content-Type": `application/json`,
        },
      }).then(success => {
        console.log("responseStatus : " + success.status);
        console.log("console.data : " + success.data);
        console.log(success);
        alert("유저가 등록되었습니다. " + success.data.email + ", " + success.data.userId);
        window.location.replace("/")
      })
      .catch(error => {
        const response = error.response;
        console.log(error);
        console.log("status :" + response.status);
        console.log(response.data);
        alert(response.data.message);
      });

    }

  }

  return (


      <div className="fir_box content_box">
        <div className="content">
          <img src="img/users.png"/>
          <p className="title">사용자 등록</p>
          <div className="input_box">
            <p>이름</p>
            <input type="text" id="small-input" onChange={onChangeUser} name="name"/>

          </div>
          <div className="input_box">
            <p>이메일</p>
            <input type="email" id="small-input" onChange={onChangeUser} name="email"/>
          </div>
          <button className="save" onClick={onclickUserAdd}>유저 등록</button>

        </div>
      </div>


  );
}

