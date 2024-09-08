export enum ContactTypeEnum {
  EMAIL = 'EMAIL',
  VKONTAKTE = 'VKONTAKTE',
  TELEGRAM = 'TELEGRAM',
  INSTAGRAM = 'INSTAGRAM',
  PHONE = 'PHONE',
  WHATSAPP = 'WHATSAPP',
}

export const ContactTypeTitle: { [key in ContactTypeEnum]: string } = {
  [ContactTypeEnum.EMAIL]: 'Почта',
  [ContactTypeEnum.VKONTAKTE]: 'VK',
  [ContactTypeEnum.TELEGRAM]: 'Телеграм',
  [ContactTypeEnum.INSTAGRAM]: 'Инстаграм',
  [ContactTypeEnum.PHONE]: 'Телефон',
  [ContactTypeEnum.WHATSAPP]: 'WhatsApp',
}
