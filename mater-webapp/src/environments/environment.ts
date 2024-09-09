import {Environment} from "../app/store/interfaces/environment.interface";

export const environment: Environment = {
  production: true,
  apiUrl: 'https://oneune.duckdns.org:9033',
  featureFlags: {
    mockTelegramUser: false
  }
};
