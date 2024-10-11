import {Component, OnInit} from '@angular/core';
import {DynamicDialogConfig, DynamicDialogRef} from "primeng/dynamicdialog";
import {UserDto} from "../../../store/dtos/user.dto";
import {AccordionModule} from "primeng/accordion";
import {NgForOf} from "@angular/common";
import {RoleDto} from "../../../store/dtos/role.dto";
import {RoleService} from "../../../services/https/role.service";
import {Button} from "primeng/button";
import {RoleTitle} from "../../../store/enums/role.enum";
import {UserService} from "../../../services/https/user.service";
import {OneuneMessageService} from "../../../services/utils/oneune-message.service";
import {LongClickDirective} from "../../../services/directives/long-click.directive";

@Component({
  selector: 'app-user-processing-dialog',
  standalone: true,
  imports: [
    AccordionModule,
    NgForOf,
    Button,
    LongClickDirective
  ],
  templateUrl: './user-processing-dialog.component.html',
  styleUrl: './user-processing-dialog.component.scss'
})
export class UserProcessingDialogComponent implements OnInit {

  readonly RoleTitle = RoleTitle;

  user: UserDto;
  freeRoles: RoleDto[];

  constructor(private dynamicDialogConfig: DynamicDialogConfig,
              private dynamicDialogRef: DynamicDialogRef,
              private roleService: RoleService,
              private userService: UserService,
              public messageService: OneuneMessageService) {
  }

  async ngOnInit(): Promise<void> {
    this.user = this.dynamicDialogConfig.data.user;
    this.freeRoles = (await this.roleService.getRoles())
      .filter(freeRole => !this.user.roles.map(userRole => userRole.id).includes(freeRole.id));
  }

  onDropRole(droppingRole: RoleDto): void {
    this.user.roles = this.user.roles.filter(userRole => userRole.id !== droppingRole.id);
    this.freeRoles.push(droppingRole);
  }

  onAddRole(addingRole: RoleDto): void {
    this.freeRoles = this.freeRoles.filter(freeRole => freeRole.id !== addingRole.id);
    this.user.roles.push(addingRole);
  }

  async onSave(): Promise<void> {
    this.userService.put(this.user)
      .then((): void => this.messageService.showSuccess('Данные о пользователе успешно обновлены!'));
    this.dynamicDialogRef.close();
  }
}
