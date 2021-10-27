import { useRef } from "react";
import {
  Box,
  Button,
  Dialog,
  DialogActions,
  DialogContent,
  DialogTitle,
} from "@material-ui/core";
import Draggable from "react-draggable";
import styled from "styled-components";

import { ReactComponent as Library } from "../../images/cube_icon.svg";

const Wrap = styled(Box)`
  text-align: right;
`;

const DialogArea = styled(Box)``;

interface IMenu {
  children: JSX.Element;
  open: {
    open: boolean;
    setOpen: any;
  };
}

const DraggablePaper = styled.div`
  background: rgba(255, 255, 255, 0.3);
`;

function PaperComponent(props: any) {
  return (
    <Draggable
      handle="#draggable-dialog-title"
      cancel={'[class*="MuiDialogContent-root"]'}
    >
      <DraggablePaper {...props} />
    </Draggable>
  );
}

const ThemeSelectButton = styled(Button)`
  && {
    border-radius: 50%;
    opacity: 0;
    transition: 0.5s;
  }
  &.active {
    opacity: 1;
  }

  & svg g polygon,
  & svg g path {
    fill: #fff;
    transition: fill 1.5s ease;
    -webkit-transition: fill 1.5s ease;
    animation: boxColor 8s infinite linear alternate;

    @keyframes boxColor {
      0% {
        fill: #fff;
      }
      14% {
        fill: #34495e;
      }
      28% {
        fill: #2c3e50;
      }
      42% {
        fill: #95a5a6;
      }
      57% {
        fill: #8e44ad;
      }
      71% {
        fill: #9b59b6;
      }
      85% {
        fill: #ff9ff3;
      }

      100% {
        fill: #fff;
      }
    }
  }
`;

export default function DraggableDialog({ children, open }: IMenu) {
  const buttonAnimation = useRef(null);

  const handleClickOpen = () => {
    open.setOpen(true);
  };

  const handleClose = () => {
    open.setOpen(false);
  };

  return (
    <Wrap>
      <ThemeSelectButton
        className={open.open === false ? "active" : ""}
        onClick={handleClickOpen}
      >
        <Library ref={buttonAnimation} />
      </ThemeSelectButton>

      <Dialog
        open={open.open}
        onClose={handleClose}
        PaperComponent={PaperComponent}
        aria-labelledby="draggable-dialog-title"
        BackdropProps={{ style: { display: "none" } }}
      >
        <DialogArea>
          <DialogTitle style={{ cursor: "move" }} id="draggable-dialog-title">
            ThreeJs theme selection Menu
          </DialogTitle>
          <DialogContent>{children}</DialogContent>
          <DialogActions>
            <Button autoFocus onClick={handleClose}>
              Cancel
            </Button>
          </DialogActions>
        </DialogArea>
      </Dialog>
    </Wrap>
  );
}
