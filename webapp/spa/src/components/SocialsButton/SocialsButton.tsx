import { ActionIcon, useMantineTheme } from "@mantine/core";
import {
  IconBrandFacebook,
  IconBrandInstagram,
  IconBrandTwitter,
} from "@tabler/icons-react";

interface SocialsButtonProps {
  type: string;
  url: string;
}

export function SocialsButton({ type, url }: SocialsButtonProps) {
  const theme = useMantineTheme();
  switch (type) {
    case "twitter": {
      return (
        <ActionIcon>
          <IconBrandTwitter
            size={18}
            color={theme.colors.blue[3]}
            stroke={1.5}
            onClick={() => window.open("http://" + url, "_blank")}
          />
        </ActionIcon>
      );
    }
    case "instagram": {
      return (
        <ActionIcon>
          <IconBrandInstagram
            size={18}
            color={theme.colors.pink[6]}
            stroke={1.5}
            onClick={() => window.open("http://" + url, "_blank")}
          />
        </ActionIcon>
      );
    }
    case "facebook": {
      return (
        <ActionIcon>
          <IconBrandFacebook
            size={18}
            color={theme.colors.blue[6]}
            stroke={1.5}
            onClick={() => window.open("http://" + url, "_blank")}
          />
        </ActionIcon>
      );
    }
    default: {
      return <></>;
    }
  }
}
