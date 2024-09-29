export enum EngineOilTypeEnum {
  PETROL = 'PETROL',
  DIESEL = 'DIESEL',
  ELECTRIC = 'ELECTRIC',
}

export const EngineOilTypeTitle: { [key in EngineOilTypeEnum]: string } = {
  [EngineOilTypeEnum.PETROL]: 'Бензин',
  [EngineOilTypeEnum.DIESEL]: 'Дизель',
  [EngineOilTypeEnum.ELECTRIC]: 'Электрический',
};

export function getEngineOilTypeTitle(engineOilType: EngineOilTypeEnum): string {
  return EngineOilTypeTitle[engineOilType];
}
