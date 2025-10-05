import React, { useContext, useState } from "react";
import { Box, TextField, Button, Typography, Paper, Grid } from "@mui/material";
import st from "./Register.module.css";
import { toast } from "react-toastify";
import { RegisterApi } from "../../../Api/Calls.jsx";
import { useNavigate } from "react-router-dom";
import { AppContext } from "../../../../Context/Context.jsx";

const Register = () => {
  const { setAuth } = useContext(AppContext);

  const navigate = useNavigate();
  const [formData, setFormData] = useState({
    name: "Aditya Choudhary",
    email: "info9adi@gmail.com",
    mobile: "9910282537",
    password: "1234",
  });

  const [ifNotExists, setIfNotExists] = useState(true);

  const handleInputChange = (e) => {
    const { name, value } = e.target;
    setFormData((prevState) => ({
      ...prevState,
      [name]: value,
    }));
  };

  const handleSubmit = async () => {
    const apiCall = await RegisterApi(formData);
    if (apiCall.status === "success") {
      if (apiCall.data[0].value === "Email already exists") {

        toast(apiCall.data[0].value, { type: "error", autoClose: 2500 });
        localStorage.setItem("email", formData.email);
        setIfNotExists(false);
        setTimeout(() => {
          navigate("/login");
        }, 2500);

      } else if (
        apiCall.data[0].value ===
        "Name, email, mobile and password are required"
      ) {

        toast(apiCall.data[0].value, { type: "error", autoClose: 2000 });

      } else if (apiCall.data[0].value === "Data inserted successfully") {

        toast("Successfully Registered", {
          position: "bottom-right",
          type: "info",
          autoClose: 2000,
        });
        localStorage.setItem("account_id", apiCall.data[0].account_id);
        setAuth(apiCall.data[0].account_id);
        setTimeout(() => {
          navigate("/home");
        }, 2000);
      }
      
    } else {
      toast(apiCall.data[0].value, { type: "info", autoClose: 3000 });
    }
  };

  const fieldsAndInput = [
    { label: "Name", name: "name", type: "text", value: formData.name },
    { label: "Email", name: "email", type: "email", value: formData.email },
    {
      label: "Password",
      name: "password",
      type: "password",
      value: formData.password,
    },
    { label: "Mobile", name: "mobile", type: "tel", value: formData.mobile },
  ];

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
          Register
        </Typography>
        {fieldsAndInput.map((item) => (
          <Box sx={{ padding: "10px" }}>
            <TextField
              className={st.field}
              fullWidth
              label={item.label}
              name={item.name}
              value={item.value}
              onChange={handleInputChange}
              required
            />
          </Box>
        ))}

        <Box
          sx={{
            padding: 2,
            width: "100%",
            display: "flex",
            justifyContent: "space-evenly",
          }}
        >
          {ifNotExists ? <Button onClick={handleSubmit}>Confirm Registration</Button> : 
          <Button onClick={()=>navigate("/login")}>Redirect to Login</Button>}
          <Button onClick={() => navigate("/home")}>Back to home</Button>
        </Box>
      </Paper>
    </Box>
  );
};

export default Register;
