import { useEffect, useState } from "react";
import styled from "styled-components";
import { Box } from "@material-ui/core";
import GlobalStyle from "../styles/common";
import { ThemeProvider } from "@material-ui/core/styles";
import theme from "../theme";
import Header from "./Header";
import Footer from "./Footer";

const ContainerWrap = styled(Box)`
  && {
    position: relative;
    padding: 200px 0 380px;
    max-width: none;
    min-height: 100vh;
    display: flex;
    justify-content: center;
    align-items: stretch;
    text-align: center;
    background: rgb(170, 75, 107);
    background: linear-gradient(
      135deg,
      rgba(170, 75, 107, 1) 0%,
      rgba(107, 107, 131, 1) 35%,
      rgba(59, 141, 153, 1) 100%
    );
    box-sizing: border-box;

    &.mobile {
      padding: 200px 0;
    }
  }
  main {
    width: 100%;
    display: flex;
    justify-content: center;
    align-items: stretch;
    text-align: center;
  }
`;

interface ContainerInterface {
  children: JSX.Element;
  platform: {
    os: {
      family: string;
    };
  };
}

const _ = ({ children, platform }: ContainerInterface) => {
  const platformProp = platform.os.family;
  const [isMobile, setIsMobile] = useState<string>("");

  useEffect(() => {
    if (platformProp === ("Android" || "iOS")) {
      setIsMobile("mobile");
    }
  }, [platformProp]);

  return (
    <>
      <ThemeProvider theme={theme}>
        <ContainerWrap className={isMobile}>
          <GlobalStyle />
          <Header platform={isMobile} />
          <Box component="main">{children}</Box>
          <Footer platform={isMobile} />
        </ContainerWrap>
      </ThemeProvider>
    </>
  );
};

export default _;
