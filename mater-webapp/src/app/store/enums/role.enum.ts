export enum RoleEnum {
  USER = 'USER',
  SELLER = 'SELLER',
  SUPPORT = 'SUPPORT',
  ADMIN = 'ADMIN'
}

export const RoleTitle: { [key in RoleEnum]: string } = {
  [RoleEnum.USER]: 'Пользователь',
  [RoleEnum.SELLER]: 'Продавец',
  [RoleEnum.SUPPORT]: 'Поддержка',
  [RoleEnum.ADMIN]: 'Администратор'
}
