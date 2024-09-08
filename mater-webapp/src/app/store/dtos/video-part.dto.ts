import {VideoDto} from "./video.dto";

export class VideoPartDto extends VideoDto{
  index: number;
  offset: number;
  amount: number;
}
