import { Suspense, useEffect, useCallback, useState, lazy } from "react";
import styled from "styled-components";
import { Box, Button, Container, CircularProgress } from "@material-ui/core";
import ControlMenu from "./DraggMenu";

const Universe = lazy(() => import("./mainAnimation/universe"));
const MagicBox = lazy(() => import("./mainAnimation/MagicBox"));
const Stars = lazy(() => import("./mainAnimation/Stars"));
const Forest = lazy(() => import("./mainAnimation/Forest"));
const Physics = lazy(() => import("./mainAnimation/Physics"));
const Test = lazy(() => import("./mainAnimation/Test"));

const Wrap = styled(Container)`
  margin: 0 auto;
  padding: 2rem;
  text-align: left;
  max-height: 100vh;
  overflow-y: auto;
  text-align: center;

  // 스크롤바
  &::-webkit-scrollbar {
    width: 6px;
  } /* 스크롤 바 */
  &::-webkit-scrollbar-track {
    background-color: "transparent";
  } /* 스크롤 바 밑의 배경 */

  &:hover {
    // 스크롤바
    &::-webkit-scrollbar {
      width: 6px;
    } /* 스크롤 바 */
    &::-webkit-scrollbar-track {
      background-color: transparent;
    } /* 스크롤 바 밑의 배경 */
    &::-webkit-scrollbar-thumb {
      background-color: #e6e6e6;
    } /* 실질적 스크롤 바 */
    &::-webkit-scrollbar-thumb:hover {
      background: #e6e6e6;
    } /* 실질적 스크롤 바 위에 마우스를 올려다 둘 때 */
    &::-webkit-scrollbar-thumb:active {
      background: #e6e6e6;
    } /* 실질적 스크롤 바를 클릭할 때 */
    &::-webkit-scrollbar-button {
      display: none;
    } /* 스크롤 바 상 하단 버튼 */
  }

  & img {
    max-width: 100%;
  }
`;

const MainBox = styled(Box)`
  position: relative;
  z-index: 2;
`;

const CanvasBox = styled(Box)`
  position: absolute;
  left: 0;
  top: 0;
  right: 0;
  bottom: 0;
  z-index: 1;
  overflow: hidden;

  canvas {
    && {
      position: absolute;
      left: 0;
      top: 0;
      right: 0;
      bottom: 0;
    }
  }
`;

const ButtonArea = styled(Box)`
  display: grid;
  grid-template-columns: 1fr 1fr 1fr;
  grid-gap: 10px;
`;

const SectionBox = styled(Box)`
  color: #fff;
`;

const ProgressStyle = styled(CircularProgress)`
  & svg {
    border-radius: 50%;
    background: rgba(255, 255, 255, 0.05);
  }

  & svg circle {
    stroke: #fff;
  }
`;

const _ = () => {
  // const [canvas, setCanvas] = useState("first");
  const [canvas, setCanvas] = useState("test");
  const [open, setOpen] = useState<boolean>(false);

  const SelectCanvas = useCallback((item: string) => {
    setCanvas(item);
  }, []);

  useEffect(() => {
    (document.querySelector("body") as HTMLBodyElement).style.overflow =
      "hidden";
    return () => {
      (document.querySelector("body") as HTMLBodyElement).style.overflow =
        "unset";
    };
  }, []);

  return (
    <>
      <Wrap>
        <MainBox>
          {/* <Typography component="h2" variant="h4">
            프로젝트 분다
          </Typography> */}
          <ControlMenu open={{ open, setOpen }}>
            <ButtonArea>
              <Button
                variant="contained"
                color="primary"
                onClick={() => {
                  SelectCanvas("first");
                  setOpen(false);
                }}
              >
                Universe
              </Button>
              <Button
                variant="contained"
                color="primary"
                onClick={() => {
                  SelectCanvas("first2");
                  setOpen(false);
                }}
              >
                Move Universe
              </Button>
              <Button
                variant="contained"
                color="primary"
                onClick={() => {
                  SelectCanvas("magicBox");
                  setOpen(false);
                }}
              >
                magicBox
              </Button>
              <Button
                variant="contained"
                color="primary"
                onClick={() => {
                  SelectCanvas("stars");
                  setOpen(false);
                }}
              >
                Stars
              </Button>
              <Button
                variant="contained"
                color="primary"
                onClick={() => {
                  SelectCanvas("forest");
                  setOpen(false);
                }}
              >
                Forest
              </Button>
              <Button
                variant="contained"
                color="primary"
                onClick={() => {
                  SelectCanvas("physics");
                  setOpen(false);
                }}
              >
                Physics
              </Button>
              <Button
                variant="contained"
                color="primary"
                onClick={() => {
                  SelectCanvas("test");
                  setOpen(false);
                }}
              >
                Test
              </Button>
            </ButtonArea>
          </ControlMenu>
          {/* <Box mt={2}>
            <img
              src={firstImage}
              alt="어두운 방안에 모니터와 컴퓨터 본체 키보드가 보이고 있다."
            />
          </Box> */}
          <SectionBox mt={4}></SectionBox>
        </MainBox>

        <Suspense fallback={<ProgressStyle />}>
          <CanvasBox>
            {canvas === "first" && <Universe />}
            {canvas === "first2" && <Universe type="controll" />}
            {canvas === "magicBox" && <MagicBox />}
            {canvas === "stars" && <Stars />}
            {canvas === "forest" && <Forest />}
            {canvas === "physics" && <Physics />}
            {canvas === "test" && <Test />}
          </CanvasBox>
        </Suspense>
      </Wrap>
    </>
  );
};

export default _;
