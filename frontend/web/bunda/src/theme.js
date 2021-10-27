import {
  createTheme,
  // responsiveFontSizes,
} from "@material-ui/core/styles";

let theme = createTheme({
  palette: {
    primary: {
      // light: "#95a5a6",
      main: "#34495e",
      dark: "#2c3e50",
      contrastText: "#fff",
    },
    secondary: {
      light: "#ff9ff3",
      main: "#9b59b6",
      dark: "#8e44ad",
      contrastText: "#fff",
    },
  },
  // typography: {
  //   fontFamily: [
  //     "Noto Sans",
  //     "Noto Sans KR",
  //     "-apple-system",
  //     "system-ui",
  //     "Segoe UI",
  //     "Roboto",
  //     "Helvetica Neue",
  //     "Arial",
  //     "sans-serif",
  //     "Apple Color Emoji",
  //     "Segoe UI Emoji",
  //     "Segoe UI Symbol",
  //     "Noto Color Emoji",
  //   ].join(","),
  // },
  breakpoints: {
    // sync with bootstrap
    values: {
      xs: 0,
      sm: 576,
      md: 768,
      lg: 1024, //992,
      xl: 1200,
    },
  },

  overrides: {
    MuiCssBaseline: {
      "@global": {
        html: {
          margin: 0,

          "& input:-webkit-autofill:focus": {
            WebkitBoxShadow: "0 0 0 30px white inset !important",
            transition: "background-color 5000s ease-in-out 0s",
          },
        },
      },
    },
  },
});

export default theme;
