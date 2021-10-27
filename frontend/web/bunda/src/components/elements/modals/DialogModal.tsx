import React from "react";
import {
  makeStyles,
  useTheme,
  //Button,
  Dialog,
  DialogTitle,
  //DialogActions,
  DialogContent,
  Box,
  Typography,
  IconButton,
} from "@material-ui/core";

import { Close } from "@material-ui/icons";

const useDialogStyle = makeStyles({
  dialogTitle: {
    margin: 0,
    padding: 2,
  },

  dialogTitleClose: {
    position: "absolute",
    right: 1,
    top: 1,
  },
});

const _ = ({ ...props }) => {
  const {
    open,
    title,
    children,
    // confirmationText,
    // cancellationText,
    // confirmationButtonProps,
    // cancellationButtonProps,
    onCancel,
    scroll,
    fullWidth = true,
    maxWidth = "sm",
    className = null,
  } = props;

  const theme = useTheme();
  const useDialogClass = useDialogStyle();

  return (
    <Dialog
      className={className}
      fullWidth={fullWidth}
      maxWidth={maxWidth}
      scroll={scroll}
      open={open}
      aria-labelledby="alert-dialog-title"
      aria-describedby="alert-dialog-description"
    >
      <DialogTitle
        id="alert-dialog-title"
        className={useDialogClass.dialogTitle}
      >
        <Box
          ml={theme.typography.pxToRem(16)}
          display="flex"
          alignItems="center"
        >
          <Typography>{title}</Typography>

          <IconButton aria-label="close" onClick={onCancel}>
            <Close />
          </IconButton>
        </Box>
      </DialogTitle>

      <DialogContent dividers={scroll === "paper"}>{children}</DialogContent>
    </Dialog>
  );
};

export default _;
