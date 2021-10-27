import { isValidElement } from "react";
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

const _ = ({ ...props }: any) => {
  const {
    open,
    title = "",
    message = "",
    confirmationText = "확인",
    fullWidth = true,
    maxWidth = "sm",
    className = null,
    dialogClose = undefined,
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
      disableBackdropClick={true}
      onBackdropClick={() => {
        dialogClose();
      }}
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
        <Button color="primary" onClick={dialogClose} autoFocus>
          {confirmationText}
        </Button>
      </DialogActions>
    </Dialog>
  );
};

export default _;
