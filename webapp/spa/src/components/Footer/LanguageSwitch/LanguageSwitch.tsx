import { Group, SegmentedControl } from "@mantine/core";
import i18n from "i18next";
import { useEffect, useState } from "react";

export function LanguageSwitch() {
  const [language, setLanguage] = useState("en");
  useEffect(() => {
    setLanguage(i18n.language);
  }, []);

  const changeLanguage = (v: string) => {
    setLanguage(v);
    i18n.changeLanguage(v);
  };

  return (
    <Group position="center" my="lg">
      <SegmentedControl
        data={[
          { label: "EN", value: "en" },
          { label: "ES", value: "es" },
        ]}
        onChange={(v) => changeLanguage(v)}
        value={language}
      />
    </Group>
  );
}
