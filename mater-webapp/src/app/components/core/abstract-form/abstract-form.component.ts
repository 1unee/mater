import { Component } from '@angular/core';
import {FormGroup} from "@angular/forms";

@Component({
  selector: 'app-abstract-form',
  standalone: true,
  imports: [],
  templateUrl: './abstract-form.component.html',
  styleUrl: './abstract-form.component.scss'
})
export abstract class AbstractFormComponent<T> {

  form: FormGroup;

  /**
   * For example:
   *
   *  this.feedbackForm = this.formBuilder.group({
   *    messageBody: ['', [
   *      Validators.required, areOnlyWhitespaces()
   *    ]],
   *  });
   */
  protected abstract _initializeForm(): void;
  protected abstract _buildModel(): T;
  public abstract onSubmit(): Promise<void>;
}
