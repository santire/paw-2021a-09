import i18n from "i18next";
import { initReactI18next } from "react-i18next";

import Backend from "i18next-http-backend";
import LanguageDetector from "i18next-browser-languagedetector";

const CONTEXT = process.env.REACT_APP_CONTEXT;
let config;
if (CONTEXT) {
  config = {
    fallbackLng: "en",
    backend: {
      loadPath: CONTEXT + "/locales/{{lng}}/{{ns}}.json",
    },
  };
} else {
  config = {
    fallbackLng: "en",
    debug: true,
  };
}

i18n.use(Backend).use(LanguageDetector).use(initReactI18next).init(config);

export default i18n;
