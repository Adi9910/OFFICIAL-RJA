import { AppProvider } from "../Context/Context.jsx";

import sty from "./App.module.css";
import { Box, Button } from "@mui/material";

import AppRouter from "./Router.jsx";
import Header from "./Component/Header/Header.jsx";

function App() {
  const toggle = () => {
    const html =
      document.documentElement.getAttribute("theme") === "dark"
        ? "light"
        : "dark";
    document.documentElement.setAttribute("theme", html);
  };

  return (
    <Box className={sty.mainBox}>
      <AppProvider>
        <Header />

        <Button variant="contained" onClick={toggle}>
          TOGGLE
        </Button>

        <AppRouter />

        <Box className={sty.textColor}>{"hello "+import.meta.env.VITE_APP_URL}</Box>
      </AppProvider>
    </Box>
  );
}

export default App;
