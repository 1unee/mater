import {BehaviorSubject} from "rxjs";

export interface LoadingReference {
  value: BehaviorSubject<boolean>;
}
