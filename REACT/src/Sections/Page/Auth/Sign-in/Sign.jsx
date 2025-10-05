import React, { useContext, useState } from "react";
import { useNavigate } from "react-router-dom";
import { toast } from "react-toastify";
import { Box, TextField, Button, Typography, Paper } from "@mui/material";
import st from "./Sign.module.css";
import { LoginApi, LoginWithOtp } from "../../../Api/Calls.jsx";
import { AppContext } from "../../../../Context/Context.jsx";
import OtpInput from "./OTP.jsx";

const Login = () => {
  const [formData, setFormData] = useState({
    email: "info9adi@gmail.com",
    password: "",
  });

  const [OTP, setOTP] = useState(false);
  const [OtpCopy, setOtpCopy] = useState(["", "", "", "", "", ""]);
  const [fetchedOTP, setFetchedOTP] = useState(["", "", "", "", "", ""]);

  const { setAuth } = useContext(AppContext);
  const navigate = useNavigate();

  const handleInputChange = (e) => {
    const { name, value } = e.target;
    setFormData((prevState) => ({
      ...prevState,
      [name]: value,
    }));
  };

  const handleSubmit = async () => {
    const data = {
      email: formData.email,
      password: OTP ? "" : formData.password,
      otp: OTP ? OtpCopy.join("") : "",
    };
    if (OtpCopy.join("") !== fetchedOTP && OTP) {
      toast("Invalid OTP", { type: "error", autoClose: 2000 });
    } else {
      const apiCall = await LoginApi(data);
      if (apiCall.status === "success") {
        toast("Successfully Logged In", { type: "info", autoClose: 2000 });
        localStorage.setItem("account_id", apiCall.data[0].account_id);
        setAuth(apiCall.data[0].account_id);
        setTimeout(() => navigate("/home"), 2000);
      } else {
        toast("Fail to login", { type: "error", autoClose: 2000 });
      }
    }
  };

  const fieldsAndInput = [
    { label: "Email", name: "email", type: "email", value: formData.email },
    {
      label: "Password",
      name: "password",
      type: "password",
      value: formData.password,
    },
  ];

  const handleOtp = async () => {
    setOTP(true);
    const otpCall = await LoginWithOtp(formData);
    if (otpCall.status === "success" && otpCall.data[0].value === "exist") {
      toast("OTP sent successfully", { type: "info", autoClose: 3000 });
      setFetchedOTP(otpCall.data[0].otp);
      setOTP(true);
    } else {
      toast("Fail to send OTP", { type: "error", autoClose: 3000 });
    }
  };

  return (
    <Box
      sx={{
        display: "flex",
        justifyContent: "center",
        alignItems: "center",
        minHeight: "60vh",
        padding: 2,
      }}
    >
      <Paper
        elevation={3}
        sx={{
          padding: 4,
          width: "100%",
          maxWidth: 400,
        }}
        className={st.paper}
      >
        <Typography variant="h4" align="center" gutterBottom>
          Login
        </Typography>
        <Box sx={{ padding: "10px" }}>
          <TextField
            className={st.field}
            fullWidth
            label={"Email"}
            name={"email"}
            value={formData.email}
            onChange={handleInputChange}
            required
          />
        </Box>

        {OTP ? (
          <OtpInput setOtpCopy={setOtpCopy} />
        ) : (
          <>
            <Box sx={{ padding: "10px" }}>
              <TextField
                className={st.field}
                fullWidth
                label={"Password"}
                name={"password"}
                value={formData.password}
                onChange={handleInputChange}
                required
              />
            </Box>
            <Button onClick={handleOtp}>Login via OTP</Button>
          </>
        )}

        <Box
          sx={{
            padding: 2,
            width: "100%",
            display: "flex",
            justifyContent: "space-evenly",
          }}
        >
          <Button disabled={OTP && OtpCopy.join("").length !== 6} onClick={handleSubmit}>Sign in</Button>
          <Button onClick={() => navigate("/home")}>Back to home</Button>
        </Box>
      </Paper>
    </Box>
  );
};
export default Login;
