import { BrowserRouter as Router, Switch, Route } from "react-router-dom";
import platform from "platform";

import Container from "./Container";
import Main from "./main/Main";
import Login from "./auth/Login";
import SignUp from "./auth/signup/0";
import Findid from "./auth/findid/0";
import Findpassword from "./auth/findpassword/0";

const _ = () => {
  return (
    <>
      <Router>
        <Container platform={platform}>
          <Switch>
            <Route path="/" exact>
              <Main />
            </Route>
            <Route path="/login" exact>
              <Login />
            </Route>
            <Route path="/signup" exact>
              <SignUp />
            </Route>
            <Route path="/findid" exact>
              <Findid />
            </Route>
            <Route path="/findpassword" exact>
              <Findpassword />
            </Route>
          </Switch>
        </Container>
      </Router>
    </>
  );
};

export default _;
