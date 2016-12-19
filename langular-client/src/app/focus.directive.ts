// Directive
import {Directive, EventEmitter, Input, ElementRef, Inject} from '@angular/core';

@Directive({
  selector: '[focus]'
})
export class FocusDirective {
  private focusEmitterSubscription;
  // Now we expect EventEmmiter as binded value
  @Input('focus')
  set focus(focusEmitter: EventEmitter<boolean>) {
    if(this.focusEmitterSubscription) {
      this.focusEmitterSubscription.unsubscribe();
    }
    this.focusEmitterSubscription = focusEmitter.subscribe(
      (()=> { setTimeout(() => { this.element.nativeElement.focus() }, 100)}).bind(this))
  }
  constructor(@Inject(ElementRef) private element: ElementRef) {}
}
