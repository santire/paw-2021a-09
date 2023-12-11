import { createStyles } from "@mantine/core";

export default createStyles((theme) => ({
  title: {
    fontFamily: `Greycliff CF, ${theme.fontFamily}`,
    fontSize: theme.fontSizes.xl * 2,
    fontWeight: 700,
  },
  address: {
    fontFamily: `Greycliff CF, ${theme.fontFamily}`,
    fontSize: theme.fontSizes.md,
  },
  socials: {
    backgroundColor:
      theme.colorScheme === "dark"
        ? theme.colors.dark[6]
        : theme.colors.gray[2],
    borderRadius: theme.radius.xl,
  },
  img: {
    maxHeight: 400,
  },
  tags: {
    minHeight: theme.spacing.xl * 2,
  },
  comment: {
    padding: `${theme.spacing.lg}px ${theme.spacing.xl}px`,
  },

  body: {
    paddingLeft: 0,
    paddingTop: theme.spacing.sm,
    fontSize: theme.fontSizes.sm,
  },

  content: {
    "& > p:last-child": {
      marginBottom: 0,
    },
  },
}));
