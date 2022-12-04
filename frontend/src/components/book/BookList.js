import Header from "../header/Header";
import {useEffect, useState} from "react";
import axios from "axios";

export function BookList() {

  const [books, setBooks] = useState([
  ]);

  // 리액트가 준비가 다 되면 호출되는 훅 함수
  useEffect(() => {

    axios.get("http://localhost:8080/api/v1/books")
    .then(success => setBooks(success.data))

  }, []);

  return (
      <div className="">
        <div>
          <Header headername="책 목록"/>
        </div>

        <div>
          <table className="table_container">
            <thead className="thead">
            <tr className="list_wrap">
              <th className="list_name">책 Id</th>
              <th className="list_name">책 이름</th>
              <th className="list_name">책 유형</th>
              <th className="list_name">책 대여 상태</th>
              <th className="list_name">책 등록일</th>

            </tr>
            </thead>

            <tbody>
            {

              // eslint-disable-next-line array-callback-return
              books.map(book => {
                return (<tr className="tr" key={book.id}>
                      <td className="">{book.id}</td>
                      <td className="">{book.bookName}</td>
                      <td className="">{book.bookType}</td>
                      <td>
                        {
                          book.bookStatus === '대여 중'
                              ?  <button className="booking_no_btn">대여 불가
                              </button>
                              : <button className="booking_btn">대여 가능</button>
                        }

                      </td>

                      <td className="">{book.createdAt}</td>

                    </tr>
                )
              })
            }

            </tbody>
          </table>

        </div>


      </div>
  )
}
