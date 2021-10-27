/* eslint-disable @typescript-eslint/no-unused-vars */
import { useCallback, useState } from "react";
import { Button, Box, TextField, Dialog, Typography } from "@material-ui/core";
import { useForm, Controller, SubmitHandler } from "react-hook-form";
import styled from "styled-components";
import { COLOR } from "../../../commons";

interface IFormInput {
  email: string;
  password: string;
  confirmPassword: string;
  name: string;
  addressLine1: string;
  zipcode: number;
  extraAddress: string;
  tel: number;
  bdayDay: number;
  sex: string;
}

const LoginWrap = styled(Box)`
  display: flex;
  justify-content: center;
  align-items: center;
  width: 100%;
`;

const LoginInner = styled.form`
  margin: 0 auto;
  padding: 2rem;
  max-width: 560px;
  width: 100%;
  /* background-color: rgba(255, 255, 255, 0.2); */
  border-radius: 6px;
`;

const FormArea = styled.ul`
  & > li {
    display: grid;
    grid-template-columns: 3fr 1fr;
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

const ConfirmButton = styled(Button)`
  && {
    margin-left: 1rem;
    height: 100%;
    line-height: 1.6;
  }
`;

const AlertMessage = styled(Box)`
  margin-top: 0.5rem;
  text-align: left;
  p {
    font-size: 13px;
    color: ${COLOR.contrastText};
  }
`;

const _ = ({ next }: any) => {
  const { control, handleSubmit } = useForm<IFormInput>();
  const onSubmit: SubmitHandler<IFormInput> = (data) => {
    console.log(data);
  };

  // 조건이 달성되야 다음으로 넘어 갈 수 있음
  // const isValid = agreements[0].checked && agreements[1].checked;
  const isValid = true;

  return (
    <LoginWrap>
      <LoginInner
        noValidate
        autoComplete="off"
        onSubmit={handleSubmit((data) => onSubmit(data))}
      >
        <Box>
          <FormArea>
            <Box component="li">
              <Controller
                name="email"
                control={control}
                render={({ field }) => (
                  <TextField
                    id="email"
                    label="이메일"
                    variant="outlined"
                    color="primary"
                    //autoComplete="email"
                    autoFocus
                    fullWidth
                    {...field}
                  />
                )}
              />
              <ConfirmButton variant="contained" color="primary">
                인증코드 받기
              </ConfirmButton>
            </Box>
            <Box component="li">
              <Controller
                name="password"
                control={control}
                render={({ field }) => (
                  <TextField
                    id="password"
                    type="password"
                    label="인증번호 입력"
                    variant="outlined"
                    color="primary"
                    autoComplete="new-password"
                    fullWidth
                    {...field}
                  />
                )}
              />
              <ConfirmButton variant="contained" color="primary">
                인증확인
              </ConfirmButton>
            </Box>
          </FormArea>
          <AlertMessage>
            <Typography>30분이내에 인증코드를 입력</Typography>
          </AlertMessage>
          <LoginButtonArea
            mt={3}
            display="flex"
            justifyContent="center"
            alignItems="center"
          >
            <Buttons
              variant="contained"
              color="primary"
              onClick={() => {
                if (isValid) {
                  next();
                } else {
                  handleSubmit((data) => onSubmit(data));
                }
              }}
            >
              다음
            </Buttons>
          </LoginButtonArea>
        </Box>
      </LoginInner>
    </LoginWrap>
  );
};

export default _;
