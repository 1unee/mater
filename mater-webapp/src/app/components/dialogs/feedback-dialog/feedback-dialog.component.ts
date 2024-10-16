import {Component, Inject, OnInit} from '@angular/core';
import {Button} from "primeng/button";
import {DropdownModule} from "primeng/dropdown";
import {InputGroupAddonModule} from "primeng/inputgroupaddon";
import {InputGroupModule} from "primeng/inputgroup";
import {InputTextModule} from "primeng/inputtext";
import {PaginatorModule} from "primeng/paginator";
import {FloatLabelModule} from "primeng/floatlabel";
import {InputTextareaModule} from "primeng/inputtextarea";
import {NotificationService} from "../../../services/https/notification.service";
import {OneuneMessageService} from "../../../services/utils/oneune-message.service";
import {StorageService} from "../../../services/utils/storage.service";
import {FormBuilder, ReactiveFormsModule, Validators} from "@angular/forms";
import {NgIf} from "@angular/common";
import {areOnlyWhitespaces} from "../../../services/utils/validators";
import {AbstractFormComponent} from "../../core/abstract-form/abstract-form.component";
import {LOADING} from "../../../app.config";
import {LoadingReference} from "../../../store/interfaces/loading-reference.interface";
import {DynamicDialogRef} from "primeng/dynamicdialog";
import {LongClickDirective} from "../../../services/directives/long-click.directive";

@Component({
  selector: 'app-feedback-dialog',
  standalone: true,
  imports: [
    Button,
    DropdownModule,
    InputGroupAddonModule,
    InputGroupModule,
    InputTextModule,
    PaginatorModule,
    FloatLabelModule,
    InputTextareaModule,
    ReactiveFormsModule,
    NgIf,
    LongClickDirective
  ],
  templateUrl: './feedback-dialog.component.html',
  styleUrl: './feedback-dialog.component.scss'
})
export class FeedbackDialogComponent extends AbstractFormComponent<string> implements OnInit {

  messageBody: string;

  constructor(private dynamicDialogRef: DynamicDialogRef,
              private notificationService: NotificationService,
              public messageService: OneuneMessageService,
              private storageService: StorageService,
              private formBuilder: FormBuilder,
              @Inject(LOADING) public loadingReference: LoadingReference) {
    super();
  }

  ngOnInit(): void {
    this._initializeForm();
  }

  protected override _initializeForm(): void {
    this.form = this.formBuilder.group({
      messageBody: ['', [
        Validators.required, areOnlyWhitespaces()
      ]],
    });
  }

  protected override _buildModel(): string {
    return this.form.value.messageBody;
  }

  async onSubmit(closeDialog: boolean): Promise<void> {
    try {
      this.loadingReference.value.next(true);
      this.notificationService.sendSimpleMailToSelf(this.storageService.user.username, this._buildModel())
        .catch((): void => this.messageService.showError('К сожалению, доставить твое пожелание разработчику не получилось по техническим причинам. Попробуй чуть позже...'))
      this.messageBody = '';
      this.messageService.showSuccess('Обращение успешно отправлено разработчику!');
      this.form.reset();
      if (closeDialog) {
        this.dynamicDialogRef.close();
      }
    } catch (e) {
      this.messageService.showDefaultError();
    } finally {
      this.loadingReference.value.next(false);
    }
  }
}
