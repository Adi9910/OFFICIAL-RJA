import { postData } from "./Methods";

const RegisterApi = (data) => {
  return new Promise(async (resolve) => {
    try {
      const response = await postData("/user/append", data);
      resolve({ status: "success", data: response });
    } catch (error) {
      resolve({ status: "error", data: "No data" });
    }
  });
};

const LoginApi = (data) => {
  return new Promise(async (resolve) => {
    try {
      const response = await postData("/user/login", data);
      resolve({ status: "success", data: response });
    } catch (error) {
      resolve({ status: "error", data: "No data" });
    }
  });
};

const ValidateEmail = (data) => {
  return new Promise(async (resolve) => {
    try {
      const response = await postData("/user/validate", data);
      resolve({ status: "success", data: response });
    } catch (error) {
      resolve({ status: "error", data: "No data" });
    }
  });
};

const LoginWithOtp = (data) => {
  return new Promise(async (resolve) => {
    try {
      const response = await postData("/user/verify", data);
      resolve({ status: "success", data: response });
    } catch (error) {
      resolve({ status: "error", data: "No data" });
    }
  });
};

export { RegisterApi, LoginApi, LoginWithOtp, ValidateEmail };
