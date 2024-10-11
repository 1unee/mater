export enum SaleLinkStatusEnum {
  INTERESTED = 'INTERESTED',
  PROCESSING = 'PROCESSING',
  CANCELLED = 'CANCELLED',
  BOUGHT = 'BOUGHT',
}

export const SaleLinkStatusTitle: { [key in SaleLinkStatusEnum]: string } = {
  [SaleLinkStatusEnum.INTERESTED]: 'Интересна',
  [SaleLinkStatusEnum.PROCESSING]: 'Переговоры',
  [SaleLinkStatusEnum.CANCELLED]: 'Передумал',
  [SaleLinkStatusEnum.BOUGHT]: 'Куплено'
}
