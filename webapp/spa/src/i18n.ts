import i18n from "i18next";
import { initReactI18next } from "react-i18next";
import Backend from "i18next-http-backend";
import LanguageDetector from "i18next-browser-languagedetector";

import { z } from "zod";
import { makeZodI18nMap } from "zod-i18n-map";
import translationEs from "zod-i18n-map/locales/es/zod.json";
import translationEn from "zod-i18n-map/locales/en/zod.json";

const CONTEXT = import.meta.env.VITE_CONTEXT;
i18n
  .use(Backend)
  .use(LanguageDetector)
  .use(initReactI18next)
  .init({
    partialBundledLanguages: true,
    fallbackLng: "en",
    ns: ["common", "zod"],
    defaultNS: "common",
    ...(CONTEXT
      ? {
          backend: {
            loadPath: CONTEXT + "/locales/{{lng}}/{{ns}}.json",
          },
        }
      : {
          debug: true,
        }),
  });
i18n.addResourceBundle("es", "zod", translationEs);
i18n.addResourceBundle("en", "zod", translationEn);
z.setErrorMap(makeZodI18nMap({ ns: ["zod", "common"], handlePath: false }));

export default i18n;
