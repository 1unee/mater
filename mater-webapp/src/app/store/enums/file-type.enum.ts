export enum FileTypeEnum {
  PHOTO = 'PHOTO',
  VIDEO = 'VIDEO'
}

export const FileTypeTitle: { [key in FileTypeEnum]: string } = {
  [FileTypeEnum.PHOTO]: 'Фото',
  [FileTypeEnum.VIDEO]: 'Видео'
}
