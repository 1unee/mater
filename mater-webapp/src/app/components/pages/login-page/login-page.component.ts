import {Component, Inject, OnInit} from '@angular/core';
import {Button} from "primeng/button";
import {IconFieldModule} from "primeng/iconfield";
import {InputIconModule} from "primeng/inputicon";
import {InputTextModule} from "primeng/inputtext";
import {LongClickDirective} from "../../../services/directives/long-click.directive";
import {NgIf} from "@angular/common";
import {PaginatorModule} from "primeng/paginator";
import {PasswordModule} from "primeng/password";
import {FormBuilder, ReactiveFormsModule, Validators} from "@angular/forms";
import {AbstractFormComponent} from "../../core/abstract-form/abstract-form.component";
import {UserLoginRequestDto} from "../../../store/dtos/user-login-request.dto";
import {DynamicDialogRef} from "primeng/dynamicdialog";
import {OneuneMessageService} from "../../../services/utils/oneune-message.service";
import {StorageService} from "../../../services/utils/storage.service";
import {LOADING} from "../../../app.config";
import {LoadingReference} from "../../../store/interfaces/loading-reference.interface";
import {UserService} from "../../../services/https/user.service";
import {UserDto} from "../../../store/dtos/user.dto";
import {OneuneRouterService} from "../../../services/utils/oneune-router.service";
import {SettingService} from "../../../services/https/setting.service";

@Component({
  selector: 'app-login-page',
  standalone: true,
    imports: [
        Button,
        IconFieldModule,
        InputIconModule,
        InputTextModule,
        LongClickDirective,
        NgIf,
        PaginatorModule,
        PasswordModule,
        ReactiveFormsModule
    ],
  templateUrl: './login-page.component.html',
  styleUrl: './login-page.component.scss'
})
export class LoginPageComponent extends AbstractFormComponent<UserLoginRequestDto> implements OnInit {

  constructor(public messageService: OneuneMessageService,
              private storageService: StorageService,
              private formBuilder: FormBuilder,
              @Inject(LOADING) public loadingReference: LoadingReference,
              private userService: UserService,
              private routerService: OneuneRouterService,
              private settingService: SettingService) {
    super(formBuilder, messageService, loadingReference);
  }

  async ngOnInit(): Promise<void> {
    this._initializeForm();
  }

  protected _initializeForm(): void {
    this.form = this.formBuilder.group({
      username: ['', [Validators.required]],
      password: ['', [Validators.required, Validators.minLength(6), Validators.maxLength(512)]]
    });
  }

  protected _buildModel(): UserLoginRequestDto {
    this.formObject = new UserLoginRequestDto();
    this.formObject.username = this.form.value.username;
    this.formObject.password = this.form.value.password;
    return this.formObject;
  }

  async onSubmit(closeDialog: boolean): Promise<void> {
    this._buildModel();
    try {
      this.loadingReference.value.next(true);
      const user: UserDto = await this.userService.login(this.formObject);
      this.storageService.setUser(user);
      this.storageService.setUserSettings(await this.settingService.getByUserId(user.id));
      this.routerService.relativeRedirect('support')
      this.messageService.showSuccess('Успешная авторизация!');
      this.form.reset();
    } finally {
      this.loadingReference.value.next(false);
    }
  }

  onOpenRegistrationPage(): void {
    this.routerService.relativeRedirect('registration');
  }
}
