import { useState, useEffect, useCallback } from "react";
import styled from "styled-components";
import {
  Box,
  Button,
  Drawer,
  List,
  ListItem,
  ListItemText,
} from "@material-ui/core";
import { Link, useHistory } from "react-router-dom";
import { Menu } from "@material-ui/icons";
import clsx from "clsx";

import logoImg from "../images/Logo_white.png";

const navItems = {
  menuList: [
    { title: "Home", address: "/" },
    { title: "Login", address: "/login" },
  ],
};

const HeaderWrap = styled.header`
  && {
    position: fixed;
    top: 0;
    left: 0;
    right: 0;
    margin: 0 auto;
    padding: 1rem 2rem;
    max-width: 1280px;
    width: 100%;
    /* background-color: rgba(52, 73, 94, 0.2); */
    text-align: center;
    color: #fff;
    box-sizing: border-box;
    z-index: 1000;
  }
`;

// 로고 정의
const LogoArea = styled(Box)`
  display: flex;
  justify-content: center;
  align-items: center;

  &.active .inner {
    width: 156px;
    height: 153px;
    /* height: 104px; */
    &:hover {
      transform: rotate3d(1, 1, 1, 25deg);
    }
  }

  &.mobile .inner {
    width: 106px;
    height: 104px;
  }

  .inner {
    width: 106px;
    height: 153px;
    overflow: hidden;
    transition: 0.3s;
  }

  & img {
    max-width: 100%;
  }
`;

const LinkList = styled(Link)`
  color: #fff;
  text-shadow: 0px 0px 4px black;
  & + & {
    margin-left: 1rem;
  }
`;

const MenuList = styled.nav`
  position: absolute;
  right: 40px;
  top: 30px;
  bottom: 0;
  display: flex;
`;

const MobileMenuArea = styled.nav`
  position: absolute;
  right: 40px;
  top: 30px;
  bottom: 0;
`;

const MobileMenuButton = styled(Button)`
  && {
    min-width: auto;
  }
`;

const MobileMenuList = styled(Box)`
  display: flex;
  padding: 0;
  width: 150px;
  justify-content: center;

  & ul {
    width: 100%;
  }

  & .MuiListItem-button {
    text-align: center;
  }
`;
interface Iplatform {
  platform: string;
}
interface ToggleTypeInterface {
  type: string;
  key: string;
}

const _ = ({ platform }: Iplatform) => {
  const history = useHistory();

  // useState
  const [scrollTopAnimation, setScrollTopAnimation] = useState(false);
  const [scrollTopValue, setScrollTopValue] = useState({ top: window.scrollY });
  const [mobileMenu, setMobileMenu] = useState<boolean>(false);

  // variable
  const toggleDrawer = useCallback(
    (open: boolean) => (event: ToggleTypeInterface) => {
      if (
        event.type === "keydown" &&
        (event.key === "Tab" || event.key === "Shift")
      ) {
        return;
      }

      setMobileMenu(open);
    },
    []
  );

  const mobileLinkClick = useCallback(
    (address: string) => {
      history.push(address);
      toggleDrawer(false);
    },
    [history, toggleDrawer]
  );

  // useEffect
  useEffect(() => {
    const scrollAction = () => {
      setScrollTopValue({ top: window.scrollY });
    };

    let throttle: null = null;
    if (!throttle) {
      setTimeout(() => {
        window.addEventListener("scroll", scrollAction);
        throttle = null;
      }, 500);
    }
    return () => {
      if (!throttle) {
        setTimeout(() => {
          window.removeEventListener("scroll", scrollAction);
          throttle = null;
        }, 500);
      }
    };
  }, []);

  useEffect(() => {
    if (scrollTopValue.top > 0) {
      setScrollTopAnimation(true);
    } else {
      setScrollTopAnimation(false);
    }
  }, [scrollTopValue]);

  return (
    <>
      <HeaderWrap>
        <LogoArea
          className={clsx(scrollTopAnimation ? "active" : "", platform)}
        >
          <Box className="inner">
            <LinkList to="/">
              <img src={logoImg} alt="분다 로고" />
            </LinkList>
          </Box>
        </LogoArea>

        {platform === "mobile" ? (
          <>
            <MobileMenuArea>
              <MobileMenuButton onClick={toggleDrawer(true)}>
                <Menu />
              </MobileMenuButton>
            </MobileMenuArea>

            <Drawer
              anchor="right"
              open={mobileMenu}
              onClose={toggleDrawer(false)}
            >
              <MobileMenuList>
                <List>
                  {navItems.menuList.map((props, i) => (
                    <Box component="li" key={i}>
                      <ListItem
                        button
                        onClick={() => mobileLinkClick(props.address)}
                      >
                        <ListItemText primary={props.title} />
                      </ListItem>
                    </Box>
                  ))}
                </List>
              </MobileMenuList>
            </Drawer>
          </>
        ) : (
          <MenuList>
            <Box component="ul">
              <Box component="li">
                <LinkList to="/">Home</LinkList>
                <LinkList to="/login">Login</LinkList>
              </Box>
            </Box>
          </MenuList>
        )}
      </HeaderWrap>
    </>
  );
};

export default _;
