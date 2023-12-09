import { Text, Container, ActionIcon, Group, Flex, Image } from "@mantine/core";
import {
  IconBrandTwitter,
  IconBrandYoutube,
  IconBrandInstagram,
} from "@tabler/icons-react";
import { useTranslation } from "react-i18next";

import useStyles from "./Footer.styles";
import { LanguageSwitch } from "./LanguageSwitch/LanguageSwitch";
import { ThemeToggle } from "./ThemeToggle/ThemeToggle";

export interface FooterProps {
  data: {
    title: string;
    links?: { label: string; link?: string }[];
  }[];
}

export function Footer({ data }: FooterProps) {
  const { classes } = useStyles();
  const { t } = useTranslation();

  const groups = data.map((group) => {
    const links = group.links?.map((link, index) =>
      link.link ? (
        <Text<"a">
          key={index}
          className={[classes.text, classes.link].join(" ")}
          component="a"
          href={link.link}
          m={0}
          p={0}
        >
          {link.label}
        </Text>
      ) : (
        <Text<"p">
          key={index}
          className={classes.text}
          component="p"
          m={0}
          p={0}
        >
          {link.label}
        </Text>
      )
    );

    return (
      <div className={classes.wrapper} key={group.title}>
        <Text className={classes.title}>{group.title}</Text>
        {links}
      </div>
    );
  });

  return (
    <footer className={classes.footer}>
      <Container>
        <div className={classes.groups}>{groups}</div>
      </Container>

      <Container className={classes.afterFooter}>
        <Text color="dimmed" size="sm">
          © 2022 gourmetable.com. {t`footer.copyright`}.
        </Text>

        <Group spacing={0} className={classes.social} position="right" noWrap>
          <ActionIcon size="lg">
            <IconBrandTwitter size={18} stroke={1.5} />
          </ActionIcon>
          <ActionIcon size="lg">
            <IconBrandYoutube size={18} stroke={1.5} />
          </ActionIcon>
          <ActionIcon size="lg">
            <IconBrandInstagram size={18} stroke={1.5} />
          </ActionIcon>
        </Group>
        <Flex align="center" justify="space-between">
          <Group align="center">
            <ThemeToggle />
            <LanguageSwitch />
          </Group>
        </Flex>
      </Container>
    </footer>
  );
}
