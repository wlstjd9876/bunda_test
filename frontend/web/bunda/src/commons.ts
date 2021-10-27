import { useEffect, useRef } from "react";
import Axios from "axios";

// Styled
const COLOR = {
  text: "#fff",
  contrastText: "#b6b6b6",
  fill: "#34495e",
  dark: "#2c3e50",
  light: "#95a5a6",
  error: "#ff6757",
};

const consts = {
  CLIENT_ID: "ClientId",
  TOKEN_ID: "TokenId",
};

// 클라이언트 토큰
const getClientId = async () => {
  let clientId: string | null = localStorage.getItem(consts.CLIENT_ID);

  if (!clientId) {
    try {
      const { data, status } = await Axios(`주소 넣자`);

      if (status === 200 && data.code === 0) {
        clientId = data.result.client_id;
        localStorage.setItem(consts.CLIENT_ID, clientId);
      }
    } catch (error) {
      console.error(error);
      // alert(error.message);
    }
  }

  return clientId;
};
getClientId();

const clearLocalStorage = () => {
  const allKeys = Object.keys(localStorage);
  // const toBeDeleted = allKeys.filter(value => {
  //   return !this.doNotDeleteList.includes(value);
  // });
  allKeys.forEach((value) => {
    localStorage.removeItem(value);
  });
};

const axios = Axios.create({
  baseURL: `${process.env.REACT_APP_API_BASEURL}/campus/v1.0`,
  // withCredentials: false,
  // baseURL: `/campus/v1.0`,
});
axios.interceptors.request.use(
  async (config) => {
    // Do something before request is sent

    config.headers = {
      ...config.headers,
      //"Access-Control-Allow-Headers": "*",

      //"Content-Type": "application/json",
      "x-client-id": await getClientId(),
      ...(!config.forGuest
        ? {
            Authorization: !!config.headers.Authorization
              ? config.headers.Authorization
              : !!localStorage.getItem(consts.TOKEN_ID)
              ? localStorage.getItem(consts.TOKEN_ID)
              : "",
          }
        : {}),
      //withCredentials: true,

      // Authorization: `${token}`,
      // "Accept-Language": i18n.language,
    };
    // config.withCredentials = true;

    return config;
  },
  function (error) {
    // Do something with request error
    return Promise.reject(error);
  }
);

axios.interceptors.response.use(
  function (response) {
    return response;
  },
  function (error) {
    const { data, status } = error.response;
    if (status === 400 || status === 401) {
      if (data.cause === "NOT_EXIST_CLIENT") {
        clearLocalStorage();
      }

      if (data.cause === "INVALID_CLIENT") {
        clearLocalStorage();
      }

      if (data.cause === "INVALID_CLIENT_ID") {
        clearLocalStorage();
      }

      if (data.cause === "CLIENT_NOT_FOUNDED") {
        clearLocalStorage();
      }
    }
    return Promise.reject(error);
  }
);

const isHtml = (val: any) => {
  // replace html tag with content
  return !(val || "")
    .replace(/<([^>]+?)([^>]*?)>(.*?)<\/\1>/gi, "")
    // remove remaining self closing tags
    .replace(/(<([^>]+)>)/gi, "")
    // remove extra space at start and end
    .trim();
};
// 값이 빈값인지 체크하는 함수 !value하면 생기는 논리적 오류를 제거 [], {}도 빈값으로 처리
const isEmpty = (value: any) => {
  if (
    value === "" ||
    value === null ||
    value === undefined ||
    (value !== null && typeof value === "object" && !Object.keys(value).length)
  ) {
    return true;
  }
  return false;
};

const useInterval = (callback: any, delay: number) => {
  const savedCallback = useRef<any>();

  useEffect(() => {
    savedCallback.current = callback;
  });

  useEffect(() => {
    function tick() {
      savedCallback.current();
    }

    let id = setInterval(tick, delay);
    return () => clearInterval(id);
  }, [delay]);
};

export { COLOR, axios, isHtml, isEmpty, useInterval };
