import {Inject, Injectable} from '@angular/core';
import {VideoPartDto} from "../../store/dtos/video-part.dto";
import {VideoDto} from "../../store/dtos/video.dto";
import {GLOBAL_CONFIG} from "../../app.config";
import {GlobalConfig} from "../../store/interfaces/global-config.interface";

@Injectable({
  providedIn: 'root'
})
export class LargeFileService {

  constructor(@Inject(GLOBAL_CONFIG) private globalConfig: GlobalConfig) {
  }

  cutVideo(video: VideoDto): VideoPartDto[] {
    const offsetConfigValue: number = this.globalConfig.configs.largeFileOffset;
    const videoParts: VideoPartDto[] = [];
    const base64: string = video.base64;
    const totalSize: number = base64.length;
    let partIndex: number = 0;

    for (let offset = 0; offset < totalSize; offset += offsetConfigValue) {
      const chunk = base64.substring(offset, Math.min(offset + offsetConfigValue, totalSize));
      const videoPart = new VideoPartDto();
      videoPart.id = video.id;
      videoPart.name = video.name;
      videoPart.type = video.type;
      videoPart.index = partIndex;
      videoPart.offset = offset;
      videoPart.amount = Math.floor(video.base64.length / offsetConfigValue) + 1
      videoPart.base64 = chunk;
      videoParts.push(videoPart);
      partIndex++;
    }
    return videoParts;
  }

  cutVideos(videos: VideoDto[]): VideoPartDto[] {
    const videoParts: VideoPartDto[] = [];
    videos
      .map((video: VideoDto): VideoPartDto[] => this.cutVideo(video))
      .forEach((_videoParts: VideoPartDto[]): number => videoParts.push(..._videoParts));
    return videoParts;
  }

  collectVideo(videoParts: VideoPartDto[]): VideoDto {
    if (videoParts.length === videoParts[0].amount) {
      const collectedBase64: string = videoParts
        .sort((a, b) => a.index - b.index)
        .map(videoPart => videoPart.base64)
        .join('');
      const collectedVideo = new VideoDto();
      collectedVideo.id = videoParts[0].id;
      collectedVideo.name = videoParts[0].name;
      collectedVideo.type = videoParts[0].type;
      collectedVideo.base64 = collectedBase64;
      return collectedVideo;
    } else {
      throw Error('Dimensions not equal!');
    }
  }

  collectVideos(videoParts: VideoPartDto[]): VideoDto[] {
    const videosMap: Map<number, VideoPartDto[]> = new Map<number, VideoPartDto[]>();

    videoParts.forEach(part => {
      if (!videosMap.has(part.id)) {
        videosMap.set(part.id, []);
      }
      videosMap.get(part.id)!.push(part);
    });

    return Array.from(videosMap.values())
      .map((parts: VideoPartDto[]): VideoDto => this.collectVideo(parts));
  }
}
