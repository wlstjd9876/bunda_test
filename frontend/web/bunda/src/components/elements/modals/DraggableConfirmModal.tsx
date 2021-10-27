import { useEffect } from "react";
import {
  Button,
  Dialog,
  DialogTitle,
  DialogActions,
  DialogContent,
  DialogContentText,
  Paper,
  makeStyles,
  Typography,
} from "@material-ui/core";
import Draggable from "react-draggable";

import { Close } from "@material-ui/icons";

const useModalStyle = makeStyles((theme) => ({
  titleArea: {
    padding: `${theme.typography.pxToRem(12)} ${theme.typography.pxToRem(16)}`,
  },
  close: {
    position: "absolute",
    top: theme.typography.pxToRem(12),
    right: theme.typography.pxToRem(12),
    padding: theme.typography.pxToRem(4),
    minWidth: "auto",
  },
}));

const _ = ({ ...props }) => {
  const {
    open,
    title,
    message,
    confirmationText,
    cancellationText,
    confirmationButtonProps,
    cancellationButtonProps,
  } = props;
  const useModalClass = useModalStyle();

  useEffect(() => {
    const Bodys = document.querySelector("body") as HTMLElement;
    Bodys.classList.add("draggabled");
    return () => {
      Bodys.classList.remove("draggabled");
    };
  }, []);

  return (
    <Dialog
      open={open}
      //onClose={handleClose}
      PaperComponent={PaperComponent}
      aria-labelledby="draggable-dialog-title"
      maxWidth="sm"
      fullWidth
    >
      <DialogTitle
        className={useModalClass.titleArea}
        style={{ cursor: "move" }}
        id="draggable-dialog-title"
      >
        <Typography component="span">{title}</Typography>
      </DialogTitle>
      <DialogContent>
        <DialogContentText>{message}</DialogContentText>
      </DialogContent>
      <DialogActions>
        <Button autoFocus {...cancellationButtonProps} color="primary">
          {cancellationText}
        </Button>
        <Button {...confirmationButtonProps} color="primary">
          {confirmationText}
        </Button>
      </DialogActions>
      <Button className={useModalClass.close} {...cancellationButtonProps}>
        <Close />
      </Button>
    </Dialog>
  );
};

export default _;

const PaperComponent = (props: any) => {
  return (
    <Draggable
      handle="#draggable-dialog-title"
      cancel={'[class*="MuiDialogContent-root"]'}
    >
      <Paper {...props} />
    </Draggable>
  );
};
