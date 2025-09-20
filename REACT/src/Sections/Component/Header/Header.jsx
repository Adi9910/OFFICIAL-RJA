import * as React from "react";
import { AppContext } from "../../../Context/Context";

import {
  Box,
  Drawer,
  Button,
  List,
  Divider,
  ListItem,
  ListItemButton,
  ListItemIcon,
  ListItemText,
} from "@mui/material";
import InboxIcon from "@mui/icons-material/MoveToInbox";
import MailIcon from "@mui/icons-material/Mail";

export default function Header() {
  const { auth } = React.useContext(AppContext);
  const [open, setOpen] = React.useState(false);

  const toggleDrawer = (newOpen) => () => {
    setOpen(newOpen);
  };

  const pageList = [
    { text: "Home", link: "/", icon: <InboxIcon /> },
    { text: "About", link: "/about", icon: <InboxIcon /> },
    { text: "Dashboard", link: "/dashboard", icon: <InboxIcon /> },
  ];

  const authList = [
    { text: "Register", link: "/register", icon: <MailIcon /> },
    { text: "Sign in", link: "/login", icon: <MailIcon /> },
  ];

  const profile = [{ text: "Profile", link: "/profile", icon: <MailIcon /> }];

  const DrawerList = (
    <Box sx={{ width: 250 }} role="presentation" onClick={toggleDrawer(false)}>
      <List>
        <ListItemButton>
          <ListItemText primary={"Close"} />
          <ListItemIcon><InboxIcon /></ListItemIcon>
        </ListItemButton>
        <Divider/>
        {pageList.map((item, index) => (
          <ListItem key={item.text} disablePadding>
            <ListItemButton to={item.link}>
              <ListItemIcon>{item.icon}</ListItemIcon>
              <ListItemText primary={item.text} />
            </ListItemButton>
          </ListItem>
        ))}
      </List>
      <Divider />
      <List>
        {auth === "success"
          ? profile.map((item, index) => (
              <ListItem key={item.text} disablePadding>
                <ListItemButton to={item.link}>
                  <ListItemIcon>{item.icon}</ListItemIcon>
                  <ListItemText primary={item.text} />
                </ListItemButton>
              </ListItem>
            ))
          : authList.map((item, index) => (
              <ListItem key={item.text} disablePadding>
                <ListItemButton to={item.link}>
                  <ListItemIcon>{item.icon}</ListItemIcon>
                  <ListItemText primary={item.text} />
                </ListItemButton>
              </ListItem>
            ))}
      </List>
    </Box>
  );

  return (
    <div>
      <Button onClick={toggleDrawer(true)}>Open drawer</Button>
      <Drawer open={open} onClose={toggleDrawer(false)}>
        {DrawerList}
      </Drawer>
    </div>
  );
}
