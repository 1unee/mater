export enum CarStateEnum {
  NEW = 'NEW',
  USED = 'USED',
  DAMAGED = 'DAMAGED',
}

export const CarStateTitle: { [key in CarStateEnum]: string } = {
  [CarStateEnum.NEW]: 'Новая',
  [CarStateEnum.USED]: 'Подержанная',
  [CarStateEnum.DAMAGED]: 'Поврежденная',
};

export function getCarStateTitle(carState: CarStateEnum): string {
  return CarStateTitle[carState];
}
