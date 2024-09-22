import {AbstractControl, FormGroup, ValidationErrors, ValidatorFn} from "@angular/forms";
import {ContactTypeEnum} from "../../store/enums/contact-type.enum";

export function areOnlyWhitespaces(): ValidatorFn {
  return (control: AbstractControl): ValidationErrors | null => {
    const value = control.value;
    const isValid: boolean = !!value ? value.trim().length !== 0 : false;
    return isValid ? null : { 'areOnlyWhitespaces': true };
  };
}

export function contactReferenceValue(form: FormGroup): ValidationErrors | null  {
  const contactType = form.get('contactType')?.value;
  const contactReference = form.get('contactReference')?.value;
  switch (contactType) {
    case ContactTypeEnum.EMAIL:
      const emailPattern: RegExp = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
      return emailPattern.test(contactReference) ? null : { 'invalidEmail': true };
    case ContactTypeEnum.VKONTAKTE:
    case ContactTypeEnum.TELEGRAM:
    case ContactTypeEnum.INSTAGRAM:
    case ContactTypeEnum.WHATSAPP:
      const urlPattern: RegExp = /^(https?:\/\/)?([a-zA-Z0-9-]+\.)+[a-zA-Z]{2,8}(\/\S*)?$/;
      return urlPattern.test(contactReference) ? null : { 'invalidUrl': true };
    case ContactTypeEnum.PHONE:
      const phonePattern: RegExp = /^\+?[1-9]\d{10}$/;
      return phonePattern.test(contactReference) ? null : { 'invalidPhone': true };
    default:
      return { 'unknownType': true };
  }
}

export function isNumber(): ValidatorFn {
  return (control: AbstractControl): ValidationErrors | null => {
    const isValid = !isNaN(parseFloat(control.value)) && isFinite(control.value);
    return isValid ? null : { notNumber: true };
  };
}

export function isInteger(): ValidatorFn {
  return (control: AbstractControl): ValidationErrors | null => {
    const isValid = Number.isInteger(Number(control.value));
    return isValid ? null : { notInteger: true };
  };
}
