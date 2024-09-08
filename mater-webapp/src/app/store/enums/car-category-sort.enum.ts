export enum CarCategorySortEnum {
  ALL = 'ALL',
  MINE = 'MINE'
}

export const CarCategorySortTitle: { [key in CarCategorySortEnum]: string } = {
  [CarCategorySortEnum.ALL]: 'Все',
  [CarCategorySortEnum.MINE]: 'Мои'
}
