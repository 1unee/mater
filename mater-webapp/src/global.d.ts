interface TelegramWebApp {
  colorScheme: 'light' | 'dark';
}

interface Window {
  Telegram: {
    WebApp: TelegramWebApp;
  };
}
