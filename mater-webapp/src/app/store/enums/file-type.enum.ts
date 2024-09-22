export enum FileTypeEnum {
  PHOTO = 'PHOTO',
  VIDEO = 'VIDEO'
}

export const FileTypeTitle: { [key in FileTypeEnum]: string } = {
  [FileTypeEnum.PHOTO]: 'Фото',
  [FileTypeEnum.VIDEO]: 'Видео'
}

export function getFileTypeTitle(fileType: string): string {
  if (fileType.startsWith('image')) {
    return FileTypeTitle[FileTypeEnum.PHOTO];
  } else if (fileType.startsWith('video')) {
    return FileTypeTitle[FileTypeEnum.VIDEO];
  } else {
    throw new Error(`FileType ${fileType} is not supported`);
  }
}
