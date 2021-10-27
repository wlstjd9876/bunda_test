import {
  Step,
  StepConnector,
  StepLabel,
  Stepper,
  withStyles,
} from "@material-ui/core";
import { COLOR } from "../../commons";

import styled from "styled-components";

const SignupGeneralStepper = withStyles({
  line: {
    borderColor: COLOR.text,
  },
})(StepConnector);

interface StepperInterface {
  activeStep: number;
  steps: any;
}

const StepperWrap = styled(Stepper)`
  && {
    background: none;
  }

  & .MuiStepIcon-active {
    fill: ${COLOR.text};
  }

  & .MuiStepIcon-completed path {
    fill: ${COLOR.fill};
  }

  & .MuiStepIcon-text {
    color: ${COLOR.text};
  }
`;

const StepLabels = styled(StepLabel)`
  & .MuiStepLabel-label {
    color: ${COLOR.fill};

    &.MuiStepLabel-active {
      font-weight: bold;
      color: ${COLOR.text};
    }
    &.MuiStepLabel-completed {
      color: ${COLOR.fill};
    }
  }

  & svg,
  & path {
    fill: ${COLOR.fill};
  }
  & .MuiStepIcon-text {
    fill: ${COLOR.fill};
    font-weight: bold;
  }
`;

const _ = ({ activeStep, steps }: StepperInterface) => {
  return (
    <StepperWrap
      alternativeLabel
      activeStep={activeStep}
      connector={<SignupGeneralStepper />}
    >
      {steps.map((label: string, i: number) => (
        <Step key={i}>
          <StepLabels>{label}</StepLabels>
        </Step>
      ))}
    </StepperWrap>
  );
};

export default _;
