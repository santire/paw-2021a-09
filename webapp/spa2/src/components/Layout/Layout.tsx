import { createStyles } from "@mantine/core";
import { useTranslation } from "react-i18next";
import { Outlet } from "react-router-dom";
import { Footer, FooterProps } from "../Footer/Footer";
import { Header } from "../Header/Header";

const useStyles = createStyles(() => ({
  app: {
    display: "flex",
    flexFlow: "column nowrap",
    minHeight: "100vh",
    justifyContent: "center",
  },
  main: {
    flexGrow: 1,
    // maxWidth: 2560,
    // margin: "0 auto",
  },
}));

export default function Layout() {
  const { classes } = useStyles();
  const { t } = useTranslation();

  const data: FooterProps["data"] = [
    {
      title: t("footer.weAre"),
      links: [
        { label: "Gianfranco Marchetti" },
        { label: "Manuel Luque" },
        { label: "Santiago Burgos" },
        { label: "Santiago Reyes" },
      ],
    },
    {
      title: t("footer.contactUs"),
      links: [
        {
          label: "gourmetablewebapp@gmail.com",
          link: "mailto:gourmetablewebapp@gmail.com",
        },
      ],
    },
  ];

  return (
    <div className={classes.app}>
      <Header />

      <main className={classes.main}>
        {/* <Navigation /> */}
        <Outlet />
      </main>
      <Footer data={data} />
    </div>
  );
}
