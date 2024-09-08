export enum SortDirection {
  ASCENDING = 'ASCENDING',
  DESCENDING = 'DESCENDING'
}

export const SortDirectionTitle: { [key in SortDirection]: string } = {
  [SortDirection.ASCENDING]: 'По возрастанию',
  [SortDirection.DESCENDING]: 'По убыванию',
}
