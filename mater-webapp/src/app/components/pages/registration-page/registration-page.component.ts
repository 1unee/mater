import {Component, Inject, OnInit} from '@angular/core';
import {Button} from "primeng/button";
import {FloatLabelModule} from "primeng/floatlabel";
import {FormBuilder, FormsModule, ReactiveFormsModule, Validators} from "@angular/forms";
import {InputTextareaModule} from "primeng/inputtextarea";
import {LongClickDirective} from "../../../services/directives/long-click.directive";
import {NgIf} from "@angular/common";
import {AbstractFormComponent} from "../../core/abstract-form/abstract-form.component";
import {IconFieldModule} from "primeng/iconfield";
import {InputIconModule} from "primeng/inputicon";
import {InputTextModule} from "primeng/inputtext";
import {OneuneMessageService} from "../../../services/utils/oneune-message.service";
import {StorageService} from "../../../services/utils/storage.service";
import {LOADING} from "../../../app.config";
import {LoadingReference} from "../../../store/interfaces/loading-reference.interface";
import {OneuneValidators} from "../../../services/utils/validators";
import {UserService} from "../../../services/https/user.service";
import {UserDto} from "../../../store/dtos/user.dto";
import {DropdownModule} from "primeng/dropdown";
import {InputGroupAddonModule} from "primeng/inputgroupaddon";
import {InputGroupModule} from "primeng/inputgroup";
import {UserRegistrationRequestDto} from "../../../store/dtos/user-registration-request.dto";
import {PasswordModule} from "primeng/password";
import {OneuneRouterService} from "../../../services/utils/oneune-router.service";
import {SettingService} from "../../../services/https/setting.service";

@Component({
  selector: 'app-registration-page',
  standalone: true,
  imports: [
    Button,
    FloatLabelModule,
    FormsModule,
    InputTextareaModule,
    LongClickDirective,
    NgIf,
    ReactiveFormsModule,
    IconFieldModule,
    InputIconModule,
    InputTextModule,
    DropdownModule,
    InputGroupAddonModule,
    InputGroupModule,
    PasswordModule
  ],
  templateUrl: './registration-page.component.html',
  styleUrl: './registration-page.component.scss'
})
export class RegistrationPageComponent extends AbstractFormComponent<UserRegistrationRequestDto> implements OnInit {

  constructor(public messageService: OneuneMessageService,
              private storageService: StorageService,
              private formBuilder: FormBuilder,
              @Inject(LOADING) public loadingReference: LoadingReference,
              private userService: UserService,
              private settingService: SettingService,
              private routerService: OneuneRouterService) {
    super(formBuilder, messageService, loadingReference);
  }

  async ngOnInit(): Promise<void> {
    this._initializeForm();
  }

  protected _initializeForm(): void {
    this.form = this.formBuilder.group({
      username: ['', [Validators.required]],
      firstPassword: ['', [Validators.required, Validators.minLength(6), Validators.maxLength(512)]],
      secondPassword: ['', [Validators.required, Validators.minLength(6), Validators.maxLength(512)]],
      email: ['', [OneuneValidators.email(false)]],
    }, {
      validators: [OneuneValidators.passwordsEqual]
    });
  }

  protected _buildModel(): UserRegistrationRequestDto {
    this.formObject = new UserRegistrationRequestDto();
    this.formObject.username = this.form.value.username;
    this.formObject.password = this.form.value.secondPassword;
    this.formObject.email = this.form.value.email === '' ? null : this.form.value.email;
    return this.formObject;
  }

  async onSubmit(closeDialog: boolean): Promise<void> {
    this._buildModel();
    try {
      this.loadingReference.value.next(true);
      const user: UserDto = await this.userService.registerByForeignLink(this.formObject);
      this.storageService.setUser(user);
      this.storageService.setUserSettings(await this.settingService.getByUserId(user.id));
      this.routerService.relativeRedirect('support');
      this.messageService.showSuccess('Успешная регистрация!');
      this.form.reset();
    } finally {
      this.loadingReference.value.next(false);
    }
  }
}
