export enum PaginationDirection {
  PREV = 'PREV',
  NEXT = 'NEXT',
}

export const PaginationDirectionTitle: { [key in PaginationDirection]: string } = {
  [PaginationDirection.PREV]: 'Предыдущая',
  [PaginationDirection.NEXT]: 'Следующая',
}
