import {createContext, useState} from "react";

export const AppContext = createContext();

export const AppProvider = ({children}) => {
    const [auth, setAuth] = useState(localStorage.getItem("account_id") || "fail");

    const value = {
        auth,
        setAuth
    };

    return (
        <AppContext.Provider value={value}>{children}</AppContext.Provider>
    );

}   
