import {AfterViewInit, Component, ElementRef, Input, OnInit, ViewChild} from '@angular/core';
import {Button, ButtonDirective} from "primeng/button";
import {NgClass, NgIf, NgStyle} from "@angular/common";
import {VgCoreModule} from "@videogular/ngx-videogular/core";
import {VgOverlayPlayModule} from "@videogular/ngx-videogular/overlay-play";
import {VgBufferingModule} from "@videogular/ngx-videogular/buffering";
import {VgControlsModule} from "@videogular/ngx-videogular/controls";
import {StyleClassModule} from "primeng/styleclass";
import {OneuneMessageService} from "../../../services/utils/oneune-message.service";
import {ImageModule} from "primeng/image";
import {TagModule} from "primeng/tag";
import {FileDto} from "../../../store/dtos/file.dto";

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
  @Input() videos: FileDto[] = [];
  @Input() height: number = 480;
  @Input() width: number = 720;

  urls: string[] = [];
  currentUrl: string;

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
      this.urls = this.videos.map((video: FileDto): string => video.url);
      this.currentUrl = this.urls[0];
    }
  }

  private _initializeVideoSource(): void {
    this.htmlSource.nativeElement.src = this.currentUrl;
    this.htmlVideo.nativeElement.load();
  }

  get currentVideoIndex(): number  {
    return this.urls.findIndex(url => url === this.currentUrl);
  }

  prev(): void {
    if (this.currentVideoIndex !== 0) {
      this.htmlVideo.nativeElement.pause();
      this.currentUrl = this.urls[this.currentVideoIndex - 1];
      this._initializeVideoSource();
    }
  }

  next(): void {
    if (this.currentVideoIndex !== this.urls.length - 1) {
      this.htmlVideo.nativeElement.pause();
      this.currentUrl = this.urls[this.currentVideoIndex + 1];
      this._initializeVideoSource();
    }
  }
}
