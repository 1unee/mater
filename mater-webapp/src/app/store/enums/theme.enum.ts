export enum ThemeEnum {
  SYSTEM = 'system',
  DARK = 'dark',
  LIGHT = 'light'
}

export const ThemeTitle: { [key in ThemeEnum]: string } = {
  [ThemeEnum.SYSTEM]: 'Системная',
  [ThemeEnum.DARK]: 'Темная',
  [ThemeEnum.LIGHT]: 'Светлая'
}
