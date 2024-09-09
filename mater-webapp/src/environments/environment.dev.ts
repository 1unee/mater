import {Environment} from "../app/store/interfaces/environment.interface";

export const environment: Environment = {
  production: false,
  apiUrl: 'http://localhost:8081',
  featureFlags: {
    mockTelegramUser: true
  }
};
