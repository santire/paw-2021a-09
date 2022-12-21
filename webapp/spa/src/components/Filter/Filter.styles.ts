import { createStyles } from "@mantine/core";
export default createStyles((theme) => {
  return {
    wrapper: {
      display: "flex",
      backgroundColor:
        theme.colorScheme === "dark" ? theme.colors.dark[6] : theme.white,
      borderRadius: theme.radius.lg,
      padding: 4,
      border: `1px solid ${
        theme.colorScheme === "dark"
          ? theme.colors.dark[8]
          : theme.colors.gray[2]
      }`,
    },

    form: {
      boxSizing: "border-box",
      flex: 1,
      padding: theme.spacing.xl,
      paddingLeft: theme.spacing.xl * 2,
      borderLeft: 0,
    },

    fields: {
      marginTop: -12,
    },

    title: {
      fontSize: "2rem",
      color:
        theme.colorScheme === "dark"
          ? theme.colors.gray[5]
          : theme.colors.dark[6],
      fontWeight: 700,
    },
  };
});
