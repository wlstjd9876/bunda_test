import { useState } from "react";
import { Box } from "@material-ui/core";
import styled from "styled-components";
import { COLOR } from "../../../commons";

import Stepper from "../../elements/Stepper";

import EmailAuthentication from "./1";
import Privacy from "./2";
import Confirm from "./3";

const steps = ["이메일 인증", "개인정보", "가입완료"];

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
  box-sizing: border-box;
`;

const Title = styled.h2`
  font-size: 32px;
  font-weight: 300;
  color: ${COLOR.text};
`;

const _ = () => {
  const [activeStep, setActiveStep] = useState<number>(0);

  return (
    <LoginWrap>
      <LoginInner>
        <Box mb={2}>
          <Title>회원가입</Title>
        </Box>
        <Box mb={2}>
          <Stepper activeStep={activeStep} steps={steps} />
        </Box>

        {activeStep === 0 && (
          <EmailAuthentication
            next={() => {
              setActiveStep(1);
            }}
          />
        )}
        {activeStep === 1 && (
          <Privacy
            next={() => {
              setActiveStep(2);
            }}
          />
        )}
        {activeStep === 2 && <Confirm />}
      </LoginInner>
    </LoginWrap>
  );
};

export default _;
