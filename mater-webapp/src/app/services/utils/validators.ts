import {AbstractControl, FormGroup, ValidationErrors, ValidatorFn} from "@angular/forms";
import {ContactTypeEnum} from "../../store/enums/contact-type.enum";

export class OneuneValidators {

  static areOnlyWhitespaces(): ValidatorFn {
    return (control: AbstractControl): ValidationErrors | null => {
      const value = control.value;
      const isValid: boolean = !!value ? value.trim().length !== 0 : false;
      return isValid ? null : { 'areOnlyWhitespaces': true };
    };
  }

  static email(required?: boolean): ValidatorFn {
    if (!!required) {
      return (control: AbstractControl): ValidationErrors | null => {
        const emailPattern: RegExp = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
        return emailPattern.test(control.value) ? null : { 'invalidEmail': true };
      };
    } else {
      return (control: AbstractControl): ValidationErrors | null => {
        const emailPattern: RegExp = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
        return control.value === '' || emailPattern.test(control.value) ? null : { 'invalidEmail': true };
      };
    }
  }

  static contactReferenceValue(form: FormGroup): ValidationErrors | null  {
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

  static isNumber(): ValidatorFn {
    return (control: AbstractControl): ValidationErrors | null => {
      const isValid = !isNaN(parseFloat(control.value)) && isFinite(control.value);
      return isValid ? null : { notNumber: true };
    };
  }

  static isInteger(): ValidatorFn {
    return (control: AbstractControl): ValidationErrors | null => {
      const isValid = Number.isInteger(Number(control.value));
      return isValid ? null : { notInteger: true };
    };
  }

  static isText(): ValidatorFn {
    return (control: AbstractControl): ValidationErrors | null => {
      const textPattern: RegExp = /^[a-zA-Zа-яА-ЯёЁ_]*$/;
      const isValid = textPattern.test(control.value);
      return isValid ? null : { notText: true };
    };
  }

  static passwordsEqual(form: FormGroup): ValidationErrors | null  {
    return form.get('firstPassword')?.value === form.get('secondPassword')?.value ? null : { 'passwordsNotEqual': true };
  }

  static valuesEqual(form: FormGroup, ...keys: string[]): ValidationErrors | null  {
    if (keys.length === 0) return null;
    const isValid: boolean = keys
      .map(<T>(key: string): T => form.get(key)?.value)
      .every(<T>(value: T): boolean => value === form.get(keys[0])?.value);
    return isValid ? null : { notEqual: true };
  }
}
