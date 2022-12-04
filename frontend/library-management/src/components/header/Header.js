import {Link} from "react-router-dom";

function Header(props) {
  return (
      <div className="nav">
        <div className="header_left">
          <p>{props.headername}</p>
        </div>
        <div className="header_right">
          <Link to="/">
            <span className="save">
              메인
            </span>
          </Link>

          {/*<span className="btn2">목록</span>*/}
          <Link to="/users">
            <span className="save">
              유저 목록 조회
            </span>
          </Link>
          <Link to="/books">

            <span className="save">책 목록 조회</span>
          </Link>
        </div>
      </div>

  );
}

export default Header