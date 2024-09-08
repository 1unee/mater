import {AfterViewInit, Component, ElementRef, Input, OnInit, ViewChild} from '@angular/core';
import {Button, ButtonDirective} from "primeng/button";
import {NgClass, NgIf, NgStyle} from "@angular/common";
import {VgCoreModule} from "@videogular/ngx-videogular/core";
import {VgOverlayPlayModule} from "@videogular/ngx-videogular/overlay-play";
import {VgBufferingModule} from "@videogular/ngx-videogular/buffering";
import {VgControlsModule} from "@videogular/ngx-videogular/controls";
import {StyleClassModule} from "primeng/styleclass";
import {VideoDto} from "../../../store/dtos/video.dto";
import {OneuneMessageService} from "../../../services/utils/oneune-message.service";
import {ImageModule} from "primeng/image";
import {TagModule} from "primeng/tag";

@Component({
  selector: 'app-video-player',
  standalone: true,
  imports: [
    ButtonDirective,
    NgClass,
    VgCoreModule,
    VgOverlayPlayModule,
    VgBufferingModule,
    VgControlsModule,
    NgIf,
    StyleClassModule,
    Button,
    ImageModule,
    TagModule,
    NgStyle
  ],
  templateUrl: './video-player.component.html',
  styleUrl: './video-player.component.scss'
})
export class VideoPlayerComponent implements OnInit, AfterViewInit {

  @ViewChild('htmlVideo') htmlVideo: ElementRef<HTMLVideoElement>;
  @ViewChild('htmlSource') htmlSource: ElementRef<HTMLSourceElement>;

  @Input() header: string = 'VideoPlayer';
  @Input() videos: VideoDto[] = [];
  @Input() height: number = 480;
  @Input() width: number = 720;

  base64Videos: string[] = [];
  base64CurrentVideo: string;

  constructor(public messageService: OneuneMessageService) {
  }

  ngOnInit(): void {
    // this._initializeVideos()
    // this._initializeVideoSource()
  }

  ngAfterViewInit(): void {
    this._initializeVideos()
    this._initializeVideoSource()
  }

  private _initializeVideos(): void {
    if (this.videos.length > 0) {
      this.base64Videos = this.videos.map((video: VideoDto): string => `data:video/mp4;base64,${video.base64}`);
      this.base64CurrentVideo = this.base64Videos[0];
    }
  }

  private _initializeVideoSource(): void {
    this.htmlSource.nativeElement.src = this.base64CurrentVideo;
    this.htmlVideo.nativeElement.load();
  }

  get currentVideoIndex(): number  {
    return this.base64Videos.findIndex(base64Video => base64Video === this.base64CurrentVideo);
  }

  prev(): void {
    if (this.currentVideoIndex !== 0) {
      this.htmlVideo.nativeElement.pause();
      this.base64CurrentVideo = this.base64Videos[this.currentVideoIndex - 1];
      this._initializeVideoSource();
    }
  }

  next(): void {
    if (this.currentVideoIndex !== this.base64Videos.length - 1) {
      this.htmlVideo.nativeElement.pause();
      this.base64CurrentVideo = this.base64Videos[this.currentVideoIndex + 1];
      this._initializeVideoSource();
    }
  }
}
