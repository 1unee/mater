export enum CarCategorySortEnum {
  ALL = 'ALL',
  MINE = 'MINE',
  FAVORITES = 'FAVORITES'
}

export const CarCategorySortTitle: { [key in CarCategorySortEnum]: string } = {
  [CarCategorySortEnum.ALL]: 'Все',
  [CarCategorySortEnum.MINE]: 'Мои',
  [CarCategorySortEnum.FAVORITES]: 'Избранные'
}
