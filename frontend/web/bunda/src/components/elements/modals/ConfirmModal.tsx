import React, { isValidElement } from "react";
import {
  Button,
  Dialog,
  DialogTitle,
  DialogActions,
  DialogContent,
  DialogContentText,
} from "@material-ui/core";

import parse from "html-react-parser";

import { isHtml } from "../../../commons";

const _ = ({ ...props }) => {
  const {
    open,
    title = "",
    message = "",
    confirmationText = "예",
    cancellationText = "아니오",
    confirmationButtonProps,
    cancellationButtonProps,
    fullWidth = true,
    maxWidth = "sm",
    className = null,
  } = props;

  console.log(props);

  return (
    <Dialog
      className={className}
      fullWidth={fullWidth}
      maxWidth={maxWidth}
      open={open}
      aria-labelledby="alert-dialog-title"
      aria-describedby="alert-dialog-description"
    >
      <DialogTitle id="alert-dialog-title">{title}</DialogTitle>
      <DialogContent>
        <DialogContentText id="alert-dialog-description">
          {isValidElement(message)
            ? message
            : isHtml(message)
            ? parse(message)
            : message}
        </DialogContentText>
      </DialogContent>
      <DialogActions>
        <Button color="primary" {...confirmationButtonProps} autoFocus>
          {confirmationText}
        </Button>
        <Button color="primary" {...cancellationButtonProps}>
          {cancellationText}
        </Button>
      </DialogActions>
    </Dialog>
  );
};

export default _;
