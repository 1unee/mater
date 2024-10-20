import {Component, Inject} from '@angular/core';
import {FormBuilder, FormGroup} from "@angular/forms";
import {OneuneMessageService} from "../../../services/utils/oneune-message.service";
import {LOADING} from "../../../app.config";
import {LoadingReference} from "../../../store/interfaces/loading-reference.interface";

@Component({
  selector: 'app-abstract-form',
  standalone: true,
  imports: [],
  templateUrl: './abstract-form.component.html',
  styleUrl: './abstract-form.component.scss'
})
export abstract class AbstractFormComponent<T> {

  protected constructor(private _formBuilder: FormBuilder,
                        public _messageService: OneuneMessageService,
                        @Inject(LOADING) public _loadingReference: LoadingReference) {
  }

  form: FormGroup;
  formObject: T;

  /**
   * For example:
   *
   *  this.feedbackForm = this.formBuilder.group({
   *    messageBody: ['<INITIAL_VALUE>', [
   *      Validators.required, areOnlyWhitespaces()
   *    ]],
   *  });
   */
  protected abstract _initializeForm(): void;
  protected abstract _buildModel(): T;
  public abstract onSubmit(closeDialog: boolean): Promise<void>;
}
