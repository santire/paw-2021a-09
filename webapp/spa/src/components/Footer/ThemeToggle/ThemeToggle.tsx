import {
  useMantineColorScheme,
  SegmentedControl,
  Group,
  Center,
  Box,
} from "@mantine/core";
import { IconSun, IconMoon } from "@tabler/icons";
import { useTranslation } from "react-i18next";
export function ThemeToggle() {
  const { colorScheme, toggleColorScheme } = useMantineColorScheme();
  const { t } = useTranslation();

  return (
    <Group position="center" my="xl">
      <SegmentedControl
        value={colorScheme}
        onChange={(value: "light" | "dark") => toggleColorScheme(value)}
        data={[
          {
            value: "light",
            label: (
              <Center>
                <IconSun size={16} stroke={1.5} />
                <Box ml={10}>{t("theme.light")}</Box>
              </Center>
            ),
          },
          {
            value: "dark",
            label: (
              <Center>
                <IconMoon size={16} stroke={1.5} />
                <Box ml={10}>{t("theme.dark")}</Box>
              </Center>
            ),
          },
        ]}
      />
    </Group>
  );
}
