export enum GearboxEnum {
  AUTOMATIC = 'AUTOMATIC',
  MANUAL = 'MANUAL',
  SEMI_AUTOMATIC = 'SEMI_AUTOMATIC',
}

export const GearboxTitle: { [key in GearboxEnum]: string } = {
  [GearboxEnum.AUTOMATIC]: 'Автомат',
  [GearboxEnum.MANUAL]: 'Механика',
  [GearboxEnum.SEMI_AUTOMATIC]: 'Полуавтомат',
};

export function getGearboxTitle(gearbox: GearboxEnum): string {
  return GearboxTitle[gearbox];
}
