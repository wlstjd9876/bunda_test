import React from "react";
import ReactDOM from "react-dom";
import reportWebVitals from "./reportWebVitals";
import RawRouter from "./components/RawRouter";
import { ThemeProvider } from "@material-ui/core/styles";
import theme from "./theme copy";

ReactDOM.render(
  <React.StrictMode>
    <ThemeProvider theme={theme}>
      <RawRouter />
    </ThemeProvider>
  </React.StrictMode>,
  document.getElementById("root")
);

// If you want to start measuring performance in your app, pass a function
// to log results (for example: reportWebVitals(console.log))
// or send to an analytics endpoint. Learn more: https://bit.ly/CRA-vitals
reportWebVitals();
