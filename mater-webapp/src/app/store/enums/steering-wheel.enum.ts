export enum SteeringWheelEnum {
  LEFT = 'LEFT',
  RIGHT = 'RIGHT',
}

export const SteeringWheelTitle: { [key in SteeringWheelEnum]: string } = {
  [SteeringWheelEnum.LEFT]: 'Левый',
  [SteeringWheelEnum.RIGHT]: 'Правый',
};

export function getSteeringWheelTitle(steeringWheel: SteeringWheelEnum): string {
  return SteeringWheelTitle[steeringWheel];
}
