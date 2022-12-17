import {
  createStyles,
  Title,
  Text,
  Button,
  Container,
  Group,
} from "@mantine/core";
import { useTranslation } from "react-i18next";
import { Link } from "react-router-dom";

const useStyles = createStyles((theme) => ({
  root: {
    paddingTop: 80,
    paddingBottom: 80,
  },

  label: {
    textAlign: "center",
    fontWeight: 900,
    fontSize: 220,
    lineHeight: 1,
    marginBottom: theme.spacing.xl * 1.5,
    color:
      theme.colorScheme === "dark"
        ? theme.colors.dark[4]
        : theme.colors.gray[3],

    [theme.fn.smallerThan("sm")]: {
      fontSize: 120,
    },
  },

  title: {
    fontFamily: `Greycliff CF, ${theme.fontFamily}`,
    textAlign: "center",
    fontWeight: 900,
    fontSize: 38,

    [theme.fn.smallerThan("sm")]: {
      fontSize: 32,
    },
  },

  description: {
    maxWidth: 500,
    margin: "auto",
    marginTop: theme.spacing.xl,
    marginBottom: theme.spacing.xl * 1.5,
  },
}));

export function ErrorPage() {
  const { classes } = useStyles();
  const { t } = useTranslation();

  return (
    <Container className={classes.root}>
      <div className={classes.label}>404</div>
      <Title className={classes.title}>{t("error.title")}</Title>
      <Text
        color="dimmed"
        size="lg"
        align="center"
        className={classes.description}
      >
        {t("error.description")}
      </Text>
      <Group position="center">
        <Link to="/">
          <Button variant="subtle" size="md" color="orange">
            {t("error.takeMeBack")}
          </Button>
        </Link>
      </Group>
    </Container>
  );
}
