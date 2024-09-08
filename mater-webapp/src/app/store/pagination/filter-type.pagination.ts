export enum FilterType {
  EQUALS = 'EQUALS',
  STARTS_WITH = 'STARTS_WITH',
  ENDS_WITH = 'ENDS_WITH',
  CONTAINS = 'CONTAINS',
  GREATER_THAN = 'GREATER_THAN',
  LESS_THAN = 'LESS_THAN'
}

export const FilterTypeTitle: { [key in FilterType]: string } = {
  [FilterType.EQUALS]: 'Равно',
  [FilterType.STARTS_WITH]: 'Начинается с',
  [FilterType.ENDS_WITH]: 'Заканчивается на',
  [FilterType.CONTAINS]: 'Содержит',
  [FilterType.GREATER_THAN]: 'Больше',
  [FilterType.LESS_THAN]: 'Меньше'
};
