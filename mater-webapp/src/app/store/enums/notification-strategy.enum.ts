export enum NotificationStrategyEnum {
  TELEGRAM_CHAT= 'TELEGRAM_CHAT',
  MAIL = 'MAIL',
  ALL = 'ALL'
}

export const NotificationStrategyTitle: { [key in NotificationStrategyEnum]: string } = {
  [NotificationStrategyEnum.TELEGRAM_CHAT]: 'В тг',
  [NotificationStrategyEnum.MAIL]: 'На почту',
  [NotificationStrategyEnum.ALL]: 'Все'
}
