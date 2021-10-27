import React, { useState } from "react";
import "./App.css";
import {
  Button,
  Box,
  Container,
  Typography,
  TextField,
  FormGroup,
  FormControlLabel,
  Checkbox,
} from "@material-ui/core";
import { useForm } from "react-hook-form";
import styled from "styled-components";

export default function App() {
  const { register, handleSubmit } = useForm();

  const onSubmit = (data: any) => {
    alert(JSON.stringify(data));
  };

  const [state, setState] = useState({
    checkedA: false,
  });

  interface PasswordSaveChecked {
    target: {
      name: string;
      checked: boolean;
    };
  }

  const handleChange = (event: PasswordSaveChecked) => {
    setState({ ...state, [event.target.name]: event.target.checked });
  };

  const LoginWrap = styled.form`
    margin: 0 auto;
    padding: 2rem;
    max-width: 480px;
    background-color: rgba(255, 255, 255, 0.05);
    border-radius: 6px;
  `;

  const Buttons = styled(Button)`
    width: 100%;
    height: 100%;
    && {
      min-width: 128px;
      font-size: 1rem;
    }
  `;

  return (
    <div className="App">
      <header className="App-header">
        <Container>
          <Box mb={3}>
            <Typography variant="h4" component="h2">
              로그인
            </Typography>
          </Box>
          <LoginWrap
            noValidate
            autoComplete="off"
            onSubmit={handleSubmit(onSubmit)}
          >
            <Box display="flex" justifyContent="center" alignItems="stretch">
              <Box>
                <Box
                  display="flex"
                  justifyContent="flex-end"
                  alignItems="center"
                >
                  <Box mr={2}>
                    <Typography>이메일</Typography>
                  </Box>
                  <TextField
                    id="outlined-basic"
                    label="이메일 입력"
                    variant="outlined"
                    {...register("firstName")}
                    color="secondary"
                  />
                </Box>
                <Box
                  mt={2}
                  display="flex"
                  justifyContent="center"
                  alignItems="center"
                >
                  <Box mr={2}>
                    <Typography>비밀번호</Typography>
                  </Box>
                  <TextField
                    id="outlined-basic"
                    label="비밀번호를 입력"
                    variant="outlined"
                    color="secondary"
                  />
                </Box>
              </Box>
              <Box
                ml={3}
                display="flex"
                justifyContent="center"
                alignItems="center"
              >
                <Buttons variant="contained" color="secondary">
                  로그인
                </Buttons>
              </Box>
            </Box>
            <Box mt={1} pl={3}>
              <FormGroup>
                <FormControlLabel
                  control={
                    <Checkbox
                      checked={state.checkedA}
                      onChange={handleChange}
                      name="checkedA"
                      color="secondary"
                    />
                  }
                  label="비밀번호 저장"
                />
              </FormGroup>
            </Box>
          </LoginWrap>
        </Container>
      </header>
    </div>
  );
}
