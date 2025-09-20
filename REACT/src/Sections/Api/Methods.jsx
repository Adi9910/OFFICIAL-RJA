import axios from "axios";

const API_BASE_URL = import.meta.env.VITE_APP_URL; // Adjust the base URL as needed

const getData = (url) => {
  return new Promise(async (resolve) => {
    try {
      const response = await axios.get(API_BASE_URL + url);
      resolve(response.data);
    } catch (error) {
      resolve(error);
    }
  });
};

const postData = (url, data) => {
  return new Promise(async (resolve) => {
    try {
      const response = await axios.post(API_BASE_URL + url, data, {
        headers: {
          "Content-Type": "application/json",
        },
      });
      resolve(response.data);
    } catch (error) {
      resolve(error);
    }
  });
};

const patchData = (url, data) => {
  return new Promise(async (resolve) => {
    try {
      const response = await axios.patch(API_BASE_URL + url, data);
      resolve(response.data);
    } catch (error) {
      resolve(error);
    }
  });
};

const putData = (url, data) => {
  return new Promise(async (resolve) => {
    try {
      const response = await axios.put(API_BASE_URL + url, data);
      resolve(response.data);
    } catch (error) {
      resolve(error);
    }
  });
};

const deleteData = (url) => {
  return new Promise(async (resolve) => {
    try {
      const response = await axios.delete(API_BASE_URL + url);
      resolve(response.data);
    } catch (error) {
      resolve(error);
    }
  });
};

export {
    getData,
    postData,
    patchData,
    putData,
    deleteData
}
