import {
  createStyles,
  Flex,
  Text,
  Image,
  TextInput,
  Button,
} from "@mantine/core";
import { useTranslation } from "react-i18next";
import homeImageSrc from "@/assets/images/home_image.png";
import { UserService } from "@/api/services/UserService";
import { useState } from "react";

const useStyles = createStyles((theme) => ({
  heading: {
    background:
      theme.colorScheme === "dark"
        ? theme.colors.dark[4]
        : theme.colors.gray[3],
    opacity: 0.8,
    paddingTop: theme.spacing.xl,
    paddingBottom: theme.spacing.xl,
    paddingLeft: theme.spacing.md,
    paddingRight: theme.spacing.md,
  },
  headingWrapper: {
    maxWidth: 1920,
    margin: "0 auto",
  },
  headingText: {
    fontSize: "5vh",
    maxWidth: "50%",
    paddingLeft: theme.spacing.xl * 2,
    paddingRight: theme.spacing.xl * 2,
    color:
      theme.colorScheme === "dark"
        ? theme.colors.dark[0]
        : theme.colors.gray[9],
  },
}));

export function HomePage() {
  const { classes } = useStyles();
  const { t } = useTranslation();

  const [email, setEmail] = useState("");
  const [password, setPassword] = useState("");

  const [result, setResult] = useState("");

  const getUser = async () => {
    const user = await UserService.getByEmail(email, { username: email, password: password });
    setResult(JSON.stringify(user))
  }

  const getUserWithJwt = async () => {
    const user = await UserService.getByEmail(email);
    setResult(JSON.stringify(user))
  }

  return (
    <>
      <div className={classes.heading}>
        <Flex
          justify="space-between"
          align="center"
          className={classes.headingWrapper}
        >
          <Text className={classes.headingText}>{t("pages.home.heading")}</Text>
          <Image src={homeImageSrc} height={"35vh"} width={"auto"} />
        </Flex>
      </div>
      <TextInput label="email" value={email} onChange={e => setEmail(e.currentTarget.value)} />
      <TextInput label="password" value={password} onChange={e => setPassword(e.currentTarget.value)} />
      <Button onClick={getUser}>GET WITH BASIC</Button>
      <Button onClick={getUserWithJwt}>GET WITH JWT</Button>
      <Button onClick={() => setResult("")}>CLEAR</Button>

      <p>
        {result}
      </p>
    </>
  );
}
