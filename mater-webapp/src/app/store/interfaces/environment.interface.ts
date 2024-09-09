export interface Environment {
  production: boolean;
  apiUrl: string;
  featureFlags: {
    mockTelegramUser: boolean
  };
}
