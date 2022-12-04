import Header from "../header/Header";
import {useEffect, useState} from "react";
import axios from "axios";
import {Link} from "react-router-dom";

export function UserList() {

  const [users, setUsers] = useState([]);

  // 리액트가 준비가 다 되면 호출되는 훅 함수
  useEffect(() => {

    axios.get("http://localhost:8080/api/v1/users")
    .then(success => setUsers(success.data))

  }, []);

  return (
      <div className="">
        <div>
          <Header headername="유저 목록"/>
        </div>

        <div>
          <table className="table_container">
            <thead className="thead">
            <tr className="list_wrap">
              <th className="list_name">유저 Id</th>
              <th className="list_name">이메일</th>
              <th className="list_name">이름</th>
              <th className="list_name">생성일</th>
              <th className="list_name">수정일</th>
              <th className="list_name">대여 목록</th>

            </tr>
            </thead>

            <tbody>
            {
              users.map(user => {
                return (<tr className="tr" key={user.userId}>
                      <td className="">{user.userId}</td>

                      <td className="">{user.email}</td>
                      <td className="">{user.name}</td>
                      <td className="">{user.createdAt}</td>
                      <td className="">{user.modifiedAt}</td>
                      <td>
                        <Link to={{
                          pathname: "/users/rental"
                        }} state={{

                          user : {
                            userId: user.userId,
                            email : user.email,
                            name : user.name
                          }

                          }}>
                          <button className="booking_btn">대여 목록 보기
                          </button>
                        </Link>
                      </td>
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