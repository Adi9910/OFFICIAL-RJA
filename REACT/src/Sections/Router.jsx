import { useContext } from "react";
import { BrowserRouter, Route, Routes } from "react-router-dom";
import { AppContext } from "../Context/Context.jsx";

import Profile from "./Page/Auth/Profile/Profile.jsx";
import Login from "./Page/Auth/Sign-in/Sign.jsx";
import Register from "./Page/Auth/Register/Register.jsx";

import Home from "./Page/Main/Home/Home.jsx";
import Dashboard from "./Page/Main/Dashboard/Dashboard.jsx";
import About from "./Page/Main/About/About.jsx";


const AppRouter = () => {
  const { auth } = useContext(AppContext);

  return (
    <BrowserRouter>
      <Routes>
        {/* for any other route */}
        <Route path="*" element={<Home />} />

        {/* Main pages */}
        <Route path="/" element={<Home />} />
        <Route path="/about" element={<About/>} />
        <Route path="/dashboard" element={ auth === "fail"  ? <Login /> : <Dashboard/>} />

        {/* auth related */}
        <Route path="/profile" element={ auth === "fail"  ? <Login /> : <Profile />} />
        <Route path="/login" element={<Login />} />
        <Route path="/register" element={<Register />} />

      </Routes>
    </BrowserRouter>
  );
};
export default AppRouter;
