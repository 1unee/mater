import {Directive, EventEmitter, HostListener, Output} from '@angular/core';

@Directive({
  selector: '[appLongClick]',
  standalone: true
})
export class LongClickDirective {

  @Output() longClick = new EventEmitter<void>();

  private readonly TIMEOUT_TO_LONG_CLICK: number = 1000;
  private timeout: any | null = null;

  @HostListener('touchstart') onTouchStart(): void {
    this.startLongClick();
  }

  @HostListener('touchend') onTouchEnd(): void {
    this.endLongClick();
  }

  @HostListener('mouseleave') onMouseLeave(): void {
    this.endLongClick();
  }

  @HostListener('mousedown') onMouseDown(): void {
    this.startLongClick();
  }

  @HostListener('mouseup') onMouseUp(): void {
    this.endLongClick();
  }

  private startLongClick(): void {
    this.timeout = setTimeout((): void => {
      this.longClick.emit();
    }, this.TIMEOUT_TO_LONG_CLICK);
  }

  private endLongClick(): void {
    if (this.timeout) {
      clearTimeout(this.timeout);
      this.timeout = null;
    }
  }
}
