/* eslint-disable @typescript-eslint/no-unused-vars */
import { Button, Box, TextField, Typography } from "@material-ui/core";
import { useForm } from "react-hook-form";
import styled from "styled-components";
import { COLOR } from "../../../commons";

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

const LoginInner = styled.form`
  margin: 0 auto;
  padding: 2rem;
  max-width: 360px;
  width: 100%;
  /* background-color: rgba(255, 255, 255, 0.2); */
  border-radius: 6px;
`;

const Title = styled.h2`
  font-size: 32px;
  font-weight: 300;
  color: ${COLOR.text};
`;

const FormArea = styled.ul`
  margin-top: 2rem;
  & > li {
    display: flex;
    justify-content: center;
    align-items: center;
  }

  & > li + li {
    margin-top: 1rem;
  }

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
  const { handleSubmit } = useForm<Inputs>();

  const onSubmit = (data: any) => {
    // alert(JSON.stringify(data));
    alert("얼럿");
  };

  return (
    <LoginWrap>
      <LoginInner
        noValidate
        autoComplete="off"
        onSubmit={handleSubmit((data) => onSubmit(data))}
      >
        <Box>
          <Title>아이디 찾기</Title>
        </Box>
        <Box>
          <FormArea>
            <Box component="li">
              <TextField
                // inputRef={register}
                id="name"
                label="이름"
                variant="outlined"
                color="primary"
                name="name"
                autoComplete="name"
                autoFocus
                fullWidth
              />
            </Box>
            <Box component="li">
              <TextField
                // inputRef={register}
                id="tel"
                label="전화번호"
                variant="outlined"
                color="primary"
                name="tel"
                autoComplete="tel"
                fullWidth
              />
            </Box>
            <Box component="li">
              <TextField
                // inputRef={register}
                id="certification"
                label="인증번호"
                variant="outlined"
                color="primary"
                name="certification"
                fullWidth
              />
            </Box>
          </FormArea>
          <LoginButtonArea
            mt={3}
            display="flex"
            justifyContent="center"
            alignItems="center"
          >
            <Buttons
              variant="contained"
              color="primary"
              onClick={handleSubmit((data) => onSubmit(data))}
            >
              아이디 찾기
            </Buttons>
          </LoginButtonArea>
        </Box>
      </LoginInner>
    </LoginWrap>
  );
};

export default _;
