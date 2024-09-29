export enum TransmissionEnum {
  FWD = 'FWD',
  RWD = 'RWD',
  AWD = 'AWD',
}

export const TransmissionTitle: { [key in TransmissionEnum]: string } = {
  [TransmissionEnum.FWD]: 'Передний',
  [TransmissionEnum.RWD]: 'Задний',
  [TransmissionEnum.AWD]: 'Полный',
};

export function getTransmissionTitle(transmission: TransmissionEnum): string {
  return TransmissionTitle[transmission];
}
