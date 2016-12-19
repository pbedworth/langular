import {Component, OnInit, Input, ViewChild, EventEmitter} from '@angular/core';
declare var $:any;

@Component({
  selector: 'textedit',
  templateUrl: './textedit.component.html',
  styleUrls: ['./textedit.component.css']
})
export class TexteditComponent implements OnInit {
  @ViewChild('inlineEditControl') inlineEditControl;

  @Input() text;
  @Input() type;

  private editing:boolean = false;

  constructor() { }

  edit(text) {
    console.log('edit', this.inlineEditControl);
    this.editing = true;
    this.text = text;
    $('textarea').outerWidth($('textarea').parent().parent().parent().width());
    this.moveFocus();
  }

  onSubmit(text) {

  }

  cancel(text) {

  }

  ngOnInit() {
  }

  private inputFocused = new EventEmitter<boolean>();
  moveFocus() {
    this.inputFocused.emit(true);
  }

}
