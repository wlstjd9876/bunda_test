/* eslint-disable @typescript-eslint/no-unused-vars */
import { useState } from "react";
import {
  Button,
  Box,
  TextField,
  FormGroup,
  FormControlLabel,
  Checkbox,
  Typography,
} from "@material-ui/core";
import { useForm } from "react-hook-form";
import { useHistory } from "react-router-dom";
import styled from "styled-components";

import { axios, COLOR } from "../../commons";

type Inputs = {
  example: string;
  exampleRequired: string;
};

const LoginWrap = styled(Box)`
  display: flex;
  justify-content: center;
  align-items: center;
  width: 100%;
`;

const FormWrap = styled.form`
  margin: 0 auto;
  padding: 2rem;
  max-width: 360px;
  width: 100%;
  /* background-color: rgba(255, 255, 255, 0.3); */
  border-radius: 6px;
`;

const FormArea = styled(Box)`
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

const BottomFunctionArea = styled(Box)`
  & * {
    color: ${COLOR.text} !important;
    border-color: ${COLOR.text} !important;
  }
`;

const ErrorMessage = styled(Typography)`
  && {
    margin-top: 0.5rem;
    margin-bottom: -0.5rem;
    font-size: 13px;
    color: ${COLOR.error} !important;
    text-align: left;
  }
`;

const _ = () => {
  const history = useHistory();
  const { handleSubmit } = useForm<Inputs>();

  const onSubmit = (data: any) => {
    alert("얼럿");
  };

  const [state, setState] = useState({
    checkedA: false,
  });

  interface PasswordCheck {
    target: {
      name: string;
      checked: boolean;
    };
  }

  const handleChange = (event: PasswordCheck) => {
    setState({ ...state, [event.target.name]: event.target.checked });
  };

  const checkedClick = (props: PasswordCheck) => {
    handleChange(props);
  };

  console.log(process.env);

  return (
    <LoginWrap>
      <FormWrap
        noValidate
        autoComplete="off"
        onSubmit={handleSubmit((data) => onSubmit(data))}
      >
        <Box display="flex" justifyContent="center" alignItems="stretch">
          <FormArea>
            <Box display="flex" justifyContent="flex-end" alignItems="center">
              <TextField
                // inputRef={register}
                id="email"
                label="이메일 입력"
                variant="outlined"
                color="primary"
                name="email"
                // autoComplete="email"
                autoFocus
              />
            </Box>
            <Box
              mt={2}
              display="flex"
              justifyContent="center"
              alignItems="center"
            >
              <TextField
                id="password"
                type="password"
                name="password"
                label="비밀번호를 입력"
                variant="outlined"
                color="primary"
                // autoComplete="current-password"
              />
            </Box>
          </FormArea>
          <LoginButtonArea
            ml={3}
            display="flex"
            justifyContent="center"
            alignItems="center"
          >
            <Buttons
              variant="contained"
              color="primary"
              onClick={handleSubmit((data) => onSubmit(data))}
            >
              로그인
            </Buttons>
          </LoginButtonArea>
        </Box>
        <BottomFunctionArea mt={1}>
          <FormGroup>
            <FormControlLabel
              control={
                <Checkbox
                  checked={state.checkedA}
                  onChange={checkedClick}
                  name="checkedA"
                  color="primary"
                />
              }
              label="로그인 상태 유지"
            />
          </FormGroup>
          <Box>
            <Button onClick={() => history.push("/findid")}>아이디 찾기</Button>
            <Typography component="span">|</Typography>
            <Button onClick={() => history.push("/findpassword")}>
              비밀번호 찾기
            </Button>
            <Typography component="span">|</Typography>
            <Button onClick={() => history.push("/signup")}>회원가입</Button>
          </Box>
        </BottomFunctionArea>
      </FormWrap>
    </LoginWrap>
  );
};

export default _;
