import { Button, Box, Typography } from "@material-ui/core";
import { useHistory } from "react-router-dom";
import styled from "styled-components";
import { COLOR } from "../../../commons";

import { ReactComponent as Checked } from "../../../images/checked_icon.svg";

const LoginWrap = styled(Box)`
  display: flex;
  justify-content: center;
  align-items: center;
  width: 100%;
`;

const LoginInner = styled(Box)`
  margin: 0 auto;
  padding: 2rem;
  max-width: 560px;
  width: 100%;
  /* background-color: rgba(255, 255, 255, 0.2); */
  border-radius: 6px;

  & * {
    color: ${COLOR.text} !important;
    border-color: ${COLOR.text} !important;
  }
`;

const Buttons = styled(Button)`
  width: 100%;
  height: 100%;
  && {
    min-width: 128px;
    font-size: 1rem;
  }
`;

const LoginButtonArea = styled(Box)`
  & .MuiButton-label {
    font-size: 18px;
    font-weight: bold;
  }
`;

const IconBox = styled(Box)`
  svg {
    width: 160px;
    height: auto;

    path {
      fill: ${COLOR.text};
    }

    circle {
      fill: ${COLOR.text};
    }
  }
`;

const TextBox = styled(Box)`
  p {
    font-size: 18px;
    color: #34495e;
    font-weight: bold;
  }
`;

const _ = () => {
  const history = useHistory();

  return (
    <LoginWrap>
      <LoginInner>
        <Box>
          <LoginButtonArea
            mt={3}
            display="flex"
            justifyContent="center"
            alignItems="center"
          >
            <Box>
              <IconBox>
                <Checked />
              </IconBox>
              <TextBox mt={2}>
                <Typography>회원가입이 완료 되었습니다</Typography>
              </TextBox>
              <Box mt={3}>
                <Buttons
                  variant="contained"
                  color="primary"
                  onClick={() => history.push("/login")}
                >
                  로그인 하러가기
                </Buttons>
              </Box>
            </Box>
          </LoginButtonArea>
        </Box>
      </LoginInner>
    </LoginWrap>
  );
};

export default _;
