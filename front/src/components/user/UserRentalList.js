import {useLocation} from "react-router-dom";
import Header from "../header/Header";
import {useEffect, useState} from "react";
import axios from "axios";

export function UserRentalList() {

  const location = useLocation();

  const user = location.state.user;

  const [status, setStatus] = useState(null);

  const [userRentals, setUserRentals] = useState([
    {
      userId: '',
      email: '',
      name: '',
      books: [],
    }
  ]);

  useEffect(() => {
    axios.get("http://localhost:8080/api/v1/users/" + user.userId + "/rentals")
    .then(function (success) {

      setStatus(success.status);

      setUserRentals(success.data);
    }).catch(function (error) {
      setStatus(error.response.status);

    })

    ;
  }, []);


  const onClickReturnBook = (bookId) => {
    axios
    .patch("http://localhost:8080/api/v1/books/" + bookId +"/return", JSON.stringify(userRentals.userId), {
      headers: {
        "Content-Type": `application/json`,
      },
    }).then(success => {
      console.log("responseStatus : " + success.status);
      console.log("console.data : " + success.data);
      console.log(success);
      window.location.replace("/users/rental")
    })
    .catch(error => {
      console.log(error);
      console.log("status :" + error.response.status);
      console.log(error.response.data);
      alert(error.response.data.message);
    });
  }

  return (
      <div>
        <div>
          <Header headername="유저 대여 목록"/>
        </div>

        <div className="user_info">
          <div className="info_box">
            <div className="p_box"><div className="strong_box">유저 Id : </div><div className="user_properties">{user.userId}</div></div>
            <div className="p_box"><div className="strong_box">유저 이메일 : </div><div className="user_properties">{user.email}</div></div>
            <div className="p_box"><div className="strong_box">유저 이름 : </div><div className="user_properties">{user.name}</div></div>
            <div className="p_box"><div className="strong_box">대여 권 수 : </div><div className="user_properties">{userRentals.books ? userRentals.books.length : 0} 권</div></div>
          </div>
        </div>

        <div>
          <table className="table_user_rentals_container">
            <thead className="thead">
            <tr className="list_wrap">
              <th className="list_name">책 Id</th>
              <th className="list_name">책 이름</th>
              <th className="list_name">책 유형</th>
              <th className="list_name">반납</th>

            </tr>
            </thead>


            <tbody>
            {

              status === 200 ?

                  // eslint-disable-next-line array-callback-return
                  userRentals.books && userRentals.books.map(book => {
                    return (<tr className="tr" key={book.id}>
                          <td className="" id="bookId">{book.id}</td>
                          <td className="">{book.bookName}</td>
                          <td className="">{book.bookType}</td>
                          <td>
                            <button className="booking_no_btn" onClick={(event) => onClickReturnBook(book.id)} >반납 하기
                            </button>

                          </td>

                        </tr>
                    )
                  })
                  : null
            }


            </tbody>
          </table>

        </div>
        {
          status === 404 ? <div className="no_data"><h1> 대여 목록이 없습니다 </h1></div>
              : <div></div>
        }
      </div>
  )
}